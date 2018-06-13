package Backend;

public class GameManager {

    private class Player{
        private int color, score;
        public Player(int color){
            this.color = color;
            this.score = 0;
        }
    }

    private Player activePlayer;
    private Player[] players;
    private Board b;

    public GameManager(){
        int size = 3;
        b = new Board(size);
        players = new Player[2];
        players[0] = new Player(1);
        players[1] = new Player(2);
        activePlayer = players[0];
    }

    //TODO:change name of exception
    public void move(int x, int y, Board.DIRECTIONS dir){
        if(!b.hasRemainingPlays()) throw new RuntimeException("No more plays");
        b.addEdge(x,y, dir);
        if(b.isSquareFilled(x,y)){
            b.colorBox(x,y, activePlayer.color);
            this.activePlayer.score++;
        }
        changeTurn();
        b.asciiPrintBoard();
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
}
