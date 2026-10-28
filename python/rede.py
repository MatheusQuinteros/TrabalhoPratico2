import torch
import torch.nn as nn
import torch.nn.functional as F

class Rede(nn.Module):
    def __init__(self):
        super(Rede, self).__init__()
        
        # Tronco comum: Processa o tabuleiro (3 canais * 5 * 5 = 75 entradas + 1 entrada para ver se eh jogador ou paçoca)
        self.fc1 = nn.Linear(76, 128)
        self.fc2 = nn.Linear(128, 64)
        
        # Cabeça 1: Escolhe a peça de origem (25 casas do tabuleiro)
        self.saida_peca = nn.Linear(64, 25)
        
        # Cabeça 2: Escolhe o destino (25 casas do tabuleiro)
        self.saida_destino = nn.Linear(64, 25)

    def forward(self, x):
        # x chega como [Batch, 3, 5, 5]
        x = x.view(x.size(0), -1) # Achata para 75
        
        # Extração de características compartilhadas
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        
        # Cada cabeça gera suas próprias pontuações (logits)
        logits_peca = self.saida_peca(x)
        logits_destino = self.saida_destino(x)
        
        return logits_peca, logits_destino