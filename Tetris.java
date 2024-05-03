import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tetris{ //TODO: usare definizioni da https://tetris.wiki/Tetris_Guideline
	final int frameRatio = 40;
	int pausa=500; //millisecondi

	JFrame frame;
	JPanel pnlGioco;
	JDialog dlgHaiPerso;

	JLabel[][] lblDisplay;
	Casella[][] blocchiSolidi;

	//TODO: mettere uno sfondo

	ImageIcon icon = new ImageIcon("./tile.png");

	Image image = icon.getImage().getScaledInstance(frameRatio, frameRatio, Image.SCALE_SMOOTH); //TODO: capire sta cosa
	ImageIcon scaledIcon = new ImageIcon(image);

	Timer timer;

	Pezzo pezzo;

	Tetris(){
		frame = new JFrame();
		timer = new Timer(pausa, new PassaTempo());

		pnlGioco = new JPanel(new GridLayout(CostantiTetris.HEIGHT,CostantiTetris.WIDTH));

		lblDisplay = new JLabel[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];
		blocchiSolidi = new Casella[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];
		
		pezzo = new Pezzo(lblDisplay, blocchiSolidi, scaledIcon);
		pezzo = pezzo.random();
		
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
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
		frame.setSize(frameRatio*CostantiTetris.WIDTH, frameRatio*CostantiTetris.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	void haiPerso(){
		JLabel lblHaiPerso = new JLabel("Hai Perso!");

		lblHaiPerso.setFont(new Font("Arial", Font.PLAIN,64));
		lblHaiPerso.setHorizontalAlignment(SwingConstants.CENTER);

		dlgHaiPerso = new JDialog(frame, true);
		dlgHaiPerso.setSize(400,300);
		dlgHaiPerso.setLocation(500,200); //TODO centra il frame
		dlgHaiPerso.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


		dlgHaiPerso.add(lblHaiPerso);

		dlgHaiPerso.setVisible(true);
	}

	class PassaTempo implements ActionListener{

		public void actionPerformed(ActionEvent e){
			cancellaBlocchi();
			disegnaBlocchi();

			pezzo.spostaPezzo(0, 1);
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
					pezzo.spostaPezzo(-1, 0);
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					pezzo.spostaPezzo(1, 0);
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					pezzo.spostaPezzo(0, 1);
				break;
				case KeyEvent.VK_SPACE:
					System.out.println("Ruota");
					pezzo.ruotaPezzo();
				break;
				default:
					pezzo.disegna();
			}
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}

	}

    void cancellaBlocchi(){
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
				lblDisplay[i][j].setBackground(Color.BLACK); //TODO: mettilo trasparente
				lblDisplay[i][j].setIcon(null);
			}
		}
    }

	void disegnaBlocchi(){
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
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
