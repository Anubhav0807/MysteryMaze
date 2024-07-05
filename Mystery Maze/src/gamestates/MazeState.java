package gamestates;

import static utilz.Constants.SizeConsts.TILE_SIZE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import main.Game;
import maze.MazeGenerator;
import ui.EndScreenOverlay;
import ui.HUD;

public class MazeState extends State implements StateMethods {
	
	public MazeState(Game game) {
		super(game);
		initClasses();
	}

	private Player player;
	private MazeGenerator mazeGenerator;
	private HUD hud;
	private EndScreenOverlay endScreen;
	
	public boolean gameNotOver = true;
	public boolean levelCleared = false;

	@Override
	public void initClasses() {
		player = new Player(game, TILE_SIZE-4, TILE_SIZE-4);
		mazeGenerator = new MazeGenerator(player);
		player.setMap(mazeGenerator.getMaze());
		hud = new HUD(game, player);
		endScreen = new EndScreenOverlay(hud);
	}

	@Override
	public void update() {
		mazeGenerator.update();
		if (gameNotOver) {
			player.update();
			hud.update();
		} else if (levelCleared) {
			mazeGenerator.generateMaze();
			gameNotOver = true;
			player.reset();
			hud.initTimer();
		}
	}

	@Override
	public void render(Graphics g) {
		mazeGenerator.render(g);
		if (gameNotOver || levelCleared) {
			player.render(g);
			hud.render(g);
		} else {
			endScreen.render(g);
		}
	}

	@Override
	public void windowFocusLost() {
		player.stop();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		case KeyEvent.VK_W:
			player.W = true;
			break;
		case KeyEvent.VK_UP:
			player.up = true;
			break;
			
		case KeyEvent.VK_A:
			player.A = true;
			break;
		case KeyEvent.VK_LEFT:
			player.left = true;
			break;
			
		case KeyEvent.VK_S:
			player.S = true;
			break;
		case KeyEvent.VK_DOWN:
			player.down = true;
			break;
			
		case KeyEvent.VK_D:
			player.D = true;
			break;
		case KeyEvent.VK_RIGHT:
			player.right = true;
			break;
			
		case KeyEvent.VK_SPACE:
			player.dropBomb();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		
		case KeyEvent.VK_W:
			player.W = false;
			break;
		case KeyEvent.VK_UP:
			player.up = false;
			break;
			
		case KeyEvent.VK_A:
			player.A = false;
			break;
		case KeyEvent.VK_LEFT:
			player.left = false;
			break;
			
		case KeyEvent.VK_S:
			player.S = false;
			break;
		case KeyEvent.VK_DOWN:
			player.down = false;
			break;
			
		case KeyEvent.VK_D:
			player.D = false;
			break;
		case KeyEvent.VK_RIGHT:
			player.right = false;
			break;
			
		case KeyEvent.VK_SPACE:
			player.canDrop = true;
			break;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public HUD getHud() {
		return hud;
	}
	
	public MazeGenerator getMazeGenerator() {
		return mazeGenerator;
	}
	
	public EndScreenOverlay getEndScreenOverlay() {
		return endScreen;
	}

}
