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
	
	public void chooseCoffeeType(MachineCafeAutomatiqueOption choice) throws Exception;
	
	public void chooseCoffeeQuality(MachineCafeAutomatiqueOption choice) throws Exception;
	
	public void refillWater() throws Exception;
	
	public void refillMilk() throws Exception;
	
	public void refillBeans() throws Exception;
	
	void switchOff() throws Exception;
	
	
}
