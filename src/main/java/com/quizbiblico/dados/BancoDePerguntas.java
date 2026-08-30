package com.quizbiblico.dados;

import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BancoDePerguntas {

    private static final List<String> ORDEM_CANONICA = List.of(
            "Gênesis", "Êxodo", "Levítico", "Números", "Deuteronômio", "Josué", "Juízes", "Rute",
            "1 Samuel", "2 Samuel", "1 Reis", "2 Reis", "1 Crônicas", "2 Crônicas", "Esdras",
            "Neemias", "Ester", "Jó", "Salmos I", "Salmos II", "Salmos III", "Salmos IV", "Salmos V",
            "Provérbios", "Eclesiastes", "Cântico dos Cânticos", "Isaías", "Jeremias", "Lamentações",
            "Ezequiel", "Daniel", "Oséias", "Joel", "Amós", "Obadias", "Jonas", "Miquéias", "Naum",
            "Habacuque", "Sofonias", "Ageu", "Zacarias", "Malaquias",
            "Mateus", "Marcos", "Lucas", "João", "Atos", "Romanos", "1 Coríntios", "2 Coríntios",
            "Gálatas", "Efésios", "Filipenses", "Colossenses", "1 Tessalonicenses", "2 Tessalonicenses",
            "1 Timóteo", "2 Timóteo", "Tito", "Filemom", "Hebreus", "Tiago", "1 Pedro", "2 Pedro",
            "1 João", "2 João", "3 João", "Judas", "Apocalipse"
    );

    private final List<Pergunta> todas;
    private final List<String> livrosOrdenados;

    public BancoDePerguntas(List<Pergunta> todas) {
        this.todas = todas;
        this.livrosOrdenados = calcularLivrosDisponiveis();
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

    public List<String> livrosDisponiveis() {
        return livrosOrdenados;
    }

    private List<String> calcularLivrosDisponiveis() {
        Set<String> presentes = new HashSet<>();
        for (Pergunta p : todas) {
            presentes.add(p.getLivro());
        }

        List<String> ordenados = new ArrayList<>(presentes);
        ordenados.sort((a, b) -> Integer.compare(posicaoNaOrdemCanonica(a), posicaoNaOrdemCanonica(b)));

        return ordenados;
    }

    private int posicaoNaOrdemCanonica(String livro) {
        int posicao = ORDEM_CANONICA.indexOf(livro);
        return (posicao == -1) ? Integer.MAX_VALUE : posicao;
    }

    public List<Pergunta> buscar(int nivel, TipoFiltro tipo, String valor) {
        List<Pergunta> resultado = new ArrayList<>();

        for (Pergunta p : todas) {
            if (p.getNivel() != nivel) {
                continue;
            }
            if (tipo == TipoFiltro.LIVRO && !valor.equalsIgnoreCase(p.getLivro())) {
                continue;
            }
            if (tipo == TipoFiltro.TESTAMENTO && !valor.equalsIgnoreCase(p.getTestamento())) {
                continue;
            }
            resultado.add(p);
        }

        return resultado;
    }
}