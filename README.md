# Jogo da Paçoquinha

Interface gráfica em Java Swing, sem dependências externas. Requer JDK 8 ou superior
e ambiente gráfico para abrir a janela.

## Estrutura

```
jogo/              lógica da partida: JogoDaPacoquinha, Turno, ChecadorVitoria
jogo/tabuleiro/    Tabuleiro e Peca (package jogo.tabuleiro)
interfacegrafica/  interface Swing: InterfaceGrafica (janela), PainelTabuleiro, CasaView
testes/            TesteJogo
```

## Execução

Na pasta do projeto, compile e execute:

```bash
javac -encoding UTF-8 -d out jogo/tabuleiro/*.java jogo/*.java interfacegrafica/*.java
java -cp out interfacegrafica.InterfaceGrafica
```

Selecione a peça e depois a casa de destino. A mensagem no topo indica a jogadora
da vez e se deve mover uma peça própria ou a paçoquinha. Clique novamente na peça
selecionada para cancelar a seleção. As peças devem percorrer toda a distância
livre na direção escolhida. O botão **Nova partida** reinicia o jogo. As faixas coloridas marcam a linha-objetivo
de cada jogadora, e o contorno tracejado indica o último movimento.

A jogadora 1 vence ao levar a paçoquinha à linha de cima; a jogadora 2, à linha de
baixo, independentemente de quem a moveu. Quem termina o movimento de uma peça
própria deixando a paçoquinha sem saída vence por bloqueio da próxima jogadora.

## Testes

```bash
javac -encoding UTF-8 -cp out -d out testes/TesteJogo.java
java -Djava.awt.headless=true -cp out testes.TesteJogo
```

Os testes cobrem as fases do turno, vitória por chegada e bloqueio, preservação
do estado após o encerramento e cliques da interface. Podem rodar sem monitor.

As alterações das correções 3 e 4 e os trechos produzidos por IA estão identificados
por comentários nos arquivos Java.
