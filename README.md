# Jogo da Paçoquinha

Projeto Maven em Java 21 com interface Swing. Requer JDK 21, Apache Maven 3.8+
e ambiente gráfico para abrir a janela. Confira as versões no terminal:

```bash
java -version
javac -version
mvn -version
```

O comando `mvn -version` informa qual Java o próprio Maven está usando; ele
também precisa apontar para o JDK 21. Se a compilação informar `release version
21 not supported`, o Maven está usando um JDK antigo. Ajuste `JAVA_HOME` e o
`PATH` para o JDK 21 e abra um terminal novo.

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
mvn -P treino install -DskipTests
mvn -f treino/pom.xml compile exec:java
```

O primeiro comando compila os módulos e instala o jogo no repositório local do
Maven; o segundo compila e roda só o treino. O perfil `treino` inclui o módulo
de treino no build. Para compilar os dois módulos juntos sem instalar o jogo,
use `mvn -P treino compile`.

Parte em Python:

```bash
pip install -r python/requirements.txt
python python/ciclo_treino.py
```

O ciclo executa as partidas Java e treina a rede em Python (4.000 iterações de 50
partidas por padrão). A cada 500 iterações, salva checkpoints `.pth` e `.onnx`
em `python/modelos/`, identificados pelo número da iteração. O arquivo ONNX
usado pelo jogo continua sendo atualizado em `jogo/modelo_pacoquinha.onnx`; os
pesos retomáveis (`.pth`) e os dados de treino ficam em `python/`.

As alterações das correções 3 e 4 e os trechos produzidos por IA estão identificados
por comentários nos arquivos Java.

## Executável

`mvn package` gera `jogo/target/jogo-1.0-SNAPSHOT.jar`, que já traz o onnxruntime e o
modelo `modelo_pacoquinha.onnx`. Ele roda de qualquer pasta (precisa de Java 21):

```bash
java -jar jogo/target/jogo-1.0-SNAPSHOT.jar
```

Para um executável que não precisa de Java instalado, use o `jpackage`:

```bash
mkdir -p dist/entrada && cp jogo/target/jogo-1.0-SNAPSHOT.jar dist/entrada/
jpackage --type app-image --name JogoDaPacoquinha --input dist/entrada \
  --main-jar jogo-1.0-SNAPSHOT.jar --main-class interfacegrafica.InterfaceGrafica --dest dist
```

O executável fica em `dist/JogoDaPacoquinha/bin/JogoDaPacoquinha`. Com `--type deb` sai um
instalador `.deb`; para `.exe`, rode o mesmo comando no Windows.
