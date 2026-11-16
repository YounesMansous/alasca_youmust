package equipements.modulable;

import fr.sorbonne_u.alasca.physical_data.Measure;
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
	
	public MachineCafeAutomatiqueState getState() throws Exception;
	
	public MachineCafeAutomatiqueDrink getSelectedDrink() throws Exception;

	public MachineCafeAutomatiqueIntensity getSelectedIntensity() throws Exception;

	public MachineCafeAutomatiqueSize getSelectedSize() throws Exception;
	
	public Measure<Double> getTargetTemperature() throws Exception;
	
	public TimedMeasure<Double> getWaterTankLevel() throws Exception;
	
	public TimedMeasure<Double> getMilkTankLevel() throws Exception;
	
	public TimedMeasure<Double> getBeansTanklevel() throws Exception;
	
	public TimedMeasure<Double> getCurrentTemperature() throws Exception;
	
	public boolean isHeating() throws Exception;
	
	public boolean startHeating() throws Exception;
	
	public boolean endHeating() throws Exception;
	
	public boolean isAwaiting() throws Exception;
	
	public boolean startWarming() throws Exception;
	
	public boolean isWarmingUp() throws Exception;
	
	public boolean startRinsing() throws Exception;
	
	public boolean isRinsing() throws Exception;
	
	public boolean startGrinding() throws Exception;
	
	public boolean isGrinding() throws Exception;
	
	public boolean startInfusing() throws Exception;
	
	public boolean isInfusing() throws Exception;
	
	public boolean startBrewing() throws Exception;
	
	public boolean isBrewing() throws Exception;
	
	public boolean startSteaming() throws Exception;
	
	public boolean isSteaming() throws Exception;
	
	public boolean startReady() throws Exception;
	
	public boolean isReady() throws Exception;
	
	public boolean startEcoStandBy() throws Exception;
	
	public boolean isEcoStandBy() throws Exception;

}
