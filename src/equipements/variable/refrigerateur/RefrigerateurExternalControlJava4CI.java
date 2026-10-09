package equipements.variable.refrigerateur;


public interface RefrigerateurExternalControlJava4CI 
extends		RefrigerateurExternalControlCI{
	/* Convention des modes en int (identique à AdjustableCI) :
	 * 1 = ECO, 2 = NORMAL, 3 = RAPIDE */

	public double	getTargetTemperatureJava4() throws Exception;

	public double	getCurrentTemperatureJava4() throws Exception;

	public int	getModeJava4() throws Exception;
	
	public void setModeJava4(int im) throws Exception;
	
	public double	getModePowerJava4(int im) 
			throws Exception;

}
