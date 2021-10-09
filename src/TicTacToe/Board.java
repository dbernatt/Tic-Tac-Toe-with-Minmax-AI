package TicTacToe;
import java.util.HashSet;

public class Board {

    static final int BOARD_WIDTH = Window.K;

    public enum State {Blank, X, O}

    private State[][] board;

    private State playersTurn;
    private State winner;
    private HashSet<Integer> movesAvailable;
    private int moveCount;

    private boolean gameOver;

    public State[][] getBoard() {
        return board;
    }

    Board() {
        board = new State[BOARD_WIDTH][BOARD_WIDTH];
        movesAvailable = new HashSet<>();
        reset();
    }

    private void initialize () {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = State.Blank;
            }
        }

        movesAvailable.clear();

        for (int i = 0; i < BOARD_WIDTH*BOARD_WIDTH; i++) {
            movesAvailable.add(i);
        }
    }

    void reset () {
        moveCount = 0;
        gameOver = false;
        playersTurn = State.X;
        winner = State.Blank;
        initialize();
    }

    public boolean move (int index) {
        return move( index / BOARD_WIDTH, index % BOARD_WIDTH);
    }

    public boolean move (int x, int y) {

        if (gameOver) {
            throw new IllegalStateException("TicTacToe is over. No moves can be played.");
        }

        if (board[x][y] == State.Blank) {
            board[x][y] = playersTurn;
        } else {
            return false;
        }

        moveCount++;
        movesAvailable.remove(x * BOARD_WIDTH + y);

        // The game is a draw.
        if (moveCount == BOARD_WIDTH * BOARD_WIDTH) {
            winner = State.Blank;
            gameOver = true;
        }

        // Check for a winner.
        checkRow(x);
        checkColumn(y);
        checkDiagonalFromTopRight(x, y);
        checkDiagonalFromTopLeft(x, y);

        playersTurn = (playersTurn == State.X) ? State.O : State.X;
        return true;
    }

    public boolean isGameOver () {
        return gameOver;
    }

    State[][] toArray () {
        return board.clone();
    }

    public State getTurn () {
        return playersTurn;
    }

    public State getWinner () {
        if (!gameOver) {
            throw new IllegalStateException("TicTacToe is not over yet.");
        }
        return winner;
    }

    public HashSet<Integer> getAvailableMoves () {
        return movesAvailable;
    }

    public void checkRow (int row) {

        boolean found;
        for (int i = 0; i < BOARD_WIDTH - Window.J + 1; i++) {

            if(board[row][i] != State.Blank){

                found = true;
                for(int j = i + 1; j < i + Window.J; j++) {
                    if(board[row][i] != board[row][j]){
                        found = false;
                        break;
                    }
                }

                if(found) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    public void checkColumn (int column) {
        boolean found;
        for (int i = 0; i < BOARD_WIDTH - Window.J + 1; i++) {

            if(board[i][column] != State.Blank){

                found = true;
                for(int j = i + 1; j < i + Window.J; j++) {
                    if(board[i][column] != board[j][column]){
                        found = false;
                        break;
                    }
                }

                if(found) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
        }
    }

    public void checkDiagonalFromTopRight (int x, int y) {

        while(x != 0 && y != 0) {
            x--;
            y--;
        }

        int xx = x;
        int yy = y;
        int hany = Window.J-1;

        while(xx + hany < Window.K && yy + hany < Window.K) {
            if(board[xx][yy] != State.Blank){

                int i = xx+1;
                int j = yy+1;
                boolean ok = true;

                while(i <= xx + hany && j <= yy + hany) {
                    if(board[xx][yy] != board[i][j]){
                        ok = false;
                        break;
                    }
                    i++;
                    j++;
                }

                if(ok) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
            xx++;
            yy++;
        }
    }

    public void checkDiagonalFromTopLeft (int x, int y) {

        while(x != 0 && y != Window.K-1) {
            x--;
            y++;
        }

        int xx = x;
        int yy = y;
        int hany = Window.J-1;

        while(xx + hany < Window.K && yy - hany >= 0) {

            if(board[xx][yy] != State.Blank){
                int i = xx + 1;
                int j = yy - 1;
                boolean ok = true;

                while(i <= xx + hany && j >= yy - hany) {
                    if(board[xx][yy] != board[i][j]){
                        ok = false;
                        break;
                    }
                    i++;
                    j--;
                }

                if(ok) {
                    winner = playersTurn;
                    gameOver = true;
                }
            }
            xx++;
            yy--;
        }
    }


    public Board getDeepCopy () {
        Board board             = new Board();

        for (int i = 0; i < board.board.length; i++) {
            board.board[i] = this.board[i].clone();
        }

        board.playersTurn       = this.playersTurn;
        board.winner            = this.winner;
        board.movesAvailable    = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.moveCount         = this.moveCount;
        board.gameOver          = this.gameOver;
        return board;
    }

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < BOARD_WIDTH; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {

                if (board[y][x] == State.Blank) {
                    sb.append("-");
                } else {
                    sb.append(board[y][x].name());
                }
                sb.append(" ");

            }
            if (y != BOARD_WIDTH -1) {
                sb.append("\n");
            }
        }

        return new String(sb);
    }

}
