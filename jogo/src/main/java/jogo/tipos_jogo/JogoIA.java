package jogo.tipos_jogo;

import ai.onnxruntime.OrtException;
import jogo.IA;
import jogo.tipos_turno.Turno;
import jogo.tipos_turno.TurnoJogo;

public class JogoIA extends Jogo implements AutoCloseable {
    private final IA ia;

    public JogoIA(IA ia) {
        this(new TurnoJogo(), ia);
    }

    protected JogoIA(Turno turnoInicial, IA ia) {
        super(turnoInicial);
        this.ia = ia;
    }

    public ResultadoPartida jogaRodadaPvIA(int origemX, int origemY, int destinoX, int destinoY) throws OrtException {
        if (getResultado().terminou()) return getResultado();

        // A jogadora 1 é humana; a IA controla a jogadora 2.
        int[] jogada;
        if (getJogadoraAtual() == Jogadora.JOGADORA_1) {
            jogada = new int[] {origemX, origemY, destinoX, destinoY};
        } else {
            jogada = ia.escolheJogada(this);
        }
        return jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
    }

    @Override
    public void close() throws OrtException {
        ia.close();
    }
}
