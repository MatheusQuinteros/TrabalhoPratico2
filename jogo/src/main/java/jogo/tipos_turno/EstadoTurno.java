package jogo.tipos_turno;

import jogo.tipos_jogo.Jogadora;

/** As quatro etapas que alternam entre mover a paçoquinha e mover uma peça. */
public enum EstadoTurno {
    JOGADORA_2_MOVE_PECA(Jogadora.JOGADORA_2, false),
    JOGADORA_1_MOVE_PACOQUINHA(Jogadora.JOGADORA_1, true),
    JOGADORA_1_MOVE_PECA(Jogadora.JOGADORA_1, false),
    JOGADORA_2_MOVE_PACOQUINHA(Jogadora.JOGADORA_2, true);

    private final Jogadora jogadora;
    private final boolean moverPaçoquinha;

    EstadoTurno(Jogadora jogadora, boolean moverPaçoquinha) {
        this.jogadora = jogadora;
        this.moverPaçoquinha = moverPaçoquinha;
    }

    public Jogadora jogadora() {
        return jogadora;
    }

    public boolean deveMoverPaçoquinha() {
        return moverPaçoquinha;
    }

    public EstadoTurno proximo() {
        switch (this) {
            case JOGADORA_2_MOVE_PECA:
                return JOGADORA_1_MOVE_PACOQUINHA;
            case JOGADORA_1_MOVE_PACOQUINHA:
                return JOGADORA_1_MOVE_PECA;
            case JOGADORA_1_MOVE_PECA:
                return JOGADORA_2_MOVE_PACOQUINHA;
            case JOGADORA_2_MOVE_PACOQUINHA:
                return JOGADORA_2_MOVE_PECA;
            default:
                throw new IllegalStateException("Estado de turno não reconhecido: " + this);
        }
    }
}
