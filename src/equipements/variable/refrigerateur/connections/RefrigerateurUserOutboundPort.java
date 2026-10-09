package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurMode;
import equipements.variable.refrigerateur.RefrigerateurUserCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class RefrigerateurUserOutboundPort 
extends		AbstractOutboundPort
implements RefrigerateurUserCI{

	private static final long serialVersionUID = 1L;

	public				RefrigerateurUserOutboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurUserCI.class, owner);
	}
	
	public				RefrigerateurUserOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
		super(uri, RefrigerateurUserCI.class, owner);
		}

	@Override
	public void switchOn() throws Exception {
		((RefrigerateurUserCI)this.getConnector()).switchOn();
		
	}

	@Override
	public void switchOff() throws Exception {
		((RefrigerateurUserCI)this.getConnector()).switchOff();
		
	}

	@Override
	public void setTargetTemperature(Measure<Double> target) throws Exception {
		((RefrigerateurUserCI)this.getConnector()).setTargetTemperature(target);
		
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		((RefrigerateurUserCI)this.getConnector()).setMode(m);
		
	}

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurUserCI)this.getConnector()).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurUserCI)this.getConnector()).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurUserCI)this.getConnector()).getCurrentTemperature();

	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurUserCI)this.getConnector()).getMode();

	}
}
