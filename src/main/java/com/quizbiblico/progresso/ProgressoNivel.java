package com.quizbiblico.progresso;

import java.util.HashSet;
import java.util.Set;

public class ProgressoNivel {

    private final Set<Integer> idsRespondidos = new HashSet<>();
    private int acertos = 0;
    private int erros = 0;

    public void registrar(int idPergunta, boolean acertou) {
        idsRespondidos.add(idPergunta);
        if (acertou) {
            acertos++;
        } else {
            erros++;
        }
    }

    public boolean jaRespondeu(int idPergunta) {
        return idsRespondidos.contains(idPergunta);
    }

    public void zerar() {
        idsRespondidos.clear();
        acertos = 0;
        erros = 0;
    }

    public int getAcertos() {
        return acertos;
    }

    public int getErros() {
        return erros;
    }

    public int getRespondidas() {
        return idsRespondidos.size();
    }

    public Set<Integer> getIdsRespondidos() {
        return idsRespondidos;
    }

    public double percentualAcerto() {
        int total = acertos + erros;
        if (total == 0) {
            return 0.0;
        }
        return (acertos * 100.0) / total;
    }
}