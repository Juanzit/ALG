package classes;

import java.util.List;
import java.util.Random;

public class AG {

    private int dim;
    private int tamPop;
    private int tamDNA;
    private final char EMPTY = ' ';
    private final char X = 'X';
    private final char O = 'O';
    private final char DRAW = '-';
    private PopJogoDaVelha p;

    private char[][] board;

    public AG(int dim, int tamPop, int tamDNA) {
        this.dim = dim;
        this.tamPop = tamPop;
        this.tamDNA = tamDNA;
        board = new char[dim][dim];
        p = new PopJogoDaVelha(tamPop, tamDNA);
        resetBoard();
    }

    public Cromossomo getCromossomo(int i) {
        if (i >= 0 && i < p.getSize()) {
            return p.getCromossomo(i);
        } else {
            throw new IllegalArgumentException("Índice inválido.");
        }
    }

    private void resetBoard() {
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isWinningMove(int row, int col, char player) {
        boolean winInRow = true;
        for (int i = 0; i < dim; i++) {
            if (board[row][i] != player) {
                winInRow = false;
                break;
            }
        }
        boolean winInCol = true;
        for (int i = 0; i < dim; i++) {
            if (board[i][col] != player) {
                winInCol = false;
                break;
            }
        }

        boolean winInDiagonal = true;
        if (row == col) {
            for (int i = 0; i < dim; i++) {
                if (board[i][i] != player) {
                    winInDiagonal = false;
                    break;
                }
            }
        }

        boolean winInSecondaryDiagonal = true;
        if (row + col == dim - 1) {
            for (int i = 0; i < dim; i++) {
                if (board[i][dim - 1 - i] != player) {
                    winInSecondaryDiagonal = false;
                    break;
                }
            }
        }

        return winInRow || winInCol || winInDiagonal || winInSecondaryDiagonal;
    }

    private boolean isValidMove(int row, int col) {
        return (row >= 0 && row < dim && col >= 0 && col < dim && board[row][col] == ' ');
    }

    private void apply(Cromossomo c) {
        resetBoard();
        char currentPlayer = X;
        int moves = 0;

        for (int i = 0; i < c.sequenciaJogadas.size(); i++) {
            int position = c.sequenciaJogadas.get(i);
            int row = position / dim;
            int col = position % dim;

            if (isValidMove(row, col)) {
                board[row][col] = currentPlayer;
                moves++;

                if (isWinningMove(row, col, currentPlayer)) {
                    c.reached = true;
                    c.distance = 0.0; // Define a distância como zero quando o objetivo é alcançado
                    break;

                }
                c.score = 2; // Pontuação de vitória

                if (isBoardFull() || moves == dim * dim) {
                    break;
                }

                currentPlayer = (currentPlayer == X) ? O : X;
            }
        }

        if (!c.reached) {
            c.distance = moves; // Define a distância como o número de movimentos realizados se o objetivo não for alcançado
        }
        c.error = moves;
    }

    private char playGame(Cromossomo c1, Cromossomo c2) {
        resetBoard();

        char currentPlayer = X;
        int moves = 0;
        int g1 = -1;
        int g2 = -1;

        int row;
        int col;

        while (g1 < this.tamDNA && g2 < this.tamDNA) {
            if (currentPlayer == X) {
                do {
                    g1++;
                    int position = c1.getGene(g1);

                    row = position / dim;
                    col = position % dim;
                } while (!isValidMove(row, col));
            } else {
                do {
                    g2++;
                    int position = c2.getGene(g2);

                    row = position / dim;
                    col = position % dim;
                } while (!isValidMove(row, col));
            }

            board[row][col] = currentPlayer;
            moves++;

            if (isWinningMove(row, col, currentPlayer)) {
                if (currentPlayer != X) {
                    return X;
                } else {
                    return O;
                }
            }

            if (isBoardFull() || moves == dim * dim) {
                return DRAW;
            }

            currentPlayer = (currentPlayer == X) ? O : X;
        }

        if (g1 < this.tamDNA) {
            return X;
        } else {
            return O;
        }
    }

    private String getBoardState() {
        StringBuilder state = new StringBuilder();
        for (char[] row : board) {
            for (char cell : row) {
                state.append(cell);
            }
        }
        return state.toString();
    }

    private int getNextMove(char currentPlayer, Cromossomo cromossomoX, Cromossomo cromossomoO) {
        String boardState = getBoardState(); 
        String movesX = cromossomoX.getMoves(boardState);
        String movesO = cromossomoO.getMoves(boardState);

        String moves = (currentPlayer == X) ? movesX : movesO;
        String[] moveList = moves.split(",");
        int randomIndex = new Random().nextInt(moveList.length);
        String input = "";
        int number;

        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            number = 0;
        }

        return number;

    }

    private int movesToIndex(char[][] board) {
        int index = 0;
        for (int row = 0; row < dim; row++) {
            for (int col = 0; col < dim; col++) {
                if (board[row][col] != EMPTY) {
                    index++;
                }
            }
        }
        return index;
    }

    public String getMoves(String boardState, List<Integer> validMoves) {
        StringBuilder moves = new StringBuilder();

        for (int move : validMoves) {
            moves.append(move).append(",");
        }

        if (moves.length() > 0) {
            moves.deleteCharAt(moves.length() - 1);
        }

        return moves.toString();
    }

    private int getRandomValidMove() {
        Random random = new Random();
        while (true) {
            int position = random.nextInt(dim * dim);
            int row = position / dim;
            int col = position % dim;
            if (isValidMove(row, col)) {
                return position;
            }
        }
    }

    public void genetic() {
        int generation = 0;

        do {
            for (int i = 0; i < p.getSize(); i++) {
                Cromossomo c1 = p.getCromossomo(i);

                for (int j = 0; j < 10; j++) {
                    int randomIndex = new Random().nextInt(p.getSize());
                    Cromossomo c2 = p.getCromossomo(randomIndex);

                    char result = playGame(c1, c2); 

                    if (result == X) {
                        c1.upScore(2);
                    } else if (result == O) {
                        c2.upScore(2);
                    } else {
                        c1.upScore(1);
                        c2.upScore(1);
                    }
                }
            }
            p.sort();

            if (generation == 99) {
                break; 
            }
            p.removeWeakest();
            Cromossomo[] pais = p.selectParents();

            for (int i = p.sizePop(); i < tamPop; i++) {
                Cromossomo filho = new Cromossomo(tamDNA);
                filho.reproduce(pais[0], pais[1]);
                p.addCromossomo(filho);
            }

            p.mutate();

            generation++;
        } while (generation < 100); // Número de gerações desejadas

        System.out.println("O melhor cromossomo gerado!!!!!!");
        Cromossomo bestCromossomo = p.getBest();
        bestCromossomo.exibir();
    }

}
