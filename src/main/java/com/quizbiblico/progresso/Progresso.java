package com.quizbiblico.progresso;

import com.quizbiblico.modelo.TipoFiltro;

import java.util.HashMap;
import java.util.Map;

public class Progresso {

    private final Map<String, ProgressoNivel> porChave = new HashMap<>();

    public static String montarChave(int nivel, TipoFiltro tipo, String valor) {
        String complemento = (valor == null) ? "" : valor.toUpperCase();
        return nivel + "|" + tipo + "|" + complemento;
    }

    public ProgressoNivel obter(int nivel, TipoFiltro tipo, String valor) {
        String chave = montarChave(nivel, tipo, valor);
        ProgressoNivel progresso = porChave.get(chave);

        if (progresso == null) {
            progresso = new ProgressoNivel();
            porChave.put(chave, progresso);
        }

        return progresso;
    }

    public void zerar(int nivel, TipoFiltro tipo, String valor) {
        obter(nivel, tipo, valor).zerar();
    }

    public void zerarTudo() {
        porChave.clear();
    }

    public int totalRespondidasGeral() {
        int soma = 0;
        for (ProgressoNivel p : porChave.values()) {
            soma += p.getRespondidas();
        }
        return soma;
    }

    public Map<String, ProgressoNivel> getTodos() {
        return porChave;
    }
}