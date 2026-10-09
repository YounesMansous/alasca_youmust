package equipements.variable.refrigerateur;


public interface RefrigerateurInternalControlI 
extends RefrigerateurStateI{
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= RefrigerateurStateI.staticInvariants();
		return ret;
	}
	
	public boolean		cooling() throws Exception;

	public void			startCooling() throws Exception;

	public void			stopCooling() throws Exception;


}
