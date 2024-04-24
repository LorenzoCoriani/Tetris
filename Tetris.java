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
	Pezzo pezzo = new Pezzo(boolPezzo,6,4 );

	Casella[][] lblBlocchi;
	
	Tetris(){
		frame = new JFrame();
		pnlGioco = new JPanel(new GridLayout(height,width));
		lblBlocchi = new Casella[height][width];
		
		for(int i=0; i<height; i++){
			for(int j=0; j<width; j++){
				lblBlocchi[i][j] = new Casella("c", false);
				lblBlocchi[i][j].lbl.setPreferredSize(new Dimension(70,70));
				lblBlocchi[i][j].lbl.setVerticalAlignment(SwingConstants.NORTH);
				lblBlocchi[i][j].lbl.setOpaque(true);
				lblBlocchi[i][j].lbl.setBackground(Color.BLACK);
				pnlGioco.add(lblBlocchi[i][j].lbl);
			}
		}
		
		//add
		frame.add(pnlGioco,"Center");
		
		//eventi
		frame.addKeyListener(new TastoPremuto());
		
		
		//impostazioni frame
		frame.setSize(800, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	class TastoPremuto implements KeyListener{
		public void keyPressed(KeyEvent e){
			int key = e.getExtendedKeyCode();
			switch(key){
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					System.out.println("Su");
					//mette pezzo direttamente;
				break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					System.out.println("Sinistra");
					pezzo.x--;
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					System.out.println("Destra");
					pezzo.x++;
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					System.out.println("Sotto");
					pezzo.y++;
				break;
				case KeyEvent.VK_SPACE:
					System.out.println("Ruota");
					pezzo.ruotaPezzo();
				break;
			}
			disegnaPezzo(pezzo);
		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}



	}
	
	void disegnaPezzo(Pezzo pezzo){
		int i, j;
		int x, y;
		x=pezzo.y;
		y=pezzo.x;


		for(i=0; i<4; i++){
			for(j=0; j<4; j++){
				if(pezzo.pezzo[i][j]){
					lblBlocchi[i+x][j+y].lbl.setBackground(Color.YELLOW);//pezzo.tipo);
					//lblBlocchi[i+y][j+x].lbl.setBackground(Color.BLACK);
				}
				System.out.println(i+", "+j);
			}
		}
	}

	public static void main(String[] args){
		new Tetris();
	}
}
