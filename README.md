# Jogo da Paçoquinha

Projeto Maven em Java 21 com interface Swing. Requer JDK 21, Apache Maven 3.8+
e ambiente gráfico para abrir a janela.

## Estrutura

```
pom.xml
src/main/java/
  jogo/              lógica da partida: JogoDaPacoquinha, Turno, ChecadorVitoria
  jogo/tabuleiro/    Tabuleiro e Peca (package jogo.tabuleiro)
  interfacegrafica/  interface Swing: InterfaceGrafica (janela), PainelTabuleiro, CasaView
  ia/                oponente com IA: JogoIA e TreinoIA
src/test/java/       testes JUnit 5
ia/python/           rede neural em PyTorch (treino fora do Maven)
```

## Execução

Na pasta do projeto, compile e execute:

```bash
mvn compile exec:java
```

Para gerar um `.jar` executável:

```bash
mvn package
java -jar target/jogo-da-pacoquinha-1.0-SNAPSHOT.jar
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
mvn test
```

Os testes JUnit 5 ficam em `src/test/java` e rodam sem monitor.

As alterações das correções 3 e 4 e os trechos produzidos por IA estão identificados
por comentários nos arquivos Java.
