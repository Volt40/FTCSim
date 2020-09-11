package ftcsim.util;

/**
 * Enum with common units.
 */
public enum Units {
	
	INCHES(1, 0.0833333, 0.0254, 25.4, Settings.FIELD_LENGTH / Settings.FIELD_SIZE_INCHES),
	FEET(12, 1, 0.3048, 304.8, Settings.FIELD_LENGTH / Settings.FIELD_SIZE_FEET),
	METERS(39.3701, 3.28084, 1, 1000, Settings.FIELD_LENGTH / Settings.FIELD_SIZE_METERS),
	MILLIMETERS(0.0393701, 0.00328084, 0.001, 1, Settings.FIELD_LENGTH / Settings.FIELD_SIZE_MILLIMETERS),
	PIXELS(Settings.FIELD_SIZE_INCHES / Settings.FIELD_LENGTH, Settings.FIELD_SIZE_FEET / Settings.FIELD_LENGTH, Settings.FIELD_SIZE_METERS / Settings.FIELD_LENGTH, Settings.FIELD_SIZE_MILLIMETERS / Settings.FIELD_LENGTH, 1);
	
	// Conversion fields.
	private double toInches, toFeet, toMeters, toMillimeters, toPixels;
	
	/**
	 * Initiates the unit with the given conversions.
	 * @param toInches
	 * @param toFeet
	 * @param toMeters
	 * @param toMillimeters
	 * @param toPixels
	 */
	private Units(double toInches, double toFeet, double toMeters, double toMillimeters, double toPixels) {
		this.toInches = toInches;
		this.toFeet = toFeet;
		this.toMeters = toMeters;
		this.toMillimeters = toMillimeters;
		this.toPixels = toPixels;
	}
	
	/**
	 * value -> inches.
	 * @param value Value in this unit.
	 * @return Conversion to inches.
	 */
	public double toInches(double value) {
		return value * toInches;
	}
	
	/**
	 * value -> feed.
	 * @param value Value in this unit.
	 * @return Conversion to feed.
	 */
	public double toFeet(double value) {
		return value * toFeet;
	}
	
	/**
	 * value -> meters.
	 * @param value Value in this unit.
	 * @return Conversion to meters.
	 */
	public double toMeters(double value) {
		return value * toMeters;
	}
	
	/**
	 * value -> millimeters.
	 * @param value Value in this unit.
	 * @return Conversion to millimeters.
	 */
	public double toMillimeters(double value) {
		return value * toMillimeters;
	}
	
	/**
	 * value -> pixels.
	 * @param value Value in this unit.
	 * @return Conversion to pixels.
	 */
	public double toPixels(double value) {
		return value * toPixels;
	}
	
}
