package ftcsim.simulation;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

/**
 * Represents a single robot simulation.
 */
public abstract class Simulation extends Pane {
	
	// List with every simulation.
	private static List<Simulation> simulations = new ArrayList<Simulation>();
	
	/**
	 * Starts all simulations.
	 */
	public static void startAllSimulations() {
		for (Simulation s : simulations)
			s.start();
	}
	
	/**
	 * Stops all simulations.
	 */
	public static void stopAllSimulations() {
		for (Simulation s : simulations)
			s.stop();
	}
	
	/**
	 * Resets all simulations.
	 */
	public static void resetAllSimulations() {
		for (Simulation s : simulations)
			s.reset();
	}
	
	/**
	 * Creates the simulation and adds it to the list.
	 */
	public Simulation() {
		simulations.add(this);
	}
	
	/**
	 * Starts the simulation.
	 */
	public abstract void start();
	
	/**
	 * Stops the simulation.
	 */
	public abstract void stop();
	
	/**
	 * Resets the simulation.
	 */
	public abstract void reset();
	
}
