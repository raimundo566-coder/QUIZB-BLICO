package com.quizbiblico.dados;

import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BancoDePerguntas {

    private final List<Pergunta> todas;

    public BancoDePerguntas(List<Pergunta> todas) {
        this.todas = todas;
    }

    public int total() {
        return todas.size();
    }

    public int totalPorNivel(int nivel) {
        int contador = 0;
        for (Pergunta p : todas) {
            if (p.getNivel() == nivel) {
                contador++;
            }
        }
        return contador;
    }

    public Set<String> livrosDisponiveis() {
        Set<String> livros = new TreeSet<>();
        for (Pergunta p : todas) {
            livros.add(p.getLivro());
        }
        return livros;
    }

    public List<Pergunta> buscar(int nivel, TipoFiltro tipo, String valor) {
        List<Pergunta> resultado = new ArrayList<>();

        for (Pergunta p : todas) {
            if (p.getNivel() != nivel) {
                continue;
            }
            if (tipo == TipoFiltro.LIVRO && !p.getLivro().equalsIgnoreCase(valor)) {
                continue;
            }
            if (tipo == TipoFiltro.TESTAMENTO && !p.getTestamento().equalsIgnoreCase(valor)) {
                continue;
            }
            resultado.add(p);
        }

        return resultado;
    }
}