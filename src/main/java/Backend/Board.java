package Backend;

import java.util.HashSet;
import java.util.Set;

public class Board {

    //maxPlays indica la cantidad máxima de jugadas posibles.
    //currPlay indica cuantas jugadas se hicieron.
    public int size;
    private int maxPlays, currPlay;

    public enum DIRECTIONS  { TOP, RIGHT, BOTTOM, LEFT}

    private class Box{
        int color, nOfEdges;
        Boolean left,right, top, bottom;

        Box(Boolean left, Boolean right,
                   Boolean top, Boolean bottom){
            color = nOfEdges = 0;
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        Box(boolean left, boolean right, boolean top,
            boolean bottom, int color, int nOfEdges){
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
    public Board(int n){
        if(n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        this.size = n;
        this.currPlay = 0;
        this.maxPlays = (n * n) - n;
        matrix = new Box[n][n];
        createMatrix(matrix, n);
    }

    public Board(int n, int currPlay){
        if(n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        this.size = n;
        this.currPlay = currPlay;
        this.maxPlays = 2 * ( n * n + n);
        matrix = new Box[n][n];
    }

    //Agregar una arista dada la posición de la caja y la dirección
    //TODO: cambiar el nombre de las exceptions
    public void addEdge(int x, int y, DIRECTIONS dir){
        if(x < 0 || x > this.matrix.length || y < 0 || y > this.matrix.length){
            throw new IllegalArgumentException();
        }
        switch(dir){
            case TOP:
                if(this.matrix[x][y].top) throw new RuntimeException("already an edge");
                this.matrix[x][y].top = true;
                if( (x-1) >= 0){
                    this.matrix[x-1][y].bottom = true;
                    this.matrix[x-1][y].nOfEdges++;
                }
                break;
            case RIGHT:
                if(this.matrix[x][y].right) throw new RuntimeException("already an edge");
                this.matrix[x][y].right = true;
                if( (y+1) < size){
                    this.matrix[x][y+1].left = true;
                    this.matrix[x][y+1].nOfEdges++;
                }
                break;
            case BOTTOM:
                if(this.matrix[x][y].bottom) throw new RuntimeException("already an edge");
                this.matrix[x][y].bottom = true;
                if( (x+1) < size){
                    this.matrix[x+1][y].top = true;
                    this.matrix[x+1][y].nOfEdges++;
                }
                break;
            case LEFT:
                if(this.matrix[x][y].left) throw new RuntimeException("already an edge");
                this.matrix[x][y].left = true;
                if( (y-1) >= 0){
                    this.matrix[x][y-1].right = true;
                    this.matrix[x][y-1].nOfEdges++;
                }
                break;
        }
        currPlay++;
        this.matrix[x][y].nOfEdges++;
    }

    public boolean makeMove(int x, int y, DIRECTIONS dir, int color){
        if(!hasRemainingPlays()) throw new RuntimeException("No more plays");
        addEdge(x,y, dir);
        if(isSquareFilled(x,y)){
            colorBox(x,y, color);
            return true;
        }
        return false;
    }

    //Metodo para saber si todavía hay jugadas por hacer.
    public boolean hasRemainingPlays(){
        return currPlay < maxPlays;
    }

    //Método para saber si se completó un cuadrado.
    public boolean isSquareFilled(int x, int y){
        return matrix[x][y].top && matrix[x][y].right && matrix[x][y].bottom && matrix[x][y].left;
    }

    //Método para colorear el cuadrado+
    public void colorBox(int x, int y, int color){
        matrix[x][y].color = color;
    }

    //Creando la matriz de tipo Box.
    private void createMatrix(Box[][] m, int n){
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                m[i][j] = new Box(false, false, false, false);
            }
        }
    }

    public boolean hasEdge(int x, int y, DIRECTIONS dir){
        switch(dir){
            case TOP:
                return matrix[x][y].top;
            case LEFT:
                return matrix[x][y].left;
            case RIGHT:
                return matrix[x][y].right;
            case BOTTOM:
                return matrix[x][y].bottom;
        }
        return false;
    }


    public int numberOfCapturableBoxes(){
        int ret = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(matrix[i][j].nOfEdges == 3){
                    ret++;
                }
                //Si tengo 2 o más vecinos con #e == 3 -> soy capturable
                if(matrix[i][j].nOfEdges == 1){
                    if(degree3Neighbors(i,j) > 1){
                        ret++;
                    }
                }
            }
        }
        return ret;
    }

    private int degree3Neighbors(int x, int y){
        int count = 0;
        if( (x-1) >= 0 && matrix[x-1][y].nOfEdges == 3){
                count++;
        }
        if( (x+1) < size && matrix[x+1][y].nOfEdges == 3){
            count++;
        }
        if( (y-1) >= 0 && matrix[x][y-1].nOfEdges == 3){
            count++;
        }
        if( (y+1) < size && matrix[x][y+1].nOfEdges == 3){
            count++;
        }
        return count;
    }

    private Box[][] getMatrix(){
        return this.matrix;
    }

    //tal vez pueda usar esto para loadGame
    private void setMatrix(Box[][] m){
        this.matrix = m;
    }

    private Board cloneBoard(){
        Board aux = new Board(this.size, currPlay);
        Box box = null;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                box = this.matrix[i][j];
                aux.matrix[i][j] = new Box(box.left, box.right,
                        box.top, box.bottom, box.color, box.nOfEdges);
                ;
            }
        }
        return aux;
    }

    //Devuelve un conjunto con todas las posibles jugadas
    public Set<Board> getPossibleMoves(Board b, int color){
        Set<Board> ret = new HashSet<>();
        Board aux;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for(DIRECTIONS dir: DIRECTIONS.values()){
                    if(!hasEdge(i,j,dir)){
                        aux = this.cloneBoard(); //se puede optimizar acá
                        /*if(aux.makeMove(i,j,dir, color)){
                            return getPossibleMoves(aux, color);
                        }*/
                        aux.makeMove(i,j,dir, color);
                        ret.add(aux);
                    }
                }
            }
        }
        return ret;
    }

    public void asciiPrintBoard(){
        String[] aux = new String[3];
        for (int i = 0; i < aux.length; i++) {
            aux[i] = "";
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //Top layer
                for (int k = 0; k < 3; k++) {
                    if(matrix[i][j].top){
                        aux[0] += "1 ";
                    }else{
                        aux[0] += "0 ";
                    }
                }
                aux[0] += " ";
                //Middle layer
                if(matrix[i][j].left){
                    aux[1] += "1 ";
                }else{
                    aux[1] += "0 ";
                }
                aux[1] += matrix[i][j].color + " ";
                if(matrix[i][j].right){
                    aux[1] += "1 ";
                }else{
                    aux[1] += "0 ";
                }
                aux[1] += " ";
                //Bottom layer
                for (int k = 0; k < 3; k++) {
                    if(matrix[i][j].bottom){
                        aux[2] += "1 ";
                    }else{
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

    //TODO:MEJORAR
    /*Por ahora solo hago lo necesario para que funcione con los tests
    **que quiero pero después habría que hacer una buena función.
    * Ideas: calcular el número de aristas por fila y elevar ese número a un primo.
    *           +es fácil de chequear
    *           -hay que encontrar una vez los primos y después elevar los numeros
             Convertir la cantidad de aristas en un string
                +fácil de hacer
                -más costoso para chequear

       Ver Zoobrist Hashing
     */
    public int hashCode(){
        int[] exponents = new int[size];
        int sum;
        for (int i = 0; i < size; i++) {
            sum = 0;
            for (int j = 0; j < size; j++) {
                sum += matrix[i][j].nOfEdges;
            }
            exponents[i]=sum;
        }
        if(size == 2){
            return (int) Math.pow(2, exponents[0]) * (int) Math.pow(3, exponents[1]);
        }
        return (int) Math.pow(2, exponents[0]) * (int) Math.pow(3, exponents[1]) * (int) Math.pow(5, exponents[2]);
    }

    //Métodos que faltan de juego: saveBoard, loadBoard
    //Métodos que faltan de IA: existChain, createChain
}
