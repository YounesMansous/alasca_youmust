package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurMode;
import equipements.variable.refrigerateur.RefrigerateurUserJava4CI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;

public class RefrigerateurUserJava4InboundPort 
extends RefrigerateurUserInboundPort
implements RefrigerateurUserJava4CI{

	private static final long serialVersionUID = 1L;

	public				RefrigerateurUserJava4InboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurUserJava4CI.class, owner);
	}
	
	public				RefrigerateurUserJava4InboundPort(
			String uri, ComponentI owner
			) throws Exception
		{
			super(uri, RefrigerateurUserJava4CI.class, owner);
		}

	
	
	@Override
	public void setTargetTemperatureJava4(double target) throws Exception {
		this.setTargetTemperature(new Measure<Double>(target, TEMPERATURE_UNIT));		
	}

	@Override
	public double getTargetTemperatureJava4() throws Exception {
		return this.getTargetTemperature().getMeasure().getData();
	}

	@Override
	public void setModeJava4(int im) throws Exception {
		this.setMode(RefrigerateurMode.intToMode(im));
		
	}

	@Override
	public int getModeJava4() throws Exception {
		return RefrigerateurMode.modeToInt(this.getMode());
	}

	@Override
	public double getCurrentTemperatureJava4() throws Exception {
		return this.getCurrentTemperature().getMeasure().getData();
	}
	


}
