package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueExternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueExternalControlI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * Port entrant de l'interface de contrôle externe : il reçoit les appels du
 * HEM et les transmet au composant.
 */
public class MachineCafeAutomatiqueExternalControlInboundPort
extends		AbstractInboundPort
implements	MachineCafeAutomatiqueExternalControlCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueExternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		this(MachineCafeAutomatiqueExternalControlCI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueExternalControlInboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		this(uri, MachineCafeAutomatiqueExternalControlCI.class, owner);
	}

	// Constructeur avec l'interface à implémenter (utilisé par la version Java4)
	public MachineCafeAutomatiqueExternalControlInboundPort(
		Class<? extends MachineCafeAutomatiqueExternalControlCI> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(implementedInterface, owner);
		assert	owner instanceof MachineCafeAutomatiqueExternalControlI :
				new PreconditionException(
					"owner instanceof MachineCafeAutomatiqueExternalControlI");
	}

	// Constructeur avec URI et l'interface à implémenter
	public MachineCafeAutomatiqueExternalControlInboundPort(
		String uri,
		Class<? extends MachineCafeAutomatiqueExternalControlCI> implementedInterface,
		ComponentI owner
		) throws Exception
	{
		super(uri, implementedInterface, owner);
		assert	owner instanceof MachineCafeAutomatiqueExternalControlI :
				new PreconditionException(
					"owner instanceof MachineCafeAutomatiqueExternalControlI");
	}

	@Override
	public boolean on() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).on());
	}

	@Override
	public Measure<Double> getMaxPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).
														getMaxPowerLevel());
	}

	@Override
	public TimedMeasure<Double> getCurrentPowerLevel() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).
														getCurrentPowerLevel());
	}

	@Override
	public void setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueExternalControlI)o).
												setCurrentPowerLevel(powerLevel);
						return null;
				});
	}

	@Override
	public boolean isSuspendable() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).
														isSuspendable());
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).
				getCurrentTemperature());
	}

	@Override
	public Measure<Double> getTargetTemperature() throws Exception {
		// TODO Auto-generated method stub
		return this.getOwner().handleRequest(
				o -> ((MachineCafeAutomatiqueExternalControlI)o).
				getTargetTemperature());
	}
}
