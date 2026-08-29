package com.quizbiblico.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioTest {

    @Test
    @DisplayName("usuario novo joga os niveis gratuitos, mas nao os pagos")
    void usuarioNovo() {
        Usuario usuario = new Usuario();

        assertTrue(usuario.temAcessoAoNivel(Nivel.porCodigo(1)), "nivel 1 e gratuito");
        assertTrue(usuario.temAcessoAoNivel(Nivel.porCodigo(2)), "nivel 2 e gratuito");

        for (int codigo = 3; codigo <= 6; codigo++) {
            assertFalse(usuario.temAcessoAoNivel(Nivel.porCodigo(codigo)),
                    "nivel " + codigo + " deveria estar bloqueado");
        }
    }

    @Test
    @DisplayName("comprar um nivel libera somente aquele nivel")
    void comprarUmNivel() {
        Usuario usuario = new Usuario();

        usuario.comprarNivel(3);

        assertTrue(usuario.temAcessoAoNivel(Nivel.porCodigo(3)));
        assertFalse(usuario.temAcessoAoNivel(Nivel.porCodigo(4)));
        assertFalse(usuario.isVip());
    }

    @Test
    @DisplayName("VIP libera todos os seis niveis")
    void vipLiberaTudo() {
        Usuario usuario = new Usuario();

        usuario.ativarVip();

        assertTrue(usuario.isVip());
        for (Nivel n : Nivel.values()) {
            assertTrue(usuario.temAcessoAoNivel(n), "deveria liberar: " + n.getRotulo());
        }
    }
}