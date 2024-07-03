package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.GameState;
import main.Game;

public class KeyboardInputs implements KeyListener {
	
	private Game game;
	
	public KeyboardInputs(Game game) {
		this.game = game;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().keyPressed(e);
			break;
		case MAZE:
			game.getMazeState().keyPressed(e);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().keyReleased(e);
			break;
		case MAZE:
			game.getMazeState().keyReleased(e);
			break;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			game.closeWindow();
		}
	}

}
