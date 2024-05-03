import java.awt.Color;
import java.util.*;;

public class PezzoRandom {

    private JLabel[][] lblDisplay;
    private Casella[][] blocchiSolidi;
    private ImageIcon scaledIcon;

    private int[] numeri = {0, 1, 2, 3, 4, 5, 6};
    private ArrayList<Integer> lista;
    private int num_estratto, rand;

    public PezzoRandom(JLabel[][] lblDisplay, Casella[][] blocchiSolidi, ImageIcon scaledIcon){
        private JLabel[][] lblDisplay;
        private Casella[][] blocchiSolidi;
        private ImageIcon scaledIcon;
        lista = new ArrayList<>();
    }

    public Pezzo generaPezzo(){

        if(lista.isEmpty()){
            for(int num : numeri){
                lista.add(num);
            }
        }else{
            rand = (int) (Math.random()*lista.size());
            num_estratto = lista.remove(rand);

            switch(num_estratto){
            case 0:
                boolPezzo = CostantiTetris.PEZZO_I;
                tipoPezzo = CostantiTetris.TIPO_I;
                break;
            case 1:
                boolPezzo = CostantiTetris.PEZZO_J;
                tipoPezzo = CostantiTetris.TIPO_J;
                break;
            case 2:
                boolPezzo = CostantiTetris.PEZZO_L;
                tipoPezzo = CostantiTetris.TIPO_L;
                break;
            case 3:
                boolPezzo = CostantiTetris.PEZZO_O;
                tipoPezzo = CostantiTetris.TIPO_O;
                break;
            case 4:
                boolPezzo = CostantiTetris.PEZZO_S;
                tipoPezzo = CostantiTetris.TIPO_S;
                break;
            case 5:
                boolPezzo = CostantiTetris.PEZZO_Z;
                tipoPezzo = CostantiTetris.TIPO_Z;
                break;
            default:
                boolPezzo = CostantiTetris.PEZZO_T;
                tipoPezzo = CostantiTetris.TIPO_T;
                break;
            }

            Pezzo pezzo = new Pezzo(piece, color, 4, 0, lblDisplay, blocchiSolidi, scaledIcon);
            return pezzo;
        }
    }
}
