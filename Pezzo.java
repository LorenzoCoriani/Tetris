import java.awt.*;
import javax.swing.*;

class Pezzo{//TODO: rinomina attributi

    static boolean pezzoI[][] = {{false,false, false, false},
                                {true,  true,  true, true},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoI = new Color(0, 230, 254);

    static boolean pezzoJ[][] = {{true, false, false, false},
                                {true,  true,  true, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoJ = new Color(24, 1, 255);

    static boolean pezzoL[][] = {{false, false, true, false},
                                {true,  true,  true, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoL = new Color(255, 115, 8);

    static boolean pezzoO[][] = {{true, true, false, false},
                                {true,  true,  false, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoO = new Color(255, 222, 0);

    static boolean pezzoS[][] = {{false, true, true, false},
                                {true,  true,  false, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoS = new Color(102, 253, 0);

    static boolean pezzoZ[][] = {{true, true, false, false},
                                {false,  true,  true, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoZ = new Color(254, 16, 60);

    static boolean pezzoT[][] = {{false, true, false, false},
                                {true,  true,  true, false},
                                {false, false, false, false},
                                {false, false, false, false}};
    static Color tipoT = new Color(184, 2, 253);


    Color tipo;
    boolean pezzo[][];
    int x,y;

    /*Pezzo(){
        this.pezzo = ;
        this.x = 4;
        this.y = 0;
        this.tipo = tipo;
    }*/

    Pezzo(boolean pezzo[][], Color tipo){
        this.pezzo = pezzo;
        this.x = 4;
        this.y = 0;
        this.tipo = tipo;
    }

    Pezzo(boolean pezzo[][],int x,int y, Color tipo){
        this.pezzo = pezzo;
        this.x = x;
        this.y = y;
        this.tipo = tipo;
    }

    //metodi

    static Pezzo random(){ //TODO: 7 bag randomizer
        boolean boolPezzo[][]= {{false, false, false, false},
                                {false, false, false, false},
                                {false, false, false, false},
                                {false, false, false, false}};
        Color tipoPezzo;// = Color.WHITE;

        int rand = (int) (Math.random()*7);

        switch(rand){
            case 0:
                boolPezzo = pezzoI;
                tipoPezzo = tipoI;
                break;
            case 1:
                boolPezzo = pezzoJ;
                tipoPezzo = tipoJ;
                break;
            case 2:
                boolPezzo = pezzoL;
                tipoPezzo = tipoL;
                break;
            case 3:
                boolPezzo = pezzoO;
                tipoPezzo = tipoO;
                break;
            case 4:
                boolPezzo = pezzoS;
                tipoPezzo = tipoS;
                break;
            case 5:
                boolPezzo = pezzoZ;
                tipoPezzo = tipoZ;
                break;
            default:
                boolPezzo = pezzoT;
                tipoPezzo = tipoT;
                break;
        }

        Pezzo pezzo = new Pezzo(boolPezzo, 4, 0, tipoPezzo);
        return pezzo;
    }

    void disegna(JLabel lblDisplay[][], ImageIcon scaledIcon){
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				if(pezzo[i][j]){
					lblDisplay[i+y][j+x].setBackground(tipo);
					lblDisplay[i+y][j+x].setIcon(scaledIcon);
				}
			}
		}
	}
}


