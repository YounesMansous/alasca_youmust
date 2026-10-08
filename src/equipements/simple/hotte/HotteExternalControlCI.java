package equipements.simple.hotte;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface HotteExternalControlCI 
extends		RequiredCI,
			OfferedCI,
			HotteConfigurationI{
	
	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= HotteConfigurationI.staticInvariants();
		return ret;
	}

	public static boolean	invariants(HotteExternalControlCI instance) throws Exception
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= HotteExternalControlCI.staticInvariants();
		ret &= AssertionChecking.checkInvariant(
				!instance.on() || instance.getMode()!=null ,
									HotteExternalControlCI.class, instance,
									"!on() || getMode()!=null");
		return ret;
	}
	
	public boolean		on() throws Exception;

	public HotteMode getMode() throws Exception;
	
	public void setMode(HotteMode m) throws Exception;
	
	public Measure<Double>	getModePower(HotteMode m) throws Exception;
	
	public void turnOn() throws Exception;

	public void turnOff() throws Exception;

}
