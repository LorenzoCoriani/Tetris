import java.awt.*;
class Pezzo{//TODO: rinomina attributi
    Color tipo;
    boolean pezzo[][];
    int x,y;

    Pezzo(boolean pezzo[][], Color tipo){
        this.pezzo = pezzo;
        this.x = 4;
        this.y = 0;
        this.tipo = tipo;
    }

    Pezzo(boolean pezzo[][],int x,int y, Color tipo){
        this.pezzo = pezzo;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }
    //ruotiamo il pezzo in senso orario
    public void ruotaPezzo(){
        int size = pezzo.length;
        boolean[][] roteaPezzo = new boolean[size][size];
        //
        for(int i = 0; i<size; i++){
            for(int j = 0;j<size;j++){
                //calcolo dalla posizione della cella ruotata
                roteaPezzo[j][size-1-i] = pezzo[i][j];
            }
        }
    }
}
