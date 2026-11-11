package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface MachineCafeAutomatiqueInternalControlCI extends
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
	
	public boolean isCapsulIn() throws Exception;
	
	public MachineCafeAutomatiqueState getState() throws Exception;
	
	public MachineCafeAutomatiqueOption getSelectedOption() throws Exception;
	
	public TimedMeasure<Double> getTargetTemperature() throws Exception;
	
	public TimedMeasure<Double> getTankLevel() throws Exception;
	
	public TimedMeasure<Double> getCurrentTemperature() throws Exception;
	
	public boolean isAwaiting() throws Exception;
	
	public boolean startReadyState() throws Exception;;
	
	public boolean isReady() throws Exception;;
	
	public boolean isHeating() throws Exception;;
	
	public boolean startHeating() throws Exception;;
	
	public boolean isBrewing() throws Exception;;
	
	public boolean startBrewing() throws Exception;;

}
