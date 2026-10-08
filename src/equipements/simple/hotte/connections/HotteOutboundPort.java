package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteMode;
import equipements.simple.hotte.HotteState;
import equipements.simple.hotte.HotteUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class HotteOutboundPort 
extends		AbstractOutboundPort
implements	HotteUserCI{

	private static final long serialVersionUID = 1L;

	public				HotteOutboundPort(ComponentI owner)
	throws Exception
	{
		super(HotteUserCI.class, owner);
	}

	public				HotteOutboundPort(String uri, ComponentI owner)
	throws Exception
	{
		super(uri, HotteUserCI.class, owner);
	}
	
	@Override
	public HotteState	getState() throws Exception
	{
		return ((HotteUserCI)this.getConnector()).getState();
	}
	
	@Override
	public HotteMode	getMode() throws Exception
	{
		return ((HotteUserCI)this.getConnector()).getMode();
	}
	
	@Override
	public void			turnOn() throws Exception
	{
		((HotteUserCI)this.getConnector()).turnOn();
	}
	
	@Override
	public void			turnOff() throws Exception
	{
		((HotteUserCI)this.getConnector()).turnOff();
	}

	@Override
	public void			setMode(HotteMode m) throws Exception
	{
		((HotteUserCI)this.getConnector()).setMode(m);
	}
}
