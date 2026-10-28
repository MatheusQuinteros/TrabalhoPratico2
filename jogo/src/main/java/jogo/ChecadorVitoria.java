package jogo;

import java.util.Optional;
import jogo.tabuleiro.Tabuleiro;

public class ChecadorVitoria {
    /** Vencedora após a jogada atual, ou vazio se a partida continua. */
    public Optional<Jogadora> verificar(Tabuleiro tabuleiro, Turno turno) {
        int linhaPacoca = tabuleiro.getPacoca().getX();
        int colunaPacoca = tabuleiro.getPacoca().getY();

        for (Jogadora jogadora : Jogadora.values()) {
            if (linhaPacoca == jogadora.getLinhaObjetivo()) return Optional.of(jogadora);
        }

        // A verificação ocorre antes de avançar o turno; após mover a paçoca,
        // a jogadora ainda pode abrir uma saída ao mover uma peça sua.
        if (turno.deveMoverPacoquinha()) return Optional.empty();

        for (int dLinha = -1; dLinha <= 1; dLinha++) {
            for (int dColuna = -1; dColuna <= 1; dColuna++) {
                if (dLinha == 0 && dColuna == 0) continue;
                int linha = linhaPacoca + dLinha;
                int coluna = colunaPacoca + dColuna;
                if (Tabuleiro.dentroDoTabuleiro(linha, coluna) && tabuleiro.casaVazia(linha, coluna)) {
                    return Optional.empty();
                }
            }
        }

        // A próxima jogadora está bloqueada: vence quem acabou de mover sua peça.
        return Optional.of(turno.getJogadorAtual());
    }
}
