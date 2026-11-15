package equipements.modulable.connections;

import java.util.ArrayList;

import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueState;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Port sortant de l'interface de contrôle interne : c'est par lui que le
 * contrôleur local appelle la machine.
 */
public class MachineCafeAutomatiqueInternalControlOutboundPort
extends		AbstractOutboundPort
implements	MachineCafeAutomatiqueInternalControlCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueInternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueInternalControlCI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueInternalControlOutboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, MachineCafeAutomatiqueInternalControlCI.class, owner);
	}

	// Lectures de l'état
	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																		on();
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																	getState();
	}

	@Override
	public ArrayList<MachineCafeAutomatiqueOption> getSelectedOptions() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															getSelectedOptions();
	}

	@Override
	public Measure<Double> getTargetTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
														getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
														getCurrentTemperature();
	}

	@Override
	public TimedMeasure<Double> getWaterTankLevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				getWaterTankLevel();
	}

	@Override
	public TimedMeasure<Double> getMilkTankLevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				getMilkTankLevel();
	}

	@Override
	public TimedMeasure<Double> getBeansTanklevel() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				getBeansTanklevel();
	}

	@Override
	public boolean isHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isHeating();
	}

	@Override
	public boolean startHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startHeating();
	}

	@Override
	public boolean endHeating() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				endHeating();
	}

	@Override
	public boolean isAwaiting() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isAwaiting();
	}

	@Override
	public boolean startWarming() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startWarming();
	}

	@Override
	public boolean isWarmingUp() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isWarmingUp();
	}

	@Override
	public boolean startRinsing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startRinsing();
	}

	@Override
	public boolean isRinsing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isRinsing();
	}

	@Override
	public boolean startGrinding() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startGrinding();
	}

	@Override
	public boolean isInfusing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isInfusing();
	}

	@Override
	public boolean startInfusing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startInfusing();
	}

	@Override
	public boolean isBrewing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isBrewing();
	}

	@Override
	public boolean startBrewing() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startBrewing();
	}

	@Override
	public boolean startSteaming() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startSteaming();
	}

	@Override
	public boolean isSteaming() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isSteaming();
	}

	@Override
	public boolean startReady() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startReady();
	}

	@Override
	public boolean isReady() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isReady();
	}

	@Override
	public boolean startEcoStandBy() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				startEcoStandBy();
	}

	@Override
	public boolean isEcoStandBy() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isEcoStandBy();
	}

	@Override
	public boolean isGrinding() throws Exception {
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
				isGrinding();
	}


}
