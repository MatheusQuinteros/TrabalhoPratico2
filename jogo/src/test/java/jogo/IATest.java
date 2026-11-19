package jogo;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import jogo.tipos_jogo.ResultadoPartida;
import jogo.tipos_jogo.JogoIA;
import jogo.tipos_jogo.JogoPvP;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

class IATest {

    private static boolean[] marcadas(int... indices) {
        boolean[] mascara = new boolean[25];
        for (int i : indices) mascara[i] = true;
        return mascara;
    }

    @Test
    void destinosDaPecaNoCantoInicial() {
        JogoPvP jogo = new JogoPvP();
        // (4,0) sobe até (1,0), parando antes da peça em (0,0); na diagonal para em (3,1) por causa da paçoca em (2,2)
        assertArrayEquals(marcadas(1 * 5 + 0, 3 * 5 + 1), jogo.destinosValidos(4, 0));
    }

    @Test
    void mascaraOrigensMarcaSoPecasDoJogadorDaVez() {
        JogoPvP jogo = new JogoPvP(); // começa com o jogador 2, linha 4
        assertArrayEquals(marcadas(20, 21, 22, 23, 24), IA.mascaraOrigens(jogo));
    }

    @Test
    void mascaraOrigensNaVezDaPacoca() {
        JogoPvP jogo = new JogoPvP();
        assertEquals(ResultadoPartida.EM_ANDAMENTO, jogo.jogaRodadaPvP(4, 0, 1, 0));
        assertTrue(jogo.deveMoverPacoca());
        assertArrayEquals(marcadas(2 * 5 + 2), IA.mascaraOrigens(jogo));
    }

    @Test
    void exploracaoTotalSoFazJogadasValidas() throws Exception {
        Path modelo = Path.of("..", IA.CAMINHO_MODELO);
        Assumptions.assumeTrue(Files.exists(modelo), "modelo ONNX não gerado");

        try (IA ia = new IA(modelo.toString())) {
            ia.setEpsilon(1); // toda jogada é aleatória
            JogoPvP jogo = new JogoPvP();
            for (int rodada = 0; rodada < 200 && !jogo.getResultado().terminou(); rodada++) {
                int[] jogada = ia.escolheJogada(jogo);
                assertDoesNotThrow(() -> jogo.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]));
            }
        }
    }

    @Test
    void aplicaMascaraZeraProbabilidadeDasInvalidas() {
        float[] logits = {5f, 1f, 3f};
        IA.aplicaMascara(logits, new boolean[]{false, true, true});
        assertEquals(Float.NEGATIVE_INFINITY, logits[0]);
        assertEquals(1f, logits[1]);
    }

    @Test
    void partidaInteiraSoComJogadasValidas() throws Exception {
        // os testes rodam a partir de jogo/, então o caminho do modelo sobe um nível
        Path modelo = Path.of("..", IA.CAMINHO_MODELO);
        Assumptions.assumeTrue(Files.exists(modelo), "modelo ONNX não gerado");

        try (IA ia = new IA(modelo.toString())) {
            JogoIA jogo = new JogoIA(ia);
            for (int rodada = 0; rodada < 200 && !jogo.getResultado().terminou(); rodada++) {
                int[] jogada = ia.escolheJogada(jogo);
                assertDoesNotThrow(() -> jogo.jogaRodadaPvP(jogada[0], jogada[1], jogada[2], jogada[3]));
            }
        }
    }
}
