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
}
