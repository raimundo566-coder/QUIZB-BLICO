package com.quizbiblico.modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PersistenciaUsuario {

    private static final String CAMINHO_PADRAO = "dados/usuario.json";

    private final Path arquivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public PersistenciaUsuario() {
        this(CAMINHO_PADRAO);
    }

    public PersistenciaUsuario(String caminho) {
        this.arquivo = Path.of(caminho);
    }

    public void salvar(Usuario usuario) throws IOException {
        Path pasta = arquivo.getParent();
        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        try (Writer escritor = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            gson.toJson(usuario, escritor);
        }
    }

    public Usuario carregar() throws IOException {
        if (!Files.exists(arquivo)) {
            return new Usuario();
        }

        try (Reader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            Usuario lido = gson.fromJson(leitor, Usuario.class);
            return (lido == null) ? new Usuario() : lido;
        } catch (JsonSyntaxException e) {
            throw new IOException("Usuario corrompido: " + arquivo.toAbsolutePath(), e);
        }
    }

    public Path getArquivo() {
        return arquivo;
    }
}