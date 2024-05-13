/*
Autore: Dario Nappi, Lorenzo Coriani
Classe: 4^F
Data: per ill 15/5/24
Testo: Tetris
*/
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.sound.sampled.*;


class Tetris{ //TODO: https://tetris.wiki/Tetris_Guideline 
	final int frameRatio = 40;
	int pausa=500; //millisecondi

	JFrame frame; //TODO: mettere al centro dello schermo
	JPanel pnlGioco;
	JPanel pnlScorta; //per pezzo di riserva
	JPanel pnlProssimi;

	JDialog dlgGameOver;

	boolean gameOver=false; //TODO: forse aggiungere punteggio su file e chiedere nome scoreboard.txt
	int righe=0; //TODO: mostra righe

	JLabel[][] lblDisplay;
	JLabel[][] lblScorta;
	ArrayList<JLabel[][]> lblProssimi = new ArrayList<>();

	Casella[][] blocchiSolidi;

	//TODO: mettere uno sfondo

	ImageIcon icon = new ImageIcon("./tile.png");

	Image image = icon.getImage().getScaledInstance(frameRatio, frameRatio, Image.SCALE_SMOOTH); //TODO: capire sta cosa
	ImageIcon scaledIcon = new ImageIcon(image);

	Timer timer;
	GestoreAudio audio = new GestoreAudio();

	Pezzo pezzo;
	Pezzo scorta;
	LinkedList<Pezzo> prossimi = new LinkedList<>(); //per adesso

	Tetris(){
		audio.suona("./tetris_music.wav", true);
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0,0,100)); //TODO: colore temporaneo?
		frame.setLayout(new GridBagLayout());

		timer = new Timer(pausa, new PassaTempo());

		pnlGioco = new JPanel(new GridLayout(CostantiTetris.HEIGHT,CostantiTetris.WIDTH));
		pnlScorta = new JPanel(new GridLayout(4,4));

		pnlProssimi = new JPanel(new GridLayout(CostantiTetris.N_NEXT, 1));

		lblDisplay = new JLabel[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];
		lblScorta = new JLabel[4][4];

		blocchiSolidi = new Casella[CostantiTetris.HEIGHT][CostantiTetris.WIDTH];
		
		pezzo = new Pezzo(lblDisplay, blocchiSolidi, scaledIcon);
		pezzo = pezzo.random();

		pnlGioco.setPreferredSize(new Dimension(frameRatio*CostantiTetris.WIDTH, frameRatio*CostantiTetris.HEIGHT));

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

		pnlScorta.setPreferredSize(new Dimension(frameRatio*4, frameRatio*4));

		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				lblScorta[i][j] = new JLabel();
				lblScorta[i][j].setOpaque(true);
				lblScorta[i][j].setPreferredSize(new Dimension(70,70));
				lblScorta[i][j].setBackground(Color.BLACK);
				pnlScorta.add(lblScorta[i][j]);
			}
		}


		for(int i=0; i<CostantiTetris.N_NEXT; i++){
			JPanel pnlProssimo = new JPanel(new GridLayout(4,4));
			pnlProssimo.setPreferredSize(new Dimension(frameRatio*4, frameRatio*4));

			prossimi.add(pezzo.random());
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


		//constraints //TODO: metti in ordine sta roba, guarda cosa puoi eliminare
		GridBagConstraints constrPnlGioco = new GridBagConstraints();
        constrPnlGioco.gridx = 1;
        constrPnlGioco.gridy = 0;
        constrPnlGioco.fill = GridBagConstraints.NONE; // Prevent resizing
        constrPnlGioco.anchor = GridBagConstraints.CENTER; // Center the panel
        constrPnlGioco.weightx = 0.0; // Don't allow horizontal stretching
        constrPnlGioco.weighty = 0.0; // Don't allow vertical stretching

        GridBagConstraints constrPnlScorta = new GridBagConstraints();
        constrPnlScorta.gridx = 0;
        constrPnlScorta.gridy = 0;
        constrPnlScorta.fill = GridBagConstraints.NONE; // Prevent resizing
        constrPnlScorta.anchor = GridBagConstraints.NORTHWEST; // Center the panel
        constrPnlScorta.weightx = 0.0; // Don't allow horizontal stretching
        constrPnlScorta.weighty = 0.0; // Don't allow vertical stretching

        GridBagConstraints constrPnlProssimo = new GridBagConstraints();
        constrPnlProssimo.gridx = 2;
        constrPnlProssimo.gridy = 0;
        constrPnlProssimo.fill = GridBagConstraints.NONE; // Prevent resizing
        constrPnlProssimo.anchor = GridBagConstraints.NORTHEAST; // Center the panel
        constrPnlProssimo.weightx = 0.0; // Don't allow horizontal stretching
        constrPnlProssimo.weighty = 0.0; // Don't allow vertical stretching
		//add
		frame.add(pnlScorta, constrPnlScorta);
		frame.add(pnlGioco, constrPnlGioco);
		frame.add(pnlProssimi, constrPnlProssimo);

		//eventi
		frame.addKeyListener(new TastoPremuto());

		//timer
		timer.start();

		//impostazioni frame
		//frame.setSize(frameRatio*CostantiTetris.WIDTH, frameRatio*(CostantiTetris.HEIGHT);
		frame.setSize(1000, 900); //TODO: metti una roba migliore
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	void gameOver(){
		JLabel lblGameOver = new JLabel("<html>Game Over <br> Righe: "+righe+"</html>\n");

		lblGameOver.setFont(new Font("Arial", Font.PLAIN,64));
		lblGameOver.setForeground(Color.WHITE);
		lblGameOver.setOpaque(true);
		lblGameOver.setBackground(Color.BLACK);
		lblGameOver.setHorizontalAlignment(SwingConstants.CENTER);

		dlgGameOver = new JDialog(frame, true);
		dlgGameOver.setTitle("GAME OVER");
		dlgGameOver.setSize(400,300);
		dlgGameOver.setLocation(500,200); //TODO centra il frame
		dlgGameOver.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		dlgGameOver.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		});

		dlgGameOver.add(lblGameOver);

		//TODO: suono hai perso;
		audio.stop();//tmp?

		dlgGameOver.setVisible(true);
	}


	class GestoreAudio{
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

	class PassaTempo implements ActionListener{

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

	class TastoPremuto implements KeyListener{
		public void keyPressed(KeyEvent e){
			int key = e.getExtendedKeyCode();

			boolean solidificato = false;
			cancellaBlocchi();
			disegnaBlocchi();

			switch(key){
				case KeyEvent.VK_UP:
				case KeyEvent.VK_W:
					pezzo.ruotaPezzo();
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
					System.out.println("ti manca ruotare a sinistra");
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

    void cancellaBlocchi(){
		for(int i=0; i<CostantiTetris.HEIGHT; i++){
			for(int j=0; j<CostantiTetris.WIDTH; j++){
				lblDisplay[i][j].setBackground(Color.BLACK); //TODO: forse mettilo trasparente
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

    void aggiornaScorta(){
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
    void aggiornaProssimi(){

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

	void nuovoPezzo(){
		if(!gameOver){
            //Pezzo nuovoPezzo = pezzo.random(); //TODO: provvisorio crealo solo dopo che le righe vanno in basso

			pezzo = prossimi.removeFirst();
			prossimi.addLast(pezzo.random());

			aggiornaProssimi();
		}
	}

	void scortaPezzo(){ //TODO: mostra pezzo di scorta
		if(scorta != null && pezzo.isScortaDisponibile()){
			Pezzo tmp = scorta;
			scorta = pezzo;
			pezzo = tmp;

			pezzo.setX(CostantiTetris.INITIAL_X);
			pezzo.setY(CostantiTetris.INITIAL_Y);
		}else if(pezzo.isScortaDisponibile()){
			scorta = pezzo;
			pezzo = pezzo.random();
		}
		pezzo.occupaScorta();
		aggiornaScorta();
	}

	public static void main(String[] args){
		new Tetris();
	}
}
