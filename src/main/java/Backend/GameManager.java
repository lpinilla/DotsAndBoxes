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

    private Player activePlayer;
    private Player[] players;
    private Board b;
    public GAME_STATUS gameStatus;

    public GameManager(int size){
        b = new Board(size);
        players = new Player[2];
        players[0] = new Player(1);
        players[1] = new Player(2);
        activePlayer = players[0];
        gameStatus = GAME_STATUS.PLAYING;
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
        }else{
            activePlayer = players[0];
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
}
