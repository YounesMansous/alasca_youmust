package equipements.modulable;


public interface MachineCafeAutomatiqueExternalControlJava4CI extends
MachineCafeAutomatiqueExternalControlCI{
	
	public double getMaxPowerLevelJava4() throws Exception;
	
	public double getCurrentPowerLevelJava4() throws Exception;

	public void	setCurrentPowerLevelJava4(double powerLevel) throws Exception;
	
	public double  getCurrentTemperatureJava4() throws Exception;
	
	public double  getTargetTemperatureJava4() throws Exception;
}
