package ftcsim.util;

/**
 * Represents a color to identify a robot in a simulation.
 */
public enum RobotColor {
	
	// Color presets.
	BLUE("-fx-background-color: linear-gradient(to top right, #53a0d4, #0c7dc9);"),
	RED("-fx-background-color: linear-gradient(to top right, #de5968, #c90017);"),
	GREEN("-fx-background-color: linear-gradient(to top right, #85e690, #0bb51e);"),
	PURPLE("-fx-background-color: linear-gradient(to top right, #ac58e8, #8912de);");
	
	// This color's css.
	private String css;
	
	/**
	 * Creates a RobotColor with the css provided.
	 * @param css Css
	 */
	private RobotColor(String css) {
		this.css = css;
	}
	
	/**
	 * Returns the css for this color.
	 * @return The css for this color.
	 */
	public String getCss() {
		return css;
	}
	
}
