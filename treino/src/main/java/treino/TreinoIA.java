package treino;

import jogo.Turno;
import jogo.tabuleiro.Tabuleiro;

import java.util.ArrayList;

public class TreinoIA {
    private static final int TAMANHO_TAB = 4;
    public record PassoTreinamento(float[][][] tabuleiro, int[] jogada) {} //sugestão da IA para fazer uma tupla

    private void jogaPartidaTreinamento(){
        JogoIA jogoP1 = new JogoIA(true); //p1 vai jogar com a perspectiva do p2
        ArrayList<PassoTreinamento> jogadasP1= new ArrayList<>();

        JogoIA jogoOficial = new JogoIA(false);
        ArrayList<PassoTreinamento> jogadasP2= new ArrayList<>();


        while (jogoOficial.getVencedora() != -1){
            if (jogoOficial.getJogadorAtual() == 2){
                int[] jogada = jogoOficial.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], TAMANHO_TAB - jogada[1], jogada[2], TAMANHO_TAB - jogada[3]);
                PassoTreinamento passo = new PassoTreinamento(jogoOficial.getTabuleiro(), jogada);
                jogadasP2.add(passo);
            }
            else{
                int[] jogada = jogoP1.receberJogadaIA();
                jogoOficial.jogaRodadaPvP(jogada[0], TAMANHO_TAB - jogada[1], jogada[2], TAMANHO_TAB - jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], jogada[1] , jogada[2], jogada[3]);
                PassoTreinamento passo = new PassoTreinamento(jogoP1.getTabuleiro(), jogada);
                jogadasP2.add(passo);
            }
        }
        
        if (jogoOficial.getVencedora() == 1){
            enviaListaJogadas(jogadasP1);
        }
        else{
            enviaListaJogadas(jogadasP2);
        }
    }

    private void enviaListaJogadas(ArrayList<PassoTreinamento> listaJogadas){
        ...
    }

    public static void main(String[] args) {
        
    }
}