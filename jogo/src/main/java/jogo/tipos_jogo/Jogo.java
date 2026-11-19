package jogo.tipos_jogo;

import java.util.Optional;

import jogo.ChecadorVitoria;
import jogo.tabuleiro.Tabuleiro;
import jogo.tipos_turno.Turno;

public abstract class Jogo {
    protected final Tabuleiro tabuleiro_jogo;
    protected final Turno turno_jogo;
    protected final ChecadorVitoria checador_jogo;
    private ResultadoPartida resultado = ResultadoPartida.EM_ANDAMENTO;

    protected Jogo(Turno turnoInicial) {
        tabuleiro_jogo = new Tabuleiro();
        turno_jogo = java.util.Objects.requireNonNull(turnoInicial, "turnoInicial");
        checador_jogo = new ChecadorVitoria();
    }

    public ResultadoPartida jogaRodadaPvP(int origemX, int origemY, int destinoX, int destinoY) {
        if (resultado.terminou()) {
            return resultado;
        }

        int camada = 0;
        if (!turno_jogo.deveMoverPaçoquinha()) {
            camada = turno_jogo.getJogadoraAtual().camada();
        }
        tabuleiro_jogo.jogar(origemX, origemY, camada, destinoX, destinoY);

        Optional<Jogadora> vencedora = checador_jogo.vencedora(tabuleiro_jogo, turno_jogo);
        if (vencedora.isPresent()) {
            resultado = ResultadoPartida.vitoriaDe(vencedora.get());
            return resultado;
        }

        turno_jogo.avançarTurno();
        return resultado;
    }

    public float[][][] getTabuleiro() {
        return tabuleiro_jogo.getTabuleiro();
    }

    public ResultadoPartida getResultado() {
        return resultado;
    }

    public Optional<Jogadora> getVencedora() {
        return resultado.vencedora();
    }

    public Jogadora getJogadoraAtual() {
        return turno_jogo.getJogadoraAtual();
    }

    public boolean deveMoverPacoca() {
        return turno_jogo.deveMoverPaçoquinha();
    }

    public boolean[] destinosValidos(int linha, int coluna) {
        return tabuleiro_jogo.destinosValidos(linha, coluna);
    }

    public TipoCasa getCasa(int linha, int coluna) {
        float[][][] casas = tabuleiro_jogo.getTabuleiro();
        for (int camada = 0; camada < tabuleiro_jogo.CAMADAS; camada++) {
            if (casas[camada][linha][coluna] != 0) {
                if (camada == 0) return TipoCasa.PACOQUINHA;
                if (camada == 1) return TipoCasa.JOGADORA_1;
                return TipoCasa.JOGADORA_2;
            }
        }
        return TipoCasa.VAZIA;
    }
}
