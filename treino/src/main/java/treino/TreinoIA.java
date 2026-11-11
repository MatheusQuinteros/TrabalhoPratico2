package treino;

import jogo.IA;
import jogo.JogoIA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

import ai.onnxruntime.OrtException;

public class TreinoIA {
    private static final int TAMANHO_TAB = 4;
    private static final String CAMINHO_JSON = "python/jogadas_vencedor.json";
    private static final int MAX_JOGADAS = 200; // evita partidas infinitas com a rede ainda aleatória
    private static final int PARTIDAS_PADRAO = 50;
    private static final double EPSILON_PADRAO = 0.1;
    public record PassoTreinamento(float[][][] tabuleiro, boolean jogaPacoquinha, int[] jogada) {} //sugestão da IA para fazer uma tupla

    private int vitoriasP1 = 0;
    private int vitoriasP2 = 0;
    private int semVencedor = 0;

    private ArrayList<PassoTreinamento> jogaPartidaTreinamento(IA ia) throws OrtException {
        JogoIA jogoP1 = new JogoIA(true, ia); //p1 vai jogar com a perspectiva do p2
        ArrayList<PassoTreinamento> jogadasP1= new ArrayList<>();

        JogoIA jogoOficial = new JogoIA(false, ia);
        ArrayList<PassoTreinamento> jogadasP2= new ArrayList<>();


        int jogadas = 0;
        while (jogoOficial.getVencedora() == -1 && jogadas < MAX_JOGADAS){
            jogadas++;
            // regra não define o que fazer quando a jogadora da vez não tem jogada: descarta a partida
            if (!temJogada(jogoOficial)) break;

            if (jogoOficial.getJogadorAtual() == 2){
                int[] jogada = ia.escolheJogada(jogoOficial);
                PassoTreinamento passo = new PassoTreinamento(jogoOficial.getTabuleiro(), jogoOficial.deveMoverPacoca(), jogada);
                jogoOficial.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(TAMANHO_TAB - jogada[0], jogada[1], TAMANHO_TAB - jogada[2], jogada[3]);
                jogadasP2.add(passo);
            }
            else{
                int[] jogada = ia.escolheJogada(jogoP1);
                PassoTreinamento passo = new PassoTreinamento(jogoP1.getTabuleiro(), jogoP1.deveMoverPacoca(), jogada);
                jogoOficial.jogaRodadaPvP(TAMANHO_TAB - jogada[0], jogada[1], TAMANHO_TAB - jogada[2], jogada[3]);
                jogoP1.jogaRodadaPvP(jogada[0], jogada[1] , jogada[2], jogada[3]);
                jogadasP1.add(passo);
            }
        }
        
        if (jogoOficial.getVencedora() == 1){
            vitoriasP1++;
            return jogadasP1;
        }
        else if (jogoOficial.getVencedora() == 2){
            vitoriasP2++;
            return jogadasP2;
        }
        semVencedor++;
        return new ArrayList<>();
    }

    private static boolean temJogada(JogoIA jogo) {
        for (boolean origem : IA.mascaraOrigens(jogo)) {
            if (origem) return true;
        }
        return false;
    }

    private void enviaListaJogadas(ArrayList<PassoTreinamento> listaJogadas){
        // Gera uma lista de objetos {"tabuleiro": [[[...]]], "jogaPacoquinha": bool, "jogada": [ol, oc, dl, dc]},
        // formato esperado por lerTabuleiros() em python/treino.py
        Gson gson = new Gson();
        try (FileWriter escritor = new FileWriter(CAMINHO_JSON)) {
            gson.toJson(listaJogadas, escritor);
        } catch (IOException e) {
            System.err.println("Erro ao salvar " + CAMINHO_JSON + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) throws OrtException {
        int numPartidas = args.length > 0 ? Integer.parseInt(args[0]) : PARTIDAS_PADRAO;
        double epsilon = args.length > 1 ? Double.parseDouble(args[1]) : EPSILON_PADRAO;

        TreinoIA treino = new TreinoIA();
        ArrayList<PassoTreinamento> todasJogadas = new ArrayList<>();
        try (IA ia = new IA()) {
            ia.setEpsilon(epsilon);
            for (int i = 0; i < numPartidas; i++) {
                todasJogadas.addAll(treino.jogaPartidaTreinamento(ia));
            }
        }
        treino.enviaListaJogadas(todasJogadas);

        System.out.printf("Partidas: %d | vitórias P1: %d | vitórias P2: %d | sem vencedor: %d | passos: %d%n",
                numPartidas, treino.vitoriasP1, treino.vitoriasP2, treino.semVencedor, todasJogadas.size());
    }
}