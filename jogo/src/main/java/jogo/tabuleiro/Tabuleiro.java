package jogo.tabuleiro;

import java.util.Optional;

public class Tabuleiro {
    public static final int TAMANHO = 5;
    public static final int CAMADAS = Camada.values().length;
    private static final float OCUPADA = 1f;
    private static final float LIVRE = 0f;

    private final float[][][] tabuleiro; // float para facilitar a conversão em tensor no PyTorch
    private final Peca pacoca;

    public Tabuleiro() {
        tabuleiro = new float[CAMADAS][TAMANHO][TAMANHO];

        for (int coluna = 0; coluna < TAMANHO; coluna++) {
            tabuleiro[Camada.JOGADORA_1.indice()][0][coluna] = OCUPADA;
            tabuleiro[Camada.JOGADORA_2.indice()][TAMANHO - 1][coluna] = OCUPADA;
        }

        int centro = TAMANHO / 2;
        tabuleiro[Camada.PACOQUINHA.indice()][centro][centro] = OCUPADA;
        pacoca = new Peca(centro, centro);
    }

    /** Cópia do estado atual; alterá-la não muda o tabuleiro. */
    public float[][][] getTabuleiro() {
        return copiar(tabuleiro);
    }

    public Peca getPacoca() {
        return pacoca;
    }

    /** Camada que ocupa a casa, ou vazio se a casa está livre. */
    public Optional<Camada> getConteudo(int linha, int coluna) {
        for (Camada camada : Camada.values()) {
            if (tabuleiro[camada.indice()][linha][coluna] != LIVRE) return Optional.of(camada);
        }
        return Optional.empty();
    }

    public boolean casaVazia(int linha, int coluna) {
        return getConteudo(linha, coluna).isEmpty();
    }

    public static boolean dentroDoTabuleiro(int linha, int coluna) {
        return linha >= 0 && linha < TAMANHO && coluna >= 0 && coluna < TAMANHO;
    }

    public void jogar(int origemX, int origemY, Camada camada, int destinoX, int destinoY)
            throws MovimentoInvalidoException {
        if (!dentroDoTabuleiro(origemX, origemY) || !dentroDoTabuleiro(destinoX, destinoY)) {
            throw new MovimentoInvalidoException("A posição está fora do tabuleiro.");
        }
        if (tabuleiro[camada.indice()][origemX][origemY] != OCUPADA) {
            throw new MovimentoInvalidoException("A casa de origem não tem a peça que deve ser movida.");
        }

        int deslocamentoX = destinoX - origemX;
        int deslocamentoY = destinoY - origemY;

        if (deslocamentoX == 0 && deslocamentoY == 0) {
            throw new MovimentoInvalidoException("O destino precisa ser diferente da origem.");
        }
        if (deslocamentoX != 0 && deslocamentoY != 0
                && Math.abs(deslocamentoX) != Math.abs(deslocamentoY)) {
            throw new MovimentoInvalidoException("O movimento deve ser em linha reta ou diagonal.");
        }

        int passoX = Integer.signum(deslocamentoX);
        int passoY = Integer.signum(deslocamentoY);
        int ultimoX = origemX;
        int ultimoY = origemY;

        // Encontra a última casa vazia antes de uma peça ou da borda.
        while (dentroDoTabuleiro(ultimoX + passoX, ultimoY + passoY)
                && casaVazia(ultimoX + passoX, ultimoY + passoY)) {
            ultimoX += passoX;
            ultimoY += passoY;
        }

        if (destinoX != ultimoX || destinoY != ultimoY) {
            throw new MovimentoInvalidoException("A peça deve ir até a última casa livre nessa direção.");
        }

        tabuleiro[camada.indice()][origemX][origemY] = LIVRE;
        tabuleiro[camada.indice()][destinoX][destinoY] = OCUPADA;

        if (camada == Camada.PACOQUINHA) {
            pacoca.moverPara(destinoX, destinoY);
        }
    }

    /** Copia o tabuleiro para não alterar o original. */
    public static float[][][] copiar(float[][][] original) {
        float[][][] copia = new float[original.length][][];
        for (int c = 0; c < original.length; c++) {
            copia[c] = new float[original[c].length][];
            for (int l = 0; l < original[c].length; l++) {
                copia[c][l] = original[c][l].clone();
            }
        }
        return copia;
    }
}
