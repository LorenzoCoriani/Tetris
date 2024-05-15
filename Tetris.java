/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per il 15/5/24
Testo: Tetris
*/
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

import javax.sound.sampled.*;


class Tetris{//https://tetris.wiki/Tetris_Guideline
	final int frameRatio = 40;
	int pausa=500; //millisecondi

	JFrame frame;
	JPanel pnlGioco;
	//
	JPanel pnlScorta; //pnlPezzoScorta + JLabel "HOLD"
	JPanel pnlPezzoScorta; //per pezzo di riserva
	//
	JPanel pnlProssimi;

	JDialog dlgGameOver;

	JTextField txtGiocatore;
	JButton btnInvia;

	boolean gameOver=false;
	int righe=0;

	JLabel[][] lblDisplay;
	JLabel[][] lblScorta;
	ArrayList<JLabel[][]> lblProssimi = new ArrayList<>();

	JLabel lblHold;//scritta HOLD
	JLabel lblNext;//scritta NEXT

	Font font = new Font("Arial", Font.PLAIN,48);
	Border bordo = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);

	Casella[][] blocchiSolidi;

	ImageIcon icon = new ImageIcon("./tile.png");

	Image image = icon.getImage().getScaledInstance(frameRatio, frameRatio, Image.SCALE_SMOOTH);
	ImageIcon scaledIcon = new ImageIcon(image);

	Timer timer;
	GestoreAudio audio = new GestoreAudio();

	GeneratorePezzo generaPezzo;

	Pezzo pezzo;
	Pezzo scorta;
	LinkedList<Pezzo> prossimi = new LinkedList<>(); //per adesso

	Tetris(){
		audio.suona("./tetris_music.wav", true);
		frame = new JFrame();
		frame.getContentPane().setBackground(CostantiTetris.BACKGROUND_COLOR);
		frame.setLayout(new GridBagLayout());

		txtGiocatore = new JTextField();

		txtGiocatore.setHorizontalAlignment(SwingConstants.CENTER);
		txtGiocatore.setFont(font);
		txtGiocatore.setForeground(Color.WHITE);
		txtGiocatore.setBackground(Color.BLACK);

		btnInvia = new JButton("INVIA");
		btnInvia.setFont(font);
		btnInvia.setForeground(Color.WHITE);
		btnInvia.setBackground(Color.BLACK);

		timer = new Timer(pausa, new PassaTempo());

		pnlGioco = new JPanel(new GridLayout(CostantiTetris.HEIGHT,CostantiTetris.WIDTH));
		pnlGioco.setPreferredSize(new Dimension(frameRatio*CostantiTetris.WIDTH, frameRatio*CostantiTetris.HEIGHT));
		//
		pnlScorta = new JPanel(new GridLayout(2,1));
		pnlScorta.setOpaque(false);
		pnlPezzoScorta = new JPanel(new GridLayout(4,4));
		pnlPezzoScorta.setBorder(bordo);
		//
		pnlProssimi = new JPanel(new GridLayout(CostantiTetris.N_NEXT+1, 1));//+1 per JLabel "NEXT"
		pnlProssimi.setOpaque(false);

		lblDisplay = new JLabel[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];
		lblScorta = new JLabel[4][4];

		lblHold = new JLabel("HOLD");
		lblNext = new JLabel("NEXT");
		//
		lblHold.setForeground(Color.WHITE);
		lblNext.setForeground(Color.WHITE);
		//
		lblHold.setOpaque(false);
		lblNext.setOpaque(false);
		//
		lblHold.setHorizontalAlignment(SwingConstants.CENTER);
		lblNext.setHorizontalAlignment(SwingConstants.CENTER);
		//
		lblHold.setVerticalAlignment(SwingConstants.NORTH);
		lblNext.setVerticalAlignment(SwingConstants.NORTH);
		//
		lblHold.setFont(font);
		lblNext.setFont(font);


		blocchiSolidi = new Casella[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];

		generaPezzo = new GeneratorePezzo(lblDisplay, blocchiSolidi, scaledIcon);
		pezzo = generaPezzo.random();


		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
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

		pnlPezzoScorta.setPreferredSize(new Dimension(frameRatio*4, frameRatio*4));

		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				lblScorta[i][j] = new JLabel();
				lblScorta[i][j].setOpaque(true);
				lblScorta[i][j].setPreferredSize(new Dimension(70,70));
				lblScorta[i][j].setBackground(Color.BLACK);
				pnlPezzoScorta.add(lblScorta[i][j]);
			}
		}


		for(int i=0; i<CostantiTetris.N_NEXT; i++){
			JPanel pnlProssimo = new JPanel(new GridLayout(4,4));
			pnlProssimo.setPreferredSize(new Dimension(frameRatio*4, frameRatio*4));
			pnlProssimo.setBorder(bordo);

			prossimi.add(generaPezzo.random());
			pnlProssimi.add(pnlProssimo);
			lblProssimi.add(new JLabel[4][4]);
			for(int j=0; j<4; j++){
				for(int k=0; k<4; k++){
					lblProssimi.get(i)[j][k] = new JLabel();
					lblProssimi.get(i)[j][k].setOpaque(true);
					lblProssimi.get(i)[j][k].setPreferredSize(new Dimension(70,70));

					if(prossimi.get(i).getBlocco(j,k)){
						lblProssimi.get(i)[j][k].setBackground(prossimi.get(i).getTipo());
						lblProssimi.get(i)[j][k].setIcon(scaledIcon);
					}else{
						lblProssimi.get(i)[j][k].setBackground(Color.BLACK);
						lblProssimi.get(i)[j][k].setIcon(null);
					}
					pnlProssimo.add(lblProssimi.get(i)[j][k]);
				}
			}
		}

		//constraints
		GridBagConstraints constrPnlGioco = new GridBagConstraints();
        constrPnlGioco.gridx = 1;
        constrPnlGioco.gridy = 0;
        constrPnlGioco.anchor = GridBagConstraints.CENTER;

        GridBagConstraints constrPnlScorta = new GridBagConstraints();
        constrPnlScorta.gridx = 0;
        constrPnlScorta.gridy = 0;
        constrPnlScorta.anchor = GridBagConstraints.NORTHWEST;

        GridBagConstraints constrPnlProssimo = new GridBagConstraints();
        constrPnlProssimo.gridx = 2;
        constrPnlProssimo.gridy = 0;
        constrPnlProssimo.anchor = GridBagConstraints.NORTHEAST;
		//add

		pnlScorta.add(pnlPezzoScorta);
		pnlScorta.add(lblHold);

		pnlProssimi.add(lblNext);

		frame.add(pnlScorta, constrPnlScorta);
		frame.add(pnlGioco, constrPnlGioco);
		frame.add(pnlProssimi, constrPnlProssimo);

		//eventi
		frame.addKeyListener(new TastoPremuto());
		btnInvia.addActionListener(new ClickInvia());
		//timer
		timer.start();

		//impostazioni frame
		frame.setSize(1000, 900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void gameOver(){
		JPanel pnlInvia = new JPanel(new GridLayout(2,1));
		JLabel lblGameOver = new JLabel("<html>Game Over <br> Righe: "+righe+"<br><br>nickname:</html>\n");

		lblGameOver.setFont(font);
		lblGameOver.setForeground(Color.WHITE);
		lblGameOver.setOpaque(true);
		lblGameOver.setBackground(Color.BLACK);
		lblGameOver.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameOver.setVerticalAlignment(SwingConstants.BOTTOM);

		dlgGameOver = new JDialog(frame);
		dlgGameOver.setTitle("GAME OVER");
		dlgGameOver.setSize(400,500);
		dlgGameOver.setLocationRelativeTo(frame);
		Point pos = dlgGameOver.getLocation();
		dlgGameOver.setUndecorated(true);
		dlgGameOver.getRootPane().setBorder(bordo);
		dlgGameOver.setLocation(pos.x, pos.y/2);
		dlgGameOver.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		//add
		pnlInvia.add(lblGameOver);
		pnlInvia.add(txtGiocatore);
		pnlInvia.add(btnInvia);

		dlgGameOver.add(lblGameOver, "Center");
		dlgGameOver.add(pnlInvia, "South");

		audio.stop();

		dlgGameOver.setVisible(true);
	}

	private void aggiungiInLeaderboard(){//imprecisazione mette gli uguali sopra invece che sotto
        int i=0;

		try{
			FileReader fR = new FileReader("./leaderboard.txt");
			BufferedReader reader = new BufferedReader(fR);

			Vector<String> testo = new Vector<>();
			String riga;
			String[] parti;
			riga = reader.readLine();
			while(riga != null){
				testo.add(riga);
				parti = riga.split(" - ");
				if(parti.length == 2 && i < Integer.parseInt(parti[1])){
					i++;
				}
				riga = reader.readLine();
			}
			reader.close();

			FileWriter fW = new FileWriter("./leaderboard.txt");
			PrintWriter writer = new PrintWriter(fW);

			testo.add(i, txtGiocatore.getText().trim()+" - "+righe);

			for(String linea : testo){
				writer.println(linea);
			}
			writer.close();

		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private class GestoreAudio{
		private Clip clip;

		void suona(String nomeFile, boolean loop){
			try{
				File file = new File(nomeFile);
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
				clip = AudioSystem.getClip();

				if(loop){
					clip.loop(Clip.LOOP_CONTINUOUSLY);
				}else{
					clip.start();
				}

				clip.open(audioStream);
				clip.start();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		void stop(){
			clip.stop();
		}
    }

	private class PassaTempo implements ActionListener{

		public void actionPerformed(ActionEvent e){
			if(gameOver){
				timer.stop();
				gameOver();
			}else{
				cancellaBlocchi();
				disegnaBlocchi();
				if(pezzo.spostaPezzo(0, 1)){ //se si è solidificato
					righe += pezzo.solidificaPezzo();// se una riga è cancellata
					nuovoPezzo();
				}
				if(!pezzo.disegna()){//se non è disegnabile
					gameOver = true;
				}
			}
		}
	}

	private class ClickInvia implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(!txtGiocatore.getText().trim().isEmpty()){
				btnInvia.setEnabled(false);
				txtGiocatore.setEditable(false);
				aggiungiInLeaderboard();
			}else{
				txtGiocatore.setText("");
			}
		}
	}

	private class TastoPremuto implements KeyListener{
		public void keyPressed(KeyEvent e){
			int key = e.getExtendedKeyCode();

			boolean solidificato = false;
			cancellaBlocchi();
			disegnaBlocchi();

			switch(key){
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					pezzo.ruotaPezzo(1);
				break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_A:
					solidificato = pezzo.spostaPezzo(-1, 0);
				break;
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_D:
					solidificato = pezzo.spostaPezzo(1, 0);
				break;
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_S:
					solidificato = pezzo.spostaPezzo(0, 1);
				break;
				case KeyEvent.VK_Z:
				case KeyEvent.VK_CONTROL:
					pezzo.ruotaPezzo(-1);
				break;
				case KeyEvent.VK_SPACE:
					solidificato = pezzo.cadutaIstantanea();
				break;
				case KeyEvent.VK_SHIFT:
				case KeyEvent.VK_C:
					scortaPezzo();
				break;
			}
			gameOver = !(pezzo.disegna());

			if(solidificato){
				righe += pezzo.solidificaPezzo();
				nuovoPezzo();
			}

			if(gameOver){
				timer.stop();
				gameOver();
			}

		}
		public void keyReleased(KeyEvent e){}
		public void keyTyped(KeyEvent e){}

	}

	private void cancellaBlocchi(){
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
				lblDisplay[i][j].setBackground(Color.BLACK);
				lblDisplay[i][j].setIcon(null);
			}
		}
    }

	private void disegnaBlocchi(){
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
				lblDisplay[i][j].setBackground(blocchiSolidi[i][j].colore);
				if(blocchiSolidi[i][j].colore != Color.BLACK)
					lblDisplay[i][j].setIcon(scaledIcon);
			}
		}
    }

    private void aggiornaScorta(){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){

				if(scorta.getBlocco(i,j)){
					lblScorta[i][j].setBackground(scorta.getTipo());
					lblScorta[i][j].setIcon(scaledIcon);
				}else{
					lblScorta[i][j].setBackground(Color.BLACK);
					lblScorta[i][j].setIcon(null);
				}
			}
		}
    }
    private void aggiornaProssimi(){

		for(int i=0; i<CostantiTetris.N_NEXT; i++){
			for(int j=0; j<4; j++){
				for(int k=0; k<4; k++){
					if(prossimi.get(i).getBlocco(j,k)){
						lblProssimi.get(i)[j][k].setBackground(prossimi.get(i).getTipo());
						lblProssimi.get(i)[j][k].setIcon(scaledIcon);
					}else{
						lblProssimi.get(i)[j][k].setBackground(Color.BLACK);
						lblProssimi.get(i)[j][k].setIcon(null);
					}
				}
			}
		}
    }

	private void nuovoPezzo(){
		if(!gameOver){
			pezzo = prossimi.removeFirst();
			prossimi.addLast(generaPezzo.random());

			aggiornaProssimi();
		}
	}

	private void scortaPezzo(){
		if(pezzo.isScortaDisponibile()){
			if(scorta != null){
				Pezzo tmp = scorta;
				scorta = pezzo;
				pezzo = tmp;

				pezzo.setX(CostantiTetris.INITIAL_X);
				pezzo.setY(CostantiTetris.INITIAL_Y);
			}else{
				scorta = pezzo;
				nuovoPezzo();
			}
			pezzo.occupaScorta();
			aggiornaScorta();
		}
	}

	public static void main(String[] args){
		new Tetris();
	}
}
