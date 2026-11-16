package equipements.modulable;

public interface MachineCafeAutomatiqueUserI extends 
MachineCafeAutomatiqueStateI{
	
	
	public static boolean invariants() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public void chooseCoffeeType(MachineCafeAutomatiqueDrink choice);

	public void chooseCoffeeIntensity(MachineCafeAutomatiqueIntensity choice);

	public void chooseCoffeeSize(MachineCafeAutomatiqueSize choice);
	
	public void refillWater();
	
	public void refillMilk();
	
	public void refillBeans();
	
	public void switchOff();
	
	
}
