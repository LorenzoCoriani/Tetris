/*
Autore: Dario Nappi
Classe: 4^F
Data: 10/3/'24
Testo: Picross
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
