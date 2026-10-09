package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface RefrigerateurUserI 
extends RefrigerateurStateI{

	public static boolean	invariants(RefrigerateurUserI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= RefrigerateurStateI.invariants(instance);
		return ret;
	}
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= RefrigerateurStateI.staticInvariants();
		return ret;
	}

	
	public void			switchOn();

	public void			switchOff();

	public void			setTargetTemperature(Measure<Double> target);
	
	public void			setMode(RefrigerateurMode m	);

}
