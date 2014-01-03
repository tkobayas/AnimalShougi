package jp.gr.java_conf.tkobayas.animalshougi;

public enum GamePhase {
	STARTING("STARTING"), PLAYER1("PLAYER1"), PLAYER2("PLAYER2"), GAMEOVER("GAMEOVER");
	
    private final String text;

    private GamePhase(final String text) {
      this.text = text;
    }
    
    @Override
    public String toString() {
      return this.text;
    }
}
