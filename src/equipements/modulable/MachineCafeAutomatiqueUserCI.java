package equipements.modulable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface MachineCafeAutomatiqueUserCI
extends	OfferedCI,
		RequiredCI,
		MachineCafeAutomatiqueConfigurationI
{
	public static boolean staticInvariants() throws Exception{
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;

	}
	
	public void orderCoffee(MachineCafeAutomatiqueOption choice) throws Exception;
	
	public void insertCapsule() throws Exception;
	
	public void refillTank() throws Exception;
	
	public void switchOff() throws Exception;
	
	
}
