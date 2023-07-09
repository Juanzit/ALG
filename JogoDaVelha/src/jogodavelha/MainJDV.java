package jogodavelha;

import classes.AG;
import classes.Cromossomo;
import classes.JogoDaVelha;

public class MainJDV {
    public static void main(String[] args) {
        JogoDaVelha jogo = new JogoDaVelha('X');
        AG ag = new AG(3, 100, 9);
        ag.genetic();
        Cromossomo melhorDNA = ag.getCromossomo(0);
        jogo.jogar(melhorDNA);
    }
    
}
