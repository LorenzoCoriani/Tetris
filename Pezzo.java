import java.awt.*;
import javax.swing.*;
import java.util.*;

class Pezzo{//TODO: rinomina attributi

    Color tipo;
    boolean pezzo[][];

    private JLabel[][] lblDisplay;
    private Casella[][] blocchiSolidi;
    private ImageIcon scaledIcon;

    int x,y;

    Pezzo(boolean pezzo[][], Color tipo, int x, int y, JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon scaledIcon){
        this.pezzo = pezzo;
        this.tipo = tipo;
        this.x = x;
        this.y = y;

        this.lblDisplay = lblDisplay;
        this.blocchiSolidi = blocchiSolidi;
        this.scaledIcon = scaledIcon;
    }

    Pezzo(JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon scaledIcon){
        this.lblDisplay = lblDisplay;
        this.blocchiSolidi = blocchiSolidi;
        this.scaledIcon = scaledIcon;
    }

    //metodi

    Pezzo random(){ //TODO: fixa
        boolean boolPezzo[][];
        Color tipoPezzo;// = Color.WHITE;

		/*int[] numeri = {0, 1, 2, 3, 4, 5, 6};
		ArrayList<Integer> lista = new ArrayList<>();
		*/
        int num_estratto = 0;
        /*int rand = 0;

		if(lista.isEmpty()){
			for(int num : numeri){
				lista.add(num);
			}
		}else{
			rand = (int) (Math.random()*lista.size());

			num_estratto = lista.remove(rand);

			System.out.println(""+lista.size());
		}*/
		num_estratto = (int) (Math.random()*7);

		switch(num_estratto){
			case 0:
				boolPezzo = CostantiTetris.PEZZO_I;
				tipoPezzo = CostantiTetris.TIPO_I;
				break;
			case 1:
				boolPezzo = CostantiTetris.PEZZO_J;
				tipoPezzo = CostantiTetris.TIPO_J;
				break;
			case 2:
				boolPezzo = CostantiTetris.PEZZO_L;
				tipoPezzo = CostantiTetris.TIPO_L;
				break;
			case 3:
				boolPezzo = CostantiTetris.PEZZO_O;
				tipoPezzo = CostantiTetris.TIPO_O;
				break;
			case 4:
				boolPezzo = CostantiTetris.PEZZO_S;
				tipoPezzo = CostantiTetris.TIPO_S;
				break;
			case 5:
				boolPezzo = CostantiTetris.PEZZO_Z;
				tipoPezzo = CostantiTetris.TIPO_Z;
				break;
			default:
				boolPezzo = CostantiTetris.PEZZO_T;
				tipoPezzo = CostantiTetris.TIPO_T;
				break;
		}
		Pezzo pezzo = new Pezzo(boolPezzo, tipoPezzo, 4, 0, lblDisplay, blocchiSolidi, scaledIcon);
		return pezzo;

    }

	void disegna(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo[i][j]){
					lblDisplay[i+y][j+x].setBackground(tipo);
					lblDisplay[i+y][j+x].setIcon(scaledIcon);
				}
			}
		}
	}

    void spostaPezzo(int spostaX, int spostaY){

		if(validaX(spostaX))
			x += spostaX;
		if(validaY(spostaY))
			y += spostaY;


		disegna();

    }

    public void ruotaPezzo() {
        boolean[][] matriceTemp = pezzo;

        int left, right;
        int quanto=3;

        boolean temp;
		boolean valido = true;

        if(tipo == CostantiTetris.TIPO_I)
            quanto=4;
        else if(tipo == CostantiTetris.TIPO_O)
            quanto=0;

        for (int i = 0; i < quanto; i++) {
            for (int j = i + 1; j < quanto; j++) {
                temp = matriceTemp[i][j];
                matriceTemp[i][j] = matriceTemp[j][i];
                matriceTemp[j][i] = temp;
            }
        }
        for (int i = 0; i < quanto; i++) {
            left = 0;
            right = quanto - 1;
            while (left < right) {
                // Swap elements (i, left) with (i, right) for row reversal
                temp = matriceTemp[i][left];
                matriceTemp[i][left] = matriceTemp[i][right];
                matriceTemp[i][right] = temp;
                left++;
                right--;
            }
        }


		//controllo blocchi fissi //TODO: fixa
		/*for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(matriceTemp[i][j] && blocchiSolidi[y+i][x+j].occupato){
					valido = false;
				}
			}
		}

		if(valido){

		}*/
		pezzo = matriceTemp;
		disegna();

    }

    void solidificaPezzo(){ //TODO: sistema robe che fanno cagare tipo boolean perso
		boolean perso=false;
		boolean riga=true;

		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo[i][j]){
					blocchiSolidi[y+i][x+j].occupato = pezzo[i][j];
					blocchiSolidi[y+i][x+j].colore = tipo;
					lblDisplay[y+i][x+j].setBackground(tipo);
					lblDisplay[i+y][j+x].setIcon(scaledIcon);//TODO: potresti sostituire scaledIcon mettere "animazione"
					if(y+i == 0)
						perso = true;
				}
			}
		}

		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			riga=true;
			for(int j=0; j<CostantiTetris.WIDTH&&riga; j++){
				if(!blocchiSolidi[i][j].occupato)
					riga = false;
			}
			if(riga)
				cancellaRiga(i);
		}
		if(perso){
			System.out.println("Hai perso");//TODO: aggiungi dialog
			//pezzo = ; //TODO:distruggi il pezzo
		}else{
            Pezzo nuovoPezzo = random(); //TODO: crealo solo dopo che le righe vanno in basso
			x = 4; 
            y = 0;
            tipo = nuovoPezzo.tipo;
            this.pezzo = nuovoPezzo.pezzo;
		}
	}

    private boolean validaX(int spostaX){
		boolean valido=true;

		//collisioni bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(pezzo[i][j] && !(x+spostaX+j>=0 && x+spostaX+j<CostantiTetris.WIDTH)){
					valido = false;
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(pezzo[i][j] && validaIntervallo(x+spostaX+j, y+i) && blocchiSolidi[y+i][x+spostaX+j].occupato){
					valido = false;
				}
			}
		}

		return valido;
	}

	private boolean validaY(int spostaY){
		boolean valido=true;

		//collisioni bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(pezzo[i][j] && !(y+spostaY+i<CostantiTetris.HEIGHT)){
					valido = false;
					solidificaPezzo(); //temporaneo
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(pezzo[i][j] && validaIntervallo(x+j, y+spostaY+i) && blocchiSolidi[y+spostaY+i][x+j].occupato ){
					valido = false;
					solidificaPezzo(); //temporaneo
				}
			}
		}

		return valido;
	}

	private boolean validaIntervallo(int x, int y) {
		return y >= 0 && y < CostantiTetris.HEIGHT && x >= 0 && x < CostantiTetris.WIDTH;
	}

    private void cancellaRiga(int riga){ //TODO: sposta in una classe apposita per blocchiSolidi
		for(int i=0; i<CostantiTetris.WIDTH; i++){
			lblDisplay[riga][i].setBackground(Color.BLACK);
			lblDisplay[riga][i].setIcon(null);
		}

		for(int i=riga-1; i>0; i--){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
				blocchiSolidi[i+1][j].colore = blocchiSolidi[i][j].colore;
				blocchiSolidi[i+1][j].occupato = blocchiSolidi[i][j].occupato;
			}
		}
	}
}


