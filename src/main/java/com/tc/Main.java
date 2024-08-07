package com.tc;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
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

        Menu menu = new Menu();
        menu.menu();

        Gson gsony = new Gson();
        try (FileReader reader = new FileReader("novo.json")) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> input = gsony.fromJson(reader, type);

            List<String> tapes = (List<String>) input.get("tapes");
            String initialState = (String) input.get("initialState");
            Set<String> finalStates = new HashSet<>((List<String>) input.get("finalStates"));

            Map<String, List<Object>> transitions = (Map<String, List<Object>>) input.get("transitionFunction");
            Map<Mtx.Pair<String, List<Character>>, Mtx.Transition> transitionFunction = new HashMap<>();
            for (Map.Entry<String, List<Object>> entry : transitions.entrySet()) {
                String[] keyParts = entry.getKey().substring(1, entry.getKey().length() - 1).split(", ");
                String state = keyParts[0];
                List<Character> symbols = new ArrayList<>();
                for (String symbol : Arrays.copyOfRange(keyParts, 1, keyParts.length)) {
                    symbols.add(symbol.charAt(0));
                }

                List<Object> valueParts = entry.getValue();
                String newState = (String) valueParts.get(0);
                List<Character> newSymbols = new ArrayList<>();
                for (String newSymbol : (List<String>) valueParts.get(1)) {
                    newSymbols.add(newSymbol.charAt(0));
                }
                List<Mtx.Direction> directions = new ArrayList<>();
                for (String direction : (List<String>) valueParts.get(2)) {
                    directions.add(Mtx.Direction.valueOf(direction));
                }

                transitionFunction.put(new Mtx.Pair<>(state, symbols), new Mtx.Transition(newState, newSymbols, directions));
            }

            Mtx tm = new Mtx(tapes, 'B', initialState, finalStates, transitionFunction);
            String result = tm.execute();

            System.out.println("Resultado nas fitas: " + result);
            System.out.println("Histórico das fitas:");
            for (String tapeState : tm.tapeHistory) {
                System.out.println(tapeState);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}