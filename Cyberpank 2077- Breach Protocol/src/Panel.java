import javax.swing.*;

import java.awt.*;

public class Panel extends JPanel { // Singleton class which will draw the game
    private static final Panel instance = new Panel();

    private static final Font BIG = new Font(Font.MONOSPACED, Font.BOLD, 32);
    private static final Font NORMAL = new Font(Font.MONOSPACED, Font.BOLD, 18);

    private static final Color TEXT = new Color(221, 241, 90);
    private static final Color HIGHLIGHT = new Color(102, 217, 239);
    private static final Color GOOD = new Color(29, 212, 121);
    private static final Color ERROR = new Color(252, 86, 81);
    private static final Color BACKGROUND = new Color(35, 36, 26);

/* 
 * +---------------+   +-----------+
 * |    TIMER      |   | BUFFER    |
 * +---------------+   +-----------+
 *   
 * +---------------+   +-----------+
 * |    MATRIX     |   | SEQUENCES |
 * |               |   |           |
 * |               |   |           |
 * |               |   |           |
 * |               |   |           |
 * +---------------+   +-----------+
 *
 * Generally each part of the UI is in its own method
 * depending on the state of the game we draw different parts
 *
 *
 */

    private Game game;
    private Gameplay gp;

    private Panel(){}
    public static Panel getInstance() { return instance; }

    public void setGameplay(Gameplay gp) { this.gp = gp; }
    public void setGame(Game game) { this.game = game; }

