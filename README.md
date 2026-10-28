# Jogo da Paçoquinha

Projeto Maven em Java 21 com interface Swing. Requer JDK 21, Apache Maven 3.8+
e ambiente gráfico para abrir a janela.

## Estrutura

Projeto Maven com dois módulos. O treino da IA fica separado e não entra no jogo.

```
pom.xml                    POM pai: versões em comum e lista de módulos
jogo/                      módulo do jogo (vira o .jar executável)
  src/main/java/
    jogo/                  regras: JogoDaPacoquinha, Turno, ChecadorVitoria
    jogo/tabuleiro/        Tabuleiro e Peca
    interfacegrafica/      interface Swing: InterfaceGrafica, PainelTabuleiro, CasaView
  src/test/java/           testes JUnit 5
treino/                    módulo de treino (só compila quando pedido)
  src/main/java/treino/    partidas IA x IA que geram o gabarito: TreinoIA, JogoIA
python/                    rede neural e treino em PyTorch (fora do Maven)
dados/                     gabaritos e pesos gerados (não versionado)
```

## Execução

Na pasta do projeto, compile e execute o jogo:

```bash
mvn compile exec:java -pl jogo
```

Para gerar um `.jar` executável:

```bash
mvn package
java -jar jogo/target/jogo-1.0-SNAPSHOT.jar
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

Os testes JUnit 5 ficam em `jogo/src/test/java` e rodam sem monitor.

## Treino da IA

O Java joga as partidas de treino e grava o gabarito; o Python treina a rede e
exporta os pesos que o Java usa na inferência.

```bash
mvn install
mvn -f treino/pom.xml compile exec:java
```

O primeiro comando instala o módulo do jogo no repositório local do Maven; o
segundo compila e roda só o treino. Para compilar os dois módulos juntos (por
exemplo, no VS Code), use o perfil `treino`: `mvn -P treino compile`.

Parte em Python:

```bash
pip install -r python/requirements.txt
```

As alterações das correções 3 e 4 e os trechos produzidos por IA estão identificados
por comentários nos arquivos Java.
