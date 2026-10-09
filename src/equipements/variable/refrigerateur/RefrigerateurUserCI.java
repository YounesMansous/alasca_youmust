package equipements.variable.refrigerateur;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface RefrigerateurUserCI 
extends		OfferedCI,
			RequiredCI,
			RefrigerateurConfigurationI{

	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= RefrigerateurConfigurationI.staticInvariants();
		return ret;
	}
	
	public void			switchOn() throws Exception;

	public void			switchOff() throws Exception;

	public void			setTargetTemperature(Measure<Double> target)
	throws Exception;
	
	public void			setMode(RefrigerateurMode m	) throws Exception;

	public boolean		on() throws Exception;

	public TimedMeasure<Double>	getTargetTemperature() throws Exception;

	public TimedMeasure<Double>	getCurrentTemperature() throws Exception;

	public RefrigerateurMode	getMode() throws Exception;

}
