package equipements.variable.refrigerateur;

import equipements.variable.refrigerateur.connections.RefrigerateurExternalControlJava4InboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurInternalControlInboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurUserJava4InboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;


@OfferedInterfaces(offered={RefrigerateurUserJava4CI.class,
		RefrigerateurInternalControlCI.class,
		RefrigerateurExternalControlJava4CI.class})
public class Refrigerateur 
extends		AbstractComponent
implements	RefrigerateurUserI,
			RefrigerateurInternalControlI,
			RefrigerateurExternalControlI
{

	public static enum	RefrigerateurState
	{
		/** Refrigerateur is on, but not COOLING.									*/
		ON,
		/** Refrigerateur is COOLING.												*/
		COOLING,
		/** Refrigerateur is off.													*/
		OFF
	}
	
	public static boolean			VERBOSE = false;

	public static int				X_RELATIVE_POSITION = 0;

	public static int				Y_RELATIVE_POSITION = 0;

	public static final String		REFLECTION_INBOUND_PORT_URI =
			"REFRIGERATEUR-RIP-URI";	
	
	public static final String		USER_INBOUND_PORT_URI =
												"REFRIGERATEUR-USER-INBOUND-PORT-URI";

	public static final String		INTERNAL_CONTROL_INBOUND_PORT_URI =
									"REFRIGERATEUR-INTERNAL-CONTROL-INBOUND-PORT-URI";

	public static final String		EXTERNAL_CONTROL_INBOUND_PORT_URI =
									"REFRIGERATEUR-EXTERNAL-CONTROL-INBOUND-PORT-URI";
	
	public static final TimedMeasure<Double>	FAKE_CURRENT_TEMPERATURE =
			new TimedMeasure<>(
				new Measure<>(
						5.0,
						TEMPERATURE_UNIT));
	
	public static final Measure<Double>	ECO_POWER = new Measure<Double>(
			50.0,
			POWER_UNIT);
	
	public static final Measure<Double>	NORMAL_POWER = new Measure<Double>(
			100.0,
			POWER_UNIT);
	
	public static final Measure<Double>	RAPIDE_POWER = new Measure<Double>(
			150.0,
			POWER_UNIT);
	
	public static final RefrigerateurState	INITIAL_STATE = RefrigerateurState.OFF;
	
	public static final RefrigerateurMode	INITIAL_MODE = RefrigerateurMode.NORMAL;

	protected RefrigerateurUserJava4InboundPort			rip;
	
	protected RefrigerateurInternalControlInboundPort		ricip;

	protected RefrigerateurExternalControlJava4InboundPort	recip;
	
	protected RefrigerateurState currentState;

	protected RefrigerateurMode currentMode;

	protected TimedMeasure<Double>				targetTemperature;
	
	protected boolean isSuspended;

	public static Measure<Double> getPower(RefrigerateurMode m){
		assert	m != null :
			new PreconditionException("m != null");
			switch (m) {
			
			case ECO:
				return ECO_POWER;

			case NORMAL:
				return NORMAL_POWER;

			case RAPIDE:				
				return RAPIDE_POWER;

			default:
				throw new IllegalArgumentException("mode inconnu : " + m);
			}
		
	}
	
	protected static boolean	implementationInvariants(Refrigerateur r)
	{
		assert	r != null : new PreconditionException("r != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				r.currentState != null,
						Refrigerateur.class, r,
				"r.currentState != null");
		ret &= AssertionChecking.checkImplementationInvariant(
				r.targetTemperature.getData() >=
							MIN_TARGET_TEMPERATURE.getData() &&
					r.targetTemperature.getData() <=
								MAX_TARGET_TEMPERATURE.getData(),
								Refrigerateur.class, r,
				"targetTemperature.getData() >= MIN_TARGET_TEMPERATURE.getData() && "
				+ "targetTemperature.getData() <= MAX_TARGET_TEMPERATURE.getData()");
		ret &= AssertionChecking.checkImplementationInvariant(
				r.currentMode != null,
						Refrigerateur.class, r,
				"r.currentMode != null");
		ret &= AssertionChecking.checkImplementationInvariant(
				r.currentState != RefrigerateurState.COOLING || !r.isSuspended,
						Refrigerateur.class, r,
				"r.currentState != RefrigerateurState.COOLING || !r.isSuspended");
		ret &= AssertionChecking.checkImplementationInvariant(
				r.currentState != RefrigerateurState.OFF || !r.isSuspended,
						Refrigerateur.class, r,
				"r.currentState != RefrigerateurState.OFF || !r.isSuspended");
		return ret;
	}
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= RefrigerateurInternalControlI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				REFLECTION_INBOUND_PORT_URI != null &&
									!REFLECTION_INBOUND_PORT_URI.isEmpty(),
				Refrigerateur.class,
				"REFLECTION_INBOUND_PORT_URI != null && "
								+ "!REFLECTION_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty(),
						Refrigerateur.class,
				"USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				INTERNAL_CONTROL_INBOUND_PORT_URI != null &&
								!INTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
								Refrigerateur.class,
				"INTERNAL_CONTROL_INBOUND_PORT_URI != null && "
							+ "!INTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				EXTERNAL_CONTROL_INBOUND_PORT_URI != null &&
								!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
								Refrigerateur.class,
				"EXTERNAL_CONTROL_INBOUND_PORT_URI != null &&"
							+ "!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
						Refrigerateur.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
						Refrigerateur.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(Refrigerateur r)
	{
		assert	r != null : new PreconditionException("r != null");

		boolean ret = true;
		ret &= staticInvariants();
		ret &= RefrigerateurUserI.invariants(r);
		ret &= RefrigerateurExternalControlI.invariants(r);
		return ret;
	}
	
	protected			Refrigerateur() throws Exception
	{
		this(USER_INBOUND_PORT_URI, INTERNAL_CONTROL_INBOUND_PORT_URI,
			 EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	protected			Refrigerateur(
			String refrigerateurUserInboundPortURI,
			String refrigerateurInternalControlInboundPortURI,
			String refrigerateurExternalControlInboundPortURI
			) throws Exception
		{
			this(REFLECTION_INBOUND_PORT_URI,
				 refrigerateurUserInboundPortURI,
				 refrigerateurInternalControlInboundPortURI,
				 refrigerateurExternalControlInboundPortURI);
		}

	
	protected			Refrigerateur(
			String reflectionInboundPortURI,
			String refrigerateurUserInboundPortURI,
			String refrigerateurInternalControlInboundPortURI,
			String refrigerateurExternalControlInboundPortURI
			) throws Exception
		{
			super(reflectionInboundPortURI, 1, 0);

			this.initialise(refrigerateurUserInboundPortURI,
					refrigerateurInternalControlInboundPortURI,
					refrigerateurExternalControlInboundPortURI);
		}
	
	protected void		initialise(
			String refrigerateurUserInboundPortURI,
			String refrigerateurInternalControlInboundPortURI,
			String refrigerateurExternalControlInboundPortURI
			) throws Exception
		{
			assert	refrigerateurUserInboundPortURI != null && !refrigerateurUserInboundPortURI.isEmpty():
				new PreconditionException(
					"refrigerateurUserInboundPortURI != null && !refrigerateurUserInboundPortURI.isEmpty()");
			assert	refrigerateurInternalControlInboundPortURI != null && !refrigerateurInternalControlInboundPortURI.isEmpty():
				new PreconditionException(
						"refrigerateurInternalControlInboundPortURI != null && !refrigerateurInternalControlInboundPortURI.isEmpty()");
			assert	refrigerateurExternalControlInboundPortURI != null && !refrigerateurExternalControlInboundPortURI.isEmpty():
				new PreconditionException(
						"refrigerateurExternalControlInboundPortURI != null && !refrigerateurExternalControlInboundPortURI.isEmpty()");

			this.currentState = INITIAL_STATE;
			this.currentMode = INITIAL_MODE;
			this.targetTemperature =
				new TimedMeasure<>(
						new Measure<>(
								STANDARD_TARGET_TEMPERATURE.getData(),
								STANDARD_TARGET_TEMPERATURE.getMeasurementUnit()));

			this.rip = new RefrigerateurUserJava4InboundPort(refrigerateurUserInboundPortURI, this);
			this.rip.publishPort();
			this.ricip = new RefrigerateurInternalControlInboundPort(
					refrigerateurInternalControlInboundPortURI, this);
			this.ricip.publishPort();
			this.recip = new RefrigerateurExternalControlJava4InboundPort(
					refrigerateurExternalControlInboundPortURI, this);
			this.recip.publishPort();

			if (VERBOSE) {
				this.tracer.get().setTitle("Refrigerateur component");
				this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
													  Y_RELATIVE_POSITION);
				this.toggleTracing();		
			}

			assert	Refrigerateur.implementationInvariants(this) :
					new ImplementationInvariantException(
							"Refrigerateur.implementationInvariants(this)");
			assert	Refrigerateur.invariants(this) :
					new InvariantException("Refrigerateur.invariants(this)");
		}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.rip.unpublishPort();
			this.ricip.unpublishPort();
			this.recip.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	@Override
	public boolean on() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its state: " +
											this.currentState + ".\n");
		}
		return this.currentState != RefrigerateurState.OFF ;
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its target"
							+ " temperature " + this.targetTemperature + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");

		TimedMeasure<Double> ret = this.targetTemperature;

		assert	ret != null && TEMPERATURE_UNIT.equals(
									ret.getMeasure().getMeasurementUnit()) :
				new PostconditionException(
						"return != null && TEMPERATURE_UNIT.equals("
						+ "return.getMeasure().getMeasurementUnit())");
		assert	ret.getMeasure().getData() >= MIN_TARGET_TEMPERATURE.getData() &&
					ret.getMeasure().getData() <= MAX_TARGET_TEMPERATURE.getData() :
				new PostconditionException(
						"return.getMeasure().getData() >= "
						+ "MIN_TARGET_TEMPERATURE.getData() "
						+ "&& return.getMeasure().getData() <= "
						+ "MAX_TARGET_TEMPERATURE.getData()");

		return ret;
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() {
		assert	this.on() : new PreconditionException("on()");

		// Temporary implementation; would need a temperature sensor.
		TimedMeasure<Double> currentTemperature = FAKE_CURRENT_TEMPERATURE;
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns the current"
							+ " temperature " + currentTemperature + ".\n");
		}

		return  currentTemperature;
	}

	@Override
	public RefrigerateurMode getMode() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its mode : " +
													this.currentMode + ".\n");
		}

		return this.currentMode;
	}

	@Override
	public Measure<Double> getModePower(RefrigerateurMode m) {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its mode power.\n");
		}		
		return Refrigerateur.getPower(m);
	}
	

	@Override
	public void suspend() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur is suspended " );
		}

		assert	this.on() : new PreconditionException("on()");		
		assert	!this.suspended() : new PreconditionException("!suspended()");	
		this.currentState = RefrigerateurState.ON;
		this.isSuspended = true;
		
		assert this.suspended() : new PostconditionException("suspended()");
	}

	@Override
	public void resume() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur resumes" );
		}

		assert	this.on() : new PreconditionException("on()");		
		assert	this.suspended() : new PreconditionException("suspended()");	
		this.isSuspended = false;		
		
		assert !this.suspended() : new PostconditionException("!suspended()");
	}

	@Override
	public boolean suspended() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its state: " +
											this.isSuspended + ".\n");
		}
		return this.isSuspended ;
	}

	@Override
	public boolean cooling() throws Exception {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur returns its cooling status " + 
						(this.currentState == RefrigerateurState.COOLING) + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");

		return this.currentState == RefrigerateurState.COOLING;
	}

	@Override
	public void startCooling() throws Exception {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur starts cooling.\n");
		}
		assert	this.on() : new PreconditionException("on()");
		assert	!this.cooling() : new PreconditionException("!cooling()");
		assert	!this.suspended() : new PreconditionException("!suspended()");

		this.currentState = RefrigerateurState.COOLING;

		assert	this.cooling() : new PostconditionException("cooling()");		
	}

	@Override
	public void stopCooling() throws Exception {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur stops cooling.\n");
		}
		assert	this.on() : new PreconditionException("on()");
		assert	this.cooling() : new PreconditionException("cooling()");

		this.currentState = RefrigerateurState.ON;

		assert	!this.cooling() : new PostconditionException("!cooling()");		
	}

	@Override
	public void switchOn() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur switches on.\n");
		}

		assert	!this.on() : new PreconditionException("!on()");

		this.currentState = RefrigerateurState.ON;

		assert	 this.on() : new PostconditionException("on()");		
	}

	@Override
	public void switchOff() {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur switches off.\n");
		}

		assert	this.on() : new PreconditionException("on()");
		
		this.isSuspended = false;
		this.currentState = RefrigerateurState.OFF;

		assert	 !this.on() : new PostconditionException("!on()");		
	}

	@Override
	public void setTargetTemperature(Measure<Double> target) {
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur sets a new target "
										+ "temperature: " + target + ".\n");
		}

		assert	this.on() : new PreconditionException("on()");
		assert	target != null &&
						TEMPERATURE_UNIT.equals(target.getMeasurementUnit()) :
				new PreconditionException(
						"target != null && TEMPERATURE_UNIT.equals("
						+ "target.getMeasurementUnit())");
		assert	target.getData() >= MIN_TARGET_TEMPERATURE.getData() &&
						target.getData() <= MAX_TARGET_TEMPERATURE.getData() :
				new PreconditionException(
						"target.getData() >= MIN_TARGET_TEMPERATURE.getData() "
						+ "&& target.getData() <= MAX_TARGET_TEMPERATURE.getData()");

		this.targetTemperature = new TimedMeasure<>(target);

		assert	getTargetTemperature().getMeasure().equals(target) :
				new PostconditionException(
						"getTargetTemperature().getMeasure().equals(target)");		
	}

	@Override
	public void setMode(RefrigerateurMode m) {
		assert	m != null :
			new PreconditionException("m != null");
		assert	this.on() :
				new PreconditionException("on()");
		assert	this.getMode() != m :
				new PreconditionException("getMode() != " + m.toString());
		
		if (Refrigerateur.VERBOSE) {
			this.traceMessage("Refrigerateur is set "+ m+ ".\n");
		}
		this.currentMode = m;			
	}



}
