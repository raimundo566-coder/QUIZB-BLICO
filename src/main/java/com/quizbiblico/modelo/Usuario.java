package com.quizbiblico.modelo;

import java.util.HashSet;
import java.util.Set;

public class Usuario {
    private final Set<Integer> niveisComprados = new HashSet<>();
    private boolean vip = false;
    public boolean temAcessoAoNivel (Nivel nivel){
        if (nivel.isGratuito()) return true;
        if (vip) return true;
        return niveisComprados.contains(nivel.getCodigo());
    }
    public void comprarNivel(int codigo){
        niveisComprados.add(codigo);
    }
    public void ativarVip(){
        this.vip = true;
    }
    public boolean isVip() {
        return vip;
    }
    public Set<Integer> getNiveisComprados() {
        return niveisComprados;
    }
}

