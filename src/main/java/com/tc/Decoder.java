package com.tc;

import java.util.ArrayList;

public class Decoder {
    Decoder(){    
    }
    public String decState(String state){
        return "q"+(state.length()-1);
    }
    public String decSimbol(String simbol){
        if(simbol.equals("111")){
            return "B";
        } else if(simbol.equals("1")){
            return "0";
        } else if(simbol.equals("11")){
            return "1";
        } else {
            System.err.println("Simbolo invalido");
            return "0";
        }
    }
    public Character decSimbol(Character simbol){
        if(simbol.equals("111")){
            return 'B';
        } else if(simbol.equals("1")){
            return '0';
        } else if(simbol.equals("11")){
            return '1';
        } else {
            System.err.println("Simbolo invalido");
            return '0';
        }
    }
    public String decDirecao(String direcao){
        if(direcao.equals("1")){
            return "L";
        } else if(direcao.equals("11")){
            return "R";
        } else if(direcao.equals("111")){
            return "N";
        } else {
            System.err.println("Direcao invalida");
            return "S";
        }
    }
    public ArrayList<String> decTransicao(String transicao){
        int i=0;
        String aux = new String();
        while(!aux.contains("0")){
            aux = String.valueOf(transicao.charAt(i));
            i++;
        }
        aux.substring(0, i-1);
        transicao.substring(i);
        String estadoatual = decState(aux);
        aux = "";
        while(!aux.contains("0")){
            aux = String.valueOf(transicao.charAt(i));
            i++;
        }
        aux.substring(0, i-1);
        transicao.substring(i);
        String simbololeitura = decSimbol(aux);
        aux = "";
        while(!aux.contains("0")){
            aux = String.valueOf(transicao.charAt(i));
            i++;
        }
        aux.substring(0, i-1);
        transicao.substring(i);
        String estadotransicao = decState(aux);
        aux = "";
        while(!aux.contains("0")){
            aux = String.valueOf(transicao.charAt(i));
            i++;
        }
        aux.substring(0, i-1);
        transicao.substring(i);
        String simboloescrita = decSimbol(aux);
        aux = "";
        while(!aux.contains("0")){
            aux = String.valueOf(transicao.charAt(i));
            i++;
        }
        aux.substring(0, i-1);
        transicao.substring(i);
        String direction = decDirecao(aux);
        ArrayList <String> x = new ArrayList<>();
        x.add(estadoatual);
        x.add(simbololeitura);
        x.add(estadotransicao);
        x.add(simboloescrita);
        x.add(direction);
        return x;
    }
}
