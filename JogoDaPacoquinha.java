import java.util.List;

public class JogoDaPacoquinha {

    private int[][] tabuleiro; 
    // 0 casa livre
    // 1 a 5 peças do jogador 1, 
    // 6 a 10 peças do jogador 2,
    // 11 paçoquinha

    private int estadoTurno;
    // 0 - jogador 1 paçoca
    // 1 - jogador 1 peça
    // 2 - jogador 2 paçoca
    // 3 - jogador 2 peça

    public JogoDaPacoquinha() {
        // Construtor: Inicializa o tabuleiro 5x5 e o estado inicial
        tabuleiro = new int[5][5];
        iniciaTabuleiro();
        estadoTurno = 3;
    }

    private void iniciaTabuleiro() {
        // Inicia o tabuleiro com as peças em seus locais iniciais
    }

    public int[][] getTabuleiro() {
        return tabuleiro;
    }

    public int getEstadoTurno() {
        return estadoTurno;
    }

    public int ganhou() {
        // Retorna: 0 (segue o jogo), 1 (Jogadora 1 venceu), 2 (Jogadora 2 venceu)
        return 0;
    }

    public void avancarJogada() {
        // Avança o estado do turno ciclicamente
        estadoTurno = (estadoTurno % 4) + 1;
    }

    public boolean jogar(int idPeca, int destinoX, int destinoY) {
        // Valida e executa a jogada, retornando true se válida ou false caso contrário
        return false;
    }

    private List<int[]> jogadasValidas(int idPeca) {
        // Retorna a lista de jogadas válidas com base na peça escolhida, retornando vazia se não for valida
        return null;
    }

    private boolean pecaValida(int idPeca) {
        return false;
    }
}