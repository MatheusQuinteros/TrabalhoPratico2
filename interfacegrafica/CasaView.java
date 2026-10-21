/* começo de código gerado por IA - interface gráfica Java Swing. */
package interfacegrafica;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

/** Desenha uma casa do tabuleiro a partir do estado recebido; não conhece o jogo. */
public class CasaView extends JButton {
    static final Color TEXTO = new Color(62, 48, 38);
    static final Color JOGADORA_1 = new Color(44, 112, 118);
    static final Color JOGADORA_2 = new Color(167, 70, 59);
    private static final Color PACOQUINHA = new Color(200, 145, 47);
    private static final Color CASA_ESCURA = new Color(233, 221, 198);
    private static final Color CASA_CLARA = new Color(244, 234, 215);
    private static final Color ULTIMO_MOVIMENTO = new Color(62, 48, 38, 90);
    private static final int ULTIMA_LINHA = 4;

    private final int linha;
    private final int coluna;
    private int peca = -1;
    private boolean selecionada;
    private boolean ultimoMovimento;

    CasaView(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setToolTipText(descricaoPosicao() + objetivo());
    }

    void mostrar(int peca, boolean selecionada, boolean ultimoMovimento) {
        this.peca = peca;
        this.selecionada = selecionada;
        this.ultimoMovimento = ultimoMovimento;
        getAccessibleContext().setAccessibleName(descricaoPosicao() + ": "
                + (peca == -1 ? "vazia" : peca == 0 ? "paçoquinha" : "jogadora " + peca)
                + (selecionada ? ", selecionada" : "")
                + (ultimoMovimento ? ", último movimento" : ""));
        repaint();
    }

    private String descricaoPosicao() {
        return "Linha " + (linha + 1) + ", coluna " + (coluna + 1);
    }

    private String objetivo() {
        if (linha == 0) return " · objetivo da jogadora 1";
        if (linha == ULTIMA_LINHA) return " · objetivo da jogadora 2";
        return "";
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g.setColor((linha + coluna) % 2 == 0 ? CASA_ESCURA : CASA_CLARA);
        g.fillRoundRect(0, 0, w, h, 14, 14);
        pintarObjetivo(g, w, h);
        if (ultimoMovimento) {
            g.setColor(ULTIMO_MOVIMENTO);
            g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1, new float[] {6, 5}, 0));
            g.drawRoundRect(6, 6, w - 13, h - 13, 10, 10);
        }
        pintarPeca(g, w, h);
        if (selecionada || hasFocus()) {
            g.setColor(TEXTO);
            g.setStroke(new BasicStroke(3));
            g.drawRoundRect(3, 3, w - 7, h - 7, 12, 12);
        }
        g.dispose();
    }

    /** Faixa na borda externa das linhas para onde cada jogadora quer levar a paçoquinha. */
    private void pintarObjetivo(Graphics2D g, int w, int h) {
        if (linha != 0 && linha != ULTIMA_LINHA) return;
        Color cor = linha == 0 ? JOGADORA_1 : JOGADORA_2;
        g.setColor(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 40));
        g.fillRoundRect(0, 0, w, h, 14, 14);
        g.setColor(cor);
        int faixa = Math.max(4, h / 16);
        g.fillRoundRect(4, linha == 0 ? 2 : h - faixa - 2, w - 8, faixa, faixa, faixa);
    }

    private void pintarPeca(Graphics2D g, int w, int h) {
        if (peca == -1) return;
        int tamanho = Math.min(w, h) * 2 / 3;
        int esquerda = (w - tamanho) / 2;
        int topo = (h - tamanho) / 2;
        g.setColor(peca == 0 ? PACOQUINHA : peca == 1 ? JOGADORA_1 : JOGADORA_2);
        if (peca == 0) g.fillRoundRect(esquerda, topo, tamanho, tamanho, 14, 14);
        else g.fillOval(esquerda, topo, tamanho, tamanho);
        String texto = peca == 0 ? "Paç." : String.valueOf(peca);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(12, tamanho / 3)));
        g.drawString(texto, (w - g.getFontMetrics().stringWidth(texto)) / 2,
                (h - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent());
    }
}
/* fim de código gerado por IA - interface gráfica Java Swing. */
