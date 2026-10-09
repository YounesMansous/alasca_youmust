package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurInternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class RefrigerateurInternalControlOutboundPort 
extends		AbstractOutboundPort
implements	RefrigerateurInternalControlCI{

	private static final long serialVersionUID = 1L;

	public				RefrigerateurInternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurInternalControlCI.class, owner);
	}

	public				RefrigerateurInternalControlOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			super(uri, RefrigerateurInternalControlCI.class, owner);
		}

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurInternalControlCI)this.getConnector()).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurInternalControlCI)this.getConnector()).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurInternalControlCI)this.getConnector()).getCurrentTemperature();

	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurInternalControlCI)this.getConnector()).getMode();

	}

	@Override
	public boolean cooling() throws Exception {
		return ((RefrigerateurInternalControlCI)this.getConnector()).cooling();

	}

	@Override
	public void startCooling() throws Exception {
		((RefrigerateurInternalControlCI)this.getConnector()).startCooling();		
	}

	@Override
	public void stopCooling() throws Exception {
		((RefrigerateurInternalControlCI)this.getConnector()).stopCooling();		
	}
}
