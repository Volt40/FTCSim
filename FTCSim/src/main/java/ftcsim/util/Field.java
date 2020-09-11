package ftcsim.util;

import java.util.ArrayList;
import java.util.List;

import ftcsim.simulation.Simulation;
import javafx.scene.layout.Pane;

/*
 * A field where simulations take place.
 */
public class Field extends Pane {
	
	// This field's units.
	private Units units;
	
	// Simulations on this field.
	private List<Simulation> simulations;
	
	/**
	 * Constructs a field object.
	 * @param length Length of the field (in pixels).
	 * @param units Units of this field.
	 */
	public Field(double length, Units units) {
		setPrefSize(length, length);
		this.units = units;
		simulations = new ArrayList<Simulation>();
	}
	
	/**
	 * Returns this field's units.
	 * @return this field's units.
	 */
	public Units units() {
		return units;
	}
	
	/**
	 * Adds the simulation to this field.
	 * @param sim Simulation to be added.
	 */
	public void addSimulation(Simulation sim) {
		simulations.add(sim);
		getChildren().add(sim);
	}
	
	/**
	 * Removes the specific simulation.
	 * @param sim Simulation to be removed.
	 * @return true if the simulation was removed, false otherwise.
	 */
	public boolean removeSimulation(Simulation sim) {
		this.getChildren().remove(sim);
		return simulations.remove(sim);
	}
	
	/**
	 * Starts the simulations in this field.
	 */
	public void startSimulations() {
		for (Simulation s : simulations)
			s.start();
	}
	
	/**
	 * Stops the simulations in this field.
	 */
	public void stopSimulations() {
		for (Simulation s : simulations)
			s.stop();
	}
	
	/**
	 * Resets the simulations in this field.
	 */
	public void resetSimulations() {
		for (Simulation s : simulations)
			s.reset();
	}
	
}
