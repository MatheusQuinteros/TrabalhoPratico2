package jogo.tipos_jogo;

/** Jogadoras reconhecidas pelas regras do jogo. */
public enum Jogadora {
    JOGADORA_1(1),
    JOGADORA_2(2);

    private final int numero;

    Jogadora(int numero) {
        this.numero = numero;
    }

    public int numero() {
        return numero;
    }

    public int camada() {
        return numero;
    }

    public Jogadora oponente() {
        if (this == JOGADORA_1) {
            return JOGADORA_2;
        }
        return JOGADORA_1;
    }

    public static Jogadora deNumero(int numero) {
        for (Jogadora jogadora : values()) {
            if (jogadora.numero == numero) {
                return jogadora;
            }
        }
        throw new IllegalArgumentException("Número de jogadora inválido: " + numero);
    }
}
