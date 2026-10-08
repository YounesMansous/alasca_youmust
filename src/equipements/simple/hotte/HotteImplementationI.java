package equipements.simple.hotte;


public interface HotteImplementationI extends HotteConfigurationI {
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= HotteConfigurationI.staticInvariants();
		return ret;
	}
	
	public HotteState	getState();

	public HotteMode	getMode();

	public void			turnOn();

	public void			turnOff();
	
	public void			setMode(HotteMode m);
	
}
