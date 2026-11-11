package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Port sortant de l'interface utilisateur : c'est par lui qu'un client
 * (le tester par exemple) appelle la machine.
 */
public class MachineCafeAutomatiqueUserOutboundPort
extends		AbstractOutboundPort
implements	MachineCafeAutomatiqueUserCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueUserOutboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueUserCI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueUserOutboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, MachineCafeAutomatiqueUserCI.class, owner);
	}

	// Chaque méthode passe par le connecteur
	@Override
	public void orderCoffee(MachineCafeAutomatiqueOption choice)
	throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.getConnector()).orderCoffee(choice);
	}

	@Override
	public void insertCapsule() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.getConnector()).insertCapsule();
	}

	@Override
	public void refillTank() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.getConnector()).refillTank();
	}

	@Override
	public void switchOff() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.getConnector()).switchOff();
	}
}
