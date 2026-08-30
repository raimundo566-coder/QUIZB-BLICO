package com.quizbiblico.dados;

import com.quizbiblico.modelo.Pergunta;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeitorCSV {

    private static final int COL_ID = 0;
    private static final int COL_NIVEL = 4;
    private static final int COL_LIVRO = 5;
    private static final int COL_TESTAMENTO = 6;
    private static final int COL_REFERENCIA = 9;
    private static final int COL_PERGUNTA = 10;
    private static final int COL_ALT_A = 11;
    private static final int COL_ALT_B = 12;
    private static final int COL_ALT_C = 13;
    private static final int COL_ALT_D = 14;
    private static final int COL_RESPOSTA = 15;
    private static final int COLUNAS_MINIMAS = COL_RESPOSTA + 1;

    public List<Pergunta> carregar(String caminho) throws IOException {
        List<Pergunta> perguntas = new ArrayList<>();
        Path arquivo = Path.of(caminho);
        if (!Files.exists(arquivo)) {
            throw new IOException("Não achei o arquivo: " + arquivo.toAbsolutePath());
        }
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        try (BufferedReader br = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8);
             CSVReader leitor = new CSVReaderBuilder(br).withCSVParser(parser).build()) {
            String[] linha;
            boolean primeira = true;
            int numeroDaLinha = 0;
            while ((linha = leitor.readNext()) != null) {
                numeroDaLinha++;
                if (primeira) { primeira = false; continue; }

                if (linha.length < COLUNAS_MINIMAS) {
                    System.out.println("  >> Aviso: linha " + numeroDaLinha + " de " + caminho
                            + " tem " + linha.length + " coluna(s), esperava pelo menos " + COLUNAS_MINIMAS
                            + ". Pulando essa linha.");
                    continue;
                }

                try {
                    perguntas.add(montar(linha));
                } catch (NumberFormatException e) {
                    System.out.println("  >> Aviso: linha " + numeroDaLinha + " de " + caminho
                            + " tem numero invalido, pulando essa pergunta.");
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("CSV malformado: " + e.getMessage(), e);
        }
        return perguntas;
    }

    public List<Pergunta> carregarPasta(String caminhoPasta) throws IOException {
        List<Path> arquivos = new ArrayList<>();
        Path pasta = Path.of(caminhoPasta);

        try (DirectoryStream<Path> conteudo = Files.newDirectoryStream(pasta, "*.csv")) {
            for (Path arquivo : conteudo) {
                arquivos.add(arquivo);
            }
        }

        Collections.sort(arquivos);

        List<Pergunta> todas = new ArrayList<>();
        for (Path arquivo : arquivos) {
            todas.addAll(carregar(arquivo.toString()));
        }

        return todas;
    }

    private Pergunta montar(String[] c) {
        return new Pergunta(
                Integer.parseInt(c[COL_ID].trim()),
                c[COL_PERGUNTA].trim(),
                c[COL_ALT_A].trim(), c[COL_ALT_B].trim(),
                c[COL_ALT_C].trim(), c[COL_ALT_D].trim(),
                Integer.parseInt(c[COL_RESPOSTA].trim()),
                Integer.parseInt(c[COL_NIVEL].trim()),
                c[COL_TESTAMENTO].trim(),
                c[COL_LIVRO].trim(),
                c[COL_REFERENCIA].trim());
    }
}