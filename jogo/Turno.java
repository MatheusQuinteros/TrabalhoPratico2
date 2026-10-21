package jogo;

public class Turno{
    private int estadoTurno;

    public Turno(){
        estadoTurno = 0;
    }

    public int getJogadorAtual(){ //Devolve 1 caso seja vez do jogador 1, e 2 caso seja o jogador 2, se nao for nenhum dos dois, retorna -1
        if (estadoTurno==1||estadoTurno==2){
            return 1;
        }
        if (estadoTurno==3||estadoTurno==0){
            return 2;
        }
        return -1;
    }

    public boolean deveMoverPaçoquinha(){//retorna true caso seja verdade, e false caso contrario
        if (estadoTurno == 1|| estadoTurno == 3){
            return true;
        }
        return false;
    }

    public void avançarTurno(){// avança a jogada
        estadoTurno = (estadoTurno + 1) % 4;
    }
}