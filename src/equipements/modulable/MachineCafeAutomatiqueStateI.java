package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface MachineCafeAutomatiqueStateI extends MachineCafeAutomatiqueConfigurationI{
	
	public static boolean staticInvariants() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public static boolean	invariants(MachineCafeAutomatiqueStateI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= MachineCafeAutomatiqueStateI.staticInvariants();
		ret &= AssertionChecking.checkInvariant(
				instance.getState() != null && 
				instance.on() == (instance.getState() != MachineCafeAutomatiqueState.OFF),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.getState() != null && instance.on() == "
				+ "(instance.getState() != MachineCafeAutomatiqueState.OFF");
		ret &= AssertionChecking.checkInvariant(
				instance.getWaterTankLevel().getData() >= 0.0 &&
				instance.getWaterTankLevel().getData() <= WATER_TANK_CAPACITY.getData(),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.getWaterTankLevel().getData() >= 0.0 &&"
				+ " instance.getWaterTankLevel().getData() <= WATER_TANK_CAPACITY.getData()");
		ret &= AssertionChecking.checkInvariant(
				instance.getMilkTankLevel().getData() >= 0.0 &&
				instance.getMilkTankLevel().getData() <= MILK_TANK_CAPACITY.getData(),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.getMilkTankLevel().getData() >= 0.0 &&"
				+ " instance.getMilkTankLevel().getData() <= MILK_TANK_CAPACITY.getData()");
		ret &= AssertionChecking.checkInvariant(
				instance.getBeansTanklevel().getData() >= 0.0 &&
				instance.getBeansTanklevel().getData() <= BEANS_TANK_CAPACITY.getData(),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.getBeansTanklevel().getData() >= 0.0 &&"
				+ " instance.getBeansTanklevel().getData() <= BEANS_TANK_CAPACITY.getData()");
		ret &= AssertionChecking.checkInvariant(
				instance.getWaterTankLevel().getMeasurementUnit().equals(LEQUID_UNIT) && 
				instance.getMilkTankLevel().getMeasurementUnit().equals(LEQUID_UNIT) &&
				instance.getBeansTanklevel().getMeasurementUnit().equals(WEIGHT_UNIT) &&
				instance.getCurrentTemperature().getMeasurementUnit().equals(TEMPERATURE_UNIT) &&
				instance.getTargetTemperature().getMeasurementUnit().equals(TEMPERATURE_UNIT),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"				instance.getWaterTankLevel().getMeasurementUnit().equals(LEQUID_UNIT) && \r\n"
				+ "				instance.getMilkTankLevel().getMeasurementUnit().equals(LEQUID_UNIT) &&\r\n"
				+ "				instance.getBeansTanklevel().getMeasurementUnit().equals(WEIGHT_UNIT) &&\r\n"
				+ "				instance.getCurrentTemperature().getMeasurementUnit().equals(TEMPERATURE_UNIT) &&\r\n"
				+ "				instance.getTargetTemperature().getMeasurementUnit().equals(TEMPERATURE_UNIT)");
		ret &= AssertionChecking.checkInvariant(
				instance.getTargetTemperature().getData() >= MIN_TARGET_TEMPERATURE.getData() &&
				instance.getTargetTemperature().getData() <= MAX_TARGET_TEMPERATURE.getData(),
				MachineCafeAutomatiqueStateI.class,
				instance,
				"				instance.getTargetTemperature().getData() >= MIN_TARGET_TEMPERATURE.getData() &&\r\n"
				+ "				instance.getTargetTemperature().getData() <= MAX_TARGET_TEMPERATURE.getData()");
		ret &= AssertionChecking.checkInvariant(
				instance.on() || instance.getSelectedDrink() == null,
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.on() || instance.getSelectedDrink() == null");
		ret &= AssertionChecking.checkInvariant(
				instance.getSelectedIntensity() != null && instance.getSelectedSize() != null,
				MachineCafeAutomatiqueStateI.class,
				instance,
				"instance.getSelectedIntensity() != null && instance.getSelectedSize() != null");
		return ret;
	}
	
	public MachineCafeAutomatiqueState getState();
	
	public MachineCafeAutomatiqueDrink getSelectedDrink();

	public MachineCafeAutomatiqueIntensity getSelectedIntensity();

	public MachineCafeAutomatiqueSize getSelectedSize();
	
	public Measure<Double> getTargetTemperature();
	
	public TimedMeasure<Double> getCurrentTemperature();
	
	public TimedMeasure<Double> getWaterTankLevel();
	
	public TimedMeasure<Double> getMilkTankLevel();
	
	public TimedMeasure<Double> getBeansTanklevel();
	
	public boolean on();
}
