# Jogo da Paçoquinha

Interface gráfica em Java Swing, sem dependências externas. Requer JDK 8 ou superior
e ambiente gráfico para abrir a janela.

Na pasta do projeto, compile e execute:

```bash
mkdir -p /tmp/pacoca-classes
javac -encoding UTF-8 -d /tmp/pacoca-classes tabuleiro/*.java Turno.java ChecadorVitoria.java JogoDaPacoquinha.java InterfaceGrafica.java
java -cp /tmp/pacoca-classes InterfaceGrafica
```

Selecione a peça e depois a casa de destino. A mensagem no topo indica a jogadora
da vez e se deve mover uma peça própria ou a paçoquinha. Clique novamente na peça
selecionada para cancelar a seleção. As peças devem percorrer toda a distância
livre na direção escolhida. O botão **Nova partida** reinicia o jogo.

A jogadora 1 vence ao levar a paçoquinha à linha de cima; a jogadora 2, à linha de
baixo, independentemente de quem a moveu. Quem termina o movimento de uma peça
própria deixando a paçoquinha sem saída vence por bloqueio da próxima jogadora.

## Testes

```bash
javac -encoding UTF-8 -cp /tmp/pacoca-classes -d /tmp/pacoca-classes testes/TesteJogo.java
java -Djava.awt.headless=true -cp /tmp/pacoca-classes TesteJogo
```

Os testes cobrem as fases do turno, vitória por chegada e bloqueio, preservação
do estado após o encerramento e cliques da interface. Podem rodar sem monitor.

As alterações das correções 3 e 4 e os trechos produzidos por IA estão identificados
por comentários nos arquivos Java.
