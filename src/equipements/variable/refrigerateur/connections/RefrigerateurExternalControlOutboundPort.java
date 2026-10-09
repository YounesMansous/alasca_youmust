package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurExternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class RefrigerateurExternalControlOutboundPort 
extends		AbstractOutboundPort
implements RefrigerateurExternalControlCI{
	
	private static final long serialVersionUID = 1L;


	public				RefrigerateurExternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurExternalControlCI.class, owner);
	}
	
	public				RefrigerateurExternalControlOutboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
		super(uri, RefrigerateurExternalControlCI.class, owner);
		}

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).getCurrentTemperature();
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).getMode();
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		((RefrigerateurExternalControlCI)this.getConnector()).setMode(m);
	}

	@Override
	public Measure<Double> getModePower(RefrigerateurMode m) throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).getModePower(m);
	}

	@Override
	public void suspend() throws Exception {
		((RefrigerateurExternalControlCI)this.getConnector()).suspend();		
	}

	@Override
	public void resume() throws Exception {
		((RefrigerateurExternalControlCI)this.getConnector()).resume();		
	}

	@Override
	public boolean suspended() throws Exception {
		return ((RefrigerateurExternalControlCI)this.getConnector()).suspended();
	}
	
	
}
