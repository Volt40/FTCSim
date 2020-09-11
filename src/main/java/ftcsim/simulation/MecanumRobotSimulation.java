package ftcsim.simulation;

import ftcsim.kinematics.MecanumKinematics;
import ftcsim.util.RobotColor;
import ftcsim.util.Units;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class MecanumRobotSimulation extends Simulation {
	
	// Kinematics model for this robot.
	private volatile MecanumKinematics kinematicsModel;
	
	// Offsets for updating the layout position of this pane.
	private volatile double xPaneOffset, yPaneOffset;
	
	// Visual parts of the robot.
	private Pane body;
	private Wheel w1, w2, w3, w4;
	
	// Animation timer and calculations thread for this simulation.
	private AnimationTimer animationTimer;
	private Thread calculator;
	private volatile boolean simulationActive;
	
	// Robot's position and rotation.
	private volatile double x, y, theta;
	
	// Robot's motor speeds in the format {w1, w2, w3, w4}.
	private volatile double[] motorSpeeds;
	
	// Robot's motor's max speed.
	private volatile double speed;
	
	// Reset values.
	private double rX, rY, rTheta;
	
	/**
	 * Creates a robot simulation with the specific parameters.
	 * @param color Color of the robot.
	 * @param l1 Horizontal distance from the center to the wheels.
	 * @param l2 Vertical distance from the center to the wheels.
	 * @param wd Diameter of the wheels.
	 */
	public MecanumRobotSimulation(double startX, double startY, double startTheta, double l1, double l2, double wd, double maxRPM, Units units, RobotColor color) {
		// Set units and make conversions.
		startX = units.toPixels(startX);
		startY = units.toPixels(startY);
		l1 = units.toPixels(l1);
		l2 = units.toPixels(l2);
		wd = units.toPixels(wd);
		// Calculate radius.
		double r = wd / 2;
		// Set overall dimensions.
		setPrefSize(2 * l1 + r, 2 * l2 + r);
		// Create robot body.
		body = new Pane();
		body.setPrefSize(2 * l1, 2 * l2);
		body.setStyle("-fx-border-color: black; " + color.getCss());
		body.setLayoutX(r / 2);
		body.setLayoutY(r / 2);
		body.getChildren().add(new Circle((2 * l1) / 2, 0, 3));
		// Create Wheels
		w1 = new Wheel(Wheel.RIGHT, r, r * 2);
		w1.setLayoutX(0);
		w1.setLayoutY(0);
		w2 = new Wheel(Wheel.LEFT, r, r * 2);
		w2.setLayoutX(2 * l1);
		w2.setLayoutY(0);
		w3 = new Wheel(Wheel.LEFT, r, r * 2);
		w3.setLayoutX(0);
		w3.setLayoutY(2 * l2 - r);
		w4 = new Wheel(Wheel.RIGHT, r, r * 2);
		w4.setLayoutX(2 * l1);
		w4.setLayoutY(2 * l2 - r);
		// Add all to the pane.
		getChildren().addAll(body, w1, w2, w3, w4);
		// Create the threads.
		simulationActive = false;
		animationTimer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				updateLayoutPosition();
			}
		};
		calculator = new Thread(new Runnable() {
			@Override
			public void run() {
				simulationActive = true;
				long lastTimeNano = -1;
				while(simulationActive) {
					long currentTimeNano = System.nanoTime();
					if (lastTimeNano == -1) {
						// If this is the first loop iteration, skip it.
						lastTimeNano = currentTimeNano;
						continue;
					}
					// Call the update method.
					update(1e-9 * (currentTimeNano - lastTimeNano));
					// Update last time.
					lastTimeNano = currentTimeNano;
				}
			}
		});
		// Calculate offsets.
		xPaneOffset = (2 * l1 + r) / 2;
		yPaneOffset = (2 * l2 + r) / 2;
		// Initiate motor speeds array.
		motorSpeeds = new double[] {0, 0, 0, 0};
		// Set start position.
		x = startX;
		y = startY;
		theta = startTheta;
		// Set speed.
		speed = Math.PI * wd * maxRPM / 60;
		// Create the model.
		kinematicsModel = new MecanumKinematics(l1, l2, speed);
		// Update the layout position.
		updateLayoutPosition();
	}
	
	/**
	 * Returns the array with this robot's motor speeds.
	 * @return the array with this robot's motor speeds.
	 */
	public double[] getMotorSpeeds() {
		return motorSpeeds;
	}
	
	/**
	 * Starts the simulation.
	 */
	@Override
	public void start() {
		animationTimer.start();
		calculator.start();
	}
	
	/**
	 * Stops the simulation.
	 */
	@Override
	public void stop() {
		animationTimer.stop();
		simulationActive = false;
	}
	
	/**
	 * Resets the simulation.
	 */
	@Override
	public void reset() {
		// Stop the simulation.
		stop();
		// Reset values.
		x = rX;
		y = rY;
		theta = rTheta;
		motorSpeeds = new double[] {0, 0, 0, 0};
		// Update position.
		updateLayoutPosition();
	}
	
	/**
	 * Updates/steps forward the simulation.
	 * @param now The timestamp of the current frame given in nanoseconds.
	 */
	private void update(double dt) {
		// Calculate motor turns.
		double[] motorTurns = new double[motorSpeeds.length];
		for (int i = 0; i < motorTurns.length; i++)
			if (motorSpeeds[i] > 1)
				motorTurns[i] = dt;
			else if (motorSpeeds[i] < -1)
				motorTurns[i] = -1 * dt;
			else
				motorTurns[i] = motorSpeeds[i] * dt;
		// Animate the wheels.
		w1.update(-motorTurns[0]);
		w2.update(-motorTurns[1]);
		w3.update(-motorTurns[2]);
		w4.update(-motorTurns[3]);
		// Solve the kinematic model.
		double[] dv = kinematicsModel.solve(motorTurns);
		// Rotate the vector.
		double distance = Math.hypot(dv[0], dv[1]);
		double angle = Math.atan2(dv[1], dv[0]);
		// Correct the speed.
		distance = correctSpeed(distance, angle, speed * dt);
		// Rotate the vector.
		angle -= theta;
		// Calculate delta values.
		double dx = distance * Math.cos(angle);
		double dy = distance * Math.sin(angle);
		double da = dv[2];
		// Update position.
		x += dx;
		y += dy;
		theta += da;
	}
	
	/**
	 * Updates the layout position of this pane with x, y and theta.
	 */
	private void updateLayoutPosition() {
		// Set location.
		setLayoutX(x - xPaneOffset);
		setLayoutY(y - yPaneOffset);
		setRotate(Math.toDegrees(Math.PI - (theta + (Math.PI / 2))));
		// Animate Wheels.
		w1.animate();
		w2.animate();
		w3.animate();
		w4.animate();
	}
	
	/**
	 * Corrects the speed output from the kinematics equation by accounting for the inherent gearing 
	 * of a mecanum drive-train.
	 * @param rawDistance Raw distance output.
	 * @param mecaRadians Shape of output vector.
	 * @return Corrected distance.
	 */
	private static double correctSpeed(double rawDistance, double mecaRadians, double maxSpeed) {
		/*
		 * The kinematics equation for the mecanum drive is only able to accurately predict the speed
		 * of the robot when the angle of the displacement vector is in the range [-pi/4, pi/4]. We 
		 * can correct the speed if the angle is output that range by inverting and re-scaling the 
		 * distance by sqrt(2)/2.
		 */
		mecaRadians = Math.abs(mecaRadians);
		if (mecaRadians <= Math.PI / 4 || mecaRadians >= 3 * (Math.PI / 4))
			// The distance does not need to be corrected.
			return rawDistance;
		double maxDia = maxSpeed * (Math.sqrt(2) / 2); // Max speed along 45 degrees.
		double nd = (rawDistance - maxDia) / (maxSpeed - maxDia); // Normalized distance.
		nd *= Math.sqrt(2) / 2; // Scale by sqrt(2)/2.
		nd *= -1; // Invert.
		// Calculate corrected distance.
		double correctedDistance = maxDia + nd * (maxSpeed - maxDia);
		return correctedDistance;
	}
	
	/**
	 * Represents a wheel and it's animation.
	 */
	private static class Wheel extends Pane {
		
		// Static identifiers for the wheel.
		private static final int RIGHT = 0, LEFT = 1, LINE_COUNT = 6;
		
		// Lines for the animation.
		private Line[] lines;
		
		// Length and width of this wheel.
		private volatile double length, width;
		
		// Wheel type.
		private int type;
		
		// Roll position
		private volatile double rollPos;
		
		/**
		 * Constructs a wheel of the given type.
		 * @param type Use Wheel.RIGHT or Wheel.LEFT.
		 * @param length Length of the wheel.
		 * @param width Height of the wheel.
		 */
		public Wheel(int type, double length, double width) {
			// Set overall dimensions.
			setPrefSize(length, width);
			setStyle("-fx-border-color: black; -fx-background-color: linear-gradient(to top right, #6e6e6e, #8c8c8c);");
			setClip(new Rectangle(length, width));
			// Set fields.
			this.length = length;
			this.width = width;
			this.type = type;
			// Create lines.
			rollPos = 0;
			lines = new Line[LINE_COUNT];
			for (int i = 0; i < lines.length; i++)
				lines[i] = new Line();
			animate();
			// Add lines to the pane.
			getChildren().addAll(lines);
		}
		
		/**
		 * Updates the position of the rollers.
		 * @param speed
		 */
		public void update(double speed) {
			rollPos += speed * width;
			rollPos %= width / (lines.length - 2);
		}
		
		// Animate the wheel by moving the lines up by distance.
		public void animate() {
			double spacing = width / (lines.length - 2);
			for (int i = 0; i < lines.length; i++) {
				switch(type) {
				case RIGHT:
					lines[i].setStartX(0);
					lines[i].setStartY(rollPos + (i - 1) * spacing);
					lines[i].setEndX(length);
					lines[i].setEndY(rollPos + i * spacing);
					break;
				case LEFT:
					lines[i].setStartX(0);
					lines[i].setStartY(rollPos + i * spacing);
					lines[i].setEndX(length);
					lines[i].setEndY(rollPos + (i - 1) * spacing);
					break;
				}
			}
		}
		
	}
	
}
