/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per ill 15/5/24
Testo: Tetris
*/
import java.awt.*;

class Casella{
    //attributi
    Color colore;
    boolean occupato;
    //costruttori
    Casella(){
        this.colore = Color.BLACK;
        this.occupato = false;
    }
    Casella(Color colore){
        this.colore = colore;
        this.occupato = false;
    }
    Casella(Color colore, boolean occupato){
        this.colore = colore;
        this.occupato = occupato;
    }
    //metodi
    void invertiStato(){
        if(occupato)
            occupato=false;
        else
            occupato=true;
    }
}
