package main;

import java.awt.Graphics;
import java.awt.event.WindowEvent;

import javax.swing.JPanel;

import gamestates.GameState;
import gamestates.MazeState;
import gamestates.MenuState;
import utilz.AudioPlayer;

import static utilz.Constants.GameConsts.*;

public class Game implements Runnable {
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	public AudioPlayer audioPlayer;
	
	private MenuState menuState;
	private MazeState mazeState;
	
	public Game() {
		gamePanel = new GamePanel(this);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		initClasses();
		gameWindow = new GameWindow(this, gamePanel);	
		startGameLoop();
	}

	private void initClasses() {
		audioPlayer = new AudioPlayer();
		menuState = new MenuState(this);
		mazeState = new MazeState(this);
	}
	
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void update() {
		switch (GameState.state) {
		case MENU:
			menuState.update();
			break;
		case MAZE:
			mazeState.update();
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (GameState.state) {
		case MENU:
			menuState.render(g);
			break;
		case MAZE:
			mazeState.render(g);
			break;
		}
	}
	
	public JPanel getGamePanel() {
		return gamePanel;
	}
	
	public MenuState getMenuState() {
		return menuState;
	}
	
	public MazeState getMazeState() {
		return mazeState;
	}
	
	public void windowFocusLost() {
		switch (GameState.state) {
		case MENU:
			menuState.windowFocusLost();
			break;
		case MAZE:
			mazeState.windowFocusLost();
			break;
		}
	}
	
	public void closeWindow() {
		gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void run() {
		
		double timePerFrame  = 1_000_000_000 / FPS_SET;
		double timePerUpdate = 1_000_000_000 / UPS_SET;

		long previousTime = System.nanoTime();
		long currentTime = System.nanoTime();

		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();

		double deltaU = 0;
		double deltaF = 0;
		
		while (true) {
			currentTime = System.nanoTime();

			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

}
