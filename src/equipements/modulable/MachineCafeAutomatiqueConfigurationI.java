package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.exceptions.AssertionChecking;

public interface MachineCafeAutomatiqueConfigurationI {
	/** measurement unit for power used in this appliance.					*/
	public static final MeasurementUnit	POWER_UNIT = MeasurementUnit.WATTS;
	/** measurement unit for tension used in this appliance.				*/
	public static final MeasurementUnit	TENSION_UNIT = MeasurementUnit.VOLTS;
	
	public static final MeasurementUnit	TEMPERATURE_UNIT = MeasurementUnit.CELSIUS;
	
	public static final MeasurementUnit LEQUID_UNIT = MeasurementUnit.LITRES;
	
	public static final MeasurementUnit WEIGHT_UNIT = MeasurementUnit.RAW; 
	
	public static final Measure<Double>	WATER_TANK_CAPACITY = new Measure<Double>(1.8, LEQUID_UNIT);
	
	public static final Measure<Double>	MILK_TANK_CAPACITY = new Measure<Double>(0.5, LEQUID_UNIT);
	
	public static final Measure<Double>	BEANS_TANK_CAPACITY = new Measure<Double>(2.5, WEIGHT_UNIT);
	
	public static final Measure<Double>	MAX_POWER_LEVEL = new Measure<Double>(1500.0, POWER_UNIT);
	
	public static final Measure<Double>	TENSION = new Measure<Double>(220.0, TENSION_UNIT);
	
	public static final Measure<Double>	MIN_TARGET_TEMPERATURE = new Measure<Double>(80.0, TEMPERATURE_UNIT);
	
	public static final Measure<Double>	MAX_TARGET_TEMPERATURE = new Measure<Double>(95.0, TEMPERATURE_UNIT);
	
	

	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	/**
	 * return true if the static invariants are observed, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	true if the static invariants are observed, false otherwise.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				POWER_UNIT != null,
						MachineCafeAutomatiqueConfigurationI.class,
				"POWER_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION_UNIT != null,
						MachineCafeAutomatiqueConfigurationI.class,
				"TENSION_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(TEMPERATURE_UNIT != null, MachineCafeAutomatiqueConfigurationI.class, "TEMPERATURE_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(LEQUID_UNIT != null, MachineCafeAutomatiqueConfigurationI.class, "LEQUID_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				WATER_TANK_CAPACITY != null && WATER_TANK_CAPACITY.getMeasurementUnit().equals(LEQUID_UNIT) && WATER_TANK_CAPACITY.getData() == 1.8,
				MachineCafeAutomatiqueConfigurationI.class,
				"WATER_TANK_CAPACITY != null && WATER_TANK_CAPACITY.getMeasurementUnit().equals(LEQUID_UNIT) && WATER_TANK_CAPACITY.getData() == 1.8");
		ret &= AssertionChecking.checkStaticInvariant(
				MILK_TANK_CAPACITY != null && MILK_TANK_CAPACITY.getMeasurementUnit().equals(LEQUID_UNIT) && MILK_TANK_CAPACITY.getData() == 0.5,
				MachineCafeAutomatiqueConfigurationI.class,
				"MILK_TANK_CAPACITY != null && MILK_TANK_CAPACITY.getMeasurementUnit().equals(LEQUID_UNIT) && MILK_TANK_CAPACITY.getData() == 0.5");
		ret &= AssertionChecking.checkStaticInvariant(
				BEANS_TANK_CAPACITY != null && BEANS_TANK_CAPACITY.getMeasurementUnit().equals(WEIGHT_UNIT) && BEANS_TANK_CAPACITY.getData() == 2.5,
				MachineCafeAutomatiqueConfigurationI.class,
				"BEANS_TANK_CAPACITY != null && BEANS_TANK_CAPACITY.getMeasurementUnit().equals(WEIGHT_UNIT) && BEANS_TANK_CAPACITY.getData() == 2.5");
		ret &= AssertionChecking.checkStaticInvariant(
				MAX_POWER_LEVEL != null && MAX_POWER_LEVEL.getMeasurementUnit().equals(POWER_UNIT) &&
				MAX_POWER_LEVEL.getData() > 0.0,
				MachineCafeAutomatiqueConfigurationI.class,
				"MAX_POWER_LEVEL != null && MAX_POWER_LEVEL.getMeasurementUnit().equals(POWER_UNIT) &&\r\n"
				+ "MAX_POWER_LEVEL.getData() > 0.0");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 220.0,
				MachineCafeAutomatiqueConfigurationI.class,
				"TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 220.0");
		ret &= AssertionChecking.checkStaticInvariant(MAX_TARGET_TEMPERATURE != null &&
				MAX_TARGET_TEMPERATURE.getMeasurementUnit().equals(
						TEMPERATURE_UNIT),
				MachineCafeAutomatiqueConfigurationI.class, "M_TARGET_TEMPERATURE != null &&\r\n"
						+ "				MAX_TARGET_TEMPERATURE.getMeasurementUnit().equals(\r\n"
						+ "						TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(MAX_TARGET_TEMPERATURE != null &&
				MAX_TARGET_TEMPERATURE.getMeasurementUnit().equals(
						TEMPERATURE_UNIT),
				MachineCafeAutomatiqueConfigurationI.class, "MAX_TARGET_TEMPERATURE != null &&\r\n"
						+ "				MAX_TARGET_TEMPERATURE.getMeasurementUnit().equals(\r\n"
						+ "						TEMPERATURE_UNIT)");

		return ret;
	}
}
