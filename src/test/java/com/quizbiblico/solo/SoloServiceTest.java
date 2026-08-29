package com.quizbiblico.solo;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;
import com.quizbiblico.modelo.Usuario;
import com.quizbiblico.progresso.Progresso;
import com.quizbiblico.progresso.ProgressoNivel;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SoloServiceTest {

    private static List<Pergunta> perguntasDoNivel(int quantidade, int nivel) {
        List<Pergunta> lista = new ArrayList<>();
        int idBase = nivel * 1000;

        for (int i = 0; i < quantidade; i++) {
            lista.add(new Pergunta(
                    idBase + i,
                    "Pergunta " + i,
                    "Alternativa A", "Alternativa B", "Alternativa C", "Alternativa D",
                    0,
                    nivel,
                    "AT",
                    "Teste",
                    "Tt " + i + ":1"));
        }

        return lista;
    }

    @Test
    @DisplayName("a segunda partida nao repete nenhuma pergunta da primeira")
    void naoRepetePerguntaEntrePartidas() {
        BancoDePerguntas banco = new BancoDePerguntas(perguntasDoNivel(20, 1));
        SoloService solo = new SoloService(banco, new Progresso(), new Usuario());

        Partida primeira = solo.iniciar(1, TipoFiltro.GERAL, null);
        List<Integer> idsDaPrimeira = new ArrayList<>();
        while (primeira.temProxima()) {
            Pergunta atual = primeira.atual();
            idsDaPrimeira.add(atual.getId());
            solo.responder(primeira, atual.getRespostaCorreta());
        }

        Partida segunda = solo.iniciar(1, TipoFiltro.GERAL, null);
        while (segunda.temProxima()) {
            Pergunta atual = segunda.atual();
            assertFalse(idsDaPrimeira.contains(atual.getId()),
                    "id " + atual.getId() + " ja tinha caido na primeira partida");
            solo.responder(segunda, atual.getRespostaCorreta());
        }
    }

    @Test
    @DisplayName("com menos de 10 perguntas restantes, a rodada usa o que sobrou")
    void ultimaRodadaComMenosQueDez() {
        BancoDePerguntas banco = new BancoDePerguntas(perguntasDoNivel(3, 1));
        SoloService solo = new SoloService(banco, new Progresso(), new Usuario());

        Partida partida = solo.iniciar(1, TipoFiltro.GERAL, null);

        assertEquals(3, partida.getTotal());
    }

    @Test
    @DisplayName("depois de responder tudo, tentar iniciar de novo lanca excecao")
    void acervoEsgotadoLancaExcecao() {
        BancoDePerguntas banco = new BancoDePerguntas(perguntasDoNivel(2, 1));
        SoloService solo = new SoloService(banco, new Progresso(), new Usuario());

        Partida partida = solo.iniciar(1, TipoFiltro.GERAL, null);
        while (partida.temProxima()) {
            Pergunta atual = partida.atual();
            solo.responder(partida, atual.getRespostaCorreta());
        }

        assertThrows(IllegalStateException.class,
                () -> solo.iniciar(1, TipoFiltro.GERAL, null));
    }

    @Test
    @DisplayName("nivel pago sem compra nem VIP lanca excecao ao iniciar")
    void nivelBloqueadoLancaExcecao() {
        BancoDePerguntas banco = new BancoDePerguntas(perguntasDoNivel(15, 4));
        SoloService solo = new SoloService(banco, new Progresso(), new Usuario());

        assertFalse(solo.podeJogar(4));
        assertThrows(IllegalStateException.class,
                () -> solo.iniciar(4, TipoFiltro.GERAL, null));
    }

    @Test
    @DisplayName("responder soma no placar da partida e no historico do progresso")
    void responderAtualizaPlacarEProgresso() {
        BancoDePerguntas banco = new BancoDePerguntas(perguntasDoNivel(5, 1));
        Progresso progresso = new Progresso();
        SoloService solo = new SoloService(banco, progresso, new Usuario());

        Partida partida = solo.iniciar(1, TipoFiltro.GERAL, null);
        Pergunta primeira = partida.atual();

        boolean acertou = solo.responder(partida, primeira.getRespostaCorreta());

        assertTrue(acertou);
        assertEquals(1, partida.getAcertos());

        ProgressoNivel caderneta = progresso.obter(1, TipoFiltro.GERAL, null);
        assertEquals(1, caderneta.getRespondidas());
        assertTrue(caderneta.jaRespondeu(primeira.getId()));
    }
}