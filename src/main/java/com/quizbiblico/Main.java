package com.quizbiblico;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.dados.LeitorCSV;
import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.Usuario;
import com.quizbiblico.progresso.PersistenciaProgresso;
import com.quizbiblico.progresso.Progresso;
import com.quizbiblico.solo.SoloService;
import com.quizbiblico.ui.Entrada;
import com.quizbiblico.ui.MenuPrincipal;
import com.quizbiblico.ui.SoloConsoleUI;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Entrada entrada = new Entrada();

        try {
            // 1. acervo
            LeitorCSV leitor = new LeitorCSV();
            List<Pergunta> perguntas = leitor.carregar("dados/genesis.csv");
            BancoDePerguntas banco = new BancoDePerguntas(perguntas);

            // 2. progresso salvo
            PersistenciaProgresso arquivista = new PersistenciaProgresso();
            Progresso progresso = arquivista.carregar();

            // 3. regras
            Usuario usuario = new Usuario();
            SoloService solo = new SoloService(banco, progresso, usuario);

            // 4. telas
            SoloConsoleUI soloUI = new SoloConsoleUI(solo, banco, entrada);
            MenuPrincipal menu = new MenuPrincipal(soloUI, usuario, entrada);

            System.out.println("Carregado: " + banco.total() + " perguntas.");

            // 5. roda o app
            menu.abrir();

            // 6. salva ao sair
            arquivista.salvar(progresso);
            System.out.println();
            System.out.println("Progresso salvo. Ate a proxima!");

        } catch (IOException e) {
            System.out.println("Falhou: " + e.getMessage());
        } finally {
            entrada.fechar();
        }
    }
}