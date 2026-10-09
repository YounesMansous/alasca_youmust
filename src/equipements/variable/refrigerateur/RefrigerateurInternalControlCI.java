package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface RefrigerateurInternalControlCI 
extends		OfferedCI,
			RequiredCI,
			RefrigerateurConfigurationI{
	
	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= RefrigerateurConfigurationI.staticInvariants();
		return ret;
	}
	
	public boolean		on() throws Exception;
	
	public TimedMeasure<Double>	getTargetTemperature() throws Exception;

	public TimedMeasure<Double>	getCurrentTemperature() throws Exception;

	public RefrigerateurMode	getMode() throws Exception;
	
	public boolean		cooling() throws Exception;

	public void			startCooling() throws Exception;

	public void			stopCooling() throws Exception;


}
