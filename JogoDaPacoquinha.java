import java.util.List;

public class JogoDaPacoquinha {

    private Tabuleiro tabuleiro_jogo; 
    private Turno turno_jogo;
    private ChecadorVitoria checador_jogo;

    public JogoDaPacoquinha() {
        // Construtor: Inicializa o tabuleiro 5x5 e o estado inicial
        tabuleiro_jogo = new Tabuleiro();
        turno_jogo = new Turno();
        checador_jogo = new ChecadorVitoria();
    }

    public int jogaRodadaPvP(int origemX, int origemY, int destinoX, int destinoY){
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

        if (checador_jogo.ganhou(tabuleiro_jogo)) return turno_jogo.getJogadorAtual();

        turno_jogo.avançarTurno();

        return 3;
    }
}