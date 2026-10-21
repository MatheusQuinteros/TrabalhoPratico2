/* começo de código gerado por IA - testes de regressão das correções e da interface. */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import tabuleiro.Tabuleiro;

public class TesteJogo {
    private static int verificacoes;

    private static void verifica(boolean condicao, String descricao) {
        verificacoes++;
        if (!condicao) throw new AssertionError(descricao);
    }

    private static Object campo(Object objeto, String nome) throws Exception {
        Field campo = objeto.getClass().getDeclaredField(nome);
        campo.setAccessible(true);
        return campo.get(objeto);
    }

    private static Tabuleiro tabuleiro(JogoDaPacoquinha jogo) throws Exception {
        return (Tabuleiro) campo(jogo, "tabuleiro_jogo");
    }

    private static void limpar(Tabuleiro tabuleiro) {
        for (float[][] camada : tabuleiro.getTabuleiro()) {
            for (float[] linha : camada) Arrays.fill(linha, 0);
        }
        int x = tabuleiro.getPacoca().getX();
        int y = tabuleiro.getPacoca().getY();
        tabuleiro.getTabuleiro()[0][x][y] = 1;
    }

    private static void cercar(Tabuleiro tabuleiro) {
        for (int x = 1; x <= 3; x++) {
            for (int y = 1; y <= 3; y++) {
                if (x != 2 || y != 2) tabuleiro.getTabuleiro()[1][x][y] = 1;
            }
        }
    }

    private static void testarVitoria() {
        ChecadorVitoria checador = new ChecadorVitoria();
        for (int estado = 0; estado < 4; estado++) {
            Turno turno = new Turno();
            for (int i = 0; i < estado; i++) turno.avançarTurno();
            Tabuleiro tabuleiro = new Tabuleiro();
            verifica(checador.ganhou(tabuleiro, turno) == -1, "Estado inicial sem vitória");
            limpar(tabuleiro);
            cercar(tabuleiro);
            int esperado = turno.deveMoverPaçoquinha() ? -1 : turno.getJogadorAtual();
            verifica(checador.ganhou(tabuleiro, turno) == esperado, "Bloqueio na fase " + estado);
            tabuleiro.getTabuleiro()[1][1][1] = 0;
            verifica(checador.ganhou(tabuleiro, turno) == -1, "Uma saída diagonal basta");
            for (int linha : new int[] {0, 4}) {
                Tabuleiro chegada = new Tabuleiro();
                limpar(chegada);
                verifica(chegada.jogar(2, 2, 0, linha, 2), "Chegada válida");
                verifica(checador.ganhou(chegada, turno) == (linha == 0 ? 1 : 2),
                        "Vitória pela linha independe de quem moveu");
            }
        }
    }

    private static void testarEncerramento() throws Exception {
        for (int jogadora = 1; jogadora <= 2; jogadora++) {
            JogoDaPacoquinha jogo = new JogoDaPacoquinha();
            if (jogadora == 1) {
                Turno turno = (Turno) campo(jogo, "turno_jogo");
                turno.avançarTurno();
                turno.avançarTurno();
            }
            Tabuleiro tabuleiro = tabuleiro(jogo);
            limpar(tabuleiro);
            cercar(tabuleiro);
            tabuleiro.getTabuleiro()[jogadora][4][4] = 1;
            verifica(jogo.jogaRodadaPvP(4, 4, 4, 0) == jogadora, "Quem bloqueou vence");
            verifica(jogo.getVencedora() == jogadora, "Vencedora persistida");
            String antes = Arrays.deepToString(tabuleiro.getTabuleiro());
            verifica(jogo.jogaRodadaPvP(4, 0, 4, 4) == jogadora, "Retorna vitória já registrada");
            verifica(jogo.jogaRodadaPvP(-1, -1, 99, 99) == jogadora, "Não processa jogada após fim");
            verifica(antes.equals(Arrays.deepToString(tabuleiro.getTabuleiro())), "Tabuleiro congelado");
            verifica(jogo.getJogadorAtual() == jogadora, "Turno congelado");
        }
        JogoDaPacoquinha jogo = new JogoDaPacoquinha();
        verifica(jogo.jogaRodadaPvP(0, 0, 1, 0) == 0, "Rejeita peça adversária");
        verifica(jogo.getJogadorAtual() == 2 && !jogo.deveMoverPacoca(), "Inválida não avança");
        verifica(jogo.jogaRodadaPvP(4, 0, 1, 0) == 3, "Primeiro movimento da jogadora 2");
        verifica(jogo.getJogadorAtual() == 1 && jogo.deveMoverPacoca(), "Fase da paçoca da 1");
        verifica(jogo.jogaRodadaPvP(2, 2, 1, 1) == 3, "Move paçoca");
        verifica(jogo.getJogadorAtual() == 1 && !jogo.deveMoverPacoca(), "Fase da peça da 1");

        // Uma paçoca cercada pode ser liberada pela peça própria na mesma vez.
        Tabuleiro tabuleiro = tabuleiro(jogo);
        limpar(tabuleiro);
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (x != 1 || y != 1) tabuleiro.getTabuleiro()[1][x][y] = 1;
            }
        }
        verifica(jogo.jogaRodadaPvP(2, 1, 4, 1) == 3, "Peça própria abre saída");
        verifica(jogo.getVencedora() == -1, "Não declara vitória prematura");
    }

    private static void dispor(Container container) {
        container.doLayout();
        for (Component filho : container.getComponents()) {
            if (filho instanceof Container) dispor((Container) filho);
        }
    }

    private static void testarInterface(String imagem) throws Exception {
        InterfaceGrafica interfaceJogo = new InterfaceGrafica();
        JPanel grade = (JPanel) ((BorderLayout) interfaceJogo.getLayout())
                .getLayoutComponent(BorderLayout.CENTER);
        JogoDaPacoquinha jogo = (JogoDaPacoquinha) campo(interfaceJogo, "jogo");
        ((JButton) grade.getComponent(20)).doClick(); // Seleciona peça em (4, 0).
        ((JButton) grade.getComponent(5)).doClick(); // Move para (1, 0).
        verifica(jogo.getCasa(1, 0) == 2 && jogo.deveMoverPacoca(), "Cliques executam movimento");
        ((JButton) grade.getComponent(12)).doClick(); // Seleciona paçoca.
        ((JButton) grade.getComponent(6)).doClick();  // Move para (1, 1).
        verifica(jogo.getCasa(1, 1) == 0 && !jogo.deveMoverPacoca(), "Cliques movem paçoca");

        if (imagem != null) {
            interfaceJogo.setSize(620, 760);
            dispor(interfaceJogo);
            BufferedImage captura = new BufferedImage(620, 760, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g = captura.createGraphics();
            interfaceJogo.printAll(g);
            g.dispose();
            ImageIO.write(captura, "png", new File(imagem));
        }

        Tabuleiro tabuleiro = tabuleiro(jogo);
        limpar(tabuleiro);
        // A paçoca está em (1, 1); cerca suas oito casas vizinhas.
        for (int x = 0; x <= 2; x++) for (int y = 0; y <= 2; y++) {
            if (x != 1 || y != 1) tabuleiro.getTabuleiro()[1][x][y] = 1;
        }
        tabuleiro.getTabuleiro()[1][4][4] = 1;
        ((JButton) grade.getComponent(24)).doClick();
        ((JButton) grade.getComponent(20)).doClick();
        verifica(jogo.getVencedora() == 1, "Interface anuncia estado vencedor");
        for (Component casa : grade.getComponents()) {
            verifica(!casa.isEnabled(), "Casa desabilitada após vitória");
        }
    }

    public static void main(String[] args) throws Exception {
        testarVitoria();
        testarEncerramento();
        SwingUtilities.invokeAndWait(() -> {
            try {
                testarInterface(args.length == 0 ? null : args[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(verificacoes + " verificações passaram.");
    }
}
/* fim de código gerado por IA - testes de regressão das correções e da interface. */
