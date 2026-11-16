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
	
	public void chooseCoffeeType(MachineCafeAutomatiqueDrink choice) throws Exception;

	public void chooseCoffeeIntensity(MachineCafeAutomatiqueIntensity choice) throws Exception;

	public void chooseCoffeeSize(MachineCafeAutomatiqueSize choice) throws Exception;
	
	public void refillWater() throws Exception;
	
	public void refillMilk() throws Exception;
	
	public void refillBeans() throws Exception;
	
	void switchOff() throws Exception;
	
	
}
