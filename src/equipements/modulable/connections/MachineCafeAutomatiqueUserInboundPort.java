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

	// Chaque méthode appelle la même méthode sur le composant
	@Override
	public void orderCoffee(MachineCafeAutomatiqueOption choice)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).orderCoffee(choice);
						return null;
				});
	}

	@Override
	public void insertCapsule() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).insertCapsule();
						return null;
				});
	}

	@Override
	public void refillTank() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).refillTank();
						return null;
				});
	}

	@Override
	public void switchOff() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((MachineCafeAutomatiqueUserI)o).switchOff();
						return null;
				});
	}
}
