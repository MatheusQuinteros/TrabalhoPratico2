import torch
import torch.optim as optim

def treinar_lote(rede, otimizador, funcao_perda, lista_tabuleiros, lista_pecas, lista_destinos):
    if len(lista_tabuleiros) == 0:
        return 0.0

    inputs_tabuleiros = torch.stack(lista_tabuleiros)
    targets_pecas = torch.tensor(lista_pecas, dtype=torch.long)
    targets_destinos = torch.tensor(lista_destinos, dtype=torch.long)

    # Zera os gradientes
    otimizador.zero_grad()

    # Forward
    logits_peca, logits_destino = rede(inputs_tabuleiros)

    # Loss (usando a função de perda instanciada lá em cima)
    loss_peca = funcao_perda(logits_peca, targets_pecas)
    loss_destino = funcao_perda(logits_destino, targets_destinos)
    loss_total = loss_peca + loss_destino

    # Backward e Step
    loss_total.backward()
    otimizador.step()

    return loss_total.item()