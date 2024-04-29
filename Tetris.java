import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tetris{
	final int width = 10;
	final int height = 20;
	final int frameWidth = 400;
	final int frameHeight = 800;
	int pausa=500; //millisecondi

	JFrame frame;
	JPanel pnlGioco;

	boolean boolPezzo[][] = {{true, false,  false, false},
							 {true, true,  false, false},
							 {true, false, false, false},
							 {false, false, false, false}};
	Pezzo pezzo = Pezzo.random();//new Pezzo(boolPezzo, new Color(142, 68, 173));

	JLabel[][] lblDisplay;
	Casella[][] blocchiSolidi;

	//TODO: mettere uno sfondo

	ImageIcon icon = new ImageIcon("./tile.png");

	Image image = icon.getImage().getScaledInstance(frameHeight/height, frameWidth/width, Image.SCALE_SMOOTH); //TODO: fine tune this shit
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
		blocchiSolidi[19][0].colore = Color.RED;
		blocchiSolidi[19][0].occupato = true;

		blocchiSolidi[7][9].colore = Color.GREEN;
		blocchiSolidi[7][9].occupato = true;

		blocchiSolidi[12][3].colore = Color.BLUE;
		blocchiSolidi[12][3].occupato = true;

		//blocchiSolidi[10][6].colore = Color.YELLOW;
		//blocchiSolidi[10][6].occupato = true;

		//add
		frame.add(pnlGioco,"Center");

		//eventi
		frame.addKeyListener(new TastoPremuto());

		//timer
		timer.start();

		//impostazioni frame
		frame.setSize(frameWidth, frameHeight);
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
					disegnaPezzo();
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

        disegnaPezzo();
    }

	void spostaPezzo(int spostaX, int spostaY){

		if(validaX(spostaX))
			pezzo.x += spostaX;
		if(validaY(spostaY))
			pezzo.y += spostaY;


		disegnaPezzo();

    }
	void disegnaPezzo(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo.pezzo[i][j]){
					lblDisplay[i+pezzo.y][j+pezzo.x].setBackground(pezzo.tipo);
					lblDisplay[i+pezzo.y][j+pezzo.x].setIcon(scaledIcon);
				}
			}
		}
	}

	void solidificaPezzo(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo.pezzo[i][j]){
					blocchiSolidi[pezzo.y+i][pezzo.x+j].occupato = pezzo.pezzo[i][j];
					blocchiSolidi[pezzo.y+i][pezzo.x+j].colore = pezzo.tipo;
					lblDisplay[pezzo.y+i][pezzo.x+j].setBackground(pezzo.tipo);
					lblDisplay[i+pezzo.y][j+pezzo.x].setIcon(scaledIcon);//TODO: potresti sostituire scaledIcon mettere "animazione"
				}
			}
		}
		pezzo = Pezzo.random(); //TODO: genera nuovo pezzo casuale
	}

	boolean validaX(int spostaX){ //TODO: fixa per pezzo ruotato
		boolean valido=true;

		//collisioni bordi //programmato a cazzo
		for(int i=0; i<4 &&valido; i++){
			for(int j=0; j<4 &&valido; j++){
				if(pezzo.pezzo[i][j] && !(pezzo.x+spostaX>=0 && pezzo.x+spostaX+j<width)){
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

		//collisioni bordi //programmato a cazzo
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
