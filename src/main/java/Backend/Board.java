package Backend;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Board {

    //maxPlays indica la cantidad máxima de jugadas posibles.
    //currPlay indica cuantas jugadas se hicieron.
    public int size;
    private int maxPlays, currPlay;

    private static int primeNumbers[] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59};
    private static int maxSize = 15;

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
        if (n > maxSize) throw new IllegalArgumentException("N must be less than 15");
        this.size = n;
        this.currPlay = 0;
        this.maxPlays = 2 * ((n * n) + n);
        matrix = new Box[n][n];
        createMatrix(matrix, n);
    }

    public Board(int n, int currPlay) {
        if (n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        if (n > maxSize) throw new IllegalArgumentException("N must be less than 15");
        this.size = n;
        this.currPlay = currPlay;
        this.maxPlays = 2 * ((n * n) + n);
        matrix = new Box[n][n];
    }

    //Agregar una arista dada la posición de la caja y la dirección
    //TODO: cambiar el nombre de las exceptions
    private boolean addEdge(Board b, int x, int y, DIRECTIONS dir, int color) {
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
                    if(hasCompletedSquare(x-1,y)){
                        colorBox(x-1,y,color);
                        return true;
                    }
                }
                break;
            case RIGHT:
                if (b.matrix[x][y].right) throw new RuntimeException("already an edge");
                b.matrix[x][y].right = true;
                if ((y + 1) < size) {
                    b.matrix[x][y + 1].left = true;
                    b.matrix[x][y + 1].nOfEdges++;
                    if(hasCompletedSquare(x,y+1)){
                        colorBox(x,y+1, color);
                        return true;
                    }
                }
                break;
            case BOTTOM:
                if (b.matrix[x][y].bottom) throw new RuntimeException("already an edge");
                b.matrix[x][y].bottom = true;
                if ((x + 1) < size) {
                    b.matrix[x + 1][y].top = true;
                    b.matrix[x + 1][y].nOfEdges++;
                    if(hasCompletedSquare(x+1,y)){
                        colorBox(x+1,y,color);
                        return true;
                    }
                }
                break;
            case LEFT:
                if (b.matrix[x][y].left) throw new RuntimeException("already an edge");
                b.matrix[x][y].left = true;
                if ((y - 1) >= 0) {
                    b.matrix[x][y - 1].right = true;
                    b.matrix[x][y - 1].nOfEdges++;
                    if(hasCompletedSquare(x,y-1)){
                        colorBox(x,y-1,color);
                        return true;
                    }
                }
                break;
        }
        currPlay++;
        b.matrix[x][y].nOfEdges++;
        return false;
    }

    public int makeMove(Board b,int x, int y, DIRECTIONS dir, int color) {
        if (!hasRemainingPlays(b)) {
            return -2;
        }
        boolean consequent = false;
        if(!hasEdge(b, x,y,dir)){
            consequent = addEdge(b, x, y, dir, color);
        }else{//ya hay arista
            return -1;
        }
        //completó algún cuadrado?
        if (hasCompletedSquare(x, y)) {
            colorBox(x, y, color);
            return 1;
        }
        if(consequent) return 1;
        return 0;
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

    public int getBoxColor(int x, int y){
        return matrix[x][y].color;
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

    //método de la vieja heurística
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

    //devuelve el conjunto de todas las posibles jugadas
    public Set<Board> getPossibleMoves(Board b, int color){
        Set<Board> ret = new HashSet<>();
        getPossibleMovesRec(b, ret, color);
        return ret;
    }

    private void getPossibleMovesRec(Board b, Set<Board> set, int color){
        if(!hasRemainingPlays(b)){
            set.add(b);
            //b.asciiPrintBoard();
            return;
        }
        Board aux;
        int moveret;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for(DIRECTIONS dir : DIRECTIONS.values()){
                    if(!hasEdge(b, i,j,dir)){
                        aux = b.cloneBoard();
                        moveret = aux.makeMove(aux, i,j,dir, color);
                        if(moveret == 1){
                            getPossibleMovesRec(aux, set, color);
                        }else if (moveret == 0){
                            set.add(aux);
                        }
                    }
                }
            }
        }
    }

    public void asciiPrintBoard (StringBuffer dotFileConstructor) { //testing purposes
        String[] aux = new String[3];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = "";
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //Top layer
                for (int k = 0; k < 3; k++) {
                    if (matrix[i][j].top) {
                        aux[0] += "1";
                    } else {
                        aux[0] += "0";
                    }
                }
                aux[0] += " ";
                //Middle layer
                if (matrix[i][j].left) {
                    aux[1] += "1";
                } else {
                    aux[1] += "0";
                }
                aux[1] += matrix[i][j].color + "";
                if (matrix[i][j].right) {
                    aux[1] += "1";
                } else {
                    aux[1] += "0";
                }
                aux[1] += " ";
                //Bottom layer
                for (int k = 0; k < 3; k++) {
                    if (matrix[i][j].bottom) {
                        aux[2] += "1";
                    } else {
                        aux[2] += "0";
                    }
                }
                aux[2] += " ";
            }
            for (int t = 0; t < aux.length; t++) {
                System.out.println(aux[t]);
                dotFileConstructor.append(aux[t]);
                dotFileConstructor.append("\\n");
                aux[t] = "";
            }
            dotFileConstructor.append("\\n");
            System.out.println();

        }
        System.out.println("-------------------------");
    }

    public int hashCode() {
        int ret = 1, primeIndex = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ret ^= (int) Math.pow(primeNumbers[primeIndex++ % primeNumbers.length ],
                        boxConfiguration(matrix[i][j]));
            }
        }
        return ret;
    }

    private int boxConfiguration(Box b) {
        if (!b.top && !b.right && !b.bottom && !b.left) return 1; //vacía
        if(b.top && b.right && b.bottom && b.left) return 2;
        //aristas solas
        if (b.top && !b.right && !b.bottom && !b.left) return 3;
        if (!b.top && b.right && !b.bottom && !b.left) return 4;
        if (!b.top && !b.right && b.bottom && !b.left) return 5;
        if (!b.top && !b.right && !b.bottom && b.left) return 6;
        //2 aristas conjugadas con la superior
        if (b.top && b.right && !b.bottom && !b.left) return 7;
        if (b.top && !b.right && b.bottom && !b.left) return 8;
        if (b.top && !b.right && !b.bottom && b.left) return 9;
        //2 aristas conjugadas con derecha
        if (!b.top && b.right && b.bottom && !b.left) return 10;
        if (!b.top && b.right && !b.bottom && b.left) return 11;
        //2 aristas conjugadas con inferior
        if (!b.top && !b.right && b.bottom && b.left) return 12;
        //3 aristas
        if (!b.top && b.right && b.bottom && b.left) return 13;
        if (b.top && !b.right && b.bottom && b.left) return 14;
        if (b.top && b.right && !b.bottom && b.left) return 15;
        return 16;
    }

    @Override
    public boolean equals(Object b) {
        if (b == null) return false;
        if (this == b) return true;
        if (this.getClass() != b.getClass()) return false;
        Board other = (Board) b;
        return hashCode() == other.hashCode();
    }

    public int differenceInBoxesOfColor(int n, int n2){
        int nsum = 0, n2sum =0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(matrix[i][j].color == n){
                    nsum++;
                }else if(matrix[i][j].color == n2){
                    n2sum++;
                }
            }
        }
        return nsum - n2sum;
    }

    public void saveGame(String fileName){
        PrintWriter writer;
        try {
            writer = new PrintWriter("src/main/java/SavedGames/" + fileName + ".txt", "UTF-8");
            writer.println(size);
            writer.println(currPlay);
            StringBuffer fila;
            for (int i = 0; i < size; i++) {
                fila = new StringBuffer();
                for (int j = 0; j < size; j++) {
                    fila.append(boxConfiguration(matrix[i][j]));
                    fila.append("-");
                    fila.append(matrix[i][j].color);
                    fila.append(" ");
                }
                writer.println(fila);
            }
            writer.close();
        }catch (IOException e){
            e.getMessage(); //hacer algo con la exception. (mostrarla en pantalla)?
            System.out.println(e);
        }
    }

    public static Board loadGame(String fileName){
        File f;
        Scanner scanner = null;
        Board b = null;
        try{
            f = new File(fileName + ".txt");
            scanner = new Scanner(f);
        }catch(Exception e){
            e.getMessage();
        }
        try {
            int size = scanner.nextInt(), currPlay = scanner.nextInt();
            b = new Board(size, currPlay);
            String fila;
            String[] configandcolor;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    configandcolor = (scanner.next()).split("-");
                    b.matrix[i][j] = b.createConfig(b, i, j, new Integer(configandcolor[0]),
                            new Integer((configandcolor[1])));
                }
            }
            scanner.close();
        }catch (Exception e){ //si no funciona es porque se modificó el archivo
            e.getMessage();
        }
        return b;
    }

    private Box createConfig(Board b, int i, int j, int config, int color){
        switch(config){
            case 1:
                    return new Box(false, false, false, false, 0, 0);
            case 2:
                    return new Box(true, true, true, true, color, 4);
            case 3:
                    return new Box(false,false ,true, false, 0, 1);
            case 4:
                    return new Box(false, true, false ,false, 0, 1);
            case 5:
                return new Box(false,false ,false, true, 0, 1);
            case 6:
                return new Box(true, true, false ,false, 0, 1);
            case 7:
                return new Box(false,true,true, false,0,2);
            case 8:
                return new Box(false,false,true, true,0,2);
            case 9:
                return new Box(true,false,true,false,0,2);
            case 10:
                return new Box(false, true, false, true, 0, 2);
            case 11:
                return new Box(true, true, false, false, 0, 2);
            case 12:
                return new Box(true, false, false, true, 0,2);
            case 13:
                return new Box(true, true,false ,true, 0, 3);
            case 14:
                return new Box(true, false, true, true, 0, 3);
            case 15:
                return new Box(true, true, true, false, 0, 3);
            case 16:
                return new Box(false, true, true ,true, 0, 3);
        }
        return null;
    }


}
