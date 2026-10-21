package jogo;

import tabuleiro.Tabuleiro;

public class ChecadorVitoria {
    public int ganhou(Tabuleiro tabuleiro, Turno turno) { //retorna 1 se jogador 1 ganhou, 2 se jogador 2 ganhou e -1 se nao acabou
        int x_pacoca = tabuleiro.getPacoca().getX();
        int y_pacoca = tabuleiro.getPacoca().getY();
        
        if (x_pacoca == 0) return 1;
        if (x_pacoca == 4) return 2;

        //A chamada ocorre antes de avançar o turno; após mover a paçoca,
        // a jogadora ainda pode abrir uma saída ao mover uma peça sua. */
        if (turno.deveMoverPaçoquinha()) return -1;

        float[][][] tabuleiro_aux = tabuleiro.getTabuleiro();

        for (int i = -1; i <= 1; i++){
            int y = y_pacoca + i;

            if (y < 0 || y >= tabuleiro.TAMANHO) continue;

            for(int j = -1; j <= 1; j++){
                int x = x_pacoca + j;

                if (i == 0 && j == 0) continue;
                if (x < 0 || x >= tabuleiro.TAMANHO) continue;

                float casa = tabuleiro_aux[0][x][y] + tabuleiro_aux[1][x][y] + tabuleiro_aux[2][x][y];
                if (casa == 0) return -1;
            }
        }

        // A próxima jogadora está bloqueada: vence quem acabou de mover sua peça.
        return turno.getJogadorAtual();
    }
}
