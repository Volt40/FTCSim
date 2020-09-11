package ftcsim.application;

import ftcsim.simulation.MecanumRobotSimulation;
import ftcsim.simulation.Simulation;
import ftcsim.util.Field;
import ftcsim.util.RobotColor;
import ftcsim.util.Settings;
import ftcsim.util.Units;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FTCSim extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Field root = new Field(Settings.FIELD_LENGTH, Units.PIXELS);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest((e) -> onClose());
		primaryStage.show();
		
		MecanumRobotSimulation r = new MecanumRobotSimulation(72, 72, 0, 7, 8, 4, 90, Units.INCHES, RobotColor.BLUE);
		r.getMotorSpeeds()[0] = 0;
		r.getMotorSpeeds()[1] = 0;
		r.getMotorSpeeds()[2] = 0;
		r.getMotorSpeeds()[3] = 0;
		r.start();
		
		root.getChildren().addAll(r);
	}
	
	private static void onClose() {
		Simulation.stopAllSimulations();
		Platform.exit();
	}
	
	public static void launchFTCSim(String... args) {
		launch(args);
	}

}
