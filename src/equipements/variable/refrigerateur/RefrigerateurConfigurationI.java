package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.exceptions.AssertionChecking;

public interface RefrigerateurConfigurationI {
	
	public static final MeasurementUnit	TEMPERATURE_UNIT =
			MeasurementUnit.CELSIUS;
	
	public static final Measure<Double>	MIN_TARGET_TEMPERATURE =
			new Measure<>(
					2.0,
					TEMPERATURE_UNIT);
	
	public static final Measure<Double>	STANDARD_TARGET_TEMPERATURE =
			new Measure<>(
					4.0,
					TEMPERATURE_UNIT);
	
	public static final Measure<Double>	MAX_TARGET_TEMPERATURE =
			new Measure<>(
					8.0,
					TEMPERATURE_UNIT);
	
	public static final MeasurementUnit	POWER_UNIT = MeasurementUnit.WATTS;

	public static final MeasurementUnit	TENSION_UNIT = MeasurementUnit.VOLTS;
	
	public static final Measure<Double>	NOT_COOLING_POWER =
			new Measure<>(5.0, POWER_UNIT);

	public static final Measure<Double>	MAX_POWER_LEVEL =
			new Measure<>(150.0, POWER_UNIT);
	
	public static final Measure<Double>	TENSION =
			new Measure<>(220.0, TENSION_UNIT);
	
	public static final Measure<Double>	HYSTERESIS =
			new Measure<>(1.0, TEMPERATURE_UNIT);

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				TEMPERATURE_UNIT != null,
						RefrigerateurConfigurationI.class,
				"TEMPERATURE_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				MIN_TARGET_TEMPERATURE != null &&
					MIN_TARGET_TEMPERATURE.getMeasurementUnit().equals(
															TEMPERATURE_UNIT),
					RefrigerateurConfigurationI.class,
				"MIN_TARGET_TEMPERATURE != null && MIN_TARGET_TEMPERATURE."
				+ "getMeasurementUnit().equals(TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				STANDARD_TARGET_TEMPERATURE != null &&
						STANDARD_TARGET_TEMPERATURE.getMeasurementUnit().equals(
															TEMPERATURE_UNIT),
						RefrigerateurConfigurationI.class,
				"STANDARD_TARGET_TEMPERATURE != null && STANDARD_TARGET_TEMPERATURE."
				+ "getMeasurementUnit().equals(TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				MAX_TARGET_TEMPERATURE != null &&
					MAX_TARGET_TEMPERATURE.getMeasurementUnit().equals(
															TEMPERATURE_UNIT),
					RefrigerateurConfigurationI.class,
				"MAX_TARGET_TEMPERATURE != null && MAX_TARGET_TEMPERATURE."
				+ "getMeasurementUnit().equals(TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				POWER_UNIT != null,
						RefrigerateurConfigurationI.class,
				"POWER_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION_UNIT != null,
						RefrigerateurConfigurationI.class,
				"TENSION_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				MAX_POWER_LEVEL != null &&
					MAX_POWER_LEVEL.getMeasurementUnit().equals(POWER_UNIT) &&
					MAX_POWER_LEVEL.getData() > 0.0,
					RefrigerateurConfigurationI.class,
				"MAX_POWER_LEVEL != null && MAX_POWER_LEVEL.getMeasurementUnit()."
				+ "equals(POWER_UNIT) && MAX_POWER_LEVEL.getData() > 0.0");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 220.0,
						RefrigerateurConfigurationI.class,
				"TENSION != null && TENSION.getMeasurementUnit().equals(TENSION_UNIT) && TENSION.getData() == 220.0");
		
		ret &= AssertionChecking.checkStaticInvariant(
				MIN_TARGET_TEMPERATURE.getData() <= STANDARD_TARGET_TEMPERATURE.getData() &&
					STANDARD_TARGET_TEMPERATURE.getData() <= MAX_TARGET_TEMPERATURE.getData(),
					RefrigerateurConfigurationI.class,
				"MIN_TARGET_TEMPERATURE.getData() <= STANDARD_TARGET_TEMPERATURE.getData()"
				+ " && STANDARD_TARGET_TEMPERATURE.getData() <= "
				+ "MAX_TARGET_TEMPERATURE.getData()");
		ret &= AssertionChecking.checkStaticInvariant(
				NOT_COOLING_POWER != null &&
					NOT_COOLING_POWER.getMeasurementUnit().equals(POWER_UNIT) &&
					NOT_COOLING_POWER.getData() >= 0.0,
					RefrigerateurConfigurationI.class,
				"NOT_COOLING_POWER != null && NOT_COOLING_POWER.getMeasurementUnit()."
				+ "equals(POWER_UNIT) && NOT_COOLING_POWER.getData() >= 0.0");
		ret &= AssertionChecking.checkStaticInvariant(
				HYSTERESIS != null &&
					HYSTERESIS.getMeasurementUnit().equals(TEMPERATURE_UNIT) &&
					HYSTERESIS.getData() > 0.0,
					RefrigerateurConfigurationI.class,
				"HYSTERESIS != null && HYSTERESIS.getMeasurementUnit()."
				+ "equals(TEMPERATURE_UNIT) && HYSTERESIS.getData() > 0.0");

		return ret;
	}	
}
