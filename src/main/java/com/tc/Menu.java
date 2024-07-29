package com.tc;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
        try (FileReader reader = new FileReader("input.json")) {

        /*if(escolha.equals("S") || escolha.equals("s")){ ERROR: DECODER CAST NOT WORKING   
            Decoder dec = new Decoder();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> input = gsonx.fromJson(reader, type);
            
            String tape = (String) input.get("tape");
            String initialState = (String) input.get("initialState");
            ArrayList<String> finalStates = (ArrayList<String>) input.get("finalStates");
            for (int i = 0; i < finalStates.size(); i++) {
                finalStates.set(i, dec.decState(finalStates.get(i)));
            }
            Set <String> newFinal = new HashSet<>(finalStates);
            
            ArrayList<String> transitions = new ArrayList<String>((List<String>) input.get("transitionFunction"));
            Map<Mt.Pair<String, Character>, Mt.Transition> transitionFunction = new HashMap<>();
            for (String entry : transitions) {
                ArrayList<String> x = new ArrayList<>(dec.decTransicao(entry));
                String state = x.get(0);
                char symbol = dec.decSimbol(x.get(1)).charAt(0);
                String newState = x.get(2);
                char newSymbol = dec.decSimbol(x.get(3)).charAt(0);
            
                Mt.Direction direction = Mt.Direction.valueOf(dec.decDirecao(x.get(4)));
            
                transitionFunction.put(new Mt.Pair<>(state, symbol), new Mt.Transition(newState, newSymbol, direction));
            }
            Mt tm = new Mt(tape, 'B', dec.decState(initialState), newFinal, transitionFunction);
            String result = tm.execute();
            
            System.out.println("Resultado na fita: " + result);
            System.out.println("Histórico da fita: " + tm.tapeHistory);

        } else*/ if (escolha.equals("N") || escolha.equals("n")){     
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