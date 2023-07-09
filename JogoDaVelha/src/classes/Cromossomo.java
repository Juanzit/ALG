package classes;

import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cromossomo {

    public List<Integer> sequenciaJogadas;
    public int maxdim;
    public double error;
    public int tentativas;
    public boolean reached;
    public double distance;
    public int id;
    public int[] genes;
    public int score; 

    public void setScore(int score) {
        this.score = score;
    }
    
    public void upScore(int score){
        this.score += score;
    }
    
    public void exibir(){
        System.out.println("----------------------");
        this.show();
        System.out.println("Pontuação: " + this.score);
        System.out.println("----------------------");
    }

    public int getGene(int p){
        return genes[p];
    }
    
    public Cromossomo(int dim) {
        this.id = 0;
        sequenciaJogadas = new ArrayList<>();
        maxdim = dim;
        genes = new int[maxdim]; // Inicializa o array genes com o tamanho maxdim
        generate();
        score = 0;
        reset(); // Adicione esta linha para resetar a pontuação no construtor
    }

    public void updateScore(int points) {
        score += points;
    }

    public void reset() {
        error = 0;
        tentativas = 0;
        reached = false;
        distance = 0;
        score = 0;
    }

    private void generate() {
        Random r = new Random();
        for (int i = 0; i < maxdim; i++) {
            int gene = r.nextInt(9);
            genes[i] = gene;
        }
    }

    public Cromossomo crossover(Cromossomo x) {
        Random r = new Random();
        Cromossomo novo = new Cromossomo(maxdim);

        for (int i = 0; i < maxdim; i++) {
            int origem = r.nextInt(2);
            if (origem == 0) {
                novo.genes[i] = this.genes[i];
            } else {
                novo.genes[i] = x.genes[i];
            }
        }
        return novo;
    }

    public String getMoves(String boardState) {
        StringBuilder moves = new StringBuilder();

        // Percorre cada posição do tabuleiro
        for (int i = 0; i < boardState.length(); i++) {
            if (boardState.charAt(i) == '-') {
                moves.append(i).append(",");
            }
        }

        if (moves.length() > 0) {
            moves.deleteCharAt(moves.length() - 1); // Remove a última vírgula
        }

        return moves.toString();
    }

    public void mutate() {
        Random r = new Random();
        if (sequenciaJogadas.isEmpty()) {
            return; // Ou lance uma exceção adequada
        }
        for (int i = 0; i < maxdim; i++) {
            int loc = r.nextInt(sequenciaJogadas.size());
            sequenciaJogadas.set(loc, r.nextInt(9));
        }
        reset();
    }

    public void show() {
        System.out.print("Sequência de jogadas: ");
        for (int gene : genes) {
            System.out.print(gene + " ");
        }
        System.out.println();
    }

    public int[] getGenes() {
        return genes;
    }

    public int getSum() {
        int sum = 0;
        for (int i = 0; i < genes.length; i++) {
            sum += genes[i];
        }
        return sum;
    }

    public void reproduce(Cromossomo pai1, Cromossomo pai2) {
        Random r = new Random();
        int op = r.nextInt(1);

        for (int i = 0; i < genes.length; i++) {
            if (op == 0) {
                genes[i] = pai1.genes[i];
            } else {
                genes[i] = pai2.genes[i];
            }
        }
    }

}
