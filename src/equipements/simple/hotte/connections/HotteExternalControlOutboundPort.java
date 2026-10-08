package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteExternalControlCI;
import equipements.simple.hotte.HotteMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class HotteExternalControlOutboundPort 
extends		AbstractOutboundPort
implements HotteExternalControlCI{

	private static final long serialVersionUID = 1L;

	public				HotteExternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(HotteExternalControlCI.class, owner);
	}
	
	public				HotteExternalControlOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
		super(uri, HotteExternalControlCI.class, owner);
		}
	
	@Override
	public boolean		on() throws Exception
	{
		return ((HotteExternalControlCI)this.getConnector()).on();
	}

	@Override
	public HotteMode getMode() throws Exception {
		return ((HotteExternalControlCI)this.getConnector()).getMode();
	}

	@Override
	public void setMode(HotteMode m) throws Exception {
		 ((HotteExternalControlCI)this.getConnector()).setMode(m);		
	}

	@Override
	public Measure<Double> getModePower(HotteMode m) throws Exception {
		return ((HotteExternalControlCI)this.getConnector()).getModePower(m);
	}

	@Override
	public void turnOn() throws Exception {
		 ((HotteExternalControlCI)this.getConnector()).turnOn();		
		
	}

	@Override
	public void turnOff() throws Exception {
		 ((HotteExternalControlCI)this.getConnector()).turnOff();		
		
	}

}
