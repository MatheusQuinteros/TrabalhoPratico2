package jogo;

import ai.onnxruntime.OrtException;

public class JogoIA extends JogoDaPacoquinha{
    private final IA ia;

    public JogoIA(boolean treinamento, IA ia){
        super(treinamento);
        this.ia = ia;
    }

    public int jogaRodadaIA() throws OrtException {
        int[] jogada = ia.escolheJogada(this);
        return jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
    }
}
