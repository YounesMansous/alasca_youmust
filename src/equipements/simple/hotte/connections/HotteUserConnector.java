package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteMode;
import equipements.simple.hotte.HotteState;
import equipements.simple.hotte.HotteUserCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class HotteUserConnector 
extends		AbstractConnector
implements	HotteUserCI{

	@Override
	public HotteState	getState() throws Exception
	{
		return ((HotteUserCI)this.offering).getState();
	}
	
	@Override
	public	HotteMode	getMode() throws Exception
	{
		return ((HotteUserCI)this.offering).getMode();
	}
	
	@Override
	public void			turnOn() throws Exception
	{
		((HotteUserCI)this.offering).turnOn();
	}
	
	@Override
	public void			turnOff() throws Exception
	{
		((HotteUserCI)this.offering).turnOff();
	}
	
	@Override
	public void			setMode(HotteMode m) throws Exception
	{
		((HotteUserCI)this.offering).setMode(m);
	}
	
}
