import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the Battle Scene that is used for all four of the normal levels.
 * It extends the abstract superclass, SimpleBattle. Because of that, a lot of the basic
 * components such as the player and the lasers have already been instantiated. This one
 * class can be re-used for every normal level because the constructor takes in the level
 * as a parameter.
 * 
 * @author João Kiakumbo Sebatião (JKiakumbo)
 * @version 1.0
 */
public class Battle extends SimpleBattle implements SceneInterface {
	public static final int TOTAL_LEVELS = 4;
	
	private Scene battleScene;
	private int level;
	
	private Group enemies;
	
	private double timer = 0.0;
	private static double TIMER_LIMIT = 20.0;
	
	/**
	 * Constructor for Battle class
	 * @param sceneManager SceneManager currently being used
	 * @param level current Battle level
	 */
	public Battle(SceneManager sceneManager, int level) {
		this.sceneManager = sceneManager;
		this.level = level;
	}
	
	/**
	 * Returns the Battle Scene
	 */
	@Override
	public Scene init(int width, int height) {
		root = new Group();
		battleScene = new Scene(root, width, height, Color.BLACK);
		
		addLevelText();
		addPlayer();
		addEnemies();
		addPlayerLasers();
		
		battleScene.setOnKeyPressed(e -> handleKeyPressed(e.getCode()));
		
		return battleScene;
	}
	
	/**
	 * Entry point for game loop
	 * @param elapsedTime seconds elapsed since last loop
	 */
	public void step(double elapsedTime) {
		checkTimeUp(elapsedTime);
		
		if (stepCounter % getEnemyCreationRate() == 0) {
			createEnemy();
		}
		
		moveEnemies();
		movePlayerLasers();
		checkPlayerLaserHitEnemy();
		checkPlayerEnemyIntersect();
		checkEnemyReachBottom();	
		
		stepCounter++;
	}
	
	private void playerWinsLevel() {
		sceneManager.goToNextLevelScene(sceneManager, level + 1);
	}
	
	private void addLevelText() {
		addBottomLeftText("Level " + level);
	}
	
	private void addEnemies() {
		enemies = new Group();
		root.getChildren().add(enemies);
	}
	
	private int getEnemyCreationRate() {
		int[] enemyCreationRate = {150, 125, 100, 75};
		return enemyCreationRate[level];
	}
	
	private double getEnemyTravelRate() {
		double[] enemyTravelRate = {0.9, 1.0, 1.1, 1.2};
		return enemyTravelRate[level];
	}
	
	private void checkTimeUp(double elapsedTime) {
		timer += elapsedTime;
		if (timer > TIMER_LIMIT) {
			playerWinsLevel();
		}
	}
	
	private void createEnemy() {
		Enemy enemyObject = new Enemy();
		Rectangle enemy = enemyObject.getEnemy();
		enemies.getChildren().add(enemy);
	}
	
	private void moveEnemies() {
		for (Node enemyNode : enemies.getChildren()) {
			Rectangle enemy = (Rectangle) enemyNode;
			enemy.setY(enemy.getY() + getEnemyTravelRate());
		}
	}
	
	private void checkPlayerLaserHitEnemy() {
		checkPlayerLaserHitRectangleInGroup(enemies, true);
	}
	
	private void checkPlayerEnemyIntersect() {
		for (Node enemyNode : enemies.getChildren()) {
			Rectangle enemy = (Rectangle) enemyNode;
			playerLosesIfIntersectEnemy(enemy);
		}
	}
	
	private void checkEnemyReachBottom() {
		for (Node enemyNode : enemies.getChildren()) {
			Rectangle enemy = (Rectangle) enemyNode;
			if (enemy.getY() + Enemy.HEIGHT > Main.SIZE) {
				playerLoses();
			}
		}
	}
	
	private void handleKeyPressed(KeyCode code) {
		switch (code) {
			case Q:
				quitToMenu();
				break;
			case SPACE:
				// cheat code to skip to next level
				playerWinsLevel();
				break;
			case W: 
				shootPlayerLaser("UP");
				break;
			default:
				playerObject.handlePlayerKey(code);
		}
	}
}