package com.quizbiblico;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.dados.LeitorCSV;
import com.quizbiblico.dados.VerificadorCSV;
import com.quizbiblico.modelo.Pergunta;

import java.io.IOException;
import java.util.List;

public class VerificarAcervo {

    public static void main(String[] args) {
        try {
            LeitorCSV leitor = new LeitorCSV();
            List<Pergunta> perguntas = leitor.carregarPasta("dados");
            System.out.println(perguntas.size() + " perguntas carregadas, de todos os livros");

            BancoDePerguntas banco = new BancoDePerguntas(perguntas);
            List<String> livros = banco.livrosDisponiveis();
            System.out.println(livros.size() + " livros no acervo, na ordem biblica:");
            for (String livro : livros) {
                System.out.println("  " + livro);
            }

            System.out.println();
            VerificadorCSV verificador = new VerificadorCSV();
            List<String> problemas = verificador.verificar(perguntas);

            if (problemas.isEmpty()) {
                System.out.println("Acervo limpo: nenhum problema encontrado.");
            } else {
                System.out.println(problemas.size() + " problema(s) encontrado(s):");
                for (String problema : problemas) {
                    System.out.println("  - " + problema);
                }
            }

        } catch (IOException e) {
            System.out.println("Falhou: " + e.getMessage());
        }
    }
}