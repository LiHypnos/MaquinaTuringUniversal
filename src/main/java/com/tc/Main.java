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

        Menu menu = new Menu();
        menu.menu();
    }
}