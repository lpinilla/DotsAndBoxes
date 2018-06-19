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


    static int[] startingPos = new int[] { 20,20};
    static int pointSpacing = 50;
    static int nOfSave = 0;
    static int boardSize;
    static  GameManager gm;
    static Board backBoard;
    static GameManager.GAME_MODE game_mode;
    static JPanel infoContainer;


    //GameUI2(new Integer(args[0]), game_mode, iamode, maxDepth, maxTime, pruneActive);
    public GameUI2(int size, GameManager.GAME_MODE game_mode, IA.Mode mode, int maxDepth, long maxtime,
                   boolean isPruneActive){ //se mira no se toca
        boardSize = size;
        if(game_mode == GameManager.GAME_MODE.HVSH){
            gm = new GameManager(size -1, game_mode);
        }else {
            gm = new GameManager(size - 1, mode, maxDepth, maxtime, isPruneActive, game_mode);
        }
        backBoard = gm.getBoard();

        //UI STUFF
        label = new JLabel();
        if(game_mode != GameManager.GAME_MODE.AIVSAI) {
            label.setText(gm.whoIsActivePlayer());
        }
        String[] aux = new String[size -1];
        for (int i = 0; i < size-1; i++) {
            aux[i] = Integer.toString(i);
        }
        xCoor = new JComboBox();
        yCoor = new JComboBox();
        dir = new JComboBox();
        undoButton = new JButton();
        playButton = new JButton();
        populateComboBox(xCoor, aux);
        populateComboBox(yCoor, aux);
        populateComboBox(dir, new String[] {"TOP", "RIGHT", "BOTTOM", "LEFT"});
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
        //label, saveGame and DOT File
        JPanel firstPanel = new JPanel();
        firstPanel.add(label, BorderLayout.LINE_START);
        JButton saveGame = new JButton();
        JButton saveDot = new JButton();
        saveGame.setMinimumSize(new Dimension(150,20));
        saveGame.setPreferredSize(new Dimension(150,20));
        saveGame.setText("Save Game");
        //saveGame.repaint();
        saveDot.setMinimumSize(new Dimension(150,20));
        saveDot.setPreferredSize(new Dimension(150,20));
        saveDot.setText("Save Dot File");
        //saveGame.repaint();
        firstPanel.add(saveGame, BorderLayout.CENTER);
        firstPanel.add(saveDot, BorderLayout.PAGE_END);
        infoContainer.add(firstPanel, BorderLayout.PAGE_START);
        label.setHorizontalAlignment(0);
        label.repaint();

        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                backBoard.saveGame("Save" + Integer.toString(nOfSave++));
            }
        });

        saveDot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gm.ia.saveDOTFile();
            }
        });

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
                        if(game_mode == GameManager.GAME_MODE.HVSH){
                            showLabel();
                        }
                        gm.isAiPlaying = false;
                        //rePaintBoard();
                        if(gm.gameStatus == GameManager.GAME_STATUS.OVER) {
                            showWinner();
                        }
                    }
                }
            }
        });
    }

    //argumentos: size ; gameMode ; aiMode; depth/time, prune, load
    public static void main(String[] args){ //length = 6
        switch (args[1]){
            case "0":
                game_mode = GameManager.GAME_MODE.HVSH;
                break;
            case "1":
                game_mode = GameManager.GAME_MODE.AIVSH;
                break;
            case "2":
                game_mode = GameManager.GAME_MODE.HVSAI;
                break;
            case "3":
                game_mode = GameManager.GAME_MODE.AIVSAI;
                break;
        }
        IA.Mode iamode = null;
        long maxTime = 0;
        int maxDepth = 0;
        switch (args[2]){
            case"time":
                iamode = IA.Mode.TIME;
                maxTime = new Long(args[3]);
                break;
            case "depth":
                iamode = IA.Mode.DEPTH;
                maxDepth = new Integer(args[3]);
                break;
        }
        boolean pruneActive = false;
        if(args[4].equals("on")){
            pruneActive = true;
        }else if(args[4].equals("off")){
            pruneActive = false;
        }
        JFrame frame = new JFrame("Dots and Boxes");
        GameUI2 ui = new GameUI2(new Integer(args[0]), game_mode, iamode, maxDepth, maxTime, pruneActive);
        frame.getContentPane().add(ui);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(820,900));
        frame.setPreferredSize(new Dimension(820,900));
        frame.setLocation(650,100);
        //frame.pack(); //me cambia to-do
        frame.setVisible(true);
        frame.add(infoContainer, BorderLayout.NORTH);
        infoContainer.repaint();
        wantToPlay();
    }

    public static void wantToPlay(){
        while(gm.gameStatus != GameManager.GAME_STATUS.OVER){
            //System.out.println("playing");
            //System.out.println(gm.gameStatus);
            if(game_mode == GameManager.GAME_MODE.HVSAI ||
                    game_mode == GameManager.GAME_MODE.AIVSH) {
                showLabel();
                if (gm.playerTurn) { //si es turno de human
                    //wait for click
                    while(gm.playerTurn){ //NECESITA HABER ALGO DENTRO DEL LOOP
                        System.out.println("waiting for click"); //NO TOCAR, NO ES PARA DEBUGGEAR

                    }
                } else if(!gm.isAiPlaying){
                    backBoard = gm.aiMove();
                    rePaintBoard();
                }
            }
            if(game_mode == GameManager.GAME_MODE.AIVSAI){
                backBoard = gm.ai2Move();
                rePaintBoard();
            }
        }
        showWinner();
    }

    public static boolean play(int x, int y, Board.DIRECTIONS direction) {
        if (!gm.move(x, y, direction)) {
            JOptionPane.showMessageDialog(null, "Already an edge");
            return false;
        }
        rePaintBoard();
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

    private static void showWinner(){
        int winner;
        if(game_mode == GameManager.GAME_MODE.HVSH) {
            winner = gm.whoWins();
        }else{
            winner = gm.whoWins2();
        }
        if (winner != 0) {
            JOptionPane.showMessageDialog(null, "Player " + winner + " Wins!");
        } else {
            JOptionPane.showMessageDialog(null, "It's a Tie!");
        }
    }

    private static void showLabel(){
        label.setText(gm.whoIsActivePlayer());
        label.repaint();
    }
}
