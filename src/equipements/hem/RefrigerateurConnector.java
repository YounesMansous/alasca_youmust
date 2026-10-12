package equipements.hem;

import equipements.variable.refrigerateur.RefrigerateurExternalControlJava4CI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2026.bases.AdjustableCI;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.exceptions.PostconditionException;


public class RefrigerateurConnector 
extends		AbstractConnector
implements	AdjustableCI{

	public static final int		MAX_MODE = 3;
	
	protected int		currentMode;
	
	public				RefrigerateurConnector()
	{
		super();
		this.currentMode = 2;
	}

	protected double	getPowerLevel(int mode) throws Exception
	{
		assert	mode > 0 && mode <= MAX_MODE :
			new PreconditionException("mode > 0 && mode <= MAX_MODE");

		return ((RefrigerateurExternalControlJava4CI)this.offering).getModePowerJava4(mode);
	}
	
	@Override
	public int maxMode() throws Exception {
		return MAX_MODE;

	}

	@Override
	public boolean upMode() throws Exception {
		assert	!this.suspended() : new PreconditionException("!suspended()");
		assert	this.currentMode() < MAX_MODE :
				new PreconditionException("currentMode() < MAX_MODE");

		return this.setMode(this.currentMode + 1); 
	}

	@Override
	public boolean downMode() throws Exception {
		assert	!this.suspended() : new PreconditionException("!suspended()");
		assert	this.currentMode() > 1 :
				new PreconditionException("currentMode() > 1");

		return this.setMode(this.currentMode - 1);
	}

	@Override
	public boolean setMode(int modeIndex) throws Exception {
		assert	!this.suspended() : new PreconditionException("!suspended()");
		assert	modeIndex > 0 && modeIndex <= this.maxMode() :
				new PreconditionException(
						"modeIndex > 0 && modeIndex <= maxMode()");

		try {
			RefrigerateurExternalControlJava4CI refrigerateur = (RefrigerateurExternalControlJava4CI)this.offering;
			if (!refrigerateur.on()) {
				return false;
			}
			if (refrigerateur.getModeJava4() != modeIndex) {
				refrigerateur.setModeJava4(modeIndex);
			}
			this.currentMode = modeIndex;
			
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public int currentMode() throws Exception {
		assert	!suspended() : new PreconditionException("!suspended()");

		return this.currentMode;	
	}

	@Override
	public double getModeConsumption(int modeIndex) throws Exception {
		assert	modeIndex > 0 && modeIndex <= this.maxMode() :
			new PreconditionException(
					"modeIndex > 0 && modeIndex <= maxMode()");
	return this.getPowerLevel(modeIndex);
	}

	@Override
	public boolean suspended() throws Exception {
		RefrigerateurExternalControlJava4CI refrigerateur = (RefrigerateurExternalControlJava4CI)this.offering;

		return refrigerateur.suspended();
	}

	@Override
	public boolean suspend() throws Exception {
		RefrigerateurExternalControlJava4CI refrigerateur = (RefrigerateurExternalControlJava4CI)this.offering;

		assert	!this.suspended() : new PreconditionException("!suspended()");

		try {
			if (!refrigerateur.on()) {
				return false;
			}

			refrigerateur.suspend();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean resume() throws Exception {
		RefrigerateurExternalControlJava4CI refrigerateur = (RefrigerateurExternalControlJava4CI)this.offering;

		assert	this.suspended() : new PreconditionException("suspended()");

		try {
			if (!refrigerateur.on()) {
				return false;
			}


			refrigerateur.resume();
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public double		emergency() throws Exception
	{
		assert	this.suspended() : new PreconditionException("suspended()");

		RefrigerateurExternalControlJava4CI refrigerateur =
						(RefrigerateurExternalControlJava4CI)this.offering;
		
		double current = refrigerateur.getCurrentTemperatureJava4();
		double target  = refrigerateur.getTargetTemperatureJava4();
		double max     = RefrigerateurExternalControlJava4CI.MAX_TARGET_TEMPERATURE.getData();

		double ret;
		if (current >= max) {
			ret = 1.0;           
			// la nourriture est en danger
		} else if (current <= target) {
			ret = 0.0;         
			// il fait assez froid
		} else {
			ret = (current - target) / (max - target);
			// entre les deux : proportionnel
		}

		assert	ret >= 0.0 && ret <= 1.0 :
				new PostconditionException("return >= 0.0 && return <= 1.0");
		return ret;
	}

}
