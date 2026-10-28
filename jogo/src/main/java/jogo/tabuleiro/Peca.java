package jogo.tabuleiro;

/** Posição de uma peça no tabuleiro; só o Tabuleiro pode movê-la. */
public class Peca {
    private int x;
    private int y;

    Peca(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void moverPara(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
