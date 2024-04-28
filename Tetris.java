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

	boolean boolPezzo[][] = {{true, true,  false, false},
							 {true, true,  false, false},
							 {false, false, false, false},
							 {false, false, false, false}};
	Pezzo pezzo = new Pezzo(boolPezzo, Color.YELLOW);

	JLabel[][] lblDisplay;
	Casella[][] blocchiSolidi;

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
				lblDisplay[i][j].setBackground(Color.BLACK);

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

	void ruotaPezzo(){
        int size = pezzo.pezzo.length;
        boolean[][] roteaPezzo = new boolean[size][size];
        //
        for(int i = 0; i<size; i++){
            for(int j = 0;j<size;j++){
                //calcolo dalla posizione della cella ruotata
                roteaPezzo[j][size-1-i] = pezzo.pezzo[i][j];
            }
        }
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

	boolean validaX(int spostaX){ //TODO: fixa collisioni bordi (applicalo a tutti i blocchi del pezzo)
		boolean valido=false;

		//collisioni bordi
		if(pezzo.x+spostaX>=0 && pezzo.x+spostaX<(width-1)){
			valido = true;
		}

		//collisioni blocchi
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo.pezzo[i][j] && validaIntervallo(pezzo.x+spostaX+j, pezzo.y+i) && blocchiSolidi[pezzo.y+i][pezzo.x+spostaX+j].occupato){
					valido = false;
				}
			}
		}

		return valido;
	}

	boolean validaY(int spostaY){ //TODO: fixa collisioni bordi (applicalo a tutti i blocchi del pezzo)
		boolean valido=false;

		//collisioni bordi
		if(pezzo.y+spostaY>=0 && pezzo.y+spostaY<(height-1)){
			valido = true;
		}

		//collisioni blocchi
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo.pezzo[i][j] && validaIntervallo(pezzo.x+j, pezzo.y+spostaY+i) && blocchiSolidi[pezzo.y+spostaY+i][pezzo.x+j].occupato ){
					valido = false;
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
				lblDisplay[i][j].setBackground(Color.BLACK);
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
