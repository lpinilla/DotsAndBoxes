package Backend;

import java.util.HashSet;
import java.util.Set;

public class Board { //recordar: sin color == 0

    //maxPlays indica la cantidad máxima de jugadas posibles.
    //currPlay indica cuantas jugadas se hicieron.
    public int size;
    private int maxPlays, currPlay;
    private static int primeNumbers[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};

    public enum DIRECTIONS {TOP, RIGHT, BOTTOM, LEFT}

    private class Box {
        int color, nOfEdges;
        Boolean left, right, top, bottom;

        Box(Boolean left, Boolean right,
            Boolean top, Boolean bottom) {
            color = nOfEdges = 0;
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;

        }

        Box(boolean left, boolean right, boolean top,
            boolean bottom, int color, int nOfEdges) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
            this.color = color;
            this.nOfEdges = nOfEdges;
        }
    }

    private Box[][] matrix;

    //constructor para tablero vacío, n indica la cantidad de cuadrados
    public Board(int n) {
        if (n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        this.size = n;
        this.currPlay = 0;
        this.maxPlays = 2 * ((n * n) + n);
        matrix = new Box[n][n];
        createMatrix(matrix, n);
    }

    public Board(int n, int currPlay) {
        if (n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        this.size = n;
        this.currPlay = currPlay;
        this.maxPlays = 2 * ((n * n) + n);
        matrix = new Box[n][n];
    }

    //Agregar una arista dada la posición de la caja y la dirección
    //TODO: cambiar el nombre de las exceptions
    private void addEdge(Board b, int x, int y, DIRECTIONS dir) {
        if (x < 0 || x > b.getMatrix().length || y < 0 || y > b.matrix.length) {
            throw new IllegalArgumentException();
        }
        switch (dir) {
            case TOP:
                if (b.matrix[x][y].top) throw new RuntimeException("already an edge");
                b.matrix[x][y].top = true;
                if ((x - 1) >= 0) {
                    b.matrix[x - 1][y].bottom = true;
                    b.matrix[x - 1][y].nOfEdges++;
                }
                break;
            case RIGHT:
                if (b.matrix[x][y].right) throw new RuntimeException("already an edge");
                b.matrix[x][y].right = true;
                if ((y + 1) < size) {
                    b.matrix[x][y + 1].left = true;
                    b.matrix[x][y + 1].nOfEdges++;
                }
                break;
            case BOTTOM:
                if (b.matrix[x][y].bottom) throw new RuntimeException("already an edge");
                b.matrix[x][y].bottom = true;
                if ((x + 1) < size) {
                    b.matrix[x + 1][y].top = true;
                    b.matrix[x + 1][y].nOfEdges++;
                }
                break;
            case LEFT:
                if (b.matrix[x][y].left) throw new RuntimeException("already an edge");
                b.matrix[x][y].left = true;
                if ((y - 1) >= 0) {
                    b.matrix[x][y - 1].right = true;
                    b.matrix[x][y - 1].nOfEdges++;
                }
                break;
        }
        currPlay++;
        b.matrix[x][y].nOfEdges++;
    }

    private void removeEdge(int x, int y, DIRECTIONS dir) {
        if (hasEdge(this,x, y, dir)) {
            switch (dir) {
                case TOP:
                    matrix[x][y].top = false;
                    if ((x - 1) >= 0) {
                        this.matrix[x - 1][y].bottom = false;
                        if(matrix[x-1][y].nOfEdges == 4){
                            matrix[x-1][y].color = 0;
                        }
                        this.matrix[x - 1][y].nOfEdges--;
                    }
                    break;
                case RIGHT:
                    matrix[x][y].right = false;
                    if ((y + 1) < size) {
                        this.matrix[x][y + 1].left = false;
                        if(matrix[x][y + 1].nOfEdges == 4){
                            matrix[x][y + 1].color = 0;
                        }
                        this.matrix[x][y + 1].nOfEdges--;
                    }
                    break;
                case BOTTOM:
                    matrix[x][y].bottom = false;
                    if ((x + 1) < size) {
                        this.matrix[x + 1][y].top = false;
                        if(matrix[x + 1][y].nOfEdges == 4){
                            matrix[x + 1][y].color = 0;
                        }
                        this.matrix[x + 1][y].nOfEdges++;
                    }
                    break;
                case LEFT:
                    matrix[x][y].left = false;
                    if ((y - 1) >= 0) {
                        this.matrix[x][y - 1].right = true;
                        if(matrix[x][y - 1].nOfEdges == 4){
                            matrix[x][y-1].color = 0;
                        }
                        this.matrix[x][y - 1].nOfEdges--;
                    }
                    break;
            }
            if (matrix[x][y].nOfEdges == 4) {
                matrix[x][y].color = 0;
            }
            matrix[x][y].nOfEdges--;
        }
    }

    public boolean makeMove(Board b,int x, int y, DIRECTIONS dir, int color) {
        //if(!hasRemainingPlays()) throw new RuntimeException("No more plays");
        //estaría bueno que retorne otra cosa para distinguir si se acabaron las jugadas o no completó
        if (!hasRemainingPlays(b)) {
            return false;
        }
        addEdge(b, x, y, dir);
        //completó algún cuadrado?
        if (hasCompletedSquare(x, y)) {
            colorBox(x, y, color);
            return true;
        }
        if(checkConsecutiveSquares(x,y,color)){
            return true;
        }
        return false;
    }

    private boolean checkConsecutiveSquares(int x, int y, int color){
        if( x-1 >= 0){
            if(hasCompletedSquare(x-1,y)){
                colorBox(x-1,y,color);
                return true;
            }
        }
        if(x+1 < size){
            if(hasCompletedSquare(x+1, y)){
                colorBox(x+1,y,color);
                return true;
            }
        }
        if(y-1 >= 0){
            if(hasCompletedSquare(x,y-1)){
                colorBox(x,y-1, color);
                return true;
            }
        }
        if(y+1 < size){
            if(hasCompletedSquare(x,y+1)){
                colorBox(x,y+1, color);
                return true;
            }
        }
        return false;
    }

    //Metodo para saber si todavía hay jugadas por hacer.
    public boolean hasRemainingPlays(Board b) {
        return b.currPlay < b.maxPlays;
    }

    //Método para saber si se completó un cuadrado.
    public boolean hasCompletedSquare(int x, int y) {
        return matrix[x][y].top && matrix[x][y].right &&
                matrix[x][y].bottom && matrix[x][y].left && matrix[x][y].color == 0;
    }

    //Método para colorear el cuadrado+
    public void colorBox(int x, int y, int color) {
        if(matrix[x][y].color != 0) throw new IllegalArgumentException("Already painted!");
        matrix[x][y].color = color;
    }

    //Creando la matriz de tipo Box.
    private void createMatrix(Box[][] m, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = new Box(false, false, false, false);
            }
        }
    }

    public boolean hasEdge(Board b, int x, int y, DIRECTIONS dir) {
        switch (dir) {
            case TOP:
                return b.getMatrix()[x][y].top;
            case LEFT:
                return b.getMatrix()[x][y].left;
            case RIGHT:
                return b.getMatrix()[x][y].right;
            case BOTTOM:
                return b.getMatrix()[x][y].bottom;
        }
        return false;
    }


    public int numberOfCapturableBoxes() {
        int ret = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j].nOfEdges == 3) {
                    ret++;
                }
                //Si tengo 2 o más vecinos con #e == 3 -> soy capturable
                if (matrix[i][j].nOfEdges == 1) {
                    if (degree3Neighbors(i, j) > 1) {
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    private int degree3Neighbors(int x, int y) {
        int count = 0;
        if ((x - 1) >= 0 && matrix[x - 1][y].nOfEdges == 3) {
            count++;
        }
        if ((x + 1) < size && matrix[x + 1][y].nOfEdges == 3) {
            count++;
        }
        if ((y - 1) >= 0 && matrix[x][y - 1].nOfEdges == 3) {
            count++;
        }
        if ((y + 1) < size && matrix[x][y + 1].nOfEdges == 3) {
            count++;
        }
        return count;
    }

    private Box[][] getMatrix() {
        return this.matrix;
    }

    //tal vez pueda usar esto para loadGame
    private void setMatrix(Box[][] m) {
        this.matrix = m;
    }

    public int getCurrPlay() {
        return this.currPlay;
    }

    private Board cloneBoard() {
        Board aux = new Board(this.size, currPlay);
        Box box;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                box = this.matrix[i][j];
                aux.matrix[i][j] = new Box(box.left, box.right,
                        box.top, box.bottom, box.color, box.nOfEdges);
            }
        }
        return aux;
    }

    //Devuelve un conjunto con todas las posibles jugadas
    public Set<Board> getPossibleMoves(Board b, int color) { //faltan ver jugadas consecutivas
        if (!b.hasRemainingPlays(b)) {
            return null;
        }
        Set<Board> ret = new HashSet<>(), consequentMoves;
        Board aux;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (DIRECTIONS dir : DIRECTIONS.values()) {
                    if (!hasEdge(b,i, j, dir)) {
                        aux = b.cloneBoard();
                        if (aux.makeMove(aux, i, j, dir, color)) {
                            //ret.add(aux);
                            consequentMoves = getPossibleMoves(aux, color);
                            if(consequentMoves != null){
                                System.out.println(consequentMoves.size());
                                ret.addAll(consequentMoves);
                                //return ret;
                            }
                        } else {
                            ret.add(aux);
                        }

                        /*aux.makeMove(i,j,dir, color);
                        ret.add(aux);*/
                    }
                }
            }
        }
        return ret;
    }

    public void asciiPrintBoard () { //testing purposes
        String[] aux = new String[3];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = "";
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //Top layer
                for (int k = 0; k < 3; k++) {
                    if (matrix[i][j].top) {
                        aux[0] += "1 ";
                    } else {
                        aux[0] += "0 ";
                    }
                }
                aux[0] += " ";
                //Middle layer
                if (matrix[i][j].left) {
                    aux[1] += "1 ";
                } else {
                    aux[1] += "0 ";
                }
                aux[1] += matrix[i][j].color + " ";
                if (matrix[i][j].right) {
                    aux[1] += "1 ";
                } else {
                    aux[1] += "0 ";
                }
                aux[1] += " ";
                //Bottom layer
                for (int k = 0; k < 3; k++) {
                    if (matrix[i][j].bottom) {
                        aux[2] += "1 ";
                    } else {
                        aux[2] += "0 ";
                    }
                }
                aux[2] += " ";
            }
            for (int t = 0; t < aux.length; t++) {
                System.out.println(aux[t]);
                aux[t] = "";
            }
            System.out.println();

        }
        System.out.println("-------------------------");
    }

    public int hashCode() {
        int ret = 1, primeIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret ^= (int) Math.pow(primeNumbers[primeIndex++],
                        boxConfiguration(matrix[i][j]));
            }
        }
        return ret;
    }

    private int boxConfiguration(Box b) {
        //aristas solas
        if (b.top && !b.right && !b.bottom && !b.left) return 1;
        if (!b.top && b.right && !b.bottom && !b.left) return 2;
        if (!b.top && !b.right && b.bottom && !b.left) return 3;
        if (!b.top && !b.right && !b.bottom && b.left) return 4;
        //2 aristas conjugadas con la superior
        if (b.top && b.right && !b.bottom && !b.left) return 5;
        if (b.top && !b.right && b.bottom && !b.left) return 6;
        if (b.top && !b.right && !b.bottom && b.left) return 7;
        //2 aristas conjugadas con derecha
        if (!b.top && b.right && b.bottom && !b.left) return 8;
        if (!b.top && b.right && !b.bottom && b.left) return 9;
        //2 aristas conjugadas con inferior
        if (!b.top && !b.right && b.bottom && b.left) return 10;
        //3 aristas
        if (!b.top && b.right && b.bottom && b.left) return 11;
        if (b.top && !b.right && b.bottom && b.left) return 12;
        if (b.top && b.right && !b.bottom && b.left) return 13;
        if (b.top && b.right && b.bottom && !b.left) return 14;
        if (!b.top && !b.right && !b.bottom && !b.left) return 15;
        return 16; //ultimo estado, esta lleno
    }

    @Override
    public boolean equals(Object b) {
        if (b == null) return false;
        if (this == b) return true;
        if (this.getClass() != b.getClass()) return false;
        Board other = (Board) b;
        return hashCode() == other.hashCode();
    }

        //Métodos que faltan de juego: saveBoard, loadBoard
        //Métodos que faltan de IA: existChain, createChain ?
}
