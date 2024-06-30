package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
		switch (e.getKeyCode()) {
		
		case KeyEvent.VK_W:
			game.getPlayer().W = true;
			break;
		case KeyEvent.VK_UP:
			game.getPlayer().up = true;
			break;
			
		case KeyEvent.VK_A:
			game.getPlayer().A = true;
			break;
		case KeyEvent.VK_LEFT:
			game.getPlayer().left = true;
			break;
			
		case KeyEvent.VK_S:
			game.getPlayer().S = true;
			break;
		case KeyEvent.VK_DOWN:
			game.getPlayer().down = true;
			break;
			
		case KeyEvent.VK_D:
			game.getPlayer().D = true;
			break;
		case KeyEvent.VK_RIGHT:
			game.getPlayer().right = true;
			break;
			
		case KeyEvent.VK_SPACE:
			game.getPlayer().dropBomb();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		case KeyEvent.VK_W:
			game.getPlayer().W = false;
			break;
		case KeyEvent.VK_UP:
			game.getPlayer().up = false;
			break;
			
		case KeyEvent.VK_A:
			game.getPlayer().A = false;
			break;
		case KeyEvent.VK_LEFT:
			game.getPlayer().left = false;
			break;
			
		case KeyEvent.VK_S:
			game.getPlayer().S = false;
			break;
		case KeyEvent.VK_DOWN:
			game.getPlayer().down = false;
			break;
			
		case KeyEvent.VK_D:
			game.getPlayer().D = false;
			break;
		case KeyEvent.VK_RIGHT:
			game.getPlayer().right = false;
			break;
			
		case KeyEvent.VK_SPACE:
			game.getPlayer().canDrop = true;
			break;
			
		case KeyEvent.VK_ESCAPE:
			game.closeWindow();
			break;
		}
	}

}
