package jogo;

import java.util.Optional;
import jogo.tabuleiro.Camada;
import jogo.tabuleiro.MovimentoInvalidoException;
import jogo.tabuleiro.Tabuleiro;

public class JogoDaPacoquinha {
    private final Tabuleiro tabuleiroJogo;
    private final Turno turnoJogo;
    private final ChecadorVitoria checadorJogo;
    private Jogadora vencedora; // null enquanto a partida não termina

    public JogoDaPacoquinha(boolean treinamento) {
        tabuleiroJogo = new Tabuleiro();
        turnoJogo = new Turno(treinamento);
        checadorJogo = new ChecadorVitoria();
    }

    /**
     * Move a peça da vez (a paçoquinha ou uma peça da jogadora atual).
     *
     * @return a vencedora, se a jogada encerrou a partida
     * @throws MovimentoInvalidoException se o movimento não respeita as regras; o turno não muda
     * @throws IllegalStateException se a partida já terminou
     */
    public Optional<Jogadora> jogaRodadaPvP(int origemX, int origemY, int destinoX, int destinoY)
            throws MovimentoInvalidoException {
        if (terminou()) throw new IllegalStateException("A partida já terminou.");

        tabuleiroJogo.jogar(origemX, origemY, getCamadaDaVez(), destinoX, destinoY);

        Optional<Jogadora> ganhou = checadorJogo.verificar(tabuleiroJogo, turnoJogo);
        if (ganhou.isPresent()) {
            vencedora = ganhou.get();
            return ganhou;
        }

        turnoJogo.avancarTurno();
        return Optional.empty();
    }

    public float[][][] getTabuleiro() {
        return tabuleiroJogo.getTabuleiro();
    }

    public boolean terminou() {
        return vencedora != null;
    }

    public Optional<Jogadora> getVencedora() {
        return Optional.ofNullable(vencedora);
    }

    public Jogadora getJogadorAtual() {
        return turnoJogo.getJogadorAtual();
    }

    public boolean deveMoverPacoquinha() {
        return turnoJogo.deveMoverPacoquinha();
    }

    /** Camada da peça que deve ser movida agora. */
    public Camada getCamadaDaVez() {
        return deveMoverPacoquinha() ? Camada.PACOQUINHA : getJogadorAtual().getCamada();
    }

    public Optional<Camada> getCasa(int linha, int coluna) {
        return tabuleiroJogo.getConteudo(linha, coluna);
    }
}
