package equipements.simple.hotte;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface HotteUserCI extends		
								OfferedCI,
								RequiredCI,
								HotteConfigurationI{
	
	public static boolean	staticInvariants() throws Exception
	{
		boolean ret = true;
		ret &= HotteConfigurationI.staticInvariants();
		return ret;
	}
	
	public HotteState	getState() throws Exception;
	
	public HotteMode	getMode() throws Exception;

	public void			turnOn() throws Exception;

	public void			turnOff() throws Exception;

	public void			setMode(HotteMode m ) throws Exception;

}
