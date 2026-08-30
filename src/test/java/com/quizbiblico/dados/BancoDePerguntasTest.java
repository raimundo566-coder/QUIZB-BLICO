package com.quizbiblico.dados;

import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BancoDePerguntasTest {

    private BancoDePerguntas banco;

    @BeforeEach
    void montarAcervo() {
        List<Pergunta> perguntas = new ArrayList<>();
        // de proposito fora de ordem: Mateus e Exodo antes de Genesis
        perguntas.add(pergunta(8, 1, "NT", "Mateus"));
        perguntas.add(pergunta(6, 1, "AT", "Êxodo"));
        perguntas.add(pergunta(7, 1, "AT", "Êxodo"));
        perguntas.add(pergunta(1, 1, "AT", "Gênesis"));
        perguntas.add(pergunta(2, 1, "AT", "Gênesis"));
        perguntas.add(pergunta(3, 1, "AT", "Gênesis"));
        perguntas.add(pergunta(4, 2, "AT", "Gênesis"));
        perguntas.add(pergunta(5, 2, "AT", "Gênesis"));

        banco = new BancoDePerguntas(perguntas);
    }

    private static Pergunta pergunta(int id, int nivel, String testamento, String livro) {
        return new Pergunta(id, "Pergunta " + id, "A", "B", "C", "D",
                0, nivel, testamento, livro, "Ref " + id);
    }

    @Test
    @DisplayName("total conta todas as perguntas do acervo")
    void totalContaTudo() {
        assertEquals(8, banco.total());
    }

    @Test
    @DisplayName("totalPorNivel soma perguntas de todos os livros naquele nivel")
    void totalPorNivelSomaLivros() {
        assertEquals(6, banco.totalPorNivel(1));
        assertEquals(2, banco.totalPorNivel(2));
        assertEquals(0, banco.totalPorNivel(6));
    }

    @Test
    @DisplayName("livrosDisponiveis lista cada livro uma vez so")
    void livrosDisponiveisSemRepetir() {
        List<String> livros = banco.livrosDisponiveis();

        assertEquals(3, livros.size());
        assertTrue(livros.contains("Gênesis"));
        assertTrue(livros.contains("Êxodo"));
        assertTrue(livros.contains("Mateus"));
    }

    @Test
    @DisplayName("livrosDisponiveis respeita a ordem biblica, nao a ordem de insercao")
    void livrosDisponiveisRespeitaOrdemCanonica() {
        List<String> livros = banco.livrosDisponiveis();

        assertEquals(List.of("Gênesis", "Êxodo", "Mateus"), livros);
    }

    @Test
    @DisplayName("buscar por livro ignora maiusculas e minusculas")
    void buscarPorLivroIgnoraCase() {
        assertEquals(3, banco.buscar(1, TipoFiltro.LIVRO, "Gênesis").size());
        assertEquals(3, banco.buscar(1, TipoFiltro.LIVRO, "gênesis").size());
        assertEquals(3, banco.buscar(1, TipoFiltro.LIVRO, "GÊNESIS").size());
    }

    @Test
    @DisplayName("buscar por testamento junta todos os livros daquele testamento")
    void buscarPorTestamentoJuntaLivros() {
        List<Pergunta> resultado = banco.buscar(1, TipoFiltro.TESTAMENTO, "AT");

        assertEquals(5, resultado.size());
        for (Pergunta p : resultado) {
            assertEquals("AT", p.getTestamento());
        }
    }

    @Test
    @DisplayName("buscar geral ignora livro e testamento, so filtra o nivel")
    void buscarGeralIgnoraLivroETestamento() {
        assertEquals(6, banco.buscar(1, TipoFiltro.GERAL, null).size());
    }

    @Test
    @DisplayName("buscar um livro ou nivel que nao existe devolve lista vazia, nunca null")
    void buscarInexistenteDevolveVazio() {
        assertTrue(banco.buscar(2, TipoFiltro.LIVRO, "Levítico").isEmpty());
        assertTrue(banco.buscar(9, TipoFiltro.GERAL, null).isEmpty());
    }
}