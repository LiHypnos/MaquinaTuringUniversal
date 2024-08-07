package com.tc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinT {
    public String tradutorStat(String input) {
        return "q" + (input.length() - 1);
    }

    public String tradutorSimbol(String input) {
        switch (input) {
            case "111":
                return "B";
            case "11":
                return "1";
            case "1":
                return "0";
            default:
                return "";
        }
    }

    public String tradutorTrans(String input) {
        switch (input) {
            case "1":
                return "L";
            case "11":
                return "R";
            case "111":
                return "N";
            default:
                return "";
        }
    }

    public Map<String, List<Object>> tradutorT(String var) {
        var = var.substring(3, var.length() - 3);
        String[] transicoes = var.split("00");
        Map<String, List<Object>> transitions = new HashMap<>();

        for (String transicao : transicoes) {
            String[] aux = transicao.split("0");
            String currentState = tradutorStat(aux[0]);
            List<String> readSymbols = Arrays.asList(tradutorSimbol(aux[1]), tradutorSimbol(aux[2]), tradutorSimbol(aux[3]));
            String nextState = tradutorStat(aux[4]);
            List<String> writeSymbols = Arrays.asList(tradutorSimbol(aux[5]), tradutorSimbol(aux[6]), tradutorSimbol(aux[7]));
            List<String> directions = Arrays.asList(tradutorTrans(aux[8]), tradutorTrans(aux[9]), tradutorTrans(aux[10]));

            transitions.put("(" + currentState + ", " + String.join(", ", readSymbols) + ")", 
                            Arrays.asList(nextState, writeSymbols, directions));
        }

        return transitions;
    }
}
