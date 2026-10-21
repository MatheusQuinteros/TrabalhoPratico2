/* começo de código gerado por IA - interface gráfica Java Swing. */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.BiConsumer;
import javax.swing.JPanel;

/** Grade 5x5 de casas; repassa os cliques e redesenha a partir do estado do jogo. */
public class PainelTabuleiro extends JPanel {
    private static final int TAMANHO = 5;
    private final CasaView[][] casas = new CasaView[TAMANHO][TAMANHO];

    PainelTabuleiro(BiConsumer<Integer, Integer> aoClicar) {
        super(new GridLayout(TAMANHO, TAMANHO, 6, 6));
        setOpaque(false);
        setPreferredSize(new Dimension(480, 480));
        for (int x = 0; x < TAMANHO; x++) {
            for (int y = 0; y < TAMANHO; y++) {
                final int linha = x;
                final int coluna = y;
                casas[x][y] = new CasaView(x, y);
                casas[x][y].addActionListener(e -> aoClicar.accept(linha, coluna));
                add(casas[x][y]);
            }
        }
    }

    /**
     * @param selecionada casa de origem escolhida, ou null
     * @param ultimo origem e destino do último movimento ({x1, y1, x2, y2}), ou null
     */
    void atualizar(JogoDaPacoquinha jogo, int[] selecionada, int[] ultimo) {
        boolean encerrada = jogo.getVencedora() != -1;
        for (int x = 0; x < TAMANHO; x++) {
            for (int y = 0; y < TAMANHO; y++) {
                casas[x][y].setEnabled(!encerrada);
                casas[x][y].mostrar(jogo.getCasa(x, y),
                        selecionada != null && selecionada[0] == x && selecionada[1] == y,
                        ultimo != null && (ultimo[0] == x && ultimo[1] == y
                                || ultimo[2] == x && ultimo[3] == y));
            }
        }
    }
}
/* fim de código gerado por IA - interface gráfica Java Swing. */
