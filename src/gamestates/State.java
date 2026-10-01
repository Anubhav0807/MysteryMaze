package gamestates;

import main.Game;

public abstract class State {
	
	protected Game game;
	
	public State(Game game) {
		this.game = game;
	}

}
