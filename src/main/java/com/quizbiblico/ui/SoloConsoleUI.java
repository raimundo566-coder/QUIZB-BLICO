package com.quizbiblico.ui;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.modelo.Nivel;
import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;
import com.quizbiblico.solo.Partida;
import com.quizbiblico.solo.SoloService;

import java.util.ArrayList;
import java.util.List;

public class SoloConsoleUI {

    private static final String LETRAS = "ABCD";

    private final SoloService solo;
    private final BancoDePerguntas banco;
    private final Entrada entrada;

    public SoloConsoleUI(SoloService solo, BancoDePerguntas banco, Entrada entrada) {
        this.solo = solo;
        this.banco = banco;
        this.entrada = entrada;
    }

    public void abrir() {
        while (true) {
            System.out.println();
            System.out.println("========== MODO SOLO ==========");
            System.out.println("   1 - Por livro");
            System.out.println("   2 - Por testamento");
            System.out.println("   3 - Geral (a Biblia toda)");
            System.out.println("   0 - Voltar");
            System.out.println("===============================");

            int opcao = entrada.lerInteiro("Escolha: ", 0, 3);

            if (opcao == 0) {
                return;
            }

            TipoFiltro tipo;
            String valor;

            if (opcao == 1) {
                tipo = TipoFiltro.LIVRO;
                valor = escolherLivro();
                if (valor == null) {
                    continue;
                }
            } else if (opcao == 2) {
                tipo = TipoFiltro.TESTAMENTO;
                valor = escolherTestamento();
                if (valor == null) {
                    continue;
                }
            } else {
                tipo = TipoFiltro.GERAL;
                valor = null;
            }

            menuDeNiveis(tipo, valor);
        }
    }

    private String escolherLivro() {
        List<String> livros = new ArrayList<>(banco.livrosDisponiveis());

        System.out.println();
        System.out.println("--------- LIVROS ---------");
        for (int i = 0; i < livros.size(); i++) {
            System.out.println("   " + (i + 1) + " - " + livros.get(i));
        }
        System.out.println("   0 - Voltar");
        System.out.println("--------------------------");

        int escolha = entrada.lerInteiro("Livro: ", 0, livros.size());

        if (escolha == 0) {
            return null;
        }

        return livros.get(escolha - 1);
    }

    private String escolherTestamento() {
        System.out.println();
        System.out.println("------ TESTAMENTO ------");
        System.out.println("   1 - Antigo Testamento");
        System.out.println("   2 - Novo Testamento");
        System.out.println("   0 - Voltar");
        System.out.println("------------------------");

        int escolha = entrada.lerInteiro("Testamento: ", 0, 2);

        if (escolha == 0) {
            return null;
        }

        return (escolha == 1) ? "AT" : "NT";
    }

    private void menuDeNiveis(TipoFiltro tipo, String valor) {
        while (true) {
            System.out.println();
            System.out.println("===== NIVEIS " + descrever(tipo, valor) + " =====");

            for (Nivel n : Nivel.values()) {
                String cadeado = solo.podeJogar(n.getCodigo()) ? "   " : "[X]";
                int restam = solo.restantes(n.getCodigo(), tipo, valor);

                System.out.println("   " + n.getCodigo() + " - " + cadeado + " "
                        + n.getRotulo() + "  (restam " + restam + ")");
            }

            System.out.println("   7 - Zerar o progresso deste filtro");
            System.out.println("   0 - Voltar");
            System.out.println("==============================");

            int escolha = entrada.lerInteiro("Escolha: ", 0, 7);

            if (escolha == 0) {
                return;
            }

            if (escolha == 7) {
                confirmarZerar(tipo, valor);
                continue;
            }

            jogar(escolha, tipo, valor);
        }
    }

    private String descrever(TipoFiltro tipo, String valor) {
        if (tipo == TipoFiltro.GERAL) {
            return "(GERAL)";
        }
        return "(" + valor + ")";
    }

    private void confirmarZerar(TipoFiltro tipo, String valor) {
        System.out.println();
        System.out.println("ATENCAO: isso apaga o historico de TODOS os niveis deste filtro.");
        String resposta = entrada.lerTexto("Confirma? (S/N): ");

        if (resposta.equalsIgnoreCase("S")) {
            for (Nivel n : Nivel.values()) {
                solo.zerar(n.getCodigo(), tipo, valor);
            }
            System.out.println("Progresso zerado. As perguntas voltaram todas.");
        } else {
            System.out.println("Cancelado.");
        }
    }

    private void jogar(int nivel, TipoFiltro tipo, String valor) {
        Partida partida;

        try {
            partida = solo.iniciar(nivel, tipo, valor);
        } catch (IllegalStateException e) {
            System.out.println();
            System.out.println("   >> " + e.getMessage());
            entrada.pausar();
            return;
        }

        while (partida.temProxima()) {
            Pergunta p = partida.atual();
            String[] alternativas = p.getAlternativas();

            System.out.println();
            System.out.println("--------------------------------------------");
            System.out.println("Pergunta " + partida.getNumeroAtual()
                    + " de " + partida.getTotal() + "   [" + p.getLivro() + "]");
            System.out.println();
            System.out.println(p.getTexto());
            System.out.println();

            for (int i = 0; i < alternativas.length; i++) {
                System.out.println("   " + LETRAS.charAt(i) + ") " + alternativas[i]);
            }

            System.out.println();
            int escolhida = entrada.lerAlternativa("Sua resposta: ");
            boolean acertou = solo.responder(partida, escolhida);

            if (acertou) {
                System.out.println("   >> ACERTOU!   (" + p.getReferencia() + ")");
            } else {
                int certa = p.getRespostaCorreta();
                System.out.println("   >> Errou. A correta era " + LETRAS.charAt(certa)
                        + ") " + alternativas[certa] + "   (" + p.getReferencia() + ")");
            }
        }

        mostrarResultado(partida);
    }

    private void mostrarResultado(Partida partida) {
        System.out.println();
        System.out.println("========== FIM DA RODADA ==========");
        System.out.println("   Acertos: " + partida.getAcertos() + " de " + partida.getTotal());
        System.out.println("   Erros:   " + partida.getErros());
        System.out.println("   Aproveitamento: " + Math.round(partida.percentualAcerto()) + "%");
        System.out.println();
        System.out.println("   " + avaliar(partida.percentualAcerto()));
        System.out.println("===================================");

        entrada.pausar();
    }

    private String avaliar(double percentual) {
        if (percentual == 100) {
            return "Perfeito! Voce gabaritou.";
        }
        if (percentual >= 70) {
            return "Muito bom! Continue assim.";
        }
        if (percentual >= 50) {
            return "Na media. Da para melhorar.";
        }
        return "Estude mais um pouco e volte.";
    }
}