package classes;

import java.util.Scanner;

public class JogoDaVelha {

    private char[][] tabuleiro;
    private static final int TAMANHO_TABULEIRO = 3;
    private char jogadorHumano;

    public JogoDaVelha(char jogadorHumano) {
        tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];
        inicializarTabuleiro();
        this.jogadorHumano = jogadorHumano;
    }

    private void inicializarTabuleiro() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiro[i][j] = '-';
            }
        }
    }

    public void exibirTabuleiro() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                System.out.print(tabuleiro[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean realizarJogada(int posicao, char jogador) {
        int linha = posicao / TAMANHO_TABULEIRO;
        int coluna = posicao % TAMANHO_TABULEIRO;

        if (linha < 0 || linha >= TAMANHO_TABULEIRO || coluna < 0 || coluna >= TAMANHO_TABULEIRO) {
            return false;
        }

        if (tabuleiro[linha][coluna] == '-') {
            tabuleiro[linha][coluna] = jogador;
            return true;
        }
        return false;
    }

    public boolean verificarVitoria(char jogador) {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            if (tabuleiro[i][0] == jogador && tabuleiro[i][1] == jogador && tabuleiro[i][2] == jogador) {
                return true;
            }
        }
        for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
            if (tabuleiro[0][j] == jogador && tabuleiro[1][j] == jogador && tabuleiro[2][j] == jogador) {
                return true;
            }
        }
        if ((tabuleiro[0][0] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][2] == jogador)
                || (tabuleiro[0][2] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][0] == jogador)) {
            return true;
        }

        return false;
    }

    public boolean isEmpate() {
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                if (tabuleiro[i][j] == '-') {
                    return false;
                }
            }
        }

        return true;
    }

    public void reset() {
        inicializarTabuleiro();
    }

    public String getBoardState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                sb.append(tabuleiro[i][j]);
            }
        }
        return sb.toString();
    }

    public String getMoves() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                if (tabuleiro[i][j] == '-') {
                    sb.append(i * TAMANHO_TABULEIRO + j);
                }
            }
        }
        return sb.toString();
    }

    public void jogarContraMelhorDNA() {
        AG ag = new AG(3, 100, 9);
        ag.genetic();
        Cromossomo melhorDNA = ag.getCromossomo(0);
        jogadaComputador(melhorDNA);
    }

    public boolean jogadaHumana() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a posição da jogada (0-8): ");
        int posicao = scanner.nextInt();
        return realizarJogada(posicao, jogadorHumano);
    }

    public void jogar(Cromossomo melhorDNA) {
        char currentPlayer = jogadorHumano;

        while (true) {
            exibirTabuleiro();

            if (currentPlayer == jogadorHumano) {
                if (!jogadaHumana()) {
                    System.out.println("Jogada inválida. Tente novamente.");
                    continue;
                }

                if (verificarVitoria(jogadorHumano)) {
                    exibirTabuleiro();
                    System.out.println("Você venceu!");
                    break;
                }

                if (isEmpate()) {
                    exibirTabuleiro();
                    System.out.println("Empate!");
                    break;
                }

                currentPlayer = 'O';
            } else {
                jogadaComputador(melhorDNA);

                if (verificarVitoria('O')) {
                    exibirTabuleiro();
                    System.out.println("O computador venceu!");
                    break;
                }

                if (isEmpate()) {
                    exibirTabuleiro();
                    System.out.println("Empate!");
                    break;
                }

                currentPlayer = jogadorHumano;
            }
        }
    }

    public void jogadaComputador(Cromossomo cromossomo) {
        String moves = getMoves();

        if (!moves.isEmpty()) {
            String[] moveList = moves.split("");
            int moveIndex = cromossomo.getSum() % moveList.length;
            int move = Integer.parseInt(moveList[moveIndex]);

            int row = move / TAMANHO_TABULEIRO;
            int col = move % TAMANHO_TABULEIRO;

            if (!realizarJogada(move, 'O')) {
                jogadaComputador(cromossomo);
                return;
            }

            System.out.println("Jogada do computador: linha " + (row + 1) + ", coluna " + (col + 1));
        }
    }
}
