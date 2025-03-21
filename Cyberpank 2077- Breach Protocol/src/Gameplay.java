import java.awt.event.KeyEvent;
import java.util.Random;

public class Gameplay {
    public static final int BUFFER_LIMIT = 5;
    public Matrix matrix;
    public Sequence[] sequences;
    public int[] offset;
    public Sequence buffer;
    private final Game game;
    private Controls controls;






    public Gameplay(Game game, Controls controls) {
       this.buffer = new Sequence();
       this.game = game;
        this.controls = controls;
        this.offset = new int[BUFFER_LIMIT];
        this.sequences = new Sequence[BUFFER_LIMIT];
        this.matrix = new Matrix();


        generateSequences();
        controls.clear();

        this.sequences = generateSequences();
        this.offset = new int[sequences.length];


    }


    public Game getGame() {
        return game;
    }

    public Controls getControls() {
        return controls;
    }

    public int[] getOffset() {
        return offset;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public Sequence getBuffer() {
        return buffer;
    }

    public Sequence[] getSequences() {
        return sequences;
    }

    /***********************************************************/

    private Sequence[] generateSequences() {


//        private Sequence[] generateSequences() {
//            Sequence longSequence = new Sequence();
//            Random random = new Random();
//
//            int x = 0, y = 0;
//            boolean horizontal = true;
//
//            for (int i = 0; i < 25; i++) {
//                longSequence.add(matrix.get(x, y));
//                if (horizontal) {
//                    y = (y + 1) % matrix.getSize();
//                } else {
//                    x = (x + 1) % matrix.getSize();
//                }
//                horizontal = !horizontal;
//            }
//
//            Sequence[] sequences = new Sequence[random.nextInt(3) + 3];
//            for (int i = 0; i < sequences.length; i++) {
//                int start = random.nextInt(longSequence.size() - BUFFER_LIMIT);
//                sequences[i] = longSequence.subList(start, start + BUFFER_LIMIT);
//            }
//            return sequences;
//        }

        Sequence longSequence = new Sequence();
        int currentRow = 0;
        int currentCol = 0;
      Random    random = new Random();

        // Erzeugen einer langen Sequenz durch abwechselndes Wählen aus Zeilen und Spalten
        for (int i = 0; i < 25; i++) {
            longSequence.add(matrix.get(currentRow, currentCol));
            if (i % 2 == 0) {
                // Wählen Sie ein Symbol aus der aktuellen Zeile
                currentCol = (currentCol + 1) % Matrix.size;
            } else {
                // Wählen Sie ein Symbol aus der aktuellen Spalte
                currentRow = (currentRow + 1) % Matrix.size;
            }
        }

        // Anzahl der Zielsequenzen zwischen 3 und 5
        int numSequences = random.nextInt(3) + 3;
        Sequence[] generatedSequences = new Sequence[numSequences];

        // Erzeugen von 3-5 Teilsequenzen aus der langen Sequenz
        for (int i = 0; i < numSequences; i++) {
            int start = random.nextInt(longSequence.size() - 5);
            int end = start + random.nextInt(3) + 3; // Länge der Teilsequenz: 3-5
            generatedSequences[i] = longSequence.subList(start, end);
        }

        return generatedSequences;









        /*
         * EXAMPLE:
         *
         * SOURCE   | A0 BD 55 1C 1C 55 A0 88 |
         * ---------+-------------------------+---------
         * TARGET 1 | A0 BD                   | subList(0,1)
         * TARGET 2 |          1C 1C 55       | subList(3,5)
         * TARGET 3 |             1C 55 A0 88 | subList(4,7)
         *
         */
    }




    public void handleInputs() {





            for (int key : controls.keyPressed()) {
                if (key == KeyEvent.VK_ESCAPE) {
                    game.gameOver();
                } else if (key == KeyEvent.VK_ENTER) {
                    game.endRoundEarly();
                } else if (key == KeyEvent.VK_SPACE) {
                    if (buffer.size() < BUFFER_LIMIT) {
                        buffer.add(matrix.markCurrent());
                        adjustOffsets();
                        if (buffer.size() == BUFFER_LIMIT) {
                            game.endRoundEarly();
                        }
                    }
                } else if (key == KeyEvent.VK_LEFT) {
                    matrix.changeCol(-1);
                } else if (key == KeyEvent.VK_RIGHT) {
                    matrix.changeCol(1);
                } else if (key == KeyEvent.VK_UP) {
                    matrix.changeRow(-1);
                } else if (key == KeyEvent.VK_DOWN) {
                    matrix.changeRow(1);
                }
            }

        /*
         * EXAMPLE:
         *
         * KeyEvent e = ... ;
         * if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
         *     game.gameOver();
         * else if () { ... }
         */
    }




    private void adjustOffsets() {
        for (int i = 0; i < sequences.length; i++) {
            if (offset[i] != -1 && !buffer.matchesFromOffset(sequences[i], offset[i])) {
                offset[i]++;
                if (offset[i] + sequences[i].size() > BUFFER_LIMIT) {
                    offset[i] = -1;
                }
            }
        }
    }


            public int getPointsForRound() {
                int points = 0;
                for (int i = 0; i < sequences.length; i++) {
                    if (buffer.matchesFromOffset(sequences[i], offset[i])) {
                        points += sequences[i].size();
                    }
                }
                return points;
            }



            /*
             * EXAMPLE:
             *
             * +-Step-+-BUFFER---------+-SEQUENCE----------+-OFFSET-+
             * |   0  | -- -- -- -- -- | E9 FF             |    0   | initial state
             * |   1  | E9 -- -- -- -- | E9 FF             |    0   | partial alignment
             * |   2  | E9 E9 -- -- -- | -- E9 FF          |    1   | partial alignment after shift
             * |   3  | E9 E9 A0 -- -- | -- -- -- E9 FF    |    3   | possible in the empty space
             * |   4  | E9 E9 A0 A0 -- | -- -- -- -- E9 FF |   -1   | impossible
             * +------+----------------+-------------------+--------+-------------------------------+
             */






}
