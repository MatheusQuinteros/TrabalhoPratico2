package jogo.tipos_jogo;

/** Indica que uma jogada não pode ser aplicada ao tabuleiro atual. */
public class JogadaInvalidaException extends RuntimeException {
    public JogadaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
