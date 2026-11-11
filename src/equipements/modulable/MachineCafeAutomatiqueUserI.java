package equipements.modulable;

public interface MachineCafeAutomatiqueUserI extends 
MachineCafeAutomatiqueStateI{
	
	
	public static boolean staticInvariant() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public void orderCoffee(MachineCafeAutomatiqueOption choice);
	
	public void insertCapsule();
	
	public void refillTank();
	
	public void switchOff();
	
	
}
