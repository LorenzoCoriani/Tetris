import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tetris{
	final int width = 10;
	final int height = 20;

	JFrame frame;
	JPanel pnlGioco;

	boolean boolPezzo[][] = {{true, true,  false, false},
							 {true, true,  false, false},
							 {false, false, false,  false},
							 {false, false, false,  false}};
	Pezzo pezzo = new Pezzo(boolPezzo,0,0 );

	JLabel[][] lblBlocchi;
	Casella[][] lblTabella;

	Timer timer;
	int pausa=500; //millisecondi

	Tetris(){
		frame = new JFrame();
		timer = new Timer(pausa, new PassaTempo());

		pnlGioco = new JPanel(new GridLayout(height,width));
		lblBlocchi = new JLabel[height][width];
		lblTabella = new Casella[height][width];

		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblTabella[i][j] = new Casella("c", false);
				lblTabella[i][j].lbl.setPreferredSize(new Dimension(70,70));
				lblTabella[i][j].lbl.setVerticalAlignment(SwingConstants.NORTH);
				lblTabella[i][j].lbl.setOpaque(true);
				lblTabella[i][j].lbl.setBackground(Color.BLACK);
				lblBlocchi[i][j] = lblTabella[i][j].lbl;
				pnlGioco.add(lblBlocchi[i][j]);
			}
		}

		//add
		frame.add(pnlGioco,"Center");

		//eventi
		frame.addKeyListener(new TastoPremuto());

		//timer
		timer.start();

		//impostazioni frame
		frame.setSize(800, 1000);
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
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					System.out.println("Su");
					//mette pezzo direttamente;
				break;
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
					pezzo.ruotaPezzo();
				break;
			}
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}



	}

	void spostaPezzo(int spostaX, int spostaY){
        int i, j;
        int posX, posY;
		if(pezzo.x+spostaX>=0 && pezzo.x+spostaX<(width-1))
			pezzo.x += spostaX;
		if(pezzo.y+spostaY>=0 && pezzo.y+spostaY<(height-1))
			pezzo.y += spostaY;

        posX = pezzo.x;
        posY = pezzo.y;

        for(i = 0; i < 4; i++){
            for(j = 0; j < 4; j++){
                if(pezzo.pezzo[i][j]){
                    lblBlocchi[i+posY][j+posX].setBackground(Color.YELLOW);
                }
            }
        }

    }

    void cancellaBlocchi(){
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblBlocchi[i][j].setBackground(Color.BLACK);
			}
		}
    }

	void disegnaBlocchi(){
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblBlocchi[i][j] = lblTabella[i][j].lbl;
			}
		}
    }

	public static void main(String[] args){
		new Tetris();
	}
}
