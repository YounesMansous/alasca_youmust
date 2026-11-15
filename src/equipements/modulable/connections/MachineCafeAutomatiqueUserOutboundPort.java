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

	@Override
	public void switchOff() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.getConnector()).switchOff();
	}

	@Override
	public void chooseCoffeeType(MachineCafeAutomatiqueOption choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.getConnector()).chooseCoffeeType(choice);
		
	}

	@Override
	public void chooseCoffeeQuality(MachineCafeAutomatiqueOption choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.getConnector()).chooseCoffeeQuality(choice);
		
	}

	@Override
	public void refillWater() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.getConnector()).refillWater();
		
	}

	@Override
	public void refillMilk() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.getConnector()).refillMilk();
		
	}

	@Override
	public void refillBeans() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.getConnector()).refillBeans();
		
	}
}
