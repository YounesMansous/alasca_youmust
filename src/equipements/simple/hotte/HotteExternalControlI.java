package equipements.simple.hotte;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface HotteExternalControlI 
extends HotteConfigurationI
{
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= HotteConfigurationI.staticInvariants();
		return ret;
	}

	public static boolean	invariants(HotteExternalControlI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= HotteExternalControlI.staticInvariants();
		ret &= AssertionChecking.checkInvariant(
				!instance.on() || instance.getMode()!=null ,
									HotteExternalControlI.class, instance,
				"!on() || getMode()!=null");
		return ret;
	}
	
	public boolean		on();

	public HotteMode getMode();
	
	public void setMode(HotteMode m);
	
	public Measure<Double>	getModePower(HotteMode m);

	public void turnOn();

	public void turnOff();

	
}
