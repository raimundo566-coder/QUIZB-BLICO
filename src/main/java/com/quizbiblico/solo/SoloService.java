package com.quizbiblico.solo;

import com.quizbiblico.dados.BancoDePerguntas;
import com.quizbiblico.modelo.Nivel;
import com.quizbiblico.modelo.Pergunta;
import com.quizbiblico.modelo.TipoFiltro;
import com.quizbiblico.modelo.Usuario;
import com.quizbiblico.progresso.Progresso;
import com.quizbiblico.progresso.ProgressoNivel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoloService {

    public static final int PERGUNTAS_POR_PARTIDA = 10;

    private final BancoDePerguntas banco;
    private final Progresso progresso;
    private final Usuario usuario;

    public SoloService(BancoDePerguntas banco, Progresso progresso, Usuario usuario) {
        this.banco = banco;
        this.progresso = progresso;
        this.usuario = usuario;
    }

    public List<Pergunta> disponiveis(int nivel, TipoFiltro tipo, String valor) {
        ProgressoNivel caderneta = progresso.obter(nivel, tipo, valor);
        List<Pergunta> restantes = new ArrayList<>();

        for (Pergunta p : banco.buscar(nivel, tipo, valor)) {
            if (!caderneta.jaRespondeu(p.getId())) {
                restantes.add(p);
            }
        }

        return restantes;
    }

    public boolean podeJogar(int nivel) {
        return usuario.temAcessoAoNivel(Nivel.porCodigo(nivel));
    }

    public Partida iniciar(int nivel, TipoFiltro tipo, String valor) {
        if (!podeJogar(nivel)) {
            throw new IllegalStateException(
                    "Nivel bloqueado: " + Nivel.porCodigo(nivel).getRotulo());
        }

        List<Pergunta> restantes = disponiveis(nivel, tipo, valor);

        if (restantes.isEmpty()) {
            throw new IllegalStateException(
                    "Voce ja respondeu todas as perguntas deste filtro. Zere para recomecar.");
        }

        Collections.shuffle(restantes);

        int quantidade = Math.min(PERGUNTAS_POR_PARTIDA, restantes.size());
        List<Pergunta> sorteadas = new ArrayList<>(restantes.subList(0, quantidade));

        return new Partida(sorteadas, nivel, tipo, valor);
    }

    public boolean responder(Partida partida, int indiceAlternativa) {
        Pergunta pergunta = partida.atual();
        boolean acertou = partida.responder(indiceAlternativa);

        progresso.obter(partida.getNivel(), partida.getTipo(), partida.getValor())
                .registrar(pergunta.getId(), acertou);

        return acertou;
    }

    public void zerar(int nivel, TipoFiltro tipo, String valor) {
        progresso.zerar(nivel, tipo, valor);
    }

    public int restantes(int nivel, TipoFiltro tipo, String valor) {
        return disponiveis(nivel, tipo, valor).size();
    }

    public Progresso getProgresso() {
        return progresso;
    }
}