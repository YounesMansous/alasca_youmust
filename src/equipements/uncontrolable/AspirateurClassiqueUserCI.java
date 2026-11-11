package equipements.uncontrolable;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * The component interface <code>AspirateurClassiqueUserCI</code> declares the
 * services offered by the classic vacuum cleaner to its user. The contracts of
 * the operations are given in {@link AspirateurClassiqueImplementationI}.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public interface AspirateurClassiqueUserCI
extends		OfferedCI,
			RequiredCI,
			AspirateurClassiqueConfigurationI
{
	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= AspirateurClassiqueConfigurationI.staticInvariants();
		return ret;
	}

	public AspirateurClassiqueState	getState() throws Exception;

	public AspirateurClassiqueMode	getMode() throws Exception;

	public boolean		isMopActive() throws Exception;

	public void			turnOn() throws Exception;

	public void			turnOff() throws Exception;

	public void			setHigh() throws Exception;

	public void			setLow() throws Exception;

	public void			activateMop() throws Exception;

	public void			deactivateMop() throws Exception;
}
