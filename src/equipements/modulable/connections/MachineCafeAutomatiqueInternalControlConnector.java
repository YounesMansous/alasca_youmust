package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueDrink;
import equipements.modulable.MachineCafeAutomatiqueIntensity;
import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueSize;
import equipements.modulable.MachineCafeAutomatiqueState;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * Connecteur de l'interface de contrôle interne : il relie le port sortant
 * du contrôleur au port entrant de la machine.
 */
public class MachineCafeAutomatiqueInternalControlConnector
extends		AbstractConnector
implements	MachineCafeAutomatiqueInternalControlCI
{
	// "offering" est le port entrant de la machine

	// Lectures de l'état
	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).on();
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																	getState();
	}

	@Override
	public MachineCafeAutomatiqueDrink getSelectedDrink() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															getSelectedDrink();
	}

	@Override
	public MachineCafeAutomatiqueIntensity getSelectedIntensity() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
														getSelectedIntensity();
	}

	@Override
	public MachineCafeAutomatiqueSize getSelectedSize() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															getSelectedSize();
	}

	@Override
	public Measure<Double> getTargetTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
														getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
														getCurrentTemperature();
	}

	@Override
	public TimedMeasure<Double> getWaterTankLevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				getWaterTankLevel();
	}

	@Override
	public TimedMeasure<Double> getMilkTankLevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				getMilkTankLevel();
	}

	@Override
	public TimedMeasure<Double> getBeansTanklevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				getBeansTanklevel();
	}

	@Override
	public boolean isHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isHeating();
	}

	@Override
	public boolean startHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startHeating();
	}

	@Override
	public boolean endHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				endHeating();
	}

	@Override
	public boolean isAwaiting() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isAwaiting();
	}

	@Override
	public boolean startWarming() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startWarming();
	}

	@Override
	public boolean isWarmingUp() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isWarmingUp();
	}

	@Override
	public boolean startRinsing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startRinsing();
	}

	@Override
	public boolean isRinsing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isRinsing();
	}

	@Override
	public boolean startGrinding() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startGrinding();
	}

	@Override
	public boolean isInfusing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isInfusing();
	}

	@Override
	public boolean startInfusing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startInfusing();
	}

	@Override
	public boolean isBrewing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isBrewing();
	}

	@Override
	public boolean startBrewing() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startBrewing();
	}

	@Override
	public boolean startSteaming() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startSteaming();
	}

	@Override
	public boolean isSteaming() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isSteaming();
	}

	@Override
	public boolean startReady() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startReady();
	}

	@Override
	public boolean isReady() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isReady();
	}

	@Override
	public boolean startEcoStandBy() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				startEcoStandBy();
	}

	@Override
	public boolean isEcoStandBy() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isEcoStandBy();
	}

	@Override
	public boolean isGrinding() throws Exception{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
				isGrinding();
	}

}
