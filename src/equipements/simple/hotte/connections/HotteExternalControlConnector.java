package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteExternalControlCI;
import equipements.simple.hotte.HotteMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class HotteExternalControlConnector 
extends		AbstractConnector
implements HotteExternalControlCI{

	@Override
	public boolean on() throws Exception {
		return ((HotteExternalControlCI)this.offering).on();
	}

	@Override
	public HotteMode getMode() throws Exception {
		return ((HotteExternalControlCI)this.offering).getMode();
	}

	@Override
	public void setMode(HotteMode m) throws Exception {
		((HotteExternalControlCI)this.offering).setMode(m);		
	}

	@Override
	public Measure<Double> getModePower(HotteMode m) throws Exception {
		return ((HotteExternalControlCI)this.offering).getModePower(m);
	}

	@Override
	public void turnOn() throws Exception {
		((HotteExternalControlCI)this.offering).turnOn();		
		
	}

	@Override
	public void turnOff() throws Exception {
		((HotteExternalControlCI)this.offering).turnOff();
		
	}

}
