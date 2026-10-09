package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface RefrigerateurExternalControlCI 
extends		RequiredCI,
			OfferedCI,
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
	
	public void setMode(RefrigerateurMode m) throws Exception;
	
	public Measure<Double>	getModePower(RefrigerateurMode m) 
			throws Exception;

	public void suspend() throws Exception;

	public void resume() throws Exception;

	public boolean suspended() throws Exception;

}
