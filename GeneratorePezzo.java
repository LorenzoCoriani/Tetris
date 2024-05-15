/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per il 15/5/24
Testo: Tetris
*/
import java.awt.*;
import javax.swing.*;

import java.util.*;

class GeneratorePezzo{
    private ArrayList<Character> lista = new ArrayList<>();;
    private int rand;

    private JLabel[][] lblDisplay;
    private Casella[][] blocchiSolidi;
    private ImageIcon immagine;

    GeneratorePezzo(JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon immagine){
        riempiLista();

        this.lblDisplay = lblDisplay;
        this.blocchiSolidi = blocchiSolidi;
        this.immagine = immagine;
    }

    Pezzo random(){
		rand = (int) (Math.random()*lista.size());

        boolean boolPezzo[][];
        Color tipoPezzo;
        char let_estratta;

		if(lista.isEmpty()){
			riempiLista();
			//System.out.println("\n");
		}

		let_estratta = lista.remove(rand);
        //System.out.println(""+let_estratta);

		switch(let_estratta){
			case 'I':
				boolPezzo = CostantiTetris.PEZZO_I;
				tipoPezzo = CostantiTetris.TIPO_I;
				break;
			case 'J':
				boolPezzo = CostantiTetris.PEZZO_J;
				tipoPezzo = CostantiTetris.TIPO_J;
				break;
			case 'L':
				boolPezzo = CostantiTetris.PEZZO_L;
				tipoPezzo = CostantiTetris.TIPO_L;
				break;
			case 'O':
				boolPezzo = CostantiTetris.PEZZO_O;
				tipoPezzo = CostantiTetris.TIPO_O;
				break;
			case 'S':
				boolPezzo = CostantiTetris.PEZZO_S;
				tipoPezzo = CostantiTetris.TIPO_S;
				break;
			case 'Z':
				boolPezzo = CostantiTetris.PEZZO_Z;
				tipoPezzo = CostantiTetris.TIPO_Z;
				break;
			default:
				boolPezzo = CostantiTetris.PEZZO_T;
				tipoPezzo = CostantiTetris.TIPO_T;
				break;
		}
		return new Pezzo(boolPezzo, tipoPezzo, CostantiTetris.INITIAL_X, CostantiTetris.INITIAL_Y, lblDisplay, blocchiSolidi, immagine);
    }

    private void riempiLista(){
        char[] lettere = {'I', 'J', 'L', 'O', 'S', 'Z', 'T'};
        for(int i=0; i<7; i++){
            lista.add(lettere[i]);
        }
    }
}
