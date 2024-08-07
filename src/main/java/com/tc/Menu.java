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
import java.util.Scanner;
@SuppressWarnings("unchecked") //warnings de cast

public class Menu {
    public void menu() {
        System.out.println("Você está inputando uma máquina com linguagem binaria? (S/N)");
        Scanner scanner = new Scanner(System.in);
        String escolha = scanner.nextLine();
        Gson gsonx = new Gson();
        try (FileReader reader = new FileReader("inputbin.json")) {

        if(escolha.equals("S") || escolha.equals("s")){ 
            BinT call = new BinT();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> input = gsonx.fromJson(reader, type);

            List<String> tapes = (List<String>) input.get("tapes");
            String initialState = (String) input.get("initialState");

            Set<String> finalStates = new HashSet<>((List<String>) input.get("finalStates"));
            List<String> finalStatesList = new ArrayList<>(finalStates);
            Set<String> finalStatesFinal = new HashSet<>();

            initialState = call.tradutorStat(initialState);
            for(int i=0;i<finalStatesList.size();i++){
                finalStatesFinal.add(call.tradutorStat(finalStatesList.get(i)));
            }

            String open= (String) input.get("transitionFunction");
            Map<String, List<Object>> transitions = (Map<String, List<Object>>) call.tradutorT(open);
            
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

            Mtx tm = new Mtx(tapes, 'B', initialState, finalStatesFinal, transitionFunction);
            String result = tm.execute();

            System.out.println("Resultado nas fitas: " + result);
            System.out.println("Histórico das fitas:");
            for (String tapeState : tm.tapeHistory) {
                System.out.println(tapeState);
            }
        } else if (escolha.equals("N") || escolha.equals("n")){     
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
        
        } else {
            System.out.println("Escolha inválida");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
        scanner.close();
    }
}