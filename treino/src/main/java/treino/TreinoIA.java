package treino;

import jogo.Jogadora;
import jogo.Turno;
import jogo.tabuleiro.MovimentoInvalidoException;
import jogo.tabuleiro.Tabuleiro;

import java.util.ArrayList;
import java.util.Optional;

public class TreinoIA {
    private static final int TAMANHO_TAB = 4; 
    private void jogaPartidaTreinamento() throws MovimentoInvalidoException {
        JogoIA jogoP1 = new JogoIA(true); //p1 vai jogar com a perspectiva do p2
        ArrayList<float[][][]> jogadasP1= new ArrayList<>();

        JogoIA jogoOficial = new JogoIA(false);
        ArrayList<float[][][]> jogadasP2= new ArrayList<>();


        while (jogoOficial.terminou()){
            if (jogoOficial.getJogadorAtual() == Jogadora.DOIS){
                int[] jogada = jogoOficial.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], TAMANHO_TAB - jogada[1], jogada[2], TAMANHO_TAB - jogada[3]);
                jogadasP2.add(jogoOficial.getTabuleiro());
            }
            else{
                int[] jogada = jogoP1.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], TAMANHO_TAB - jogada[1], jogada[2], TAMANHO_TAB - jogada[3]);
                jogadasP1.add(jogoP1.getTabuleiro());
            }
        }
        
        if (jogoOficial.getVencedora().equals(Optional.of(Jogadora.UM))){
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
