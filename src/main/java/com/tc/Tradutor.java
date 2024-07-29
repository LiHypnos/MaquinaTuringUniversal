package com.tc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tc.Mt.Direction;
import com.tc.Transicao.Direcao;

public class Tradutor{
    public Tradutor(){
    }

    public String en(String valor){   //encode
        switch(valor){
            case "B":
                return "111";
            case "0":
                return "1";
            case "1":
                return "11";
            case "L":
                return "1";
            case "R":
                return "11";
            default:
                String x = "111";
                int y = Integer.parseInt(valor);
                for(int contador = 2; contador < y; contador++){
                    x = x + "1";
                }
                return x;
        }
    }
    
    public String parserCompleto(String estado, String leitura, String estadoT, String escrita, Direcao direcao){
        String x = new String();
        if (direcao == Direcao.L){
            x = "L";
        } else if (direcao == Direcao.R){
            x = "R";
        }
        return en(estado.substring(1))+"0"+en(leitura)+"0"+en(estadoT.substring(1))+"0"+en(escrita)+"0"+en(x);
    }

    public List<String> en(List<String> x) {
        List<String> y = new ArrayList<>();
        for(String z : (List<String>) x){
            y.add(en(z));
        }
        return y;
    }

    public char en(char charAt) {
        return en(String.valueOf(charAt)).charAt(0);
    }

    public Map<String, List<String>> en(Map<String, List<String>> map) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            map.put(en(entry.getKey()), en(entry.getValue()));
        }
        return map;
    }

    public String en(Direction valueOf) {
        if(valueOf == Direction.L){
            return "1";
        } else if (valueOf == Direction.R){
            return "11";
        } else if (valueOf == Direction.N){
            return "111";
        } else {
            System.err.println("Direção inválida");
            return null;
        }
    }
}
