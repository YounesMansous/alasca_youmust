package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurExternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class RefrigerateurExternalControlConnector 
extends		AbstractConnector
implements RefrigerateurExternalControlCI{

	@Override
	public boolean on() throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).on();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).getCurrentTemperature();
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).getMode();
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		((RefrigerateurExternalControlCI)this.offering).setMode(m);		
	}

	@Override
	public Measure<Double> getModePower(RefrigerateurMode m) throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).getModePower(m);
	}

	@Override
	public void suspend() throws Exception {
		((RefrigerateurExternalControlCI)this.offering).suspend();		
	}

	@Override
	public void resume() throws Exception {
		((RefrigerateurExternalControlCI)this.offering).resume();		
	}

	@Override
	public boolean suspended() throws Exception {
		return ((RefrigerateurExternalControlCI)this.offering).suspended();
	}
	

}
