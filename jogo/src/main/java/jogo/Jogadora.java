package jogo;

import jogo.tabuleiro.Camada;
import jogo.tabuleiro.Tabuleiro;

public enum Jogadora {
    UM(1, Camada.JOGADORA_1, 0),
    DOIS(2, Camada.JOGADORA_2, Tabuleiro.TAMANHO - 1);

    private final int numero;
    private final Camada camada;
    private final int linhaObjetivo;

    Jogadora(int numero, Camada camada, int linhaObjetivo) {
        this.numero = numero;
        this.camada = camada;
        this.linhaObjetivo = linhaObjetivo;
    }

    /** Número exibido para a jogadora (1 ou 2). */
    public int getNumero() {
        return numero;
    }

    /** Camada do tabuleiro onde ficam as peças da jogadora. */
    public Camada getCamada() {
        return camada;
    }

    /** Linha onde a jogadora quer levar a paçoquinha (a linha inicial das suas peças). */
    public int getLinhaObjetivo() {
        return linhaObjetivo;
    }

    public Jogadora getOponente() {
        return this == UM ? DOIS : UM;
    }
}
