package com.tc;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {
    @SuppressWarnings("unchecked") //warnings de cast
    public static void main(String[] args) {
        Gson gson = new Gson();
        String json = ArquivosIO.lerConteudoArquivo("exemplo.json");

        Transicao t = gson.fromJson(json, Transicao.class);

        t.printTransicao();

        Tradutor tc = new Tradutor();
        System.out.println(tc.parserCompleto(t.getEstado(),t.getCaractereLeitura(),t.getEstadoTransicao(),t.getCaractereEscrita(),t.getDirecao()));

        Gson gsonx = new Gson();
        try (FileReader reader = new FileReader("input.json")) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> input = gsonx.fromJson(reader, type);

            String tape = (String) input.get("tape");
            String initialState = (String) input.get("initialState");
            Set<String> finalStates = new HashSet<>((List<String>) input.get("finalStates"));

            Map<String, List<String>> transitions = (Map<String, List<String>>) input.get("transitionFunction");
            Map<Mt.Pair<String, Character>, Mt.Transition> transitionFunction = new HashMap<>();
            for (Map.Entry<String, List<String>> entry : transitions.entrySet()) {
                String[] keyParts = entry.getKey().substring(1, entry.getKey().length() - 1).split(", ");
                String state = keyParts[0];
                char symbol = keyParts[1].charAt(0);

                List<String> valueParts = entry.getValue();
                String newState = valueParts.get(0);
                char newSymbol = valueParts.get(1).charAt(0);
                Mt.Direction direction = Mt.Direction.valueOf(valueParts.get(2));

                transitionFunction.put(new Mt.Pair<>(state, symbol), new Mt.Transition(newState, newSymbol, direction));
            }

            Mt tm = new Mt(tape, 'B', initialState, finalStates, transitionFunction);
            String result = tm.execute();

            System.out.println("Resultado na fita: " + result);
            System.out.println("Histórico da fita: " + tm.tapeHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}