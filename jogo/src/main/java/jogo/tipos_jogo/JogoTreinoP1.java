package jogo.tipos_jogo;

import jogo.IA;
import jogo.tipos_turno.TurnoTreino;

/** Jogo controlado por IA que começa no estado usado durante o treinamento. */
public class JogoTreinoP1 extends JogoIA {
    public JogoTreinoP1(IA ia) {
        super(new TurnoTreino(), ia);
    }
}
