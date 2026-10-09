package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurExternalControlJava4CI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.components.ComponentI;

public class RefrigerateurExternalControlJava4InboundPort 
extends RefrigerateurExternalControlInboundPort
implements RefrigerateurExternalControlJava4CI{
	
	private static final long serialVersionUID = 1L;

	public				RefrigerateurExternalControlJava4InboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurExternalControlJava4CI.class, owner);
	}
	
	public				RefrigerateurExternalControlJava4InboundPort(
			String uri, ComponentI owner
			) throws Exception
		{
			super(uri, RefrigerateurExternalControlJava4CI.class, owner);
		}

	@Override
	public double getTargetTemperatureJava4() throws Exception {
		return this.getTargetTemperature().getMeasure().getData();
	}

	@Override
	public double getCurrentTemperatureJava4() throws Exception {
		return this.getCurrentTemperature().getMeasure().getData();
	}

	@Override
	public int getModeJava4() throws Exception {
		return RefrigerateurMode.modeToInt(this.getMode());
	}

	@Override
	public void setModeJava4(int im) throws Exception {
		this.setMode(RefrigerateurMode.intToMode(im));
		
	}

	@Override
	public double getModePowerJava4(int im) throws Exception {
		return this.getModePower(RefrigerateurMode.intToMode(im)).getData();
	}
}

