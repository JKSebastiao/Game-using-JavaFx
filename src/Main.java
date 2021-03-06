import javafx.application.Application;
import javafx.stage.Stage;
 
/**
 * This class is the entry point to the game. It is necessary for the game to start.
 * 
 * @author João Kiakumbo Sebatião (JKiakumbo)
 * @version 1.0
 */
public class Main extends Application {
	public static final String TITLE = "Laser Mania";
	public static final int SIZE = 600;
	
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle(TITLE);
    	
    	SceneManager sceneManager = new SceneManager(primaryStage);
    	sceneManager.goToMenuScene(sceneManager);
    }
    
    public static void main(String[] args) {
		launch(args);
	}
}