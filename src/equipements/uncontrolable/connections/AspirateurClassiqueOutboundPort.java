package equipements.uncontrolable.connections;

import equipements.uncontrolable.AspirateurClassiqueMode;
import equipements.uncontrolable.AspirateurClassiqueState;
import equipements.uncontrolable.AspirateurClassiqueUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * The class <code>AspirateurClassiqueOutboundPort</code> implements an
 * outbound port for the <code>AspirateurClassiqueUserCI</code> component
 * interface.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public class AspirateurClassiqueOutboundPort
extends		AbstractOutboundPort
implements	AspirateurClassiqueUserCI
{
	private static final long serialVersionUID = 1L;

	public AspirateurClassiqueOutboundPort(ComponentI owner) throws Exception
	{
		super(AspirateurClassiqueUserCI.class, owner);
	}

	public AspirateurClassiqueOutboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, AspirateurClassiqueUserCI.class, owner);
	}

	@Override
	public AspirateurClassiqueState	getState() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.getConnector()).getState();
	}

	@Override
	public AspirateurClassiqueMode	getMode() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.getConnector()).getMode();
	}

	@Override
	public boolean		isMopActive() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.getConnector()).isMopActive();
	}

	@Override
	public void			turnOn() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).turnOn();
	}

	@Override
	public void			turnOff() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).turnOff();
	}

	@Override
	public void			setHigh() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).setHigh();
	}

	@Override
	public void			setLow() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).setLow();
	}

	@Override
	public void			activateMop() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).activateMop();
	}

	@Override
	public void			deactivateMop() throws Exception
	{
		((AspirateurClassiqueUserCI)this.getConnector()).deactivateMop();
	}
}
