package ia;

import jogo.Turno;
import jogo.tabuleiro.Tabuleiro;

import java.util.ArrayList;

public class TreinoIA {
    private void jogaPartidaTreinamento(){
        JogoIA jogoP1 = new JogoIA(true); //p1 vai jogar com a perspectiva do p2
        ArrayList<float[][][]> jogadasP1= new ArrayList<>();

        JogoIA jogoOficial = new JogoIA(false);
        ArrayList<float[][][]> jogadasP2= new ArrayList<>();


        while (jogoOficial.getVencedora() != -1){
            if (jogoOficial.getJogadorAtual() == 2){
                int[] jogada = jogoOficial.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogadasP2.add(jogoOficial.getTabuleiro());
            }
            else{
                int[] jogada = jogoP1.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogadasP1.add(jogoP1.getTabuleiro());
            }
        }
        
        if (jogoOficial.getVencedora() == 1){
            enviaListaJogadas(jogadasP1, true);
            enviaListaJogadas(jogadasP2, false);
        }
        else{
            enviaListaJogadas(jogadasP1, true);
            enviaListaJogadas(jogadasP2, false);
        }
    }

    private void enviaListaJogadas(ArrayList<float[][][]> listaJogadas, boolean ganhou){
        ...
    }

    public static void main(String[] args) {
        
    }
}
