package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueExternalControlCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Port sortant de l'interface de contrôle externe : c'est par lui qu'un
 * client (le HEM ou un tester) appelle la machine.
 */
public class MachineCafeAutomatiqueExternalControlOutboundPort
extends		AbstractOutboundPort
implements	MachineCafeAutomatiqueExternalControlCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueExternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueExternalControlCI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueExternalControlOutboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, MachineCafeAutomatiqueExternalControlCI.class, owner);
	}

	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.getConnector()).
																		on();
	}

	@Override
	public Measure<Double> getMaxPowerLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.getConnector()).
															getMaxPowerLevel();
	}

	@Override
	public TimedMeasure<Double> getCurrentPowerLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.getConnector()).
														getCurrentPowerLevel();
	}

	@Override
	public void setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		((MachineCafeAutomatiqueExternalControlCI)this.getConnector()).
												setCurrentPowerLevel(powerLevel);
	}
}
