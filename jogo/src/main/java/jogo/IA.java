package jogo;

import java.util.Map;
import java.util.Random;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

//classe criada por IA responsável por fazer a inferência do modelo onnx e escolher a jogada
public class IA implements AutoCloseable {
    public static final String CAMINHO_MODELO = "python/modelo_pacoquinha.onnx";
    private static final int TAMANHO = 5;
    private static final int ENTRADAS = 3 * TAMANHO * TAMANHO + 1;

    private final OrtEnvironment ambiente;
    private final OrtSession sessao;
    private final Random aleatorio = new Random();

    public IA() throws OrtException {
        this(CAMINHO_MODELO);
    }

    public IA(String caminhoModelo) throws OrtException {
        ambiente = OrtEnvironment.getEnvironment();
        sessao = ambiente.createSession(caminhoModelo, new OrtSession.SessionOptions());
    }

    public float[][] inferencia(float[] entrada) throws OrtException {
        // retorna {logitsPeca, logitsDestino}, cada um com 25 posições
        try (OnnxTensor tensor = OnnxTensor.createTensor(ambiente, new float[][]{entrada});
             OrtSession.Result resultado = sessao.run(Map.of("entrada", tensor))) {
            float[][] logitsPeca = (float[][]) resultado.get(0).getValue();
            float[][] logitsDestino = (float[][]) resultado.get(1).getValue();
            return new float[][]{logitsPeca[0], logitsDestino[0]};
        }
    }

    public int[] escolheJogada(JogoDaPacoquinha jogo) throws OrtException {
        float[][] logits = inferencia(montaEntrada(jogo));

        float[] logitsPeca = logits[0];
        aplicaMascara(logitsPeca, mascaraOrigens(jogo));
        int origem = amostraSoftmax(logitsPeca);
        int origemLinha = origem / TAMANHO;
        int origemColuna = origem % TAMANHO;

        // o destino só pode ser uma casa alcançável a partir da origem sorteada
        float[] logitsDestino = logits[1];
        aplicaMascara(logitsDestino, jogo.destinosValidos(origemLinha, origemColuna));
        int destino = amostraSoftmax(logitsDestino);

        return new int[]{origemLinha, origemColuna, destino / TAMANHO, destino % TAMANHO};
    }

    public static float[] montaEntrada(JogoDaPacoquinha jogo) {
        // mesma ordem do flatten do numpy em lerTabuleiros: [camada][linha][coluna] + flag da paçoca
        float[][][] tabuleiro = jogo.getTabuleiro();
        float[] entrada = new float[ENTRADAS];
        int i = 0;
        for (float[][] camada : tabuleiro) {
            for (float[] linha : camada) {
                for (float casa : linha) {
                    entrada[i++] = casa;
                }
            }
        }
        entrada[i] = jogo.deveMoverPacoca() ? 1f : 0f;
        return entrada;
    }

    public static boolean[] mascaraOrigens(JogoDaPacoquinha jogo) {
        // mesma regra de camada usada em jogaRodadaPvP
        int camada = jogo.deveMoverPacoca() ? 0 : jogo.getJogadorAtual();
        float[][][] tabuleiro = jogo.getTabuleiro();
        boolean[] mascara = new boolean[TAMANHO * TAMANHO];

        for (int linha = 0; linha < TAMANHO; linha++) {
            for (int coluna = 0; coluna < TAMANHO; coluna++) {
                if (tabuleiro[camada][linha][coluna] != 1) continue;

                for (boolean destino : jogo.destinosValidos(linha, coluna)) {
                    if (destino) {
                        mascara[linha * TAMANHO + coluna] = true;
                        break;
                    }
                }
            }
        }
        return mascara;
    }

    public static void aplicaMascara(float[] logits, boolean[] mascara) {
        // -infinito vira probabilidade 0 no softmax
        for (int i = 0; i < logits.length; i++) {
            if (!mascara[i]) logits[i] = Float.NEGATIVE_INFINITY;
        }
    }

    public int amostraSoftmax(float[] logits) {
        float maximo = Float.NEGATIVE_INFINITY;
        for (float logit : logits) maximo = Math.max(maximo, logit);
        if (maximo == Float.NEGATIVE_INFINITY) {
            throw new IllegalStateException("Nenhuma jogada válida disponível");
        }

        // subtrai o máximo antes do exp para evitar overflow
        double[] probabilidades = new double[logits.length];
        double soma = 0;
        for (int i = 0; i < logits.length; i++) {
            probabilidades[i] = Math.exp(logits[i] - maximo);
            soma += probabilidades[i];
        }

        double sorteio = aleatorio.nextDouble() * soma;
        int ultimoValido = -1;
        for (int i = 0; i < logits.length; i++) {
            if (probabilidades[i] == 0) continue;
            ultimoValido = i;
            sorteio -= probabilidades[i];
            if (sorteio <= 0) return i;
        }
        return ultimoValido; // erro de arredondamento
    }

    @Override
    public void close() throws OrtException {
        sessao.close();
    }
}
