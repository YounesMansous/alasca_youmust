package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface RefrigerateurExternalControlI 
extends RefrigerateurStateI{

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= RefrigerateurStateI.staticInvariants();
		return ret;
	}
	
	public static boolean	invariants(RefrigerateurExternalControlI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= RefrigerateurExternalControlI.staticInvariants();
		ret &= AssertionChecking.checkInvariant(
				!instance.on() || instance.getMode ()!=null,
									RefrigerateurExternalControlI.class, instance,
				"!on() || getMode ()!=null");
		return ret;
	}
		
	public void setMode(RefrigerateurMode m);
	
	public Measure<Double>	getModePower(RefrigerateurMode m);

	public void suspend();

	public void resume();

	public boolean suspended();
}
