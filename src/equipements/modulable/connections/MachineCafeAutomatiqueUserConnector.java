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

	@Override
	public void switchOff() throws Exception
	{
		((MachineCafeAutomatiqueUserCI)this.offering).switchOff();
	}

	@Override
	public void chooseCoffeeType(MachineCafeAutomatiqueOption choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).chooseCoffeeType(choice);
		
	}

	@Override
	public void chooseCoffeeQuality(MachineCafeAutomatiqueOption choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).chooseCoffeeQuality(choice);
		
	}

	@Override
	public void refillWater() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).refillWater();
		
	}

	@Override
	public void refillMilk() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).refillMilk();
		
	}

	@Override
	public void refillBeans() throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).refillBeans();
		
	}
}
