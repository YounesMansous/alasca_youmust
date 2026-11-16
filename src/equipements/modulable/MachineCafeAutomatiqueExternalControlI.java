package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface MachineCafeAutomatiqueExternalControlI extends 
MachineCafeAutomatiqueStateI {
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= MachineCafeAutomatiqueStateI.staticInvariants();
		return ret;
	}
	
	public static boolean	invariants(MachineCafeAutomatiqueExternalControlI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= MachineCafeAutomatiqueExternalControlI.staticInvariants();
		ret &= MachineCafeAutomatiqueStateI.invariants(instance);
		ret &= AssertionChecking.checkInvariant(
				instance.getCurrentPowerLevel().getData() >= 0.0 && instance.getCurrentPowerLevel().getData() <=
									instance.getMaxPowerLevel().getData(),
									MachineCafeAutomatiqueExternalControlI.class, instance,
				"instance.getCurrentPowerLevel().getData() >= 0 && getCurrentPowerLevel().getData() <= "
				+ "getMaxPowerLevel().getData()");
		ret &= AssertionChecking.checkInvariant(
		        instance.getCurrentPowerLevel().getMeasurementUnit().equals(POWER_UNIT),
		        MachineCafeAutomatiqueExternalControlI.class, instance,
		        "instance.getCurrentPowerLevel().getMeasurementUnit().equals(POWER_UNIT)");
		ret &= AssertionChecking.checkInvariant(
		        instance.isSuspendable() ==
		            (instance.getState() == MachineCafeAutomatiqueState.AWAITING
		             || instance.getState() == MachineCafeAutomatiqueState.READY_HOT
		             || instance.getState() == MachineCafeAutomatiqueState.ECO_STANDBY),
		        MachineCafeAutomatiqueExternalControlI.class, instance,
		        "		        instance.isSuspendable() ==\r\n"
		        + "		            (instance.getState() == MachineCafeAutomatiqueState.AWAITING\r\n"
		        + "		             || instance.getState() == MachineCafeAutomatiqueState.READY_HOT\r\n"
		        + "		             || instance.getState() == MachineCafeAutomatiqueState.ECO_STANDBY)");
		ret &= AssertionChecking.checkInvariant(
				instance.getMaxPowerLevel().getData().equals(MAX_POWER_LEVEL.getData()),
		        MachineCafeAutomatiqueExternalControlI.class, instance,
		        "instance.getMaxPowerLevel().getData().equals(MAX_POWER_LEVEL.getData())");
		ret &= AssertionChecking.checkInvariant(
				!(instance.on() && !instance.isSuspendable()) || instance.getCurrentPowerLevel().getData() > 0.0,
		        MachineCafeAutomatiqueExternalControlI.class, instance,
		        "!(instance.on() && !instance.isSuspendable()) || instance.getCurrentPowerLevel().getData() > 0.0");
		
		return ret;
	}
	
	public Measure<Double>	getMaxPowerLevel();
	
	public TimedMeasure<Double>	getCurrentPowerLevel();

	public void	setCurrentPowerLevel(Measure<Double> powerLevel);
	
	public boolean isSuspendable();
	

}
