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
    private JPanel Drawable;

    static int boardSize = 3;
    static int[] startingPos = new int[] { 20,20};
    static int pointSpacing = 50;
    static  GameManager gm;
    static Board backBoard;
    static GameManager.GAME_MODE game_mode = GameManager.GAME_MODE.HVSAI;
    static JPanel infoContainer;



    public GameUI2(){ //se mira no se toca
        gm = new GameManager(boardSize-1, IA.Mode.DEPTH, 1,0,true, GameManager.GAME_MODE.HVSAI);
        backBoard = gm.getBoard();

        //UI STUFF
        label = new JLabel();
        label.setText(gm.whoIsActivePlayer());
        String[] aux = new String[boardSize -1];
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
        populateComboBox(dir, new String[] {"TOP", "RIGHT", "DOWN", "LEFT"});
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



        //---------------------------------------------------------------BOTON PARA JUGAR
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //OBTENER DATOS
                if(game_mode != GameManager.GAME_MODE.AIVSAI) {
                    int x = xCoor.getSelectedIndex();
                    int y = yCoor.getSelectedIndex();
                    Board.DIRECTIONS direction = null;
                    switch (dir.getSelectedIndex()) {
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
                    //JUGAR
                    if(play(x, y, direction)) {
                        //Label
                        label.setText(gm.whoIsActivePlayer());
                        label.repaint();
                        gm.isAiPlaying = false;
                        backBoard = gm.aiMove();
                        //rePaintBoard();
                    }
                }
            }
        });
    }

    public static void main(String[] args){
        JFrame frame = new JFrame("Dots and Boxes");
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
        wantToPlay();
    }

    public static void wantToPlay(){
        while(gm.gameStatus != GameManager.GAME_STATUS.OVER){
            //System.out.println("playing");
            //System.out.println(gm.gameStatus);
            if(game_mode != GameManager.GAME_MODE.HVSH) {
                if (gm.playerTurn) { //si es turno de human
                    //wait for click
                    //no need to put anything
                } else if(!gm.isAiPlaying){
                    backBoard = gm.aiMove();
                    rePaintBoard();
                    //System.out.println("waiting..");
                }
            }
            if(game_mode == GameManager.GAME_MODE.AIVSAI){
                //ai1.move y ai2.move
            }
        }
        int winner = gm.whoWins();
        if (winner != 0) {
            JOptionPane.showMessageDialog(null, "Player " + winner + " Wins!");
        } else {
            JOptionPane.showMessageDialog(null, "It's a Tie!");
        }
    }

    public static boolean play(int x, int y, Board.DIRECTIONS direction){
        if(gm.gameStatus == GameManager.GAME_STATUS.PLAYING){
            if(!gm.move(x,y,direction)){
                JOptionPane.showMessageDialog(null, "Already an edge");
                return false;
            }
            rePaintBoard();
            return true;
        }else {
            JOptionPane.showMessageDialog(null, "Player 1  Wins!"); //TODO change
        }
        return true;
    }

    private static void rePaintBoard(){
        JPanel newBoard = new myBoard();
        newBoard.setPreferredSize(new Dimension(50,1100));
        infoContainer.add(newBoard, BorderLayout.PAGE_END);
        infoContainer.repaint();
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
            //AHORA LAS EDGES Y BOXES
            for (int i = 0; i < boardSize -1; i++) {
                for (int j = 0; j < boardSize -1; j++) {
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.TOP)){
                        g.fillRect(j * pointSpacing +  startingPos[0] + pointSize /2,
                                i * pointSpacing + startingPos[1] + pointSize / 2,
                                3 * pointSize, pointSize / 3);
                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.RIGHT)){
                        g.fillRect((j+1) * pointSpacing +  startingPos[0] + pointSize /2,
                                i * pointSpacing + startingPos[1] + pointSize / 2,
                                pointSize / 3,  3 * pointSize);
                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.BOTTOM)){
                        g.fillRect(j * pointSpacing +  startingPos[0] + pointSize /2,
                                (i+1) * pointSpacing + startingPos[1] + pointSize / 2,
                                3 * pointSize, pointSize / 3);
                    }
                    if(backBoard.hasEdge(backBoard,i,j, Board.DIRECTIONS.LEFT)){
                        g.fillRect((j) * pointSpacing +  startingPos[0] + pointSize /2,
                                i * pointSpacing + startingPos[1] + pointSize / 2,
                                pointSize / 3,  3 * pointSize);
                    }
                    //g.setColor(Color.blue);
                    int boxColor = backBoard.getBoxColor(i,j);
                    if(boxColor != 0){
                        if(boxColor == 1) {
                            g.setColor(Color.RED);
                        }else{
                            g.setColor(Color.BLUE);
                        }
                        g.fillRect((j) * pointSpacing + startingPos[0] + pointSize,
                                i * pointSpacing + startingPos[1] + pointSize,
                                pointSpacing - pointSize, pointSpacing - pointSize);
                        g.setColor(Color.black);
                    }
                }
            }
        }
    }
}
