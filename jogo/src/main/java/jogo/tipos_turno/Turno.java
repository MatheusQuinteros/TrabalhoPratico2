package jogo.tipos_turno;

import jogo.tipos_jogo.Jogadora;

public abstract class Turno {
    private EstadoTurno estadoTurno;

    protected Turno(EstadoTurno estadoInicial) {
        this.estadoTurno = java.util.Objects.requireNonNull(estadoInicial, "estadoInicial");
    }

    public Jogadora getJogadoraAtual() {
        return estadoTurno.jogadora();
    }

    public boolean deveMoverPaçoquinha() {
        return estadoTurno.deveMoverPaçoquinha();
    }

    public void avançarTurno() {
        estadoTurno = estadoTurno.proximo();
    }
}
