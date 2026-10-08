package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteExternalControlCI;
import equipements.simple.hotte.HotteExternalControlI;
import equipements.simple.hotte.HotteMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

public class HotteExternalControlInboundPort 
extends		AbstractInboundPort
implements HotteExternalControlCI{
	
	private static final long serialVersionUID = 1L;

	public				HotteExternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		this(HotteExternalControlCI.class, owner);
	}
	
	public				HotteExternalControlInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			this(uri, HotteExternalControlCI.class, owner);
		}


	public				HotteExternalControlInboundPort(
			Class<? extends HotteExternalControlCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(implementedInterface, owner);

			assert	implementedInterface != null &&
					HotteExternalControlCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "HotteExternalControlCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof HotteExternalControlI :
					new PreconditionException(
							"owner instanceof HotteExternalControlI");
		}
	
	public				HotteExternalControlInboundPort(
			String uri,
			Class<? extends HotteExternalControlCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(uri, implementedInterface, owner);

			assert	implementedInterface != null &&
					HotteExternalControlCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "HotteExternalControlCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof HotteExternalControlI :
					new PreconditionException(
							"owner instanceof HotteExternalControlI");
		}
	
	@Override
	public boolean		on() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((HotteExternalControlI)o).on());
	}
	
	@Override
	public HotteMode getMode() throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((HotteExternalControlI)o).getMode());
	}
	
	@Override
	public void			setMode(HotteMode m)
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((HotteExternalControlI)o).
					setMode(m);
						return null;
					 });
	}
	
	@Override
	public Measure<Double>	getModePower(HotteMode m) throws Exception
	{
		return this.getOwner().handleRequest(
				o -> ((HotteExternalControlI)o).getModePower(m));
	}

	@Override
	public void			turnOn()
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((HotteExternalControlI)o).
					turnOn();
						return null;
					 });
	}
	
	@Override
	public void			turnOff()
	throws Exception
	{
		this.getOwner().handleRequest(
				o -> {	((HotteExternalControlI)o).
					turnOff();
						return null;
					 });
	}
}
