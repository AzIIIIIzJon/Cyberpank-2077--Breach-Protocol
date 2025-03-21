import java.util.Random;

public class Matrix{

   public static int size = 5;
    public Symbol[][] matrix;
    public int row;
    public int column;
    public  Random random;
    public boolean isHorizontal;


    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }



    public Random getRandom() {
        return random;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public Matrix(){
     Random rand = new Random();
     matrix = new Symbol[row][column];
     ausfullen();
    }


    public void ausfullen() {
        Symbol[] symbols = Symbol.values();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrix[i][j] = symbols[random.nextInt(symbols.length)];
            }
        }
    }

    public int getSize() {
        return size;
    }

    public Symbol get(int row, int column) {

        if (row < 0 && row >= matrix[row].length  && column < 0  && column >= matrix[column].length) {
            return Symbol.EMPTY;
        } else {
            return matrix[row][column];

        }
    }

//    public Symbol markCurrent() {
//        Symbol[] symbols = Symbol.values();
//        Symbol currentSymboll = matrix[row][column];
//
//        if (currentSymboll == Symbol.EMPTY) {
//            return matrix[row][column + 1] ;
//        } else{
//            for (int i = 0; i < matrix.length; i++) {
//                for (int j = 0; j < matrix[i].length; j++) {
//                    if (matrix[i][j] == Symbol.EMPTY) {
//                        matrix[i][j] = matrix[Symbol.EMPTY][Symbol.EMPTY];
//
//                    }
//                }
//            }
//        }
//
//    }
        public Symbol markCurrent() {

            if (   matrix[row][column] != Symbol.EMPTY) {
                isHorizontal = !isHorizontal;
                matrix[row][column] = Symbol.EMPTY;
                return matrix[row][column];

            }
            return Symbol.EMPTY;
        }


    public void changeCol(int change) {
        if (isHorizontal) {
        int newCol = column + change;
        if (newCol >= 0 && newCol < matrix[row].length
                ) {
            column = newCol;
        }
        }
    }
    public void changeRow(int change) {
        int newRow = row + change;
        if (newRow >= 0 && newRow < matrix.length
              ) {
            column = newRow;
        }
    }






}
