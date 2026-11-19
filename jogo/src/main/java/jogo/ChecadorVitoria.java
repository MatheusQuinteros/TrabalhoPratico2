package jogo;

import jogo.tabuleiro.Tabuleiro;
import jogo.tipos_jogo.Jogadora;
import jogo.tipos_turno.Turno;
import java.util.Optional;

public class ChecadorVitoria {
    public Optional<Jogadora> vencedora(Tabuleiro tabuleiro, Turno turno) {
        int x_pacoca = tabuleiro.getPacoca().getX();
        int y_pacoca = tabuleiro.getPacoca().getY();
        
        if (x_pacoca == 0) return Optional.of(Jogadora.JOGADORA_1);
        if (x_pacoca == 4) return Optional.of(Jogadora.JOGADORA_2);

        //A chamada ocorre antes de avançar o turno; após mover a paçoca,
        // a jogadora ainda pode abrir uma saída ao mover uma peça sua. */
        if (turno.deveMoverPaçoquinha()) return Optional.empty();

        float[][][] tabuleiro_aux = tabuleiro.getTabuleiro();

        for (int i = -1; i <= 1; i++){
            int y = y_pacoca + i;

            if (y < 0 || y >= tabuleiro.TAMANHO) continue;

            for(int j = -1; j <= 1; j++){
                int x = x_pacoca + j;

                if (i == 0 && j == 0) continue;
                if (x < 0 || x >= tabuleiro.TAMANHO) continue;

                float casa = tabuleiro_aux[0][x][y] + tabuleiro_aux[1][x][y] + tabuleiro_aux[2][x][y];
                if (casa == 0) return Optional.empty();
            }
        }

        // A próxima jogadora está bloqueada: vence quem acabou de mover sua peça.
        return Optional.of(turno.getJogadoraAtual());
    }
}
