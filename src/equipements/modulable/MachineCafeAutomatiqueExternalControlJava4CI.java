package equipements.modulable;


public interface MachineCafeAutomatiqueExternalControlJava4CI extends
MachineCafeAutomatiqueExternalControlCI{
	
	
	public boolean on() throws Exception;
	
	public double getMaxPowerLevelJava4() throws Exception;
	
	public double getCurrentPowerLevelJava4() throws Exception;

	public void	setCurrentPowerLevelJava4(double powerLevel)
	throws Exception;
}
