package com.tc;

import java.util.*;

public class Mt {
    private List<Character> tape;
    private char blankSymbol;
    private int headPosition;
    private String currentState;
    private Set<String> finalStates;
    private Map<Pair<String, Character>, Transition> transitionFunction;
    List<String> tapeHistory;

    public Mt(String tape, char blankSymbol, String initialState, Set<String> finalStates, 
                         Map<Pair<String, Character>, Transition> transitionFunction) {
        this.tape = new ArrayList<>();
        for (char c : tape.toCharArray()) {
            this.tape.add(c);
        }
        this.blankSymbol = blankSymbol;
        this.headPosition = 0;
        this.currentState = initialState;
        this.finalStates = finalStates != null ? finalStates : new HashSet<>();
        this.transitionFunction = transitionFunction != null ? transitionFunction : new HashMap<>();
        this.tapeHistory = new ArrayList<>();
        this.tapeHistory.add(getTapeString());
    }

    private String getTapeString() {
        StringBuilder sb = new StringBuilder();
        for (Character c : tape) {
            sb.append(c);
        }
        return sb.toString().trim();
    }

    public boolean step() {
        if (finalStates.contains(currentState)) {
            return false;
        }

        char tapeSymbol = tape.get(headPosition);
        Pair<String, Character> key = new Pair<>(currentState, tapeSymbol);
        if (!transitionFunction.containsKey(key)) {
            return false;
        }

        Transition transition = transitionFunction.get(key);
        tape.set(headPosition, transition.newSymbol);
        currentState = transition.newState;

        if (transition.direction == Direction.R) {
            headPosition++;
            if (headPosition == tape.size()) {
                tape.add(blankSymbol);
            }
        } else if (transition.direction == Direction.L) {
            if (headPosition == 0) {
                tape.add(0, blankSymbol);
            } else {
                headPosition--;
            }
        }

        tapeHistory.add(getTapeString());
        return true;
    }

    public String execute() {
        while (step()) {
            // Continue stepping
        }
        return getTapeString();
    }

    // Para representar a transição de estados
    static class Transition {
        String newState;
        char newSymbol;
        Direction direction;

        Transition(String newState, char newSymbol, Direction direction) {
            this.newState = newState;
            this.newSymbol = newSymbol;
            this.direction = direction;
        }
    }

    // Para representar direções
    enum Direction {
        R, L, N
    }

    // Para representar pares de estados e símbolos
    static class Pair<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Pair)) return false;
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
