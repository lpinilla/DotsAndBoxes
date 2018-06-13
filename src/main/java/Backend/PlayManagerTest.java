package Backend;

public class PlayManagerTest {

    //TODO: hacer un turn-manager

    public static void move(Board b, int x, int y, Board.DIRECTIONS dir, int color){
        if(!b.hasRemainingPlays()) throw new RuntimeException("No more plays");
        b.addEdge(x,y, dir);
        if(b.isSquareFilled(x,y)){
            b.colorBox(x,y, color);
            System.out.println("point for Slytherin");
        }
    }

    public static void main(String[] args){
        Board b = new Board(3); //tablero de 3x3 puntos (2x2 cuadrados)
        int score1 = 0, score2 = 0;
        //movements
        move(b, 0,0, Board.DIRECTIONS.TOP, 1);
        move(b, 0,0, Board.DIRECTIONS.LEFT, 2);
        move(b, 0,0, Board.DIRECTIONS.BOTTOM, 1);
        move(b, 0,0, Board.DIRECTIONS.RIGHT, 2);
        //llené un cuadrado -yay

    }
}
