package jogo.tabuleiro;

/**
 * Camadas do tabuleiro. A ordem define o índice no array float[3][5][5]
 * e precisa ser a mesma usada pela rede neural.
 */
public enum Camada {
    PACOQUINHA,
    JOGADORA_1,
    JOGADORA_2;

    public int indice() {
        return ordinal();
    }
}
