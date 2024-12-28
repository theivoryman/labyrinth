// Benjamin Duncan
// MazeController.java

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class MazeController extends JLayeredPane implements ActionListener {
	private Maze maze;
	private Player player;
	private int difficulty; // easy = 1, normal = 2, hard = 3
	private ArrayList<Enemy> enemies;

	private PaintLayer MazeLayer;
	private PaintLayer LightLayer;
	private PaintLayer PlayerLayer;

	public MazeController(int diff, int size) {

		enemies = new ArrayList<Enemy>();

		maze = new Maze(size, difficulty);
		player = new Player(0, 0, maze.getCell(0, 0).hallSize(), maze.cellSize());

		// create enemies
		int enemyChance = (4 - diff) * 1;
		int count = 0;

		for (int i = 0; i < maze.size(); i++) {
			for (int a = 0; a < maze.size(); a++) {
				if (maze.getCell(i, a).isDeadEnd() && !(i == 0 || a == 0)) {
					if (count % enemyChance == 0)
						enemies.add(new Enemy(a, i, maze.cellSize(), maze.getMaze()));
					count++;
				}
			}
		}

		// ---- assign objects into layers ----
		MazeLayer = new PaintLayer(mazeWindowSize());
		// EnemyLayer = new PaintLayer(mazeWindowSize());
		LightLayer = new PaintLayer(mazeWindowSize());
		PlayerLayer = new PaintLayer(mazeWindowSize());

		add(MazeLayer, JLayeredPane.DEFAULT_LAYER);
		// add(EnemyLayer, JLayeredPane.PALETTE_LAYER);
		add(LightLayer, JLayeredPane.MODAL_LAYER);
		add(PlayerLayer, JLayeredPane.DRAG_LAYER);

		MazeLayer.addDrawable(maze);

		for (int i = 0; i < enemies.size(); i++) {
			// EnemyLayer.addDrawable(enemies.get(i));
		}

		LightLayer.addDrawable(new LightLayer(maze.size(), maze.cellSize(), player, maze.getMaze(), diff));

		PlayerLayer.addDrawable(player);

		MazeLayer.paint();
		// EnemyLayer.paint();
		LightLayer.paint();
		PlayerLayer.paint();

		this.setVisible(true);

		MazeLayer.setVisible(true);
		// EnemyLayer.setVisible(true);
		LightLayer.setVisible(true);
		PlayerLayer.setVisible(true);

		// start timer
		Timer timer = new Timer(20, this);
		timer.start();
	}

	// TICK!
	public void actionPerformed(ActionEvent e) {
		tick();
	}

	public void tick() {
		// EnemyLayer.repaint();
		LightLayer.paint();
		PlayerLayer.repaint();
	}

	public int mazeWindowSize() {
		return maze.cellSize() * maze.size();
	}

	public int cellSize() {
		return maze.cellSize();
	}

	public int numOfCells() {
		return maze.size();
	}

	public boolean playerAtExit() {
		return maze.getCell(player.getX(), player.getY()).isExit();
	}
}