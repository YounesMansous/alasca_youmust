package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueDrink;
import equipements.modulable.MachineCafeAutomatiqueIntensity;
import equipements.modulable.MachineCafeAutomatiqueSize;
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
	public void chooseCoffeeType(MachineCafeAutomatiqueDrink choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).chooseCoffeeType(choice);

	}

	@Override
	public void chooseCoffeeIntensity(MachineCafeAutomatiqueIntensity choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).chooseCoffeeIntensity(choice);

	}

	@Override
	public void chooseCoffeeSize(MachineCafeAutomatiqueSize choice) throws Exception {
		((MachineCafeAutomatiqueUserCI)this.offering).chooseCoffeeSize(choice);

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
