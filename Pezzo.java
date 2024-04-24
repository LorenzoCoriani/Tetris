import java.awt.*;
class Pezzo{
    Color tipo;
    boolean pezzo[][];
    int x,y;

    Pezzo(boolean pezzo[][],int x,int y){
        this.pezzo = pezzo;
        this.x = x;
        this.y = y;
    }
    //ruotiamo il pezzo in senso orario
    public void ruotaPezzo(){
        int size = pezzo.length;
        boolean[][] roteaPezzo = new boolean[size][size];
        //
        for(int i = 0; i<size; i++){
            for(int j = 0;j<size;j++){
                //calcolo dalla posizione della cella routata
                roteaPezzo[j][size-1-i] = pezzo[i][j];
            }
        }
    }
}
