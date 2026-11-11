package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueUserCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * Connecteur de l'interface utilisateur : il relie le port sortant du client
 * au port entrant de la machine.
 */
public class MachineCafeAutomatiqueUserConnector
extends		AbstractConnector
implements	MachineCafeAutomatiqueUserCI
{
	// "offering" est le port entrant de la machine
	@Override
	public void orderCoffee(MachineCafeAutomatiqueOption choice)
	throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.offering).orderCoffee(choice);
	}

	@Override
	public void insertCapsule() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.offering).insertCapsule();
	}

	@Override
	public void refillTank() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.offering).refillTank();
	}

	@Override
	public void switchOff() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.offering).switchOff();
	}
}
