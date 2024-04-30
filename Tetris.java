import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tetris{ //TODO: usare definizioni da https://tetris.wiki/Tetris_Guideline
	final int width = 10;
	final int height = 20;
	final int frameRatio = 40;
	int pausa=500; //millisecondi

	JFrame frame;
	JPanel pnlGioco;

	boolean boolPezzo[][] = {{false, false, false, true},
							 {false, false, true, true},
							 {false, false, false, true},
							 {false, false, false, false}};
	Pezzo pezzo = Pezzo.random();

	JLabel[][] lblDisplay;
	Casella[][] blocchiSolidi;

	//TODO: mettere uno sfondo

	ImageIcon icon = new ImageIcon("./tile.png");

	Image image = icon.getImage().getScaledInstance(frameRatio, frameRatio, Image.SCALE_SMOOTH); //TODO: fine tune this shit
	ImageIcon scaledIcon = new ImageIcon(image);

	Timer timer;

	Tetris(){
		frame = new JFrame();
		timer = new Timer(pausa, new PassaTempo());

		pnlGioco = new JPanel(new GridLayout(height,width));
		lblDisplay = new JLabel[height][width];
		blocchiSolidi = new Casella[height][width];

		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				blocchiSolidi[i][j] = new Casella();
				lblDisplay[i][j] = new JLabel();

				lblDisplay[i][j].setPreferredSize(new Dimension(70,70));
				lblDisplay[i][j].setVerticalAlignment(SwingConstants.NORTH);
				//
				lblDisplay[i][j].setOpaque(true);
				lblDisplay[i][j].setBackground(Color.BLACK);//TODO: non impostare niente, useremo uno sfondo

				pnlGioco.add(lblDisplay[i][j]);
			}
		}

		//add
		frame.add(pnlGioco,"Center");

		//eventi
		frame.addKeyListener(new TastoPremuto());

		//timer
		timer.start();

		//impostazioni frame
		frame.setSize(frameRatio*width, frameRatio*height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	class PassaTempo implements ActionListener{

		public void actionPerformed(ActionEvent e){
			cancellaBlocchi();
			disegnaBlocchi();

			spostaPezzo(0, 1);
		}
	}

	class TastoPremuto implements KeyListener{
		public void keyPressed(KeyEvent e){
			int key = e.getExtendedKeyCode();

			cancellaBlocchi();
			disegnaBlocchi();

			switch(key){
				/*case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					System.out.println("Su");
					//mette pezzo direttamente;
				break;*/
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					System.out.println("Sinistra");
					spostaPezzo(-1, 0);
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					System.out.println("Destra");
					spostaPezzo(1, 0);
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					System.out.println("Sotto");
					spostaPezzo(0, 1);
				break;
				case KeyEvent.VK_SPACE:
					System.out.println("Ruota");
					ruotaPezzo();
				break;
				default:
					pezzo.disegna(lblDisplay, scaledIcon);
			}
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}

	}
	//TODO: metti questi metodi nel Pezzo

	public void ruotaPezzo() {

        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                boolean temp = pezzo.pezzo[i][j];
                pezzo.pezzo[i][j] = pezzo.pezzo[j][i];
                pezzo.pezzo[j][i] = temp;
            }
        }


        for (int i = 0; i < 4; i++) {
            int left = 0;
            int right = 4 - 1;
            while (left < right) {
                // Swap elements (i, left) with (i, right) for row reversal
                boolean temp = pezzo.pezzo[i][left];
                pezzo.pezzo[i][left] = pezzo.pezzo[i][right];
                pezzo.pezzo[i][right] = temp;
                left++;
                right--;
            }
        }

        pezzo.disegna(lblDisplay, scaledIcon);
    }

	void spostaPezzo(int spostaX, int spostaY){

		if(validaX(spostaX))
			pezzo.x += spostaX;
		if(validaY(spostaY))
			pezzo.y += spostaY;


		pezzo.disegna(lblDisplay, scaledIcon);

    }


	void solidificaPezzo(){ //TODO: sistema robe che fanno cagare tipo boolean perso
		boolean perso=false;
		boolean riga=true;

		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo.pezzo[i][j]){
					blocchiSolidi[pezzo.y+i][pezzo.x+j].occupato = pezzo.pezzo[i][j];
					blocchiSolidi[pezzo.y+i][pezzo.x+j].colore = pezzo.tipo;
					lblDisplay[pezzo.y+i][pezzo.x+j].setBackground(pezzo.tipo);
					lblDisplay[i+pezzo.y][j+pezzo.x].setIcon(scaledIcon);//TODO: potresti sostituire scaledIcon mettere "animazione"
					if(pezzo.y+i == 0)
						perso = true;
				}
			}
		}

		for(int i=0; i<height; i++){
			riga=true;
			for(int j=0; j<width&&riga; j++){
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
			pezzo = Pezzo.random(); //TODO: crealo solo dopo che le righe vanno in basso
		}
	}

	void cancellaRiga(int riga){
		for(int i=0; i<width; i++){
			lblDisplay[riga][i].setBackground(Color.BLACK);
			lblDisplay[riga][i].setIcon(null);
		}

		for(int i=riga-1; i>0; i--){
			for(int j=0; j<width; j++){
				blocchiSolidi[i+1][j].colore = blocchiSolidi[i][j].colore;
				blocchiSolidi[i+1][j].occupato = blocchiSolidi[i][j].occupato;
			}
		}
	}


	boolean validaX(int spostaX){
		boolean valido=true;

		//collisioni bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(pezzo.pezzo[i][j] && !(pezzo.x+spostaX+j>=0 && pezzo.x+spostaX+j<width)){
					valido = false;
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(pezzo.pezzo[i][j] && validaIntervallo(pezzo.x+spostaX+j, pezzo.y+i) && blocchiSolidi[pezzo.y+i][pezzo.x+spostaX+j].occupato){
					valido = false;
				}
			}
		}

		return valido;
	}

	boolean validaY(int spostaY){
		boolean valido=true;

		//collisioni bordi
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(pezzo.pezzo[i][j] && !(pezzo.y+spostaY+i<height)){
					valido = false;
					solidificaPezzo(); //temporaneo
				}
			}
		}

		//collisioni blocchi
		for(int i=0; i<4&&valido; i++){
			for(int j=0; j<4&&valido; j++){
				if(pezzo.pezzo[i][j] && validaIntervallo(pezzo.x+j, pezzo.y+spostaY+i) && blocchiSolidi[pezzo.y+spostaY+i][pezzo.x+j].occupato ){
					valido = false;
					solidificaPezzo(); //temporaneo
				}
			}
		}

		return valido;
	}

	boolean validaIntervallo(int x, int y) {
		return y >= 0 && y < height && x >= 0 && x < width;
	}




    void cancellaBlocchi(){
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblDisplay[i][j].setBackground(Color.BLACK); //TODO: mettilo trasparente
				lblDisplay[i][j].setIcon(null);
			}
		}
    }

	void disegnaBlocchi(){
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblDisplay[i][j].setBackground(blocchiSolidi[i][j].colore);
				if(blocchiSolidi[i][j].colore != Color.BLACK)
					lblDisplay[i][j].setIcon(scaledIcon);
			}
		}
    }

	public static void main(String[] args){
		new Tetris();
	}
}
