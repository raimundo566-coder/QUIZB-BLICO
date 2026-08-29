package com.quizbiblico.progresso;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenciaProgresso {

    private static final String CAMINHO_PADRAO = "dados/progresso.json";

    private final Path arquivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PersistenciaProgresso() {
        this(CAMINHO_PADRAO);
    }

    public PersistenciaProgresso(String caminho) {
        this.arquivo = Path.of(caminho);
    }

    public void salvar(Progresso progresso) throws IOException {
        Path pasta = arquivo.getParent();
        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        try (Writer escritor = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            gson.toJson(progresso, escritor);
        }
    }

    public Progresso carregar() throws IOException {
        if (!Files.exists(arquivo)) {
            return new Progresso();
        }

        try (Reader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            Progresso lido = gson.fromJson(leitor, Progresso.class);
            return (lido == null) ? new Progresso() : lido;
        } catch (JsonSyntaxException e) {
            throw new IOException("Progresso corrompido: " + arquivo.toAbsolutePath(), e);
        }
    }

    public Path getArquivo() {
        return arquivo;
    }
}