package jogo.tipos_jogo;

import java.util.Optional;

/** Estado final ou atual de uma partida. */
public enum ResultadoPartida {
    EM_ANDAMENTO,
    VITORIA_JOGADORA_1,
    VITORIA_JOGADORA_2;

    public boolean terminou() {
        return this != EM_ANDAMENTO;
    }

    public Optional<Jogadora> vencedora() {
        if (this == VITORIA_JOGADORA_1) {
            return Optional.of(Jogadora.JOGADORA_1);
        }
        if (this == VITORIA_JOGADORA_2) {
            return Optional.of(Jogadora.JOGADORA_2);
        }
        return Optional.empty();
    }

    public static ResultadoPartida vitoriaDe(Jogadora jogadora) {
        if (jogadora == Jogadora.JOGADORA_1) {
            return VITORIA_JOGADORA_1;
        }
        return VITORIA_JOGADORA_2;
    }
}
