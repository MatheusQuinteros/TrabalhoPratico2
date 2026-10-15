public class Tabuleiro {
    private int[][] tabuleiro;
    private static final int TAMANHO = 5; //Tamanho sempre será o mesmo

    public Tabuleiro(){
        tabuleiro = new int[TAMANHO][TAMANHO];
        tabuleiro = iniciaTabuleiro();
    }

    public void  iniciaTabuleiro(){
        for (int i = 0;i<TAMANHO;i++){
            for (int j = 0;j<TAMANHO;j++){
                
                if(i==0){
                    tabuleiro[i][j] = 1;
                }
                if(i==4){
                    tabuleiro[i][j] = 2;    
                }
                if(i==2 & j==2){
                    tabuleiro[i][j] = 3;
                }
                else tabuleiro[i][j] = 0;
            }
        }
    }

    public int[][] getTabuleiro() {
        return tabuleiro;
    }

    public int getCasa(int x, int y){
        return this.tabuleiro[x][y];
    }

    
}
