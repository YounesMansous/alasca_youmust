package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueUserCI;
import equipements.modulable.MachineCafeAutomatiqueUserI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * Port entrant de l'interface utilisateur : il reçoit les appels et les
 * transmet au composant.
 */
public class MachineCafeAutomatiqueUserInboundPort
extends		AbstractInboundPort
implements	MachineCafeAutomatiqueUserCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueUserInboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueUserCI.class, owner);
		assert	owner instanceof MachineCafeAutomatiqueUserI :
				new PreconditionException(
						"owner instanceof MachineCafeAutomatiqueUserI");
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueUserInboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, MachineCafeAutomatiqueUserCI.class, owner);
		assert	owner instanceof MachineCafeAutomatiqueUserI :
				new PreconditionException(
						"owner instanceof MachineCafeAutomatiqueUserI");
	}

	@Override
	public void switchOff() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).switchOff();
						return null;
				});
	}

	@Override
	public void chooseCoffeeType(MachineCafeAutomatiqueOption choice) throws Exception {
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).chooseCoffeeType(choice);
						return null;
				});
		
	}

	@Override
	public void chooseCoffeeQuality(MachineCafeAutomatiqueOption choice) throws Exception {
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).chooseCoffeeQuality(choice);
						return null;
				});
		
	}

	@Override
	public void refillWater() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).refillWater();
						return null;
				});
		
	}

	@Override
	public void refillMilk() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).refillMilk();
						return null;
				});
		
	}

	@Override
	public void refillBeans() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).refillBeans();
						return null;
				});
		
	}
}
