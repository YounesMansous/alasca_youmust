package equipements.modulable;

public interface MachineCafeAutomatiqueInternalControlI extends 
MachineCafeAutomatiqueStateI{

	
	public static boolean staticInvariant() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public boolean isHeating();
	
	public boolean startHeating();
	
	public boolean endHeating();
	
	public boolean isAwaiting();
	
	public boolean startWarming();
	
	public boolean isWarmingUp();
	
	public boolean startRinsing();
	
	public boolean isRinsing();
	
	public boolean startGrinding();
	
	public boolean isGrinding();
	
	public boolean isInfusing();
	
	public boolean startInfusing();
	
	public boolean isBrewing();
	
	public boolean startBrewing();
	
	public boolean startSteaming();
	
	public boolean isSteaming();
	
	public boolean startReady();
	
	public boolean isReady();
	
	public boolean startEcoStandBy();
	
	public boolean isEcoStandBy();

}