    protected void paintComponent(Graphics g){
        if(gp == null || game == null) return;

        try{
            switch(game.getState()) {
                case START  -> drawStart(g);
                case GAME   -> drawInGame(g);
                case WIN    -> drawWin(g);
                case FAIL   -> drawEnd(g);
            }
        }catch(Exception e){
            // as this runs on a separate thread we should exit the application if the ui dies
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void drawStart(Graphics g) {
        int x = getWidth()/2 - px(3);
        int y = getHeight()/2 - py(2);
        
        g.setColor(TEXT);
        g.setFont(BIG);
        g.drawString("BREACH PROTOCOL", x, y);
    }

    private void drawInGame(Graphics g){
        drawTimer(g);
        drawBuffer(g);
        drawMatrix(g);
        drawSequences(g);
    }

    private void drawTimer(Graphics g){
        int x = px();
        int y = py();
        g.setColor(BACKGROUND);
        g.fillRect(x,y,px(16),py(3));

        x += px()/2;
        y += py();

        g.setColor(TEXT);
        g.setFont(NORMAL);
        g.drawString("BREACH TIME REMAINING", x, y);
        y += py();
        x += px(12);
        float timeLeft = game.getTimer() * 100 / 1_000_000_000 / 100f;
        String text = String.format("%.2f s",timeLeft);
        g.setColor(TEXT);
        g.setFont(NORMAL);
        g.drawString(text, x, y);
    }

    private void drawBuffer(Graphics g){
        g.setColor(BACKGROUND);
        int x = px(1 + 16 + 1);
        int y = py();
        g.fillRect(x,y,px(11),py(3));

        x += px()/2;
        y += py();

        g.setColor(TEXT);
        g.setFont(NORMAL);
        g.drawString("BUFFER", x,y);

        x += px()/2;
        y += py();
        g.setFont(BIG);
        Sequence buffer = gp.getBuffer();
        for (int i = 0; i < Gameplay.BUFFER_LIMIT; i++){
            if(i<buffer.size())
                g.drawString(buffer.get(i).toString(), x,y);
            else
                g.drawString(Symbol.EMPTY.toString(), x,y);
            x+=px(2);
        }
    }

    private void drawMatrix(Graphics g){
        g.setColor(BACKGROUND);
        int x = px();
        int y = py(1 + 3 + 1);
        g.fillRect(x,y,px(16),py(18));

        g.setColor(TEXT);
        g.setFont(NORMAL);
        x += px()/2;
        y += py(); 
        g.drawString("CODE MATRIX", x, y);

        Matrix matrix = gp.getMatrix();

        g.setFont(BIG);
        x += px()/2;

        int startX = x;
        y += py(3);
        
        for (int row = 0; row < matrix.getSize(); row++) {
            for (int col = 0; col < matrix.getSize(); col++) {
                // draw the current selection box
                if(matrix.getRow() == row && matrix.isHorizontal())
                    drawBox(g, TEXT, x - px()/2, y-py(2)+py()/4, px(3), py(3));
                if(matrix.getColumn() == col && !matrix.isHorizontal())
                    drawBox(g, TEXT, x - px()/2, y-py(2)+py()/4, px(3), py(3));
                if(matrix.getRow() == row && matrix.getColumn() == col)
                    drawBox(g, HIGHLIGHT, x - px()/2, y-py(2)+py()/4, px(3), py(3));
                //draw the Symbol
                g.setColor(TEXT);
                g.drawString(matrix.get(row,col).toString(), x, y);
                x += px(3);
            }
            y += py(3);
            x = startX;
        }
    }

    private void drawSequences(Graphics g) {
        g.setColor(BACKGROUND);
        int x = px(1 + 16 + 1);
        int y = py(1 +  3 + 1);

        g.fillRect(x,y,px(11),py(18));
        g.setColor(TEXT);
        g.setFont(NORMAL);
        x += px()/2;
        y += py(); 
        g.drawString("SEQUENCES REQUIRED TO UPLOAD", x, y);
        y += py(); 

        y += py(2);
        x += px()/2;
        g.setFont(BIG);
        int startX = x;

        int[] offsets = gp.getOffsets();
        Sequence[] sequences = gp.getSequences();
        Sequence buffer = gp.getBuffer();

        for (int i = 0; i < sequences.length; i++) {
            if(offsets[i] == -1){
                g.setColor(ERROR);
                g.fillRect(x-px()/2, y-py(), px(10), py(2));
                g.setColor(BACKGROUND);
                g.drawString("FAILURE",x,y);
            }else if(buffer.matchesFromOffset(sequences[i],offsets[i])){
                g.setColor(GOOD);
                g.fillRect(x-px()/2, y-py(), px(10), py(2));
                g.setColor(BACKGROUND);
                g.drawString("INSTALLED",x,y);
            }else{
                x += px(offsets[i]*2);
                Sequence seq = sequences[i];
                for (int j = 0; j < seq.size(); j++) {
                    if(j + offsets[i] < buffer.size() && seq.get(j) == buffer.get(j + offsets[i]))
                        g.setColor(HIGHLIGHT);
                    else
                        g.setColor(TEXT);

                    g.drawString(seq.get(j).toString(),x,y);
                    x += px(2);
                }
            }
            y += py(3);
            x = startX;
        }
    }

    private void drawWin(Graphics g) {
        int x = getWidth()/2 - px(5);
        int y = getHeight()/2 - py(5);
        g.setColor(GOOD);
        g.setFont(BIG);
        g.drawString("Daemons Uploaded", x, y);
        y += py(2);
        g.setColor(TEXT);
        g.setFont(NORMAL);
        g.drawString("Score:", x, y);
        
        y += py(2);
        drawPoints(g, x, y);
    }

    private void drawEnd(Graphics g) {
        int x = getWidth()/2 - px(5);
        int y = getHeight()/2 - py(5);
        g.setColor(ERROR);
        g.setFont(BIG);
        g.drawString("Breach Detected", x, y);
        y += py(2);
        g.setColor(TEXT);
        g.setFont(NORMAL);
        g.drawString("Total Score:", x, y);

        y += py(2);
        drawPoints(g, x, y);
    }

    private void drawPoints(Graphics g, int x, int y){
        g.setColor(GOOD);
        g.setFont(BIG);
        int points = game.getTotalScore();
        g.drawString(String.valueOf(points), x, y);
    }   

    private void drawBox(Graphics g, Color c, int x, int y, int width, int height){
        g.setColor(c);
        g.fillRect(x, y, width, height);
        g.setColor(BACKGROUND);
        g.fillRect(x+5, y+5, width-10, height-10);
    }

    private int px(){ return px(1); }
    private int py(){ return py(1); }
    private int px(int times){ return times * Math.max(30,  getWidth() / 30); }
    private int py(int times){ return times * Math.max(30, getHeight() / 30); }

}