import torch
import torch.nn as nn
import torch.nn.functional as F

class Rede(nn.Module):
    def __init__(self):
        super(Rede, self).__init__()
        
        # Tronco comum: Processa o tabuleiro (3 canais * 5 * 5 = 75 entradas + 1 mover paçoca)
        self.fc1 = nn.Linear(76, 128)
        self.fc2 = nn.Linear(128, 64)
        
        # Cabeça 1: Escolhe a peça de origem (25 casas do tabuleiro)
        self.saida_peca = nn.Linear(64, 25)
        
        # Cabeça 2: Escolhe o destino (25 casas do tabuleiro)
        self.saida_destino = nn.Linear(64, 25)

    def forward(self, x):
        # x já chega com 76 entradas (75 do tabuleiro + 1 do paçoca)
        
        # Extração de características compartilhadas
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        
        # Cada cabeça gera suas próprias pontuações (logits)
        logits_peca = self.saida_peca(x)
        logits_destino = self.saida_destino(x)
        
        return logits_peca, logits_destino