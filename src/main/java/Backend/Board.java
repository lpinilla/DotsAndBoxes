package Backend;

public class Board {

    //maxPlays indica la cantidad máxima de jugadas posibles.
    //currPlay indica cuantas jugadas se hicieron.
    private int size, maxPlays, currPlay;

    public enum DIRECTIONS  { TOP, RIGHT, BOTTOM, LEFT}

    private class Box{
        int color;
        Boolean left,right, top, bottom;

        Box(Boolean left, Boolean right,
                   Boolean top, Boolean bottom){
            color = 0;
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }

    private Box[][] matrix;

    //constructor para tablero vacío, n indica la cantidad de puntos
    public Board(int n){
        if(n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        this.size = n;
        this.currPlay = 0;
        this.maxPlays = (n * n) - n;
        matrix = new Box[n][n];
        createMatrix(matrix, n);
    }

    //Agregar una arista dada la posición de la caja y la dirección
    //TODO: cambiar el nombre de las exceptions
    public void addEdge(int x, int y, DIRECTIONS dir){
        if(x < 0 || x > matrix.length || y < 0 || y > matrix.length){
            throw new IllegalArgumentException();
        }
        switch(dir){
            case TOP:
                if(matrix[x][y].top) throw new RuntimeException("already an edge");
                matrix[x][y].top = true;
                if( (x-1) >= 0){
                    matrix[x-1][y].bottom = true;
                }
                break;
            case RIGHT:
                if(matrix[x][y].right) throw new RuntimeException("already an edge");
                matrix[x][y].right = true;
                if( (y+1) < size){
                    matrix[x][y+1].left = true;
                }
                break;
            case BOTTOM:
                if(matrix[x][y].bottom) throw new RuntimeException("already an edge");
                matrix[x][y].bottom = true;
                if( (x+1) < size){
                    matrix[x+1][y].top = true;
                }
                break;
            case LEFT:
                if(matrix[x][y].left) throw new RuntimeException("already an edge");
                matrix[x][y].left = true;
                if( (y-1) >= 0){
                    matrix[x][y-1].right = true;
                }
                break;
        }
        currPlay++;
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

    //Métodos que faltan de juego: hasEdge(x,y)?, saveBoard, loadBoard, hashCode
    //Métodos que faltan de IA: existChain, createChain, Heuristic
}
