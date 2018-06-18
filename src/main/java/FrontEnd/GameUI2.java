package FrontEnd;


import Backend.*;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI2 extends  JPanel{

    private JButton undoButton, playButton;
    private JComboBox xCoor, yCoor, dir;
    static private JLabel label;
    private JPanel infoContainer, Drawable;

    static int boardSize = 3;
    static int[] startingPos = new int[] { 20,20};
    static int pointSpacing = 50;
    static  GameManager gm;
    static Board backBoard;



    public GameUI2(){
        gm = new GameManager(boardSize-1);
        backBoard = gm.getBoard();
        label = new JLabel();
        label.setText("Dots and Boxes");
        String[] aux = new String[boardSize];
        for (int i = 0; i < boardSize-1; i++) {
            aux[i] = Integer.toString(i);
        }
        xCoor = new JComboBox();
        yCoor = new JComboBox();
        dir = new JComboBox();
        undoButton = new JButton();
        playButton = new JButton();
        populateComboBox(xCoor, aux);
        populateComboBox(yCoor, aux);
        populateComboBox(dir, new String[] {"TOP", "RIGHT", "LEFT", "DOWN"});
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                pressClearButton();
                //infoContainer.repaint();
                xCoor.repaint();
                yCoor.repaint();
                dir.repaint();
            }
        });
        infoContainer = new JPanel();
        infoContainer.setLayout(new BorderLayout());
        //label
        infoContainer.add(label, BorderLayout.PAGE_START);
        label.setHorizontalAlignment(0);
        label.repaint();

        JPanel auxPanel1 = new JPanel();
        auxPanel1.setLayout(new BorderLayout());
        //XY COOR
        JPanel xyPanel = new JPanel();
        xyPanel.setLayout(new BorderLayout());
        xyPanel.add(xCoor, BorderLayout.LINE_START);
        xCoor.repaint();
        xyPanel.add(yCoor, BorderLayout.LINE_END);
        yCoor.repaint();
        auxPanel1.add(xyPanel, BorderLayout.LINE_START);
        //directions
        auxPanel1.add(dir, BorderLayout.CENTER);
        dir.repaint();

        //Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());

        buttonsPanel.add(undoButton, BorderLayout.LINE_START);
        undoButton.setMinimumSize(new Dimension(100,20));
        undoButton.setPreferredSize(new Dimension(100,20));
        undoButton.setText("Undo");
        undoButton.repaint();
        buttonsPanel.add(playButton, BorderLayout.LINE_END);
        playButton.setText("Play");
        playButton.setMinimumSize(new Dimension(100,20));
        playButton.setPreferredSize(new Dimension(100,20));
        playButton.repaint();
        auxPanel1.add(buttonsPanel, BorderLayout.LINE_END);

        infoContainer.add(auxPanel1, BorderLayout.CENTER);
        //End buttons

        JPanel boardUI = new myBoard();
        boardUI.setPreferredSize(new Dimension(50,1100));
        infoContainer.add(boardUI, BorderLayout.PAGE_END);

        infoContainer.setBackground(Color.YELLOW); //testing purposes
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //OBTENER DATOS
                int x = xCoor.getSelectedIndex();
                int y = yCoor.getSelectedIndex();
                Board.DIRECTIONS direction = null;
                switch (dir.getSelectedIndex()){
                    case 0:
                        direction = Board.DIRECTIONS.TOP;
                        break;
                    case 1:
                        direction = Board.DIRECTIONS.RIGHT;
                        break;
                    case 2:
                        direction = Board.DIRECTIONS.BOTTOM;
                        break;
                    case 3:
                        direction = Board.DIRECTIONS.LEFT;
                }
                //verificar que este

                //CONECTAR CON BACK
                play(x,y,direction, infoContainer);
                //pintar de nuevo el tablero
                 //paintnewBoard();
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("GameUI");
        GameUI2 ui = new GameUI2();
        frame.getContentPane().add(ui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(820,900));
        frame.setPreferredSize(new Dimension(820,900));
        frame.setLocation(650,100);
        //frame.pack(); //me cambia to-do
        frame.setVisible(true);
        frame.add(ui.infoContainer, BorderLayout.NORTH);
        ui.infoContainer.repaint();
        //wantToPlay()
    }

    public static void play(int x, int y, Board.DIRECTIONS direction, JPanel infoContainer){
        if(gm.gameStatus == GameManager.GAME_STATUS.PLAYING){
            gm.move(x,y,direction);
            JPanel newBoard = new myBoard();
            newBoard.setPreferredSize(new Dimension(50,1100));
            infoContainer.add(newBoard, BorderLayout.PAGE_END);
            infoContainer.repaint();
        }else {
            int winner = gm.whoWins();
            if (winner != 0) {
                label.setText("Player " + winner + " Wins!");
            } else {
                label.setText("is a Tie!");
            }
            label.repaint();
        }
    }

    private static void rePaintBoard(){

    }

    private void pressClearButton() {
        this.xCoor.setSelectedIndex(0);
        xCoor.repaint();
        this.yCoor.setSelectedIndex(0);
        xCoor.repaint();
        this.dir.setSelectedIndex(0);
    }

    private void populateComboBox(JComboBox comboBox, String[] aux) {
        ComboBoxModel cbm = new ComboBoxModel() {

            String[] data = aux;
            String selection = data[0];

            @Override
            public void setSelectedItem(Object o) {
                selection = (String) o;
            }

            @Override
            public Object getSelectedItem() {
                return selection;
            }

            @Override
            public int getSize() {
                return data.length;
            }

            @Override
            public Object getElementAt(int i) {
                return data[i];
            }

            @Override
            public void addListDataListener(ListDataListener listDataListener) {
            }

            @Override
            public void removeListDataListener(ListDataListener listDataListener) {
            }
        };

        comboBox.setModel(cbm);
        comboBox.setMaximumRowCount(aux.length);
    }

    private static class myBoard extends JPanel{

        public void paint(Graphics g){
            int pointSize = 15;
            //dots ESTO VA SI O SI
            for (int i = 0; i < boardSize; i++) {
                for (int j = 0; j < boardSize; j++) {
                    g.fillRoundRect(i * pointSpacing + startingPos[0],j * pointSpacing + startingPos[1],
                            pointSize,pointSize,pointSize,pointSize); //x, y, widht, height, arcwidth, archeight
                }
            }

            //AHORA LAS EDGES
            for (int i = 0; i < boardSize -1; i++) {
                for (int j = 0; j < boardSize -1; j++) {
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.TOP)){
                        /*g.drawLine(i + startingPos[0], j + startingPos[1],
                                i + startingPos[0] + pointSize, j + startingPos[1]);*/
                        g.fillRect(i + startingPos[0] + pointSize / 2, j + startingPos[1] + pointSize / 2,
                                3 * pointSize, pointSize / 3);
                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.RIGHT)){

                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.BOTTOM)){
                        g.drawLine(i + startingPos[0], (j+1) + startingPos[1], pointSize, pointSize);
                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.LEFT)){

                    }
                }
            }
        }
    }
}
