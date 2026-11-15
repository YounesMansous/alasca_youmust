package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface MachineCafeAutomatiqueExternalControlCI extends
OfferedCI,
RequiredCI,
MachineCafeAutomatiqueConfigurationI{

	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
	}
	
	
	public boolean on() throws Exception;
	
	public Measure<Double>	getMaxPowerLevel() throws Exception;
	
	public TimedMeasure<Double>	getCurrentPowerLevel() throws Exception;

	public void	setCurrentPowerLevel(Measure<Double> powerLevel) throws Exception;

	public boolean isSuspendable() throws Exception;
	
	public TimedMeasure<Double> getCurrentTemperature() throws Exception;
	
	public Measure<Double> getTargetTemperature() throws Exception;
}
