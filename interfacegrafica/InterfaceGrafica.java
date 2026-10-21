/* começo de código gerado por IA - interface gráfica Java Swing. */
package interfacegrafica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import jogo.JogoDaPacoquinha;

/** Janela do jogo: controla seleção e cliques e delega o desenho ao PainelTabuleiro. */
public class InterfaceGrafica extends JPanel {
    private static final Color FUNDO = new Color(250, 245, 233);
    private final JLabel turno = new JLabel("", SwingConstants.CENTER);
    private final JLabel mensagem = new JLabel("", SwingConstants.CENTER);
    private final PainelTabuleiro tabuleiro = new PainelTabuleiro(this::clicar);
    private JogoDaPacoquinha jogo = new JogoDaPacoquinha();
    private int[] origem;
    private int[] ultimoMovimento;

    public InterfaceGrafica() {
        super(new BorderLayout(0, 16));
        setBackground(FUNDO);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel cabecalho = new JPanel(new GridLayout(0, 1, 0, 7));
        cabecalho.setOpaque(false);
        JLabel titulo = new JLabel("Jogo da Paçoquinha", SwingConstants.CENTER);
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        titulo.setForeground(CasaView.TEXTO);
        turno.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        cabecalho.add(titulo);
        cabecalho.add(turno);
        add(cabecalho, BorderLayout.NORTH);

        add(tabuleiro, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout(0, 12));
        rodape.setOpaque(false);
        mensagem.setForeground(CasaView.TEXTO);
        mensagem.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        rodape.add(mensagem, BorderLayout.NORTH);
        JLabel regras = new JLabel("<html><div style='text-align:center'>"
                + "Jogadora 1 (azul) leva a paçoquinha à faixa de cima · "
                + "jogadora 2 (vermelha) à faixa de baixo"
                + "<br>Mova em linha reta ou diagonal até a borda ou outra peça."
                + "</div></html>", SwingConstants.CENTER);
        regras.setForeground(CasaView.TEXTO);
        rodape.add(regras, BorderLayout.CENTER);
        JButton reiniciar = new JButton("Nova partida");
        reiniciar.addActionListener(e -> reiniciar());
        rodape.add(reiniciar, BorderLayout.SOUTH);
        add(rodape, BorderLayout.SOUTH);
        atualizar();
        orientar();
    }

    private void clicar(int x, int y) {
        if (jogo.getVencedora() != -1) return;

        int camada = jogo.deveMoverPacoca() ? 0 : jogo.getJogadorAtual();
        if (origem != null && origem[0] == x && origem[1] == y) {
            origem = null;
            orientar();
        } else if (jogo.getCasa(x, y) == camada) {
            origem = new int[] {x, y};
            mensagem.setText("Agora clique na última casa livre da direção desejada.");
        } else if (origem == null) {
            mensagem.setText(jogo.deveMoverPacoca()
                    ? "Selecione a paçoquinha dourada."
                    : "Selecione uma peça da jogadora " + jogo.getJogadorAtual() + ".");
        } else {
            int resultado = jogo.jogaRodadaPvP(origem[0], origem[1], x, y);
            if (resultado == 0) {
                mensagem.setText("Movimento inválido. Escolha outro destino.");
            } else {
                ultimoMovimento = new int[] {origem[0], origem[1], x, y};
                origem = null;
                if (jogo.getVencedora() != -1) {
                    mensagem.setText("Partida encerrada. Clique em Nova partida para jogar novamente.");
                } else {
                    orientar();
                }
            }
        }
        atualizar();
    }

    private void orientar() {
        mensagem.setText(jogo.deveMoverPacoca()
                ? "Selecione a paçoquinha e depois o destino."
                : "Selecione uma peça sua e depois o destino.");
    }

    private void atualizar() {
        boolean encerrada = jogo.getVencedora() != -1;
        int jogadora = encerrada ? jogo.getVencedora() : jogo.getJogadorAtual();
        turno.setForeground(jogadora == 1 ? CasaView.JOGADORA_1 : CasaView.JOGADORA_2);
        turno.setText(encerrada ? "Jogadora " + jogadora + " venceu!"
                : "Vez da jogadora " + jogadora + " · "
                + (jogo.deveMoverPacoca() ? "mova a paçoquinha" : "mova uma peça sua"));
        tabuleiro.atualizar(jogo, origem, ultimoMovimento);
    }

    private void reiniciar() {
        if (jogo.getVencedora() == -1 && JOptionPane.showConfirmDialog(this,
                "Iniciar uma nova partida? O progresso atual será perdido.", "Nova partida",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        jogo = new JogoDaPacoquinha();
        origem = null;
        ultimoMovimento = null;
        orientar();
        atualizar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Jogo da Paçoquinha");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setContentPane(new InterfaceGrafica());
            janela.pack();
            janela.setMinimumSize(new Dimension(540, 700));
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
/* fim de código gerado por IA - interface gráfica Java Swing. */
