package TicTacToe;

public class AlphaBetaPruning {

    private static int depth;

    private AlphaBetaPruning() {
    }

    public static int[] run(Board board, int depth) {

        if (depth < 1) {
            throw new IllegalArgumentException("Maximum depth must be greater than 0.");
        }

        AlphaBetaPruning.depth = depth;

        int[] result = alphaBetaPruning(board.getTurn(), board, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        return new int[] {result[1], result[2]};
    }

    public static int[] run(Board board) {

        int[] result = alphaBetaPruning(board.getTurn(), board, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new int[] {result[1], result[2]};
    }

    private static int[] alphaBetaPruning(Board.State player, Board board, int alpha, int beta) {

        int score;
        int bestRow = -1;
        int bestCol = -1;

        if(board.isGameOver()) {
            System.out.println("EVALUATE");
            System.out.println(board.toString());
            score = evaluate(player,board);
            return new int[] {score, bestRow, bestCol};
        } else {
            for (Integer cellIndex : board.getAvailableMoves()) {

                Board modifiedBoard = board.getDeepCopy();
                modifiedBoard.move(cellIndex);

                if (board.getTurn() == player) {
                    score = alphaBetaPruning(player, modifiedBoard, alpha, beta)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = cellIndex / Board.BOARD_WIDTH;
                        bestCol = cellIndex % Board.BOARD_WIDTH;
                    }
                } else {
                    score = alphaBetaPruning(player, modifiedBoard, alpha, beta)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = cellIndex / Board.BOARD_WIDTH;
                        bestCol = cellIndex % Board.BOARD_WIDTH;
                    }
                }

                if (alpha >= beta) break;
            }
            return new int[] {board.getTurn() == player ? alpha : beta, bestRow, bestCol};
        }
    }

    private static int[] alphaBetaPruning(Board.State player, Board board, int alpha, int beta, int currentDepth) {

        int score;
        int bestRow = -1;
        int bestCol = -1;

        if(currentDepth++ == depth || board.isGameOver()) {
            score = evaluate(player,board);
            return new int[] {score, bestRow, bestCol};
        } else {
            for (Integer cellIndex : board.getAvailableMoves()) {

                Board modifiedBoard = board.getDeepCopy();
                modifiedBoard.move(cellIndex);

                if (board.getTurn() == player) {
                    score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentDepth)[0];
                    if (score > alpha) {
                        alpha = score;
                        bestRow = cellIndex / Board.BOARD_WIDTH;
                        bestCol = cellIndex % Board.BOARD_WIDTH;
                    }
                } else {
                    score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentDepth)[0];
                    if (score < beta) {
                        beta = score;
                        bestRow = cellIndex / Board.BOARD_WIDTH;
                        bestCol = cellIndex % Board.BOARD_WIDTH;
                    }
                }

                if (alpha >= beta) break;
            }
            return new int[] {board.getTurn() == player ? alpha : beta, bestRow, bestCol};
        }
    }

    private static int evaluate(Board.State player, Board board) {

        if (player == Board.State.Blank) {
            throw new IllegalArgumentException("Player must be X or O.");
        }

        Board.State opponent = (player == Board.State.X) ? Board.State.O : Board.State.X;

        int score;
        int rowscore;
        int columnscore;
        int diagonalscoretopright = 0;
        int diagonalscoretopleft = 0;

        rowscore = evaluateRows(board, player, opponent);
        columnscore = evaluateColumns(board, player, opponent);


        for(int row = Board.BOARD_WIDTH - Window.J; row > 0; row --) {
            int sright = evaluateDiagonalsFromTopRight(board, player, opponent, row, 0);
            int sleft = evaluateDiagonalFromTopLeft(board, player, opponent, row, Board.BOARD_WIDTH-1);

            diagonalscoretopright += sright;
            diagonalscoretopleft += sleft;
        }

        for(int column = 0; column <= Board.BOARD_WIDTH - Window.J; column ++) {
            int sright = evaluateDiagonalsFromTopRight(board, player, opponent, 0, column);
            int sleft = evaluateDiagonalFromTopLeft(board, player, opponent, 0, Board.BOARD_WIDTH-1 - column);

            diagonalscoretopright += sright;
            diagonalscoretopleft += sleft;

        }


        score = rowscore + columnscore + diagonalscoretopright + diagonalscoretopleft;
        return score;
    }

    public static int evaluateRows(Board board, Board.State player, Board.State opponent) {
        int score = 0;
        Board.State[][] cells = board.getBoard();

        for(int row = 0; row < board.BOARD_WIDTH; row++) {

            for (int i = 0; i < board.BOARD_WIDTH - Window.J + 1; i++) {
                int j = i;
                int newscore = 0;

                while(j < i + Window.J){
                    if(cells[row][j] == player) {
                        newscore = 1;
                        j++;
                        break;
                    } else if (cells[row][j] == opponent) {
                        newscore = -1;
                        j++;
                        break;
                    }
                    j++;
                }

                while(j < i + Window.J ){
                    if(cells[row][j] == player) {
                        if(newscore > 0) {
                            newscore *= 10;
                        } else if(newscore < 0) {
                            newscore = 0;
                            break;
                        } else {
                            newscore = 1;
                        }
                    } else if (cells[row][j] == opponent) {
                        if (newscore < 0) {
                            newscore *= 10;
                        } else if (newscore > 0) {
                            newscore = 0;
                            break;
                        } else {
                            newscore = -1;
                        }
                    }
                    j++;
                }
                score += newscore;
            }
        }
        return score;
    }

    public static int evaluateColumns(Board board, Board.State player, Board.State opponent) {
        int score = 0;
        Board.State[][] cells = board.getBoard();

        for(int column = 0; column < board.BOARD_WIDTH; column++) {

            for (int i = 0; i < board.BOARD_WIDTH - Window.J + 1; i++) {
                int j = i;
                int newscore = 0;
                while(j < i + Window.J){
                    if(cells[j][column] == player) {
                        newscore = 1;
                        j++;
                        break;
                    } else if (cells[j][column] == opponent) {
                        newscore = -1;
                        j++;
                        break;
                    }
                    j++;
                }

                while(j < i + Window.J){
                    if(cells[j][column] == player) {
                        if(newscore > 0) {
                            newscore *= 10;
                        } else if(newscore < 0) {
                            newscore = 0;
                            break;
                        } else {
                            newscore = 1;
                        }
                    } else if (cells[j][column] == opponent) {
                        if (newscore < 0) {
                            newscore *= 10;
                        } else if (newscore > 0) {
                            newscore = 0;
                            break;
                        } else {
                            newscore = -1;
                        }
                    }
                    j++;
                }
                score += newscore;
            }
        }
        return score;
    }

    public static int evaluateDiagonalsFromTopRight (Board board, Board.State player, Board.State opponent, int x, int y) {

        int score = 0;
        Board.State[][] cells = board.getBoard();

        int xx = x;
        int yy = y;
        int hany = Window.J-1;

        while(xx + hany < Window.K && yy + hany < Window.K) {

            int i = xx;
            int j = yy;
            int newscore = 0;

            while(i <= xx + hany && j <= yy + hany){
                if(cells[i][j] == player) {
                    newscore = 1;
                    i++;
                    j++;
                    break;
                } else if (cells[i][j] == opponent) {
                    newscore = -1;
                    i++;
                    j++;
                    break;
                }
                i++;
                j++;
            }

            while(i <= xx + hany && j <= yy + hany) {
                if (cells[i][j] == player) {
                    if (newscore > 0) {
                        newscore *= 10;
                    } else if (newscore < 0) {
                        newscore = 0;
                        break;
                    } else {
                        newscore = 1;
                    }
                } else if (cells[i][j] == opponent) {
                    if (newscore < 0) {
                        newscore *= 10;
                    } else if (newscore > 0) {
                        newscore = 0;
                        break;
                    } else {
                        newscore = -1;
                    }
                }
                i++;
                j++;
            }
            score += newscore;
            xx++;
            yy++;
        }

        return score;
    }

    public static int evaluateDiagonalFromTopLeft (Board board, Board.State player, Board.State opponent, int x, int y){
        int score = 0;
        Board.State[][] cells = board.getBoard();

        int xx = x;
        int yy = y;
        int hany = Window.J-1;

        while(xx + hany < Window.K && yy - hany >= 0) {

            int i = xx;
            int j = yy;
            int newscore = 0;

            while(i <= xx + hany && j >= yy - hany){
                if(cells[i][j] == player) {
                    newscore = 1;
                    i++;
                    j--;
                    break;
                } else if (cells[i][j] == opponent) {
                    newscore = -1;
                    i++;
                    j--;
                    break;
                }
                i++;
                j--;
            }

            while(i <= xx + hany && j >= yy - hany) {
                if (cells[i][j] == player) {
                    if (newscore > 0) {
                        newscore *= 10;
                    } else if (newscore < 0) {
                        newscore = 0;
                        break;
                    } else {
                        newscore = 1;
                    }
                } else if (cells[i][j] == opponent) {
                    if (newscore < 0) {
                        newscore *= 10;
                    } else if (newscore > 0) {
                        newscore = 0;
                        break;
                    } else {
                        newscore = -1;
                    }
                }
                i++;
                j--;
            }
            score += newscore;
            xx++;
            yy--;
        }

        return score;
    }
}
