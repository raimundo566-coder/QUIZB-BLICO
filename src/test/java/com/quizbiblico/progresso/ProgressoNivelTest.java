package com.quizbiblico.progresso;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProgressoNivelTest {

    @Test
    @DisplayName("caderneta nova comeca zerada")
    void cadernetaNovaComecaZerada() {
        ProgressoNivel caderneta = new ProgressoNivel();

        assertEquals(0, caderneta.getRespondidas());
        assertEquals(0, caderneta.getAcertos());
        assertEquals(0, caderneta.getErros());
        assertEquals(0.0, caderneta.percentualAcerto());
    }

    @Test
    @DisplayName("registrar acerto soma no placar e marca a pergunta")
    void registrarAcerto() {
        ProgressoNivel caderneta = new ProgressoNivel();

        caderneta.registrar(1001, true);

        assertEquals(1, caderneta.getAcertos());
        assertEquals(0, caderneta.getErros());
        assertTrue(caderneta.jaRespondeu(1001));
        assertFalse(caderneta.jaRespondeu(9999));
    }

    @Test
    @DisplayName("registrar erro soma nos erros")
    void registrarErro() {
        ProgressoNivel caderneta = new ProgressoNivel();

        caderneta.registrar(2002, false);

        assertEquals(0, caderneta.getAcertos());
        assertEquals(1, caderneta.getErros());
        assertTrue(caderneta.jaRespondeu(2002));
    }

    @Test
    @DisplayName("3 acertos em 5 perguntas da 60 por cento")
    void percentualDeAcerto() {
        ProgressoNivel caderneta = new ProgressoNivel();

        caderneta.registrar(1, true);
        caderneta.registrar(2, true);
        caderneta.registrar(3, true);
        caderneta.registrar(4, false);
        caderneta.registrar(5, false);

        assertEquals(5, caderneta.getRespondidas());
        assertEquals(60.0, caderneta.percentualAcerto());
    }

    @Test
    @DisplayName("a mesma pergunta nao conta duas vezes como respondida")
    void naoDuplicaId() {
        ProgressoNivel caderneta = new ProgressoNivel();

        caderneta.registrar(1001, true);
        caderneta.registrar(1001, false);

        assertEquals(1, caderneta.getRespondidas());
    }

    @Test
    @DisplayName("zerar limpa placar e historico")
    void zerarLimpaTudo() {
        ProgressoNivel caderneta = new ProgressoNivel();
        caderneta.registrar(1001, true);
        caderneta.registrar(1002, false);

        caderneta.zerar();

        assertEquals(0, caderneta.getRespondidas());
        assertEquals(0, caderneta.getAcertos());
        assertEquals(0, caderneta.getErros());
        assertFalse(caderneta.jaRespondeu(1001));
    }
}