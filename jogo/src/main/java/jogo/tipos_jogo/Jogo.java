package jogo.tipos_jogo;

import jogo.ChecadorVitoria;
import jogo.tabuleiro.Tabuleiro;
import jogo.tipos_turno.Turno;

public abstract class Jogo {

    protected  Tabuleiro tabuleiro_jogo; // tive que tornar público por conta do treinamento
    protected  Turno turno_jogo;
    protected  ChecadorVitoria checador_jogo;
    protected  int vencedora = -1;
    
    protected Jogo(Turno turnoInicial) {
        // Construtor: Inicializa o tabuleiro 5x5 e o estado inicial
        tabuleiro_jogo = new Tabuleiro();
        turno_jogo = java.util.Objects.requireNonNull(turnoInicial, "turnoInicial");
        checador_jogo = new ChecadorVitoria();
    }


    public int jogaRodadaPvP(int origemX, int origemY, int destinoX, int destinoY){
        if (vencedora != -1) return vencedora;
        
        //retorna 
        // 0 - jogada invalida (interface deve repetir)
        // 1 - jogada valida p1 campeao
        // 2 - jogada valida p2 campeao
        // 3 - jogada valida sem campeao
        
        boolean jogada_valida = true;
        
        if (turno_jogo.deveMoverPaçoquinha()){
            jogada_valida = tabuleiro_jogo.jogar(origemX, origemY, 0, destinoX, destinoY);
        }

        else {
           jogada_valida = tabuleiro_jogo.jogar(origemX, origemY, turno_jogo.getJogadorAtual(), destinoX, destinoY);
        }

        if (!jogada_valida) return 0;

        int ganhou = checador_jogo.ganhou(tabuleiro_jogo, turno_jogo);

        if (ganhou != -1) {
            vencedora = ganhou;
            return vencedora;
        }
        
        turno_jogo.avançarTurno();

        return 3;
    }

    public float[][][] getTabuleiro(){
        return  tabuleiro_jogo.getTabuleiro();
    }

    public int getVencedora() {
        return vencedora;
    }

    public int getJogadorAtual() {
        return turno_jogo.getJogadorAtual();
    }

    public boolean deveMoverPacoca() {
        return turno_jogo.deveMoverPaçoquinha();
    }

    public boolean[] destinosValidos(int linha, int coluna) {
        return tabuleiro_jogo.destinosValidos(linha, coluna);
    }

    public int getCasa(int linha, int coluna) {
        float[][][] casas = tabuleiro_jogo.getTabuleiro();
        for (int camada = 0; camada < tabuleiro_jogo.CAMADAS; camada++) {
            if (casas[camada][linha][coluna] != 0) return camada;
        }
        return -1;
    }
}
