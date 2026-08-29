package com.quizbiblico.solo;

import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;

import java.util.List;

public class Partida {

    private final List<Pergunta> perguntas;
    private final int nivel;
    private final TipoFiltro tipo;
    private final String valor;

    private int indiceAtual = 0;
    private int acertos = 0;
    private int erros = 0;

    public Partida(List<Pergunta> perguntas, int nivel, TipoFiltro tipo, String valor) {
        this.perguntas = perguntas;
        this.nivel = nivel;
        this.tipo = tipo;
        this.valor = valor;
    }

    public boolean temProxima() {
        return indiceAtual < perguntas.size();
    }

    public Pergunta atual() {
        return perguntas.get(indiceAtual);
    }

    public boolean responder(int indiceAlternativa) {
        Pergunta pergunta = perguntas.get(indiceAtual);
        boolean acertou = pergunta.isCorreta(indiceAlternativa);

        if (acertou) {
            acertos++;
        } else {
            erros++;
        }

        indiceAtual++;
        return acertou;
    }

    public int getTotal() {
        return perguntas.size();
    }

    public int getNumeroAtual() {
        return indiceAtual + 1;
    }

    public int getAcertos() {
        return acertos;
    }

    public int getErros() {
        return erros;
    }

    public double percentualAcerto() {
        int respondidas = acertos + erros;
        if (respondidas == 0) {
            return 0.0;
        }
        return (acertos * 100.0) / respondidas;
    }

    public int getNivel() {
        return nivel;
    }

    public TipoFiltro getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }
}