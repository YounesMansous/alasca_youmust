package equipements.modulable;

public interface MachineCafeAutomatiqueInternalControlI extends 
MachineCafeAutomatiqueStateI{

	
	public static boolean staticInvariant() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public boolean isAwaiting();
	
	public boolean startReadyState();
	
	public boolean isReady();
	
	public boolean isHeating();
	
	public boolean startHeating();
	
	public boolean isBrewing();
	
	public boolean startBrewing();

}
