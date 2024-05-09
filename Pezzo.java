/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per ill 15/5/24
Testo: Tetris
*/
import java.awt.*;
import javax.swing.*;
import java.util.*;

class Pezzo{

    private Color tipo;
    private boolean blocchi[][];

    private JLabel[][] lblDisplay;
    private Casella[][] blocchiSolidi;
    private ImageIcon immagine;

    private int x,y;
	boolean scortaDisponibile; //temporaneo, mettere set/get


    Pezzo(boolean blocchi[][], Color tipo, int x, int y, JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon immagine){
        this.blocchi = blocchi;
        this.tipo = tipo;
        this.x = x;
        this.y = y;

        this.lblDisplay = lblDisplay;
        this.blocchiSolidi = blocchiSolidi;
        this.immagine = immagine;

		scortaDisponibile=true;
    }

    Pezzo(JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon immagine){
        this.lblDisplay = lblDisplay;
        this.blocchiSolidi = blocchiSolidi;
        this.immagine = immagine;

		scortaDisponibile=true;
    }

    //metodi


    Pezzo random(){ //TODO: implementare randomizzazione 7-bag
        boolean boolPezzo[][];
        Color tipoPezzo;

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
		Pezzo pezzo = new Pezzo(boolPezzo, tipoPezzo, CostantiTetris.INITIAL_X, CostantiTetris.INITIAL_Y, lblDisplay, blocchiSolidi, immagine);
		return pezzo;

    }

	boolean disegna(){ //restituisce falso se si non può disegnare
		boolean disegnabile=true;

		for(int i=0; disegnabile&& i<CostantiTetris.WIDTH; i++){
			if(blocchiSolidi[0][i].occupato){
				disegnabile = false;
			}
		}
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(blocchi[i][j]){
					lblDisplay[i+y][j+x].setBackground(tipo);
					lblDisplay[i+y][j+x].setIcon(immagine);
				}
			}
		}

		return disegnabile;
	}

    boolean spostaPezzo(int spostaX, int spostaY){//restituisce se lo spostamento causa la solidifcazione
		boolean solidifica=false;

		if(validaX(spostaX))
			x += spostaX;
		if(validaY(spostaY)){
			y += spostaY;
		}else{
			solidifica = true;
		}

		return solidifica;
    }

	boolean cadutaIstantanea(){//restituisce se lo spostamento causa la solidificazione
		int spostaY=1; //spostamento

		while(validaY(spostaY)){
			y += spostaY;
		}

		return spostaPezzo(0, spostaY);
	}

    public void ruotaPezzo() {//in senso orario //TODO: aggiungere metodo rotazione antioraria, forse implementare Super Rotation System
        boolean[][] matriceTemp = new boolean[4][4];

		//copio blocchi in matriceTemp
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				matriceTemp[i][j] = blocchi[i][j];
			}
		}


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

		//controllo bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(matriceTemp[i][j] && !validaIntervallo(x+j,y+i)){
					valido = false;
				}
			}
		}

		//controllo blocchi fissi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(matriceTemp[i][j] && blocchiSolidi[y+i][x+j].occupato){
					valido = false;
				}
			}
		}

		if(valido){
			//applico rotazione, copio matriceTemp in blocchi
			for(int i=0; i<4; i++){
				for(int j=0; j<4; j++){
					blocchi[i][j] = matriceTemp[i][j];
				}
			}
		}

    }

    int solidificaPezzo(){//restituisce se una riga è cancellata //TODO: sposta in una classe apposita per blocchiSolidi
		boolean gameOver=false;
		boolean riga=true; //riga trovata

		int righe=0;//cancellate

		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(blocchi[i][j]){
					blocchiSolidi[y+i][x+j].occupato = blocchi[i][j];
					blocchiSolidi[y+i][x+j].colore = tipo;
					lblDisplay[y+i][x+j].setBackground(tipo);
					lblDisplay[i+y][j+x].setIcon(immagine);//TODO: potresti sostituire immagine mettere "animazione"
					if(y+i == 0)
						gameOver = true;
				}
			}
		}

		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			riga=true;
			for(int j=0; j<CostantiTetris.WIDTH&&riga; j++){
				if(!blocchiSolidi[i][j].occupato)
					riga = false;
			}
			if(riga){
				cancellaRiga(i);
				righe++;
			}

		}
		if(!gameOver){
            Pezzo nuovoPezzo = random(); //TODO: provvisorio crealo solo dopo che le righe vanno in basso
			x = CostantiTetris.INITIAL_X;
			y = CostantiTetris.INITIAL_Y;
			tipo = nuovoPezzo.tipo;
			this.blocchi = nuovoPezzo.blocchi;
		}

		scortaDisponibile=true;
		return righe;
	}

    private boolean validaX(int spostaX){
		boolean valido=true;

		//collisioni bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(blocchi[i][j] && !(x+spostaX+j>=0 && x+spostaX+j<CostantiTetris.WIDTH)){
					valido = false;
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(blocchi[i][j] && validaIntervallo(x+spostaX+j, y+i) && blocchiSolidi[y+i][x+spostaX+j].occupato){
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
				if(blocchi[i][j] && !(y+spostaY+i<CostantiTetris.HEIGHT)){
					valido = false;
					//solidificaPezzo(); //temporaneo
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(blocchi[i][j] && validaIntervallo(x+j, y+spostaY+i) && blocchiSolidi[y+spostaY+i][x+j].occupato ){
					valido = false;
					//solidificaPezzo(); //temporaneo
				}
			}
		}

		return valido;
	}

	private boolean validaIntervallo(int x, int y){
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
	//set
	void setX(int x){
		this.x = x;
	}
	void setY(int y){
		this.y = y;
	}

	void occupaScorta(){
		scortaDisponibile = false;
	}
	void liberaScorta(){
		scortaDisponibile = true;
	}

	boolean isScortaDisponibile(){
		return scortaDisponibile;
	}

	//get
	Color getTipo(){
		return tipo;
	}

	boolean getBlocco(int i, int j){
		return blocchi[i][j];
	}
}


