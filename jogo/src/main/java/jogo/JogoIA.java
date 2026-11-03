package jogo;

import ai.onnxruntime.OrtException;

public class JogoIA extends JogoDaPacoquinha implements AutoCloseable {
    private final IA ia;

    public JogoIA(boolean treinamento, IA ia){
        super(treinamento);
        this.ia = ia;
    }

    public int jogaRodadaPvIA(int origemX, int origemY, int destinoX, int destinoY) throws OrtException {
        if (getVencedora() != -1) return getVencedora();

        // A jogadora 1 é humana; a IA controla a jogadora 2.
        int[] jogada = getJogadorAtual() == 1
                ? new int[] {origemX, origemY, destinoX, destinoY}
                : ia.escolheJogada(this);
        return jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
    }

    @Override
    public void close() throws OrtException {
        ia.close();
    }
}
