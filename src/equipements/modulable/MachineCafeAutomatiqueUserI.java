package equipements.modulable;

public interface MachineCafeAutomatiqueUserI extends 
MachineCafeAutomatiqueStateI{
	
	
	public static boolean staticInvariant() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public void chooseCoffeeType(MachineCafeAutomatiqueOption choice);
	
	public void chooseCoffeeQuality(MachineCafeAutomatiqueOption choice);
	
	public void refillWater();
	
	public void refillMilk();
	
	public void refillBeans();
	
	public void switchOff();
	
	
}
