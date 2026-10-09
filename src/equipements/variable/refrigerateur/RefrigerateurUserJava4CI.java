package equipements.variable.refrigerateur;


public interface RefrigerateurUserJava4CI 
extends RefrigerateurUserCI{
	/* Convention des modes en int (identique à AdjustableCI) :
	 * 1 = ECO, 2 = NORMAL, 3 = RAPIDE */

	public void			setTargetTemperatureJava4(double target)
	throws Exception;
	
	public double	getTargetTemperatureJava4() throws Exception;

	public void			setModeJava4(int im	) throws Exception;
	
	public int	getModeJava4() throws Exception;

	public double	getCurrentTemperatureJava4() throws Exception;
	

}
