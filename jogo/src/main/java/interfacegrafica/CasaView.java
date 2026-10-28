/* começo de código gerado por IA - interface gráfica Java Swing. */
package interfacegrafica;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Optional;
import javax.swing.JButton;
import jogo.Jogadora;
import jogo.tabuleiro.Camada;

/** Desenha uma casa do tabuleiro a partir do estado recebido; não conhece o jogo. */
public class CasaView extends JButton {
    static final Color TEXTO = new Color(62, 48, 38);
    static final Color JOGADORA_1 = new Color(44, 112, 118);
    static final Color JOGADORA_2 = new Color(167, 70, 59);
    private static final Color PACOQUINHA = new Color(200, 145, 47);
    private static final Color CASA_ESCURA = new Color(233, 221, 198);
    private static final Color CASA_CLARA = new Color(244, 234, 215);
    private static final Color ULTIMO_MOVIMENTO = new Color(62, 48, 38, 90);

    private final int linha;
    private final int coluna;
    private Camada conteudo; // null quando a casa está vazia
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

    static Color cor(Jogadora jogadora) {
        return jogadora == Jogadora.UM ? JOGADORA_1 : JOGADORA_2;
    }

    void mostrar(Optional<Camada> conteudo, boolean selecionada, boolean ultimoMovimento) {
        this.conteudo = conteudo.orElse(null);
        this.selecionada = selecionada;
        this.ultimoMovimento = ultimoMovimento;
        getAccessibleContext().setAccessibleName(descricaoPosicao() + ": "
                + conteudo.map(CasaView::descrever).orElse("vazia")
                + (selecionada ? ", selecionada" : "")
                + (ultimoMovimento ? ", último movimento" : ""));
        repaint();
    }

    private String descricaoPosicao() {
        return "Linha " + (linha + 1) + ", coluna " + (coluna + 1);
    }

    private static String descrever(Camada camada) {
        switch (camada) {
            case PACOQUINHA: return "paçoquinha";
            case JOGADORA_1: return "jogadora 1";
            default: return "jogadora 2";
        }
    }

    /** Jogadora cuja linha-objetivo é esta, ou vazio se não é linha de objetivo. */
    private Optional<Jogadora> donaDaLinha() {
        for (Jogadora jogadora : Jogadora.values()) {
            if (jogadora.getLinhaObjetivo() == linha) return Optional.of(jogadora);
        }
        return Optional.empty();
    }

    private String objetivo() {
        return donaDaLinha().map(j -> " · objetivo da jogadora " + j.getNumero()).orElse("");
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
        Optional<Jogadora> dona = donaDaLinha();
        if (dona.isEmpty()) return;
        Color cor = cor(dona.get());
        g.setColor(new Color(cor.getRed(), cor.getGreen(), cor.getBlue(), 40));
        g.fillRoundRect(0, 0, w, h, 14, 14);
        g.setColor(cor);
        int faixa = Math.max(4, h / 16);
        boolean faixaNoTopo = dona.get() == Jogadora.UM;
        g.fillRoundRect(4, faixaNoTopo ? 2 : h - faixa - 2, w - 8, faixa, faixa, faixa);
    }

    private void pintarPeca(Graphics2D g, int w, int h) {
        if (conteudo == null) return;
        int tamanho = Math.min(w, h) * 2 / 3;
        int esquerda = (w - tamanho) / 2;
        int topo = (h - tamanho) / 2;
        String texto;
        if (conteudo == Camada.PACOQUINHA) {
            g.setColor(PACOQUINHA);
            g.fillRoundRect(esquerda, topo, tamanho, tamanho, 14, 14);
            texto = "Paç.";
        } else {
            Jogadora dona = conteudo == Camada.JOGADORA_1 ? Jogadora.UM : Jogadora.DOIS;
            g.setColor(cor(dona));
            g.fillOval(esquerda, topo, tamanho, tamanho);
            texto = String.valueOf(dona.getNumero());
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(12, tamanho / 3)));
        g.drawString(texto, (w - g.getFontMetrics().stringWidth(texto)) / 2,
                (h - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent());
    }
}
/* fim de código gerado por IA - interface gráfica Java Swing. */
