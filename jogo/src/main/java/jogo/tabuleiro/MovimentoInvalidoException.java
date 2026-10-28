package jogo.tabuleiro;

/** Lançada quando um movimento não respeita as regras; a mensagem explica o motivo. */
public class MovimentoInvalidoException extends Exception {
    public MovimentoInvalidoException(String motivo) {
        super(motivo);
    }
}
