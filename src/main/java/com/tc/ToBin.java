package com.tc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ToBin {

    public static void main(String[] args) {
        ToBin toBin = new ToBin();
        toBin.convertToBinary("novo.json", "output.json");
    }

    public void convertToBinary(String inputFilePath, String outputFilePath) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(inputFilePath)) {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(reader);

            if (!jsonElement.isJsonObject()) {
                throw new IllegalStateException("Esperava-se um objeto JSON no caminho $.");
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            // Verifica se os campos são arrays JSON
            JsonArray tapesArray = jsonObject.getAsJsonArray("tapes");
            List<String> tapes = gson.fromJson(tapesArray, List.class);

            String initialState = jsonObject.get("initialState").getAsString();

            JsonArray finalStatesArray = jsonObject.getAsJsonArray("finalStates");
            List<String> finalStates = gson.fromJson(finalStatesArray, List.class);

            // Verifica se "transitionFunction" é um objeto JSON
            JsonObject transitionFunctionObject = jsonObject.getAsJsonObject("transitionFunction");
            Map<String, List<Object>> transitionFunction = gson.fromJson(transitionFunctionObject, Map.class);

            // Converte o estado inicial
            String binaryInitialState = tradutorStat(initialState);

            // Converte os estados finais
            for (int i = 0; i < finalStates.size(); i++) {
                finalStates.set(i, tradutorStat(finalStates.get(i)));
            }

            // Converte a função de transição
            String binaryTransitionFunction = tradutorT(transitionFunction);

            // Cria um novo objeto JSON com os valores binários
            JsonObject newJsonObject = new JsonObject();
            newJsonObject.add("tapes", tapesArray);
            newJsonObject.addProperty("initialState", binaryInitialState);
            newJsonObject.add("finalStates", gson.toJsonTree(finalStates));
            newJsonObject.addProperty("transitionFunction", binaryTransitionFunction);

            // Escreve o novo objeto JSON em um arquivo
            try (FileWriter writer = new FileWriter(outputFilePath)) {
                Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                gsonPretty.toJson(newJsonObject, writer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("Erro de estrutura no JSON: " + e.getMessage());
        }
    }

    private String tradutorStat(String input) {
        String state = input.substring(1);
        int value = Integer.parseInt(state);
        for (int i = 0; i < value; i++) {
            state += "1";
        }
        state = state.substring(1);
        state += "1"; 
        return state;
    }

    private String tradutorSimbol(String input) {
        switch (input) {
            case "B":
                return "111";
            case "1":
                return "11";
            case "0":
                return "1";
            default:
                return "0";
        }
    }

    private String tradutorTrans(String input) {
        switch (input) {
            case "L":
                return "1";
            case "R":
                return "11";
            case "N":
                return "111";
            default:
                return "0";
        }
    }

    private String tradutorT(Map<String, List<Object>> transitionFunction) {
        StringBuilder binaryTransitionFunction = new StringBuilder();
        binaryTransitionFunction.append("000");
        for (Map.Entry<String, List<Object>> entry : transitionFunction.entrySet()) {
            String[] keyParts = entry.getKey().substring(1, entry.getKey().length() - 1).split(", ");
            String currentState = tradutorStat(keyParts[0]) + "0";
            String readSymbols = tradutorSimbol(keyParts[1]) + "0" +tradutorSimbol(keyParts[2]) + "0" + tradutorSimbol(keyParts[3]) + "0";

            List<Object> values = entry.getValue();
            String newState = tradutorStat((String) values.get(0)) + "0";
            List<String> newSymbolsList = (List<String>) values.get(1);
            String newSymbols = tradutorSimbol(newSymbolsList.get(0)) + "0" + tradutorSimbol(newSymbolsList.get(1)) + "0" + tradutorSimbol(newSymbolsList.get(2)) + "0";
            List<String> directionsList = (List<String>) values.get(2);
            String directions = tradutorTrans(directionsList.get(0)) + "0" + tradutorTrans(directionsList.get(1)) + "0" + tradutorTrans(directionsList.get(2));

            binaryTransitionFunction.append(currentState).append(readSymbols)
                    .append(newState).append(newSymbols).append(directions).append("00");
        }
        binaryTransitionFunction.append("0");
        return binaryTransitionFunction.toString();
    }
}
