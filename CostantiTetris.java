import java.awt.Color;
class CostantiTetris{
    public static final boolean PEZZO_I[][] = {{false,false, false, false},
                                               {true,  true,  true, true},
                                               {false, false, false, false},
                                               {false, false, false, false}};

    public static final boolean PEZZO_J[][] = {{true, false, false, false},
                                               {true,  true,  true, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};

    public static final boolean PEZZO_L[][] = {{false, false, true, false},
                                               {true,  true,  true, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};
    
    public static final boolean PEZZO_O[][] = {{true, true, false, false},
                                               {true,  true,  false, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};
    
    public static final boolean PEZZO_S[][] = {{false, true, true, false},
                                               {true,  true,  false, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};
    
    public static final boolean PEZZO_Z[][] = {{true, true, false, false},
                                               {false, true,  true, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};
    
    public static final boolean PEZZO_T[][] = {{false, true, false, false},
                                               {true,  true,  true, false},
                                               {false, false, false, false},
                                               {false, false, false, false}};

    public static final Color TIPO_I = new Color(0, 230, 254);
    public static final Color TIPO_J = new Color(24, 1, 255);
    public static final Color TIPO_L = new Color(255, 115, 8);
    public static final Color TIPO_O = new Color(255, 222, 0);
    public static final Color TIPO_S = new Color(102, 253, 0);
    public static final Color TIPO_Z = new Color(254, 16, 60);
    public static final Color TIPO_T = new Color(184, 2, 253);

    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    public static final int INITIAL_X = 4;
    public static final int INITIAL_Y = 0;

    
}
