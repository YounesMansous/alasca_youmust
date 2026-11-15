package equipements.modulable.connections;

import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueInternalControlI;
import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueState;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * Port entrant de l'interface de contrôle interne : il reçoit les appels du
 * contrôleur local et les transmet au composant.
 */
public class MachineCafeAutomatiqueInternalControlInboundPort
extends		AbstractInboundPort
implements	MachineCafeAutomatiqueInternalControlCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueInternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueInternalControlCI.class, owner);
		assert	owner instanceof MachineCafeAutomatiqueInternalControlI :
				new PreconditionException(
					"owner instanceof MachineCafeAutomatiqueInternalControlI");
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueInternalControlInboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, MachineCafeAutomatiqueInternalControlCI.class, owner);
		assert	owner instanceof MachineCafeAutomatiqueInternalControlI :
				new PreconditionException(
					"owner instanceof MachineCafeAutomatiqueInternalControlI");
	}

	// Lectures de l'état
	@Override
	public boolean on() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).on());
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getState());
	}

	@Override
	public ArrayList<MachineCafeAutomatiqueOption> getSelectedOptions() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
				getSelectedOptions());
	}

	@Override
	public Measure<Double> getTargetTemperature() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
														getTargetTemperature());
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
														getCurrentTemperature());
	}

	// Test de la phase courante
	@Override
	public boolean isAwaiting() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isAwaiting());
	}

	@Override
	public boolean isReady() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isReady());
	}

	@Override
	public boolean isHeating() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isHeating());
	}

	@Override
	public boolean isBrewing() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isBrewing());
	}

	@Override
	public boolean startHeating() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startHeating());
	}

	@Override
	public boolean startBrewing() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startBrewing());
	}

	@Override
	public TimedMeasure<Double> getWaterTankLevel() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getWaterTankLevel());
	}

	@Override
	public TimedMeasure<Double> getMilkTankLevel() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getMilkTankLevel());
	}

	@Override
	public TimedMeasure<Double> getBeansTanklevel() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getBeansTanklevel());
	}

	@Override
	public boolean endHeating() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).endHeating());
	}

	@Override
	public boolean startWarming() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startWarming());
	}

	@Override
	public boolean isWarmingUp() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isWarmingUp());
	}

	@Override
	public boolean startRinsing() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startRinsing());
	}

	@Override
	public boolean isRinsing() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isRinsing());
	}

	@Override
	public boolean startGrinding() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startGrinding());
	}

	@Override
	public boolean isInfusing() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isInfusing());
	}

	@Override
	public boolean startInfusing() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startInfusing());
	}

	@Override
	public boolean startSteaming() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startSteaming());
	}

	@Override
	public boolean isSteaming() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isSteaming());
	}

	@Override
	public boolean startReady() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startReady());
	}

	@Override
	public boolean startEcoStandBy() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).startEcoStandBy());
	}

	@Override
	public boolean isEcoStandBy() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isEcoStandBy());
	}

	@Override
	public boolean isGrinding() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isGrinding());
	}
}
