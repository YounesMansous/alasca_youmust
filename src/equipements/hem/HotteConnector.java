package equipements.hem;

import equipements.simple.hotte.HotteExternalControlJava4CI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.hem2026.bases.AdjustableCI;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.exceptions.PostconditionException;

public class HotteConnector 
extends		AbstractConnector
implements	AdjustableCI{

	public static final int		MAX_MODE = 4;

	protected int		currentMode;

	protected boolean	isSuspended;

	public				HotteConnector()
	{
		super();
		this.currentMode = 1;
		this.isSuspended = false;
	}
	
	protected double	getPowerLevel(int mode) throws Exception
	{
		assert	mode > 0 && mode <= MAX_MODE :
			new PreconditionException("mode > 0 && mode <= MAX_MODE");

		return ((HotteExternalControlJava4CI)this.offering).getModePowerJava4(mode);
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
			HotteExternalControlJava4CI hotte = (HotteExternalControlJava4CI)this.offering;
			if (!hotte.on()) {
				return false;
			}
			if (hotte.getModeJava4() != modeIndex) {
				hotte.setModeJava4(modeIndex);
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

		return this.currentMode;	}

	@Override
	public double getModeConsumption(int modeIndex) throws Exception {
		assert	modeIndex > 0 && modeIndex <= this.maxMode() :
			new PreconditionException(
					"modeIndex > 0 && modeIndex <= maxMode()");
	return this.getPowerLevel(modeIndex);
	}

	@Override
	public boolean suspended() throws Exception {
		return this.isSuspended;

	}

	@Override
	public boolean suspend() throws Exception {
		assert	!this.suspended() : new PreconditionException("!suspended()");

		try {
			if (!((HotteExternalControlJava4CI)this.offering).on()) {
				return false;
			}

			((HotteExternalControlJava4CI)this.offering).turnOff();
			this.isSuspended = true;
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean resume() throws Exception {
		assert	this.suspended() : new PreconditionException("suspended()");

		try {
			HotteExternalControlJava4CI hotte = (HotteExternalControlJava4CI)this.offering;
			if (!hotte.on()) {
				hotte.turnOn();
			}
			if (hotte.getModeJava4() != this.currentMode) {
				hotte.setModeJava4(this.currentMode);
			}
			this.isSuspended = false;

		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public double emergency() throws Exception {
		assert	this.suspended() : new PreconditionException("suspended()");

		double ret = ((double) this.currentMode) / MAX_MODE;

		assert	ret >= 0.0 && ret <= 1.0 :
				new PostconditionException("return >= 0.0 && return <= 1.0");
		return ret;

	}

}
