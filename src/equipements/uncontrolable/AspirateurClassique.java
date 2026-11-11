package equipements.uncontrolable;

import equipements.uncontrolable.connections.AspirateurClassiqueInboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.AbstractComponent;

public class AspirateurClassique
extends		AbstractComponent
implements	AspirateurClassiqueImplementationI
{

	public static final String			REFLECTION_INBOUND_PORT_URI =
			"ASPIRATEUR-CLASSIQUE-RIP-URI";
	
	public static final String			INBOUND_PORT_URI =
			"ASPIRATEUR-CLASSIQUE-INBOUND-PORT-URI";
	
	public static boolean VERBOSE = false;
	public static int X_RELATIVE_POSITION = 0;
	public static int Y_RELATIVE_POSITION = 0;
	
	/** power consumption when in mode LOW in the power unit used by
	*  the Vacuum cleaner.	 */
	
	public static final Measure<Double>	HIGH_POWER = new Measure<Double>(
			1200.0,
			POWER_UNIT);
	/** power consumption when in mode LOW in the power unit used by
	*  the Vacuum cleaner.														*/
	
	public static final Measure<Double>	MEDIUM_POWER = new Measure<Double>(
			800.0,
			POWER_UNIT);
	
	public static final Measure<Double>	LOW_POWER = new Measure<Double>(
				500.0,
				POWER_UNIT);
	/** tension required by the Vacuum cleaner in the power unit used by
	*  the Vacuum cleaner.														*/
	public static final Measure<Double>	TENSION = new Measure<Double>(
				220.0,
				TENSION_UNIT);

	/** initial state of the Vacuum cleaner.									*/
	public static final AspirateurClassiqueState	INITIAL_STATE = AspirateurClassiqueState.OFF;
	/** initial mode of the Vacuum cleaner.										*/
	public static final AspirateurClassiqueMode	INITIAL_MODE = AspirateurClassiqueMode.LOW;
	
	/** current state of the vacuum cleaner.								*/
	protected AspirateurClassiqueState	currentState;
	/** current suction mode of the vacuum cleaner.							*/
	protected AspirateurClassiqueMode	currentMode;
	/** true if the mopping function is active.								*/
	protected boolean					mopActive = false;
	/** inbound port offering the user services.							*/
	protected AspirateurClassiqueInboundPort	acip;
	
	protected AspirateurClassique() {
		super(1, 0);
	}

	@Override
	public AspirateurClassiqueState getState() {
		return null;
	}

	@Override
	public AspirateurClassiqueMode getMode() {
		return null;
	}

	@Override
	public boolean isMopActive() {
		return false;
	}

	@Override
	public void turnOn() {
		
	}

	@Override
	public void turnOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHigh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateMop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivateMop() {
		// TODO Auto-generated method stub
		
	}
	
}
