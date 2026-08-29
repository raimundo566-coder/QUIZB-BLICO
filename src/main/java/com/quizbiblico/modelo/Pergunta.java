package com.quizbiblico.modelo;

public class Pergunta {
    private int id;
    private String texto;
    private String alternativaA;
    private String alternativaB;
    private String alternativaC;
    private String alternativaD;
    private int respostaCorreta;
    private int nivel;
    private String testamento;
    private String livro;
    private String referencia;


    public Pergunta(int id, String texto, String alternativaA, String alternativaB, String alternativaC, String alternativaD, int respostaCorreta, int nivel, String testamento, String livro, String referencia) {
        this.id = id;
        this.texto = texto;
        this.alternativaA = alternativaA;
        this.alternativaB = alternativaB;
        this.alternativaC = alternativaC;
        this.alternativaD = alternativaD;
        this.respostaCorreta = respostaCorreta;
        this.nivel = nivel;
        this.testamento = testamento;
        this.livro = livro;
        this.referencia = referencia;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
    public String getAlternativaA() {
        return alternativaA;
    }
    public void setAlternativaA(String alternativaA) {
        this.alternativaA = alternativaA;
    }
    public String getAlternativaB() {
        return alternativaB;
    }
    public void setAlternativaB(String alternativaB) {
        this.alternativaB = alternativaB;
    }
    public String getAlternativaC() {
        return alternativaC;
    }
    public void setAlternativaC(String alternativaC) {
        this.alternativaC = alternativaC;
    }
    public String getAlternativaD() {
        return alternativaD;
    }
    public void setAlternativaD(String alternativaD) {
        this.alternativaD = alternativaD;
    }
    public int getRespostaCorreta() {
        return respostaCorreta;
    }
    public void setRespostaCorreta(int respostaCorreta) {
        this.respostaCorreta = respostaCorreta;
    }
    public int getNivel() {
        return nivel;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    public String getTestamento() {
        return testamento;
    }
    public void setTestamento(String testamento) {
        this.testamento = testamento;
    }
    public String getLivro() {
        return livro;
    }
    public void setLivro(String livro) {
        this.livro = livro;
    }
    public String getReferencia() {return referencia;}
    public void setReferencia(String referencia) {this.referencia = referencia;}
    // Devolve as quatro alternativas numa única caixa, em ordem.
// Evita ficar escrevendo getAlternativaA(), getAlternativaB()...
    public String[] getAlternativas() {
        return new String[] { alternativaA, alternativaB, alternativaC, alternativaD };
    }

    // Recebe o índice escolhido pelo jogador (0=A, 1=B, 2=C, 3=D)
// e responde só uma coisa: acertou ou não.
    public boolean isCorreta(int indice) {
        return indice == respostaCorreta;
    }

    // Facilita a depuração: ao imprimir a pergunta, sai algo legível.
    @Override
    public String toString() {
        return "[" + id + "] N" + nivel + " " + livro + " - " + texto;
    }
}

