package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteImplementationI;
import equipements.simple.hotte.HotteMode;
import equipements.simple.hotte.HotteState;
import equipements.simple.hotte.HotteUserCI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

public class HotteInboundPort
extends		AbstractInboundPort
implements	HotteUserCI{
	
	private static final long serialVersionUID = 1L;

	public				HotteInboundPort(ComponentI owner) throws Exception
	{
		super(HotteUserCI.class, owner);
		assert	owner instanceof HotteImplementationI :
				new PreconditionException(
						"owner instanceof HotteImplementationI");
	}
	
	public				HotteInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			super(uri, HotteUserCI.class, owner);
			assert	owner instanceof HotteImplementationI :
					new PreconditionException(
							"owner instanceof HotteImplementationI");
		}
	
	@Override
	public HotteState	getState() throws Exception
	{
		return this.getOwner().handleRequest(
							o -> ((HotteImplementationI)o).getState());
	}
	
	@Override
	public HotteMode	getMode() throws Exception
	{
		return this.getOwner().handleRequest(
							o -> ((HotteImplementationI)o).getMode());
	}
	
	@Override
	public void			turnOn() throws Exception
	{
		this.getOwner().handleRequest(
							o -> {	((HotteImplementationI)o).turnOn();
									return null;
							});
	}
	
	@Override
	public void			turnOff() throws Exception
	{
		this.getOwner().handleRequest(
							o -> {	((HotteImplementationI)o).turnOff();
									return null;
							});
	}
	
	@Override
	public void			setMode(HotteMode m) throws Exception
	{
		this.getOwner().handleRequest(
							o -> {	((HotteImplementationI)o).setMode(m);
									return null;
							});
	}
	
}
