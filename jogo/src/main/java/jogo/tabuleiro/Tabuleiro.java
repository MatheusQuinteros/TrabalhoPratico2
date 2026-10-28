package jogo.tabuleiro;

public class Tabuleiro {
    private float[][][] tabuleiro; //float pois é mais prática a conversão para tensor no pytorch
    private Peca pacoca;

    public final int TAMANHO = 5;
    public final int CAMADAS = 3;

    public Tabuleiro(){
        // Inicia o tabuleiro com as peças em seus locais iniciais
        tabuleiro = new float[CAMADAS][TAMANHO][TAMANHO];

        this.pacoca = new Peca(2,2);

        for(int i = 0; i < TAMANHO; i++){ // peças jogador 1
            tabuleiro[1][0][i] = 1;
        }

        for(int i = 0; i < TAMANHO; i++){ // peças jogador 1
            tabuleiro[2][4][i] = 1;
        }

        tabuleiro[0][2][2] = 1; // paçoca
    }

    public float[][][] getTabuleiro() {
        return copiar(tabuleiro);
    }

    public Peca getPacoca(){
        return pacoca;
    }

    public boolean jogar(int origemX, int origemY, int camada, int destinoX, int destinoY) {
        if (camada < 0 || camada >= CAMADAS
                || origemX < 0 || origemX >= TAMANHO
                || origemY < 0 || origemY >= TAMANHO
                || destinoX < 0 || destinoX >= TAMANHO
                || destinoY < 0 || destinoY >= TAMANHO) {
            return false;
        }

        if (tabuleiro[camada][origemX][origemY] != 1) {
            return false;
        }

        int deslocamentoX = destinoX - origemX;
        int deslocamentoY = destinoY - origemY;

        if (deslocamentoX == 0 && deslocamentoY == 0) {
            return false;
        }
        if (deslocamentoX != 0 && deslocamentoY != 0
                && Math.abs(deslocamentoX) != Math.abs(deslocamentoY)) {
            return false;
        }

        int passoX = Integer.signum(deslocamentoX);
        int passoY = Integer.signum(deslocamentoY);
        int ultimoX = origemX;
        int ultimoY = origemY;

        // Encontra a última casa vazia antes de uma peça ou da borda.
        while (true) {
            int proximoX = ultimoX + passoX;
            int proximoY = ultimoY + passoY;

            if (proximoX < 0 || proximoX >= TAMANHO
                    || proximoY < 0 || proximoY >= TAMANHO) {
                break;
            }

            boolean ocupada = false;
            for (int c = 0; c < CAMADAS; c++) {
                if (tabuleiro[c][proximoX][proximoY] != 0) {
                    ocupada = true;
                    break;
                }
            }
            if (ocupada) {
                break;
            }

            ultimoX = proximoX;
            ultimoY = proximoY;
        }

        if (destinoX != ultimoX || destinoY != ultimoY) {
            return false;
        }

        tabuleiro[camada][origemX][origemY] = 0;
        tabuleiro[camada][destinoX][destinoY] = 1;

        if (camada == 0) {
            if (pacoca == null) {
                // Alteração por IA: compatível com o construtor Peca(int, int).
                pacoca = new Peca(destinoX, destinoY);
            }
            pacoca.x = destinoX;
            pacoca.y = destinoY;
        }

        return true;
    }

    public static float[][][] copiar(float[][][] original) { //copia o tabuleiro para não alterar o original
    float[][][] copia = new float[original.length][][];
    for (int c = 0; c < original.length; c++) {
        copia[c] = new float[original[c].length][];
        for (int l = 0; l < original[c].length; l++) {
            copia[c][l] = new float[original[c][l].length];
            for (int k = 0; k < original[c][l].length; k++) {
                copia[c][l][k] = original[c][l][k];
            }
        }
    }
    return copia;
}
}
