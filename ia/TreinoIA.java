package ia;

import jogo.Turno;
import jogo.tabuleiro.Tabuleiro;

public class TreinoIA {
    private void jogaPartidaTreinamento(){
        JogoIA jogoP1 = new JogoIA(true); //p1 vai jogar com a perspectiva do p2
        JogoIA jogoOficial = new JogoIA(false);

        while (jogoOficial.getVencedora() != -1){
            
            jogoOficial.jogaRodadaPvP(0, 0, 0, 0)
        }
    }

    private void enviaListaJogadas(){

    }

    public static void main(String[] args) {
        
    }
}
