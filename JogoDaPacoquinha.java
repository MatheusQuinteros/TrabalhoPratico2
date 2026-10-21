import tabuleiro.Tabuleiro;

public class JogoDaPacoquinha {

    private Tabuleiro tabuleiro_jogo; 
    private Turno turno_jogo;
    private ChecadorVitoria checador_jogo;
    private int vencedora = -1;
    
    public JogoDaPacoquinha() {
        // Construtor: Inicializa o tabuleiro 5x5 e o estado inicial
        tabuleiro_jogo = new Tabuleiro();
        turno_jogo = new Turno();
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

    public int getVencedora() {
        return vencedora;
    }

    public int getJogadorAtual() {
        return turno_jogo.getJogadorAtual();
    }

    public boolean deveMoverPacoca() {
        return turno_jogo.deveMoverPaçoquinha();
    }

    public int getCasa(int linha, int coluna) {
        float[][][] casas = tabuleiro_jogo.getTabuleiro();
        for (int camada = 0; camada < tabuleiro_jogo.CAMADAS; camada++) {
            if (casas[camada][linha][coluna] != 0) return camada;
        }
        return -1;
    }
}
