"""Loop de treino: o Java joga as partidas e o Python treina a rede com as jogadas dos vencedores."""
import argparse
import os
import subprocess
import tempfile
import warnings

import torch
import torch.nn as nn
import torch.optim as optim

from rede import Rede
from treino import CAMINHO_PESOS, PASTA, exportarOnnx, lerTabuleiros, treinarLote

RAIZ = os.path.dirname(PASTA)  # os caminhos do Java são relativos à raiz do repositório
PASTA_MODELOS = os.path.join(PASTA, "modelos")


def montarClasspath():
    """Compila o Java uma vez e devolve o classpath para chamar o TreinoIA direto com `java`."""
    subprocess.run(["mvn", "-q", "-P", "treino", "install", "-DskipTests"], cwd=RAIZ, check=True)

    with tempfile.TemporaryDirectory() as pasta_tmp:
        arquivo_cp = os.path.join(pasta_tmp, "classpath.txt")
        subprocess.run(
            ["mvn", "-q", "-P", "treino", "-pl", "treino", "dependency:build-classpath",
             f"-Dmdep.outputFile={arquivo_cp}"],
            cwd=RAIZ, check=True,
        )
        with open(arquivo_cp) as f:
            dependencias = f.read().strip()

    return os.pathsep.join([os.path.join(RAIZ, "treino", "target", "classes"), dependencias])


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--iteracoes", type=int, default=20000)
    parser.add_argument("--partidas", type=int, default=50, help="partidas jogadas pelo Java por iteração")
    parser.add_argument("--salvar-a-cada", type=int, default=500,
                        help="intervalo de iterações entre checkpoints")
    args = parser.parse_args()
    if args.iteracoes < 1 or args.partidas < 1 or args.salvar_a_cada < 1:
        parser.error("--iteracoes, --partidas e --salvar-a-cada devem ser maiores que zero")

    warnings.filterwarnings("ignore", category=DeprecationWarning)

    classpath = montarClasspath()

    # Rede e otimizador ficam em memória durante todo o loop
    rede = Rede()
    if os.path.exists(CAMINHO_PESOS):
        rede.load_state_dict(torch.load(CAMINHO_PESOS))
    otimizador = optim.Adam(rede.parameters(), lr=0.001)
    funcao_perda = nn.CrossEntropyLoss()

    exportarOnnx(rede)  # o Java precisa de um modelo já na primeira iteração

    for iteracao in range(1, args.iteracoes + 1):
        subprocess.run(
            ["java", "-cp", classpath, "treino.TreinoIA", str(args.partidas)],
            cwd=RAIZ, check=True,
        )

        inputs, t_pecas, t_destinos = lerTabuleiros()
        if inputs is None:
            print(f"Iteração {iteracao}: nenhuma partida com vencedor, sem treino", flush=True)
        else:
            rede.train()
            loss = treinarLote(rede, otimizador, funcao_perda, inputs, t_pecas, t_destinos)
            print(f"Iteração {iteracao}: loss {loss:.4f}", flush=True)

            torch.save(rede.state_dict(), CAMINHO_PESOS)
            exportarOnnx(rede)

        if iteracao % args.salvar_a_cada == 0:
            os.makedirs(PASTA_MODELOS, exist_ok=True)
            nome = f"modelo_pacoquinha_iteracao_{iteracao:04d}"
            caminho_pesos = os.path.join(PASTA_MODELOS, nome + ".pth")
            caminho_onnx = os.path.join(PASTA_MODELOS, nome + ".onnx")
            torch.save(rede.state_dict(), caminho_pesos)
            exportarOnnx(rede, caminho_onnx)
            print(f"Checkpoint salvo: {caminho_pesos} e {caminho_onnx}", flush=True)
