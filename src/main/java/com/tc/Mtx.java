package com.tc;

import java.util.*;

public class Mtx {
    private List<List<Character>> tapes;
    private char blankSymbol;
    private List<Integer> headPositions;
    private String currentState;
    private Set<String> finalStates;
    private Map<Pair<String, List<Character>>, Transition> transitionFunction;
    List<String> tapeHistory;

    public Mtx(List<String> tapes, char blankSymbol, String initialState, Set<String> finalStates, 
                         Map<Pair<String, List<Character>>, Transition> transitionFunction) {
        this.tapes = new ArrayList<>();
        for (String tape : tapes) {
            List<Character> tapeList = new ArrayList<>();
            for (char c : tape.toCharArray()) {
                tapeList.add(c);
            }
            this.tapes.add(tapeList);
        }
        this.blankSymbol = blankSymbol;
        this.headPositions = new ArrayList<>(Collections.nCopies(tapes.size(), 0));
        this.currentState = initialState;
        this.finalStates = finalStates != null ? finalStates : new HashSet<>();
        this.transitionFunction = transitionFunction != null ? transitionFunction : new HashMap<>();
        this.tapeHistory = new ArrayList<>();
        this.tapeHistory.add(getTapeString());
    }

    private String getTapeString() {
        StringBuilder sb = new StringBuilder();
        for (List<Character> tape : tapes) {
            for (Character c : tape) {
                sb.append(c);
            }
            sb.append(" | ");
        }
        return sb.toString().trim();
    }

    public boolean step() {
        if (finalStates.contains(currentState)) {
            return false;
        }

        List<Character> tapeSymbols = new ArrayList<>();
        for (int i = 0; i < tapes.size(); i++) {
            if (headPositions.get(i) >= tapes.get(i).size()) {
                tapeSymbols.add(blankSymbol);
            } else {
                tapeSymbols.add(tapes.get(i).get(headPositions.get(i)));
            }
        }

        Pair<String, List<Character>> key = new Pair<>(currentState, tapeSymbols);
        if (!transitionFunction.containsKey(key)) {
            return false;
        }

        Transition transition = transitionFunction.get(key);
        currentState = transition.newState;

        for (int i = 0; i < tapes.size(); i++) {
            if (headPositions.get(i) >= tapes.get(i).size()) {
                tapes.get(i).add(blankSymbol);
            }
            tapes.get(i).set(headPositions.get(i), transition.newSymbols.get(i));
            if (transition.directions.get(i) == Direction.R) {
                headPositions.set(i, headPositions.get(i) + 1);
                if (headPositions.get(i) == tapes.get(i).size()) {
                    tapes.get(i).add(blankSymbol);
                }
            } else if (transition.directions.get(i) == Direction.L) {
                if (headPositions.get(i) == 0) {
                    tapes.get(i).add(0, blankSymbol);
                } else {
                    headPositions.set(i, headPositions.get(i) - 1);
                }
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

    static class Transition {
        String newState;
        List<Character> newSymbols;
        List<Direction> directions;

        Transition(String newState, List<Character> newSymbols, List<Direction> directions) {
            this.newState = newState;
            this.newSymbols = newSymbols;
            this.directions = directions;
        }
    }

    enum Direction {
        R, L, N
    }

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
