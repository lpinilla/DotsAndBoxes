package Backend;

public class GameManager {

    private class Player{
        private int color, score;
        Player(int color){
            this.color = color;
            this.score = 0;
        }
    }

    enum GAME_STATUS { PLAYING, OVER}

    private Player activePlayer;
    private Player[] players;
    private Board b;
    private  GAME_STATUS gameStatus;

    public GameManager(){
        int size = 3;
        b = new Board(size);
        players = new Player[2];
        players[0] = new Player(1);
        players[1] = new Player(2);
        activePlayer = players[0];
        gameStatus = GAME_STATUS.PLAYING;
    }

    //TODO:change name of exception
    public void move(int x, int y, Board.DIRECTIONS dir){
        if(b.makeMove(x,y, dir, activePlayer.color)){
            activePlayer.score++;
        }
        changeTurn();
        b.asciiPrintBoard(); //temporary
        checkIfIsGameOver();
    }

    private void checkIfIsGameOver(){
        if(!b.hasRemainingPlays()){
            gameStatus = GAME_STATUS.OVER;
            gameOver();
        }
    }

    public void gameOver(){
        //do something
    }

    public Player getCurrentPlayer(){
        return activePlayer;
    }

    public void changeTurn(){
        if(activePlayer == players[0]){
            activePlayer = players[1];
        }else{
            activePlayer = players[0];
        }
    }

    public Board getBoard(){
        return this.b;
    }
}
