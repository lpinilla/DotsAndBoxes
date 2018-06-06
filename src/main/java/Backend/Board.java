package Backend;

public class Board {

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

    public Board(int n){
        if(n <= 0) throw new IllegalArgumentException("Zero or negative values not allowed");
        matrix = new Box[n][n];
        createMatrix(matrix, n);
    }

    //Add edge to square in clockwise direction

    public void addEdge(int x, int y, int direction){
        if(x < 0 || x > matrix.length || y < 0 || y > matrix.length){
            throw new IllegalArgumentException();
        }
        switch(direction){
            case 1:
                matrix[x][y].top = true;
                break;
            case 2:
                matrix[x][y].right = true;
                break;
            case 3:
                matrix[x][y].bottom = true;
                break;
            case 4:
                matrix[x][y].left = true;
                break;
        }
    }

    /*Creando la matriz de tipo Box. Para ahorrar memoria, vamos a compartir los lados de tipo
    **Boolean, lo que también tiene la ventaja de informar a la caja adyacente correspondiente
    **que su lado también fue afectado si se agrega una arista.*/
    private void createMatrix(Box[][] m, int n){
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++) {
                if(i == 0 || i == n-1 || j == 0 || j == n-1) {
                    m[i][j] = new Box(false, false, false, false);
                }else {
                    m[i][j] = new Box(m[i][j-1].right, false, m[i-1][j].bottom, false);
                }
            }
        }
    }

    //Métodos que faltan: existChain, createChain, Heuristic, saveBoard, hashCode
}
