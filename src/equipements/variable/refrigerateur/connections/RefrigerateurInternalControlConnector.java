package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurInternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class RefrigerateurInternalControlConnector 
extends		AbstractConnector
implements	RefrigerateurInternalControlCI{

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurInternalControlCI)this.offering).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurInternalControlCI)this.offering).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurInternalControlCI)this.offering).getCurrentTemperature();
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurInternalControlCI)this.offering).getMode();
	}

	@Override
	public boolean cooling() throws Exception {
		return ((RefrigerateurInternalControlCI)this.offering).cooling();
	}

	@Override
	public void startCooling() throws Exception {
		((RefrigerateurInternalControlCI)this.offering).startCooling();		
	}

	@Override
	public void stopCooling() throws Exception {
		((RefrigerateurInternalControlCI)this.offering).stopCooling();		
	}
	

}
