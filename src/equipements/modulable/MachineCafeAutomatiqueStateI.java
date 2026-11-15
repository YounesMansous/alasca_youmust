package equipements.modulable;

import java.util.ArrayList;

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
		ret &= AssertionChecking.checkInvariant(true, MachineCafeAutomatiqueStateI.class, instance, "");
		return ret;
	}
	
	public MachineCafeAutomatiqueState getState();
	
	public ArrayList<MachineCafeAutomatiqueOption> getSelectedOptions();
	
	public Measure<Double> getTargetTemperature();
	
	public TimedMeasure<Double> getCurrentTemperature();
	
	public TimedMeasure<Double> getWaterTankLevel();
	
	public TimedMeasure<Double> getMilkTankLevel();
	
	public TimedMeasure<Double> getBeansTanklevel();
	
	public boolean on();
}
