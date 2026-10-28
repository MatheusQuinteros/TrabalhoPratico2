package jogo;

public class Turno {
    /** Fases do turno, na ordem em que acontecem. */
    private enum Fase {
        JOGADORA_2_MOVE_PECA(Jogadora.DOIS, false),
        JOGADORA_1_MOVE_PACOQUINHA(Jogadora.UM, true),
        JOGADORA_1_MOVE_PECA(Jogadora.UM, false),
        JOGADORA_2_MOVE_PACOQUINHA(Jogadora.DOIS, true);

        private final Jogadora jogadora;
        private final boolean movePacoquinha;

        Fase(Jogadora jogadora, boolean movePacoquinha) {
            this.jogadora = jogadora;
            this.movePacoquinha = movePacoquinha;
        }

        Fase proxima() {
            Fase[] fases = values();
            return fases[(ordinal() + 1) % fases.length];
        }
    }

    private Fase fase;

    /** No treino a partida começa com a jogadora 1 movendo uma peça. */
    public Turno(boolean treino) {
        fase = treino ? Fase.JOGADORA_1_MOVE_PECA : Fase.JOGADORA_2_MOVE_PECA;
    }

    public Jogadora getJogadorAtual() {
        return fase.jogadora;
    }

    public boolean deveMoverPacoquinha() {
        return fase.movePacoquinha;
    }

    public void avancarTurno() {
        fase = fase.proxima();
    }
}
