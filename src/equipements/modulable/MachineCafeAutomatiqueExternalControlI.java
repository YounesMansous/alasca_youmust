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
		ret &= AssertionChecking.checkInvariant(
				!instance.on() || instance.getCurrentPowerLevel().getData() <=
									instance.getMaxPowerLevel().getData(),
									MachineCafeAutomatiqueExternalControlI.class, instance,
				"!on() || getCurrentPowerLevel().getData() <= "
				+ "getMaxPowerLevel().getData()");
		return ret;
	}
	
	public Measure<Double>	getMaxPowerLevel();
	
	public TimedMeasure<Double>	getCurrentPowerLevel();

	public void	setCurrentPowerLevel(Measure<Double> powerLevel);
	
	public boolean isSuspendable();
	

}
