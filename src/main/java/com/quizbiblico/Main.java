package com.quizbiblico;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.dados.LeitorCSV;
import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.PersistenciaUsuario;
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
            List<Pergunta> perguntas = leitor.carregarPasta("dados");
            BancoDePerguntas banco = new BancoDePerguntas(perguntas);

            // 2. progresso salvo
            PersistenciaProgresso arquivistaProgresso = new PersistenciaProgresso();
            Progresso progresso = arquivistaProgresso.carregar();

            // 3. usuario salvo
            PersistenciaUsuario arquivistaUsuario = new PersistenciaUsuario();
            Usuario usuario = arquivistaUsuario.carregar();

            // 4. regras
            SoloService solo = new SoloService(banco, progresso, usuario);

            // 5. telas
            SoloConsoleUI soloUI = new SoloConsoleUI(solo, banco, entrada);
            MenuPrincipal menu = new MenuPrincipal(soloUI, usuario, entrada);

            System.out.println("Carregado: " + banco.total() + " perguntas, "
                    + banco.livrosDisponiveis().size() + " livros.");

            // 6. roda o app
            menu.abrir();

            // 7. salva ao sair
            arquivistaProgresso.salvar(progresso);
            arquivistaUsuario.salvar(usuario);
            System.out.println();
            System.out.println("Progresso e conta salvos. Ate a proxima!");

        } catch (IOException e) {
            System.out.println("Falhou: " + e.getMessage());
        } finally {
            entrada.fechar();
        }
    }
}