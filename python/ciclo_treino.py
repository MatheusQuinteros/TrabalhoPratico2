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
    parser.add_argument("--iteracoes", type=int, default=100)
    parser.add_argument("--partidas", type=int, default=50, help="partidas jogadas pelo Java por iteração")
    args = parser.parse_args()

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
            continue

        rede.train()
        loss = treinarLote(rede, otimizador, funcao_perda, inputs, t_pecas, t_destinos)
        print(f"Iteração {iteracao}: loss {loss:.4f}", flush=True)

        torch.save(rede.state_dict(), CAMINHO_PESOS)
        exportarOnnx(rede)
