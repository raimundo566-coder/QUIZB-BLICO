package com.quizbiblico.modelo;

public enum Nivel {
    FACIL (1, "Fácil (kids)", true),
    BASICO (2, "Básico", true),
    INTERMEDIARIA (3, "Intermediário", false),
    DIFICIL (4, "Difícil", false),
    AVANÇADO (5, "Avançado", false),
    HARD (6, "hARD (Teológica e Histórica)", false);

    private final int codigo;
    private final String rotulo;
    private final boolean gratuito;

    Nivel(int codigo, String rotulo, boolean gratuito) {
        this.codigo = codigo;
        this.rotulo = rotulo;
        this.gratuito = gratuito;
    }
    public int getCodigo() { return this.codigo; }
    public String getRotulo() { return this.rotulo; }
    public boolean isGratuito() { return this.gratuito; }

    public static Nivel porCodigo(int codigo) {
        for (Nivel n : values()) {
            if (n.codigo == codigo) {
                return n;
            }
        }
        throw new IllegalArgumentException("Nível inválido: " + codigo);
    }











}
