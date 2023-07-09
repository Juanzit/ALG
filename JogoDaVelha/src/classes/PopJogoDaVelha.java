package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PopJogoDaVelha {

    public ArrayList<Cromossomo> pop = new ArrayList<>();
    public PopJogoDaVelha(int qtde, int maxdim) {
        for (int i = 0; i < qtde; i++) {
            Cromossomo x = new Cromossomo(maxdim);
            x.id = i; 
            pop.add(x);
        }
    }

    public int sizePop() {
        return this.pop.size();
    }

    public void crossover(int lim) {

        if (lim >= 0 && lim < pop.size()) {
            for (int i = 0; i < lim; i += 2) {
                Cromossomo x = pop.get(i);
                Cromossomo y = pop.get(i + 1);
                pop.add(x.crossover(y)); 
            }
        }
    }

    public void sort() {
        Collections.sort(pop, (a, b) -> Integer.compare(b.score, a.score));
    }

    public void mutant(int lim) {
        if (lim < pop.size()) {
            for (int i = 0; i < lim; i++) {
                Cromossomo x = pop.get(pop.size() - 1 - i);
                x.mutate(); // Realiza a mutação no cromossomo x
            }
        }
    }

    public void delWorst(int lim) {
        if (lim > 0 && lim <= pop.size()) {
            for (int i = 0; i < lim; i++) {
                pop.remove(pop.size() - 1); 
            }
        }
    }

    public void showPop() {
        for (Cromossomo x : pop) {
            x.show();
        }
    }

    public void reset() {
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo x = pop.get(i);
            x.reset(); 
        }
    }

    public double averageError() {
        double sum = 0;
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo x = pop.get(i);
            sum += x.error; 
        }
        return sum / pop.size(); 
    }

    public double averageTentativas() {
        double sum = 0;
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo x = pop.get(i);
            sum += x.tentativas;
        }
        return sum / pop.size(); 
    }

    public int qtdeSucesso() {
        int sum = 0;
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo x = pop.get(i);
            if (x.reached) {
                sum++; 
            }
        }
        return sum; 
    }

    public double percSucesso() {
        int qtde = pop.size();
        if (qtde == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < qtde; i++) {
            Cromossomo x = pop.get(i);
            if (x.reached) {
                sum++; 
            }
        }
        return (double) (100 * sum) / qtde; 
    }
    public Cromossomo showBest() {
        if (pop.isEmpty()) {
            return null;
        }

        Cromossomo x = pop.get(0); 
        System.out.println("reach: " + x.reached + " distance: " + x.distance + " tent: " + x.tentativas + " error: " + x.error);
        return x; 
    }

    public void order() {
        Collections.sort(pop, (c1, c2) -> Double.compare(c1.distance, c2.distance)); // Ordena a população em ordem crescente de distância
    }

    public void showErrors() {
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo x = pop.get(i);
            System.out.println("i: " + i + " ID:" + x.id + " reach:" + x.reached + " tent:" + x.tentativas + " error:" + x.error);
        }
    }

    public Cromossomo[] getDoisMelhores() {
        Cromossomo[] doisMelhores = new Cromossomo[2];
        doisMelhores[0] = pop.get(0);
        doisMelhores[1] = pop.get(1);
        return doisMelhores;
    }

    public Cromossomo[] selectParents() {
        Cromossomo[] pais = new Cromossomo[2];
        double totalScore = 0;

        for (Cromossomo cromossomo : pop) {
            totalScore += cromossomo.score;
        }

        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            double randomValue = random.nextDouble() * totalScore;
            double acumulatedScore = 0;

            for (Cromossomo cromossomo : pop) {
                acumulatedScore += cromossomo.score;

                if (acumulatedScore >= randomValue) {
                    pais[i] = cromossomo;
                    break;
                }
            }
        }

        return pais;
    }
    public Cromossomo getBest() {
        if (pop.isEmpty()) {
            return null;
        }

        Cromossomo best = pop.get(0); 

        for (Cromossomo cromossomo : pop) {
            if (cromossomo.score < best.score) {
                best = cromossomo;
            }
        }
        return best;
    }

    public void mutate(int lim) {
        if (lim < pop.size()) {
            for (int i = 0; i < lim; i++) {
                Cromossomo x = pop.get(pop.size() - 1 - i);
                x.mutate();
            }
        }
    }

    public void mutate() {
        for (Cromossomo cromossomo : pop) {
            if (Math.random() < 0.01) { 
                cromossomo.mutate();
            }
        }
    }

    public void removeWeakest() {
        int d = pop.size() / 2;
        for (int i = pop.size() / 2; i > 0; i--) {
            pop.remove(d);
        }
    }

    public void addCromossomo(Cromossomo c) {
        pop.add(c);
    }

    public int getSize() {
        return pop.size();
    }

    public Cromossomo getCromossomo(int i) {
        return pop.get(i);
    }

    public Cromossomo getBestDNA() {
        Cromossomo bestDNA = null;
        for (int i = 0; i < pop.size(); i++) {
            Cromossomo c = pop.get(i);
            if (c.reached && (bestDNA == null || c.score > bestDNA.score)) {
                bestDNA = c;
            }
        }
        return bestDNA;
    }

    public Cromossomo tournamentSelection(int tournamentSize) {
        Random random = new Random();
        PopJogoDaVelha tournamentPop = new PopJogoDaVelha(tournamentSize, maxdim); 

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(pop.size());
            Cromossomo randomIndividual = pop.get(randomIndex);
            tournamentPop.pop.add(randomIndividual);
        }

        tournamentPop.sort(); 
        return tournamentPop.pop.get(0); 
    }

    public Cromossomo selecionarMelhorCromossomo() {
        sort(); 
        return pop.get(0); 
    }

    public void imprimirResultados() {
        for (Cromossomo c : pop) {
            System.out.println("Cromossomo " + c.id + ":");
            c.show();
            System.out.println("Pontuação: " + c.score);
            System.out.println("----------------------");

        }
    }
    public void jogarContraMelhorDNA() {
        JogoDaVelha jogo = new JogoDaVelha('X');
        jogo.jogarContraMelhorDNA();
    }

}