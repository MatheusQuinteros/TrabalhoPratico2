package tabuleiro;
import java.util.Arrays;

public class Tabuleiro {
    private float[][][] tabuleiro; //float pois é mais prática a conversão para tensor no pytorch
    private Peca pacoca;

    public final int TAMANHO = 5;
    public final int CAMADAS = 3;

    public Tabuleiro(){
        // Inicia o tabuleiro com as peças em seus locais iniciais
        tabuleiro = new float[CAMADAS][TAMANHO][TAMANHO];
        
        Arrays.fill(tabuleiro, 0);

        for(int i = 0; i < TAMANHO; i++){ // peças jogador 1
            tabuleiro[1][0][i] = 1;
        }

        for(int i = 0; i < TAMANHO; i++){ // peças jogador 1
            tabuleiro[2][4][i] = 1;
        }

        tabuleiro[0][2][2] = 1; // paçoca
    }

    public float[][][] getTabuleiro() {
        return tabuleiro;
    }

    public Peca getPacoca(){
        return pacoca;
    }

    public boolean jogar(int origemX, int origemY, int camada, int destinoX, int destinoY) {
        // Valida e executa a jogada, retornando true se válida ou false caso contrário
        return false;
    }
}