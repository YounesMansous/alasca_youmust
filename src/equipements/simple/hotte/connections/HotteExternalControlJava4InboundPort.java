package equipements.simple.hotte.connections;

import equipements.simple.hotte.HotteExternalControlJava4CI;
import equipements.simple.hotte.HotteMode;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.exceptions.PreconditionException;

public class HotteExternalControlJava4InboundPort 
extends HotteExternalControlInboundPort
implements HotteExternalControlJava4CI{

	private static final long serialVersionUID = 1L;

	public				HotteExternalControlJava4InboundPort(ComponentI owner)
	throws Exception
	{
		super(HotteExternalControlJava4CI.class, owner);
	}

	public				HotteExternalControlJava4InboundPort(
			String uri, ComponentI owner
			) throws Exception
		{
			super(uri, HotteExternalControlJava4CI.class, owner);
		}
	
	
	@Override
	public int getModeJava4() throws Exception {
		
		return modeToInt(this.getMode());
	}

	@Override
	public void setModeJava4(int im) throws Exception {
		this.setMode(intToMode(im));
		
	}

	@Override
	public double getModePowerJava4(int im) throws Exception {
		return this.getModePower(intToMode(im)).getData();
	}
	
	private HotteMode intToMode(int im) {
		assert	im >= 1 && im <= 4:
			new PreconditionException("im >= 1 && im <= 4");
		switch (im) {
		
		case 1:
			return HotteMode.VITESSE1;

		case 2:
			return HotteMode.VITESSE2;

		case 3:				
			return HotteMode.VITESSE3;

		case 4:
			return HotteMode.BOOST;

		default:
			throw new IllegalArgumentException("mode inconnu : " + im);
		}
	}
	
	private int modeToInt(HotteMode m) {
		assert	m != null:
			new PreconditionException("m != null");
		switch (m) {
		
		case VITESSE1:
			return 1;

		case VITESSE2:
			return 2;

		case VITESSE3:				
			return 3;

		case BOOST:
			return 4;

		default:
			throw new IllegalArgumentException("mode inconnu : " + m);
		}
	}

}
