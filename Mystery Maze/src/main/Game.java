package main;

import java.awt.Graphics;

import maze.MazeGenerator;

import static utilz.Constants.GameConsts.*;

public class Game implements Runnable {
	
	@SuppressWarnings("unused")
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	private MazeGenerator mazeGenerator;
	
	public Game() {
		initClasses();
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
		
		startGameLoop();
	}

	private void initClasses() {
		mazeGenerator = new MazeGenerator();
		mazeGenerator.generateMaze();
	}
	
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	private void update() {
		
	}
	
	public void render(Graphics g) {
		mazeGenerator.render(g);
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
