package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface RefrigerateurStateI 
extends RefrigerateurConfigurationI{
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= RefrigerateurConfigurationI.staticInvariants();
		return ret;
	}

	public static boolean	invariants(RefrigerateurStateI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= RefrigerateurStateI.staticInvariants();
		ret &= AssertionChecking.checkInvariant(true, RefrigerateurStateI.class, instance, "");
		return ret;
	}
	
	public boolean		on();

	public TimedMeasure<Double>	getTargetTemperature();

	public TimedMeasure<Double>	getCurrentTemperature();

	public RefrigerateurMode	getMode();


}
