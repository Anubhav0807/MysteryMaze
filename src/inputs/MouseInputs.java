package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.GameState;
import main.Game;

public class MouseInputs implements MouseListener, MouseMotionListener {
	
	private Game game;
	
	public MouseInputs(Game game) {
		this.game = game;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().mousePressed(e);
			break;
		case MAZE:
			game.getMazeState().mousePressed(e);
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().mouseReleased(e);
			break;
		case MAZE:
			game.getMazeState().mouseReleased(e);
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().mouseEntered(e);
			break;
		case MAZE:
			game.getMazeState().mouseEntered(e);
			break;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			game.getMenuState().mouseExited(e);
			break;
		case MAZE:
			game.getMazeState().mouseExited(e);
			break;
		}
	}

}
