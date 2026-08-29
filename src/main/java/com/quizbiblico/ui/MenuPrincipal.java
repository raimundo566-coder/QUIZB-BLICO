package com.quizbiblico.ui;

import com.quizbiblico.modelo.Nivel;
import com.quizbiblico.modelo.Usuario;

public class MenuPrincipal {

    private final SoloConsoleUI soloUI;
    private final Usuario usuario;
    private final Entrada entrada;

    public MenuPrincipal(SoloConsoleUI soloUI, Usuario usuario, Entrada entrada) {
        this.soloUI = soloUI;
        this.usuario = usuario;
        this.entrada = entrada;
    }

    public void abrir() {
        while (true) {
            System.out.println();
            System.out.println("+-----------------------------------+");
            System.out.println("|           QUIZ BIBLICO            |");
            System.out.println("+-----------------------------------+");
            System.out.println("|   1 - MODO SOLO                   |");
            System.out.println("|   2 - GINCANA          (em breve) |");
            System.out.println("|   3 - EBD              (em breve) |");
            System.out.println("|   4 - Liberar niveis / VIP        |");
            System.out.println("|   0 - Sair                        |");
            System.out.println("+-----------------------------------+");

            int opcao = entrada.lerInteiro("Escolha: ", 0, 4);

            switch (opcao) {
                case 0:
                    return;
                case 1:
                    soloUI.abrir();
                    break;
                case 2:
                case 3:
                    emBreve();
                    break;
                case 4:
                    menuNiveis();
                    break;
            }
        }
    }

    private void emBreve() {
        System.out.println();
        System.out.println("   >> Este modo ainda esta em construcao.");
        System.out.println("   >> Aguarde as proximas atualizacoes!");
        entrada.pausar();
    }

    private void menuNiveis() {
        while (true) {
            System.out.println();
            System.out.println("------- LIBERAR NIVEIS -------");
            System.out.println("   Situacao: " + (usuario.isVip() ? "VIP ATIVO" : "conta comum"));
            System.out.println();

            for (Nivel n : Nivel.values()) {
                System.out.println("   " + n.getCodigo() + " - " + n.getRotulo()
                        + "  [" + situacaoDo(n) + "]");
            }

            System.out.println();
            System.out.println("   7 - Ativar VIP (libera todos)");
            System.out.println("   0 - Voltar");
            System.out.println("------------------------------");

            int escolha = entrada.lerInteiro("Escolha: ", 0, 7);

            if (escolha == 0) {
                return;
            }

            if (escolha == 7) {
                usuario.ativarVip();
                System.out.println("   >> VIP ativado! Todos os niveis liberados.");
                continue;
            }

            usuario.comprarNivel(escolha);
            System.out.println("   >> Nivel " + escolha + " liberado.");
        }
    }

    private String situacaoDo(Nivel nivel) {
        if (nivel.isGratuito()) {
            return "gratuito";
        }
        if (usuario.temAcessoAoNivel(nivel)) {
            return "liberado";
        }
        return "bloqueado";
    }
}