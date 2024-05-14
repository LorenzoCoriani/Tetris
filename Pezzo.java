/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per ill 15/5/24
Testo: Tetris
*/
import java.awt.*;
import javax.swing.*;

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

    public void ruotaPezzo(int verso) { // verso: 1 orario, -1 antiorario
		boolean[][] matriceTemp = new boolean[4][4];

		//copia blocchi in matriceTemp
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				matriceTemp[i][j] = blocchi[i][j];
			}
		}
		int quanto = 3;

		if (tipo == CostantiTetris.TIPO_I)
			quanto = 4;
		else if (tipo == CostantiTetris.TIPO_O)
			quanto = 0;

		if(verso == 1) { // ruota in senso orario
			// Transpose the matrix
			for (int i = 0; i < quanto; i++) {
				for (int j = i + 1; j < quanto; j++) {
					boolean temp = matriceTemp[i][j];
					matriceTemp[i][j] = matriceTemp[j][i];
					matriceTemp[j][i] = temp;
				}
			}
			// Reverse each row
			for (int i = 0; i < quanto; i++) {
				int left = 0;
				int right = quanto - 1;
				while (left < right) {
					boolean temp = matriceTemp[i][left];
					matriceTemp[i][left] = matriceTemp[i][right];
					matriceTemp[i][right] = temp;
					left++;
					right--;
				}
			}
		}else if(verso == -1) { // ruota in senso antiorario
			// Transpose the matrix
			for (int i = 0; i < quanto; i++) {
				for (int j = i + 1; j < quanto; j++) {
					boolean temp = matriceTemp[i][j];
					matriceTemp[i][j] = matriceTemp[j][i];
					matriceTemp[j][i] = temp;
				}
			}
			// Reverse each column
			for (int i = 0; i < quanto; i++) {
				int top = 0;
				int bottom = quanto - 1;
				while (top < bottom) {
					boolean temp = matriceTemp[top][i];
					matriceTemp[top][i] = matriceTemp[bottom][i];
					matriceTemp[bottom][i] = temp;
					top++;
					bottom--;
				}
			}
		}

		//controllo bordi
		boolean valido = true;
		for (int i = 0; i < 4 && valido; i++) {
			for (int j = 0; j < 4 && valido; j++) {
				if (matriceTemp[i][j] && !validaIntervallo(x + j, y + i)) {
					valido = false;
				}
			}
		}

		//controllo blocchiSolidi
		for (int i = 0; i < 4 && valido; i++) {
			for (int j = 0; j < 4 && valido; j++) {
				if (matriceTemp[i][j] && blocchiSolidi[y + i][x + j].occupato) {
					valido = false;
				}
			}
		}

		if(valido){//applica rotazione
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
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


