package equipements.uncontrolable.connections;

import equipements.uncontrolable.AspirateurClassiqueImplementationI;
import equipements.uncontrolable.AspirateurClassiqueMode;
import equipements.uncontrolable.AspirateurClassiqueState;
import equipements.uncontrolable.AspirateurClassiqueUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * The class <code>AspirateurClassiqueInboundPort</code> implements an inbound
 * port for the <code>AspirateurClassiqueUserCI</code> component interface.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public class AspirateurClassiqueInboundPort
extends		AbstractInboundPort
implements	AspirateurClassiqueUserCI
{
	private static final long serialVersionUID = 1L;

	public AspirateurClassiqueInboundPort(ComponentI owner) throws Exception
	{
		super(AspirateurClassiqueUserCI.class, owner);
		assert	owner instanceof AspirateurClassiqueImplementationI :
				new PreconditionException(
					"owner instanceof AspirateurClassiqueImplementationI");
	}

	public AspirateurClassiqueInboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, AspirateurClassiqueUserCI.class, owner);
		assert	owner instanceof AspirateurClassiqueImplementationI :
				new PreconditionException(
					"owner instanceof AspirateurClassiqueImplementationI");
	}

	@Override
	public AspirateurClassiqueState	getState() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((AspirateurClassiqueImplementationI)o).getState());
	}

	@Override
	public AspirateurClassiqueMode	getMode() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((AspirateurClassiqueImplementationI)o).getMode());
	}

	@Override
	public boolean		isMopActive() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((AspirateurClassiqueImplementationI)o).isMopActive());
	}

	@Override
	public void			turnOn() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).turnOn();
						return null;
				});
	}

	@Override
	public void			turnOff() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).turnOff();
						return null;
				});
	}

	@Override
	public void			setHigh() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).setHigh();
						return null;
				});
	}

	@Override
	public void			setLow() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).setLow();
						return null;
				});
	}

	@Override
	public void			activateMop() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).activateMop();
						return null;
				});
	}

	@Override
	public void			deactivateMop() throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((AspirateurClassiqueImplementationI)o).deactivateMop();
						return null;
				});
	}
}
