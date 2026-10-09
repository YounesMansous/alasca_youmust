package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurMode;
import equipements.variable.refrigerateur.RefrigerateurUserCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class RefrigerateurUserConnector
extends		AbstractConnector
implements RefrigerateurUserCI{

	@Override
	public void switchOn() throws Exception {
		((RefrigerateurUserCI)this.offering).switchOn();	
	}

	@Override
	public void switchOff() throws Exception {
		((RefrigerateurUserCI)this.offering).switchOff();	
		
	}

	@Override
	public void setTargetTemperature(Measure<Double> target) throws Exception {
		((RefrigerateurUserCI)this.offering).setTargetTemperature(target);	
		
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		((RefrigerateurUserCI)this.offering).setMode(m);	
		
	}

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurUserCI)this.offering).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurUserCI)this.offering).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurUserCI)this.offering).getCurrentTemperature();

	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurUserCI)this.offering).getMode();

	}

}
