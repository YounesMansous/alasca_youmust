package equipements.uncontrolable.connections;

import equipements.uncontrolable.AspirateurClassiqueMode;
import equipements.uncontrolable.AspirateurClassiqueState;
import equipements.uncontrolable.AspirateurClassiqueUserCI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * The class <code>AspirateurClassiqueConnector</code> implements a connector
 * for the <code>AspirateurClassiqueUserCI</code> component interface.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public class AspirateurClassiqueConnector
extends		AbstractConnector
implements	AspirateurClassiqueUserCI
{
	@Override
	public AspirateurClassiqueState	getState() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.offering).getState();
	}

	@Override
	public AspirateurClassiqueMode	getMode() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.offering).getMode();
	}

	@Override
	public boolean		isMopActive() throws Exception
	{
		return ((AspirateurClassiqueUserCI)this.offering).isMopActive();
	}

	@Override
	public void			turnOn() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).turnOn();
	}

	@Override
	public void			turnOff() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).turnOff();
	}

	@Override
	public void			setHigh() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).setHigh();
	}

	@Override
	public void			setLow() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).setLow();
	}

	@Override
	public void			activateMop() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).activateMop();
	}

	@Override
	public void			deactivateMop() throws Exception
	{
		((AspirateurClassiqueUserCI)this.offering).deactivateMop();
	}
}
