package equipements.simple.hotte;

import equipements.simple.hotte.connections.HotteExternalControlJava4InboundPort;
import equipements.simple.hotte.connections.HotteInboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;


@OfferedInterfaces(offered={HotteUserCI.class,
		HotteExternalControlJava4CI.class})
public class Hotte 

extends		AbstractComponent
implements	HotteImplementationI,
			HotteExternalControlI{

	public static final String			REFLECTION_INBOUND_PORT_URI =
			"HOTTE-RIP-URI";
	
	public static final String			INBOUND_PORT_URI =
			"HOTTE-INBOUND-PORT-URI";
	
	public static final String			EXTERNAL_CONTROL_INBOUND_PORT_URI =
			"HOTTE-EXTERNAL-CONTROL-INBOUND-PORT-URI";
	
	public static boolean				VERBOSE = false;

	public static int					X_RELATIVE_POSITION = 0;

	public static int					Y_RELATIVE_POSITION = 0;

	public static final Measure<Double>	VITESSE1_POWER = new Measure<Double>(
			60.0,
			POWER_UNIT);
	
	public static final Measure<Double>	VITESSE2_POWER = new Measure<Double>(
			120.0,
			POWER_UNIT);
	
	public static final Measure<Double>	VITESSE3_POWER = new Measure<Double>(
			200.0,
			POWER_UNIT);
	
	public static final Measure<Double>	BOOST_POWER = new Measure<Double>(
			300.0,
			POWER_UNIT);
	
	public static final Measure<Double>	TENSION = new Measure<Double>(
			220.0,
			TENSION_UNIT);
	
	public static final HotteState	INITIAL_STATE = HotteState.OFF;
	
	public static final HotteMode	INITIAL_MODE = HotteMode.VITESSE1;

	protected HotteState			currentState;

	protected HotteMode				currentMode;

	protected HotteInboundPort		hip;
	
	protected HotteExternalControlJava4InboundPort	hecip;

	
	public static Measure<Double> getPower(HotteMode m){
		assert	m != null :
			new PreconditionException("m != null");
			switch (m) {
			
			case VITESSE1:
				return VITESSE1_POWER;

			case VITESSE2:
				return VITESSE2_POWER;

			case VITESSE3:				
				return VITESSE3_POWER;

			case BOOST:
				return BOOST_POWER;

			default:
				throw new IllegalArgumentException("mode inconnu : " + m);
			}
		
	}
	
	public static boolean	staticImplementationInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				INITIAL_STATE != null, Hotte.class,
				"INITIAL_STATE != null");
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				INITIAL_MODE != null, Hotte.class,
				"INITIAL_MODE != null");
		return ret;
	}
	
	
	protected static boolean	implementationInvariants(Hotte h)
	{
		assert	h != null : new PreconditionException("h != null");

		boolean ret = true;
		ret &= staticImplementationInvariants();
		ret &= AssertionChecking.checkInvariant(
				h.currentState != null, Hotte.class, h,
				"currentState != null");
		ret &= AssertionChecking.checkInvariant(
				h.currentMode != null, Hotte.class, h,
				"currentMode != null");
		return ret;
	}

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= HotteImplementationI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				REFLECTION_INBOUND_PORT_URI != null &&
									!REFLECTION_INBOUND_PORT_URI.isEmpty(),
									Hotte.class,
				"REFLECTION_INBOUND_PORT_URI != null && "
								+ "!REFLECTION_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				INBOUND_PORT_URI != null && !INBOUND_PORT_URI.isEmpty(),
						Hotte.class,
				"INBOUND_PORT_URI != null && !INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				EXTERNAL_CONTROL_INBOUND_PORT_URI != null && !EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
						Hotte.class,
				"EXTERNAL_CONTROL_INBOUND_PORT_URI != null && !EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				VITESSE1_POWER != null &&
						VITESSE1_POWER.getData() > 0.0 &&
						VITESSE1_POWER.getMeasurementUnit().equals(POWER_UNIT),
						Hotte.class,
				"VITESSE1_POWER_IN_WATTS != null && VITESSE1_POWER_IN_WATTS.getData()"
				+ " > 0.0 && VITESSE1_POWER_IN_WATTS.getMeasurementUnit().equals("
				+ "POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				VITESSE2_POWER != null &&
						VITESSE2_POWER.getData() > 0.0 &&
						VITESSE2_POWER.getMeasurementUnit().equals(POWER_UNIT),
						Hotte.class,
				"VITESSE2_POWER_IN_WATTS != null && VITESSE2_POWER_IN_WATTS.getData() >"
				+ " 0.0 && VITESSE2_POWER_IN_WATTS.getMeasurementUnit().equals("
				+ "POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				VITESSE3_POWER != null &&
						VITESSE3_POWER.getData() > 0.0 &&
						VITESSE3_POWER.getMeasurementUnit().equals(POWER_UNIT),
						Hotte.class,
				"VITESSE3_POWER_IN_WATTS != null && VITESSE3_POWER_IN_WATTS.getData() >"
				+ " 0.0 && VITESSE3_POWER_IN_WATTS.getMeasurementUnit().equals("
				+ "POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				BOOST_POWER != null &&
						BOOST_POWER.getData() > 0.0 &&
						BOOST_POWER.getMeasurementUnit().equals(POWER_UNIT),
						Hotte.class,
				"BOOST_POWER_IN_WATTS != null && BOOST_POWER_IN_WATTS.getData() >"
				+ " 0.0 && BOOST_POWER_IN_WATTS.getMeasurementUnit().equals("
				+ "POWER_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION != null &&
					(TENSION.getData() == 110.0 || TENSION.getData() == 220.0) &&
					TENSION.getMeasurementUnit().equals(TENSION_UNIT),
					Hotte.class,
				"TENSION != null && (TENSION.getData() == 110.0 || TENSION."
				+ "getData() == 220.0) && TENSION.getMeasurementUnit().equals("
				+ "TENSION_UNIT)");
		ret &= AssertionChecking.checkStaticInvariant(
				INITIAL_STATE != null && INITIAL_MODE != null,
						Hotte.class,
				"INITIAL_STATE != null && INITIAL_MODE != null");
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
						Hotte.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
						Hotte.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(Hotte h)
	{
		assert	h != null : new PreconditionException("h != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	protected Hotte() throws Exception {
		this(INBOUND_PORT_URI, EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	protected Hotte(String hotteInboundPortURI, String hotteExternalControlInboundPortURI) throws Exception {
		this(REFLECTION_INBOUND_PORT_URI, hotteInboundPortURI, hotteExternalControlInboundPortURI);
	}

	protected Hotte(String reflectionInboundPortURI, String hotteInboundPortURI,
			String hotteExternalControlInboundPortURI) throws Exception {
		super(reflectionInboundPortURI, 1, 0);
		this.initialise(hotteInboundPortURI, hotteExternalControlInboundPortURI);
	}

	protected void		initialise(String hotteInboundPortURI,String hotteExternalControlInboundPortURI)
	throws Exception
	{
		assert	hotteInboundPortURI != null :
					new PreconditionException(
										"hotteInboundPortURI != null");
		assert	!hotteInboundPortURI.isEmpty() :
					new PreconditionException(
										"!hotteInboundPortURI.isEmpty()");

		assert	hotteExternalControlInboundPortURI != null :
			new PreconditionException(
								"hotteExternalControlInboundPortURI != null");
		assert	!hotteExternalControlInboundPortURI.isEmpty() :
			new PreconditionException(
								"!hotteExternalControlInboundPortURI.isEmpty()");
		
		this.currentState = INITIAL_STATE;
		this.currentMode = INITIAL_MODE;
		this.hip = new HotteInboundPort(hotteInboundPortURI, this);
		this.hip.publishPort();

		this.hecip = new HotteExternalControlJava4InboundPort(
				hotteExternalControlInboundPortURI, this);
		this.hecip.publishPort();

		if (Hotte.VERBOSE) {
			this.tracer.get().setTitle("Hotte component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		assert	Hotte.implementationInvariants(this) :
				new ImplementationInvariantException(
						"Hotte.implementationInvariants(this)");
		assert	Hotte.invariants(this) :
				new InvariantException("Hotte.invariants(this)");
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.hip.unpublishPort();
			this.hecip.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	@Override
	public HotteState getState() {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte returns its state : " +
													this.currentState + ".\n");
		}

		return this.currentState;
	}

	@Override
	public HotteMode getMode() {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte returns its mode : " +
													this.currentMode + ".\n");
		}

		return this.currentMode;
	}

	@Override
	public void turnOn() {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte is turned on.\n");
		}

		assert	this.getState() == HotteState.OFF :
				new PreconditionException("getState() == HotteState.OFF");

		this.currentState = HotteState.ON;
		this.currentMode = HotteMode.VITESSE1;		
	}

	@Override
	public void turnOff() {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte is turned off.\n");
		}

		assert	this.getState() == HotteState.ON :
				new PreconditionException("getState() == HotteState.ON");

		this.currentState = HotteState.OFF;		
	}

	@Override
	public void setMode(HotteMode m) {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte is set "+ m.toString()+ ".\n");
		}
		
		assert	m != null :
			new PreconditionException("Mode != null");
		assert	this.getState() == HotteState.ON :
				new PreconditionException("getState() == HotteState.ON");
		assert	this.getMode() != m :
				new PreconditionException("getMode() != " + m.toString());

		this.currentMode = m;		
	}

	@Override
	public boolean on() {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte returns its state: " +
											this.currentState + ".\n");
		}
	
		return this.currentState == HotteState.ON ;
}

	@Override
	public Measure<Double> getModePower(HotteMode m) {
		if (Hotte.VERBOSE) {
			this.traceMessage("Hotte returns its mode power");
		}		
		return Hotte.getPower(m);
	}



}
