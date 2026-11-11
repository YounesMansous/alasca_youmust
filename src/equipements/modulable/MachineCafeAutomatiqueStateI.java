package equipements.modulable;

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
		ret &= AssertionChecking.checkInvariant(true, MachineCafeAutomatiqueStateI.class, instance, "");
		return ret;
	}
	
	public boolean on();
	
	public boolean isCapsulIn();
	
	public MachineCafeAutomatiqueState getState();
	
	public MachineCafeAutomatiqueOption getSelectedOption();
	
	public TimedMeasure<Double> getTargetTemperature();
	
	public TimedMeasure<Double> getTankLevel();
	
	public TimedMeasure<Double> getCurrentTemperature();
}
