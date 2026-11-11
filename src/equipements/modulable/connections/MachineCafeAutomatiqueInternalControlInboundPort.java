package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueInternalControlI;
import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueState;
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
	public boolean isCapsulIn() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).isCapsulIn());
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getState());
	}

	@Override
	public MachineCafeAutomatiqueOption getSelectedOption() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
														getSelectedOption());
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
														getTargetTemperature());
	}

	@Override
	public TimedMeasure<Double> getTankLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).getTankLevel());
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

	// Passage à la phase suivante
	@Override
	public boolean startReadyState() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueInternalControlI)o).
														startReadyState());
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
}
