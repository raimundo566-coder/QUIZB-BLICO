package com.quizbiblico.dados;

import com.quizbiblico.modelo.Pergunta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerificadorCSV {

    public List<String> verificar(List<Pergunta> perguntas) {

        List<String> problemas = new ArrayList<>();
        Set<Integer> idsVistos = new HashSet<>();
        Set<String> textosVistos = new HashSet<>();

        for (Pergunta p : perguntas) {

            if (!idsVistos.add(p.getId())) {
                problemas.add("ID repetido: " + p.getId());
            }

            if (!textosVistos.add(p.getTexto().toLowerCase())) {
                problemas.add("Pergunta repetida (id " + p.getId() + "): " + p.getTexto());
            }

            if (p.getRespostaCorreta() < 0 || p.getRespostaCorreta() > 3) {
                problemas.add("Resposta fora de 0-3 (id " + p.getId() + ")");
            }

            if (p.getNivel() < 1 || p.getNivel() > 6) {
                problemas.add("Nível fora de 1-6 (id " + p.getId() + ")");
            }

            if (!p.getTestamento().equals("AT") && !p.getTestamento().equals("NT")) {
                problemas.add("Testamento inválido (id " + p.getId() + "): " + p.getTestamento());
            }

            if (p.getTexto().isBlank()) {
                problemas.add("Pergunta vazia (id " + p.getId() + ")");
            }

            if (p.getReferencia().isBlank()) {
                problemas.add("Referência vazia (id " + p.getId() + ")");
            }

            Set<String> altVistas = new HashSet<>();
            for (String alt : p.getAlternativas()) {
                if (alt.isBlank()) {
                    problemas.add("Alternativa vazia (id " + p.getId() + ")");
                } else if (!altVistas.add(alt.toLowerCase())) {
                    problemas.add("Alternativa repetida (id " + p.getId() + "): " + alt);
                }
            }
        }

        return problemas;
    }
}