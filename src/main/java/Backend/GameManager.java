package Backend;

public class GameManager {

    private class Player{
        private int color, score;

        Player(int color){
            this.color = color;
            this.score = 0;
        }
    }

    public enum GAME_STATUS { PLAYING, OVER}
    public enum GAME_MODE { HVSH, IAVSH, HVSAI, AIVSAI};

    private Player activePlayer;
    private Player[] players; //siempre el humano va a ser el 0
    private Board b;
    public GAME_STATUS gameStatus;
    private IA ia;
    public boolean playerTurn;

    public GameManager(int size, GAME_MODE game_mode){ //no IA
        b = new Board(size);
        players = new Player[2];
        players[0] = new Player(1);
        players[1] = new Player(2);
        activePlayer = players[0]; //esto puede variar
        gameStatus = GAME_STATUS.PLAYING;
    }

    public GameManager(int size, IA.Mode iaMode, int maxDepth,
                       long totalTime, boolean prune, GAME_MODE game_mode){
        b = new Board(size);
        players = new Player[] {new Player(1), new Player(2)};
        if(game_mode == GAME_MODE.HVSH || game_mode == GAME_MODE.HVSAI) {
            activePlayer = players[0]; //esto puede variar
            playerTurn = true;
        }else{
            activePlayer = players[1];
            playerTurn = false;
        }
        gameStatus = GAME_STATUS.PLAYING;
        ia = new IA(getBoard() ,iaMode,maxDepth,totalTime,2,1,prune);
    }

    public void move(int x, int y, Board.DIRECTIONS dir){
        int moveVal = b.makeMove(b, x,y, dir, activePlayer.color);
        if( moveVal == 1){
            activePlayer.score++;
        }else if(moveVal == 0){
            changeTurn();
        }
        b.asciiPrintBoard(); //temporary
        checkIfIsGameOver();
    }

    private void checkIfIsGameOver(){
        if(!b.hasRemainingPlays(b)){
            gameStatus = GAME_STATUS.OVER;
            gameOver();
        }
    }

    public void gameOver(){
        //do something
    }

    public int whoWins(){
        if(players[0].score > players[1].score){
            return 1;
        } else if(players[0].score < players[1].score){
            return 2;
        }
        return 0;
    }

    public Player getCurrentPlayer(){
        return activePlayer;
    }

    public void changeTurn(){
        if(activePlayer == players[0]){
            activePlayer = players[1];
            playerTurn = false;
        }else{
            activePlayer = players[0];
            playerTurn = true;
        }
    }

    public Board getBoard(){
        return this.b;
    }

    public int whoIsActivePlayer(){
        if(activePlayer == players[0]){
            return 1;
        }
        return 2;
    }

    public Board aiMove(){
        System.out.println("entering IA");
        this.b = ia.miniMax();
        return b;
    }

    public void setBoard(Board b){
        this.b = b;
    }
}
