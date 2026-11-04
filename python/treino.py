import json
import os
import numpy as np
import torch
import torch.optim as optim
import torch.nn as nn
import torch.nn.functional as F

from rede import Rede

PASTA = os.path.dirname(os.path.abspath(__file__))
CAMINHO_JSON = os.path.join(PASTA, "jogadas_vencedor.json")
CAMINHO_PESOS = os.path.join(PASTA, "modelo_pacoquinha.pth")
# O ONNX é consumido pelo jogo; mantenha os pesos de treino e os dados em python/.
CAMINHO_ONNX = os.path.join(os.path.dirname(PASTA), "jogo", "modelo_pacoquinha.onnx")

def entropia(logits):
    """Entropia média da distribuição softmax: perto de 0 quando a rede fica determinística."""
    log_probs = F.log_softmax(logits, dim=1)
    return -(log_probs.exp() * log_probs).sum(dim=1).mean()

def treinarLote(rede, otimizador, funcao_perda, inputs_tabuleiros, targets_pecas, targets_destinos, beta=0.01):
    """Faz um passo de treino e retorna (loss de imitação, entropia média das duas cabeças)."""
    if inputs_tabuleiros.size(0) == 0:
        return 0.0, 0.0

    # Zera os gradientes
    otimizador.zero_grad()

    # Forward
    logits_peca, logits_destino = rede(inputs_tabuleiros)

    # Loss (usando a função de perda instanciada lá em cima)
    loss_peca = funcao_perda(logits_peca, targets_pecas)
    loss_destino = funcao_perda(logits_destino, targets_destinos)
    loss_imitacao = loss_peca + loss_destino

    # Bônus de entropia: penaliza a rede quando ela fica confiante demais e para de variar as jogadas
    entropia_media = (entropia(logits_peca) + entropia(logits_destino)) / 2
    loss_total = loss_imitacao - beta * entropia_media

    loss_total.backward()
    otimizador.step()

    return loss_imitacao.item(), entropia_media.item()

def lerTabuleiros(caminho_json=CAMINHO_JSON):
    """Lê o arquivo JSON gerado pelo Java, formata os dados e retorna

    os tensores prontos para o treinamento com a Rede de 76 entradas.
    """
    #trecho feito por IA
    try:
        with open(caminho_json, "r") as f:
            dados_jogadas = json.load(f)
    except FileNotFoundError:
        print(f"Aviso: Arquivo {caminho_json} não encontrado.")
        return None, None, None

    if not dados_jogadas:
        return None, None, None

    lista_inputs = []
    lista_targets_pecas = []
    lista_targets_destinos = []

    for passo in dados_jogadas:
        # 1. Pega o tabuleiro do JSON e achata para 1D (75 elementos para 3x5x5)
        tabuleiro_array = np.array(passo["tabuleiro"], dtype=np.float32).flatten()

        # 2. Converte o booleano 'jogaPacoquinha' em 1.0 ou 0.0
        val_pacoquinha = 1.0 if passo["jogaPacoquinha"] else 0.0

        # 3. Concatena os 75 valores do tabuleiro + 1 do paçoca = 76 entradas exatas
        entrada_completa = np.concatenate(
            [tabuleiro_array, np.array([val_pacoquinha], dtype=np.float32)]
        )
        lista_inputs.append(entrada_completa)

        # 4. Processa a jogada [orig_lin, orig_col, dest_lin, dest_col]
        jogada = passo["jogada"]
        orig_lin, orig_col, dest_lin, dest_col = jogada

        # Mapeia a coordenada 2D (5x5) para índice linear de 0 a 24
        idx_peca = orig_lin * 5 + orig_col
        idx_destino = dest_lin * 5 + dest_col

        lista_targets_pecas.append(idx_peca)
        lista_targets_destinos.append(idx_destino)
    # fim trecho feito por IA
    
    inputs_tabuleiros = torch.tensor(np.array(lista_inputs), dtype=torch.float32)
    targets_pecas = torch.tensor(lista_targets_pecas, dtype=torch.long)
    targets_destinos = torch.tensor(lista_targets_destinos, dtype=torch.long)

    return inputs_tabuleiros, targets_pecas, targets_destinos

def carregarPesos(rede, caminho_pesos=CAMINHO_PESOS):
    """Carrega os pesos salvos, se existirem e forem da mesma arquitetura da rede."""
    if not os.path.exists(caminho_pesos):
        return
    try:
        rede.load_state_dict(torch.load(caminho_pesos))
    except RuntimeError:
        print(f"Aviso: {caminho_pesos} é de outra arquitetura de rede, começando do zero.")

def exportarOnnx(rede, caminho_onnx=CAMINHO_ONNX):
    """Exporta a rede para ONNX, formato lido pela classe IA no Java."""
    rede.eval()
    torch.onnx.export(
        rede,
        torch.zeros(1, 76),
        caminho_onnx,
        input_names=["entrada"],
        output_names=["logits_peca", "logits_destino"],
        dynamic_axes={"entrada": {0: "lote"}},
        dynamo=False,
    )

# ==========================================
if __name__ == "__main__":
    # Instancia a rede, otimizador e perda fora do loop
    rede = Rede()
    carregarPesos(rede)
    otimizador = optim.Adam(rede.parameters(), lr=0.001)
    funcao_perda = nn.CrossEntropyLoss()

    inputs, t_pecas, t_destinos = lerTabuleiros()

    if inputs is not None:
        rede.train()
        loss, ent = treinarLote(
            rede, otimizador, funcao_perda, inputs, t_pecas, t_destinos
        )
        print(f"Loss Total: {loss:.4f} | entropia: {ent:.4f}")

    # Salva sempre, para que a primeira execução gere o modelo inicial
    torch.save(rede.state_dict(), CAMINHO_PESOS)
    exportarOnnx(rede)
