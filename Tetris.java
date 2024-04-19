import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tetris{
	JFrame frame;
	JPanel pnlGioco;
	JLabel[][] lblBlocchi;
	
	Tetris(){
		frame = new JFrame();
		pnlGioco = new JPanel(new GridLayout(20,10));
		lblBlocchi = new JLabel[20][10];
		
		for(int i=0; i<20; i++){
			for(int j=0; j<10; j++){
				lblBlocchi[i][j] = new JLabel("c");
				lblBlocchi[i][j].setPreferredSize(new Dimension(70,70));
				lblBlocchi[i][j].setVerticalAlignment(SwingConstants.NORTH);
				lblBlocchi[i][j].setOpaque(true);
				lblBlocchi[i][j].setBackground(Color.BLACK);
				pnlGioco.add(lblBlocchi[i][j]);
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
				
				break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					System.out.println("Sinistra");
				
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					System.out.println("Destra");
				
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					System.out.println("Sotto");
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					System.out.println("Sotto");
				break;
			}
		}
		
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}
	}
	
	public static void main(String[] args){
		new Tetris();
	}
}