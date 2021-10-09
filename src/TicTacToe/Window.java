package TicTacToe;

import sun.applet.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame {

    static final int Depth = 3;
    static final int K = 10;
    static final int J = 5;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int offset = WIDTH / K;

    private Board board;
    private Panel panel;
    private Image imageX, imageO;

    private enum Mode {Player, AI}
    private Mode mode;

    private Point[] cells;

    private static final int DISTANCE = offset / 2;

    private Window () {
        this(Mode.AI);
    }

    private Window (Mode mode) {
        this.mode = mode;
        board = new Board();
        loadCells();
        panel = createPanel();
        setWindowProperties();
        loadImages();
    }

    private void loadCells () {
        cells = new Point[K*K];

        int x = offset / 2;
        int y = offset / 2;

        for(int i = 0; i < K; i++){
            for(int j = 0; j < K; j++){
                cells[i*K+j] = new Point(x + offset*j, y + offset*i);
            }
        }
    }

    private void setWindowProperties () {
        setResizable(false);
        pack();
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private Panel createPanel () {
        Panel panel = new Panel();
        Container cp = getContentPane();
        cp.add(panel);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel.addMouseListener(new MyMouseAdapter());
        return panel;
    }

    private void loadImages () {
        imageX = getImage("x");
        imageO = getImage("o");
    }

    private static Image getImage (String path) {

        Image image;

        try {
            path = ".." + File.separator + "myassets" + File.separator + path + ".png";
            image = ImageIO.read(Window.class.getResource(path));

            if(K > 3) {
                image =  image.getScaledInstance(offset, offset, Image.SCALE_SMOOTH);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Image could not be loaded.");
        }

        return image;
    }

    private class Panel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            paintTicTacToe((Graphics2D) g);
        }

        private void paintTicTacToe (Graphics2D g) {
            setProperties(g);
            paintBoard(g);
            paintWinner(g);
        }

        private void setProperties (Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = K*offset;

            for(int i = 0; i < K+1; i++){
                g.drawLine(0, i*offset, w, i*offset);
            }

            for(int i = 0; i < K+1; i++){
                g.drawLine(i*offset,0, i*offset, w);
            }

            g.drawString("", 0, 0);
        }

        private void paintBoard (Graphics2D g) {
            Board.State[][] boardArray = board.toArray();

            for (int y = 0; y < K; y++) {
                for (int x = 0; x < K; x++) {
                    if (boardArray[y][x] == Board.State.X) {
                        g.drawImage(imageX, offset * x, offset * y, null);
                    } else if (boardArray[y][x] == Board.State.O) {
                        g.drawImage(imageO, offset * x, offset * y, null);
                    }
                }
            }
        }

        private void paintWinner (Graphics2D g) {
            if (board.isGameOver()) {
                g.setColor(Color.GREEN);
                g.setFont(new Font("Verdana", Font.BOLD, 50));

                String s;

                if (board.getWinner() == Board.State.Blank) {
                    s = "Draw";
                } else {
                    s = board.getWinner() + " Wins!";
                }

                g.drawString(s,  offset * K / 2 - getFontMetrics(g.getFont()).stringWidth(s)/2, offset * K / 2);
            }
        }
    }

    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);

            if (board.isGameOver()) {
                board.reset();
                panel.repaint();
            } else {
                playMove(e);
            }

        }

        private void playMove (MouseEvent e) {
            int move = getMove(e.getPoint());

            if (!board.isGameOver() && move != -1) {
                boolean validMove = board.move(move);
                if (mode == Mode.AI && validMove && !board.isGameOver()) {
                    int [] res = AlphaBetaPruning.run(board, Window.Depth);
                    System.out.println("x : " + res[0] + " y = " + res[1]);
                    board.move(res[0], res[1]);
                }
                panel.repaint();
            }
        }

        private int getMove (Point point) {
            for (int i = 0; i < cells.length; i++) {
                if (distance(cells[i], point) <= DISTANCE) {
                    return i;
                }
            }
            return -1;
        }

        private double distance (Point p1, Point p2) {
            double xDiff = p1.getX() - p2.getX();
            double yDiff = p1.getY() - p2.getY();

            double xDiffSquared = xDiff*xDiff;
            double yDiffSquared = yDiff*yDiff;

            return Math.sqrt(xDiffSquared+yDiffSquared);
        }
    }

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new Window(Mode.Player));
        SwingUtilities.invokeLater(() -> new Window(Mode.AI));
    }
}
