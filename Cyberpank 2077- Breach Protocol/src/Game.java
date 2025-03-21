public class Game{
    private static final long NANO = 1_000_000_000; // Conversion factor
    private static final long FPS = 60;

    private static final long TIME_START =  2 * NANO; // Time before the game starts
    private static final long TIME_ROUND = 20 * NANO; // Time for the Countdown
    private static final long TIME_END   =  3 * NANO; // Time after a round is over

    private Gameplay gameplay;
    private final Panel panel;
    private State state; // The state of the round
    private long timer; // tracks the actual time
    private int totalScore; // The score over multiple rounds
    private boolean playerDone; // If the player wishes to end a round early

    public static void main(String[] args) {
        new Game();
    }

    enum State{
        START, GAME, WIN, FAIL
    }

    public Game(){
        totalScore = 0;
        state = State.START;
        timer = TIME_START;

        panel = Panel.getInstance();
        panel.setGame(this);

        setupRound();

        gameLoop();
    }

    private void setupRound(){
        playerDone = false;
        gameplay = new Gameplay(this);
        panel.setGameplay(gameplay);
    }

    public void gameLoop() {
        long lastUpdate = System.nanoTime();
        long accumulator = 0; // accumulates the passed time since the last update
        long slice = NANO/FPS, delta; // slice = the wanted time between updates, delta = the actual time for each loop

        while(true){
            delta = System.nanoTime() - lastUpdate;
            lastUpdate += delta;
            accumulator += delta;

            while(accumulator > slice){
                accumulator -= slice;
                update(slice);
            }

            draw();
        }
    }

    private void update(long deltaTime){
        // Don't start the timer in GAME until the buffer not empty
        if(state != State.GAME || !gameplay.getBuffer().isEmpty())
            timer -= deltaTime;
        //maybe we should now change the state
        if(nextState())
            switch(state){
                case START, WIN -> {
                    setupRound();
                    timer = TIME_ROUND;
                    state = State.GAME;
                }
                case GAME -> finishRound();
                case FAIL -> gameOver();
            }

        // let gameplay handle the inputs once in game
        if(state == State.GAME)
            gameplay.handleInputs();
    }

    public void gameOver() {System.exit(0); }
    public void endRoundEarly(){
        playerDone = true;
    }

    private void finishRound(){
        // Add up the score
        int score = gameplay.getPointsForRound();
        totalScore += score;

        timer = TIME_END;
        // Decide if the game continues or not
        state = score > 0 ? State.WIN : State.FAIL;
    }

    // should we switch to the next state
    public boolean nextState(){
        return timer<0 || (state == State.GAME && playerDone);
    }

    public int getTotalScore()          { return totalScore;}
    public long getTimer()              { return timer;     }
    public State getState()             { return state;     }

    private void draw() { UI.getInstance().repaint(); }
}
