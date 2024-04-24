/*
Autore: Dario Nappi
Classe: 4^F
Data: 10/3/'24
Testo: Picross
*/
import javax.swing.*;

class Casella{
    //attributi
    JLabel lbl;
    boolean occupato;
    //costruttori
    Casella(){
        this.lbl = new JLabel();
        this.occupato = false;
    }
    Casella(JLabel lbl){
        this.lbl = lbl;
        this.occupato = false;
    }
    Casella(String etichetta, boolean occupato){
        this.lbl = new JLabel(etichetta);
        this.occupato = occupato;
    }
    Casella(JLabel lbl, boolean occupato){
        this.lbl = lbl;
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
