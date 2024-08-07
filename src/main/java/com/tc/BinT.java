package com.tc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinT{
    public String tradutorStat(String input){
        String output = "q" + (input.length()-1);
        return output;
    }
    public String tradutorSimbol(String input){
        switch (input) {
            case "111":
                return "B";
            case "11":
                return "1";
            case "1":
                return "0";
            default:
                return "0";
        }
    }
    public String tradutorTrans(String input){
        switch (input) {
            case "1":
                return "L";
            case "11":
                return "R";
            case "111":
                return "N";
            default:
                return "N";
        }
    }
    public Map<String, List<Object>> tradutorT(String var){
        var = var.substring(3, var.length()-3);
        String [] transicoes = var.split("00");
        Map<String, List<Object>> transitions = new HashMap<>();
        for (int i=0; i<transicoes.length;i++){
            String [] aux = transicoes[i].split("0");
            aux[0] = tradutorStat(aux[0]);
            for(int j=1;j<4;j++){
                aux[j] = tradutorSimbol(aux[j]);
            }
            aux[4] = tradutorStat(aux[4]);
            for(int j=5;j<8;j++){
                aux[j] = tradutorSimbol(aux[j]);
            }
            for(int j=8;j<11;j++){
                aux[j] = tradutorTrans(aux[j]);
            }
            transitions.put("("+aux[0]+","+aux[1]+","+aux[2]+","+aux[3]+")", Arrays.asList(aux[4], aux[5]+","+aux[6]+","+
            aux[7], Arrays.asList(aux[8]+","+aux[9]+","+aux[10])));
        }
        return transitions;
    }
}
