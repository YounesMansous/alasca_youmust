package equipements.simple.hotte;

import equipements.simple.hotte.connections.HotteUserConnector;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import equipements.simple.hotte.connections.HotteOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import equipements.CVMIntegrationTest;
import fr.sorbonne_u.components.utils.tests.TestsStatistics;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;

@RequiredInterfaces(required = {HotteUserCI.class, ClocksServerCI.class})
public class HotteTester extends		AbstractComponent{

	public static final int		SWITCH_ON_DELAY = 2;
	public static final int		SWITCH_OFF_DELAY = 9;

	
	public static boolean				VERBOSE = false;

	public static int					X_RELATIVE_POSITION = 0;

	public static int					Y_RELATIVE_POSITION = 0;

	protected final boolean				isUnitTest;

	protected HotteOutboundPort hop;
	
	protected String hotteInboundPortURI;
	
	protected TestsStatistics			statistics;
	
	protected static boolean	implementationInvariants(HotteTester ht)
	{
		assert	ht != null : new PreconditionException("ht != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				ht.hotteInboundPortURI != null &&
										!ht.hotteInboundPortURI.isEmpty(),
										HotteTester.class, ht,
				"ht.hotteInboundPortURI != null && "
								+ "!ht.hotteInboundPortURI.isEmpty()");
		return ret;
	}
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
						HotteTester.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
						HotteTester.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(HotteTester ht)
	{
		assert	ht != null : new PreconditionException("ht != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}
	
	protected			HotteTester(boolean isUnitTest) throws Exception
	{
		this(isUnitTest, Hotte.INBOUND_PORT_URI);
	}
	
	protected			HotteTester(
			boolean isUnitTest,
			String hotteInboundPortURI
			) throws Exception
		{
			super(1, 1);

			assert	hotteInboundPortURI != null &&
											!hotteInboundPortURI.isEmpty() :
					new PreconditionException(
							"hotteInboundPortURI != null && "
							+ "!hotteInboundPortURI.isEmpty()");

			this.isUnitTest = isUnitTest;
			this.initialise(hotteInboundPortURI);
		}
	
	protected			HotteTester(
			boolean isUnitTest,
			String hotteInboundPortURI,
			String reflectionInboundPortURI
			) throws Exception
		{
			super(reflectionInboundPortURI, 1, 1);

			this.isUnitTest = isUnitTest;
			this.initialise(hotteInboundPortURI);
		}


	protected void		initialise(
			String hotteInboundPortURI
			) throws Exception
		{
			this.hotteInboundPortURI = hotteInboundPortURI;
			this.hop = new HotteOutboundPort(this);
			this.hop.publishPort();

			if (VERBOSE) {
				this.tracer.get().setTitle("Hotte tester component");
				this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
													  Y_RELATIVE_POSITION);
				this.toggleTracing();
			}

			this.statistics = new TestsStatistics();

			assert	HotteTester.implementationInvariants(this) :
					new ImplementationInvariantException(
							"HotteTester.implementationInvariants(this)");
			assert	HotteTester.invariants(this) :
					new InvariantException("HotteTester.invariants(this)");
		}

	@Override
	public synchronized void	start()
	throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
							this.hop.getPortURI(),
							hotteInboundPortURI,
							HotteUserConnector.class.getCanonicalName());
		} catch (Throwable e) {
			throw new ComponentStartException(e) ;
		}
	}
	
	public synchronized void execute() throws Exception
	{
		if (this.isUnitTest) {
			this.traceMessage("Hotte Tester starts the tests.\n");
			this.runAllUnitTests();
			this.traceMessage("Hotte Tester ends.\n");
		} else {
			// 1. recuperer l'horloge acceleree
			ClocksServerOutboundPort clocksServerOutboundPort =
											new ClocksServerOutboundPort(this);
			clocksServerOutboundPort.publishPort();
			this.doPortConnection(
					clocksServerOutboundPort.getPortURI(),
					ClocksServer.STANDARD_INBOUNDPORT_URI,
					ClocksServerConnector.class.getCanonicalName());
			this.traceMessage("Hotte tester gets the clock.\n");
			AcceleratedClock ac =
					clocksServerOutboundPort.getClock(CVMIntegrationTest.CLOCK_URI);
			this.doPortDisconnection(clocksServerOutboundPort.getPortURI());
			clocksServerOutboundPort.unpublishPort();
			clocksServerOutboundPort = null;

			// 2. calculer les instants d'allumage et d'extinction
			Instant startInstant = ac.getStartInstant();
			Instant hotteTurnOn = startInstant.plusSeconds(SWITCH_ON_DELAY);
			Instant hotteTurnOff = startInstant.plusSeconds(SWITCH_OFF_DELAY);

			// 3. attendre le depart de l'horloge
			this.traceMessage("Hotte tester waits until start.\n");
			ac.waitUntilStart();

			// 4. programmer l'allumage et l'extinction
			this.traceMessage("Hotte tester schedules turn on and off.\n");
			long delayToTurnOn = ac.nanoDelayUntilInstant(hotteTurnOn);
			long delayToTurnOff = ac.nanoDelayUntilInstant(hotteTurnOff);
			AbstractComponent o = this;
			this.scheduleTaskOnComponent(
					new AbstractComponent.AbstractTask() {
						@Override
						public void run() {
							try {
								o.traceMessage("Hotte turns on.\n");
								hop.turnOn();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, delayToTurnOn, TimeUnit.NANOSECONDS);
			this.scheduleTaskOnComponent(
					new AbstractComponent.AbstractTask() {
						@Override
						public void run() {
							try {
								o.traceMessage("Hotte turns off.\n");
								hop.turnOff();
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}, delayToTurnOff, TimeUnit.NANOSECONDS);
		}
	}


	
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.hop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.hop.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	protected void		testSimple()
	{
		this.traceMessage("--- Test simple de la hotte ---\n");
		try {
			// 1. état initial : doit être OFF
			this.traceMessage("état initial = " + this.hop.getState() + "\n");

			// 2. allumer
			this.hop.turnOn();
			this.traceMessage("après turnOn : état = " + this.hop.getState()
							  + ", mode = " + this.hop.getMode() + "\n");

			// 3. changer de mode
			this.hop.setMode(HotteMode.BOOST);
			this.traceMessage("après setMode(BOOST) : mode = "
							  + this.hop.getMode() +" PowerMode = " +Hotte.getPower(this.hop.getMode()).getData() + "\n");
			
			
			// 4. éteindre
			this.hop.turnOff();
			this.traceMessage("après turnOff : état = " + this.hop.getState() + "\n");

		} catch (Throwable e) {
			this.traceMessage("ERREUR : " + e + "\n");
		}
		this.traceMessage("--- Fin du test simple ---\n");
	}

	protected void		runAllUnitTests()
	{
		this.testTurnOnOff();
		this.testSetMode();
		this.statistics.statisticsReport(this);
	}
	
	public void			testTurnOnOff()
	{
		this.logMessage("Feature: turning the Hotte on and off");
		this.logMessage("  Scenario: turning on when off");
		HotteState resultState = null;
		HotteMode resultMode = null;
		try {
			this.logMessage("    Given the Hotte is off");
			resultState = this.hop.getState();
			if (!HotteState.OFF.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
			this.logMessage("    When the Hotte is turned on");
			this.hop.turnOn();
			this.logMessage("    Then the Hotte is on");
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the Hotte is in mode VITESSE1");
			resultMode = this.hop.getMode();
			if (!HotteMode.VITESSE1.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning on when on");
		this.logMessage("    Given the Hotte is on");
		try {
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Hotte is turned on");
		this.logMessage("    Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.hop.turnOn();
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch(Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning off when on");
		this.logMessage("    Given the Hotte is on");
		try {
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Hotte is turned off");
		try {
			this.hop.turnOff();
			this.logMessage("    Then the Hotte is off");
			resultState = this.hop.getState();
			if (!HotteState.OFF.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning off when off");
		this.logMessage("    Given the Hotte is off");
		try {
			resultState = this.hop.getState();
			if (!HotteState.OFF.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Hotte is turned off");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.hop.turnOff();
			this.logMessage("     but the precondition exception was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();
	}
	
	public void			testSetMode()
	{
		this.logMessage("Feature: switching the Hotte Vitesse1, Vitesse2, Vitesse3 and Boost.");
		this.logMessage("  Scenario: set the Hotte Boost from Vitesse1");
		this.logMessage("    Given the Hotte is on");
		HotteState resultState = null;
		HotteMode resultMode = null;
		try {
			this.hop.turnOn();
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And the Hotte is Vitesse1");
			resultMode = this.hop.getMode();
			if (!HotteMode.VITESSE1.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    When the Hotte is set Boost");
			this.logMessage("    Then the Hotte is on");
			
			this.hop.setMode(HotteMode.BOOST);
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And  the Hotte is Boost");
			resultMode = this.hop.getMode();
			if (!HotteMode.BOOST.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: set the Hotte Boost from Boost");
		this.logMessage("    Given the Hotte is on");
		this.logMessage("    And the Hotte is Boost");
		this.logMessage("    When the Hotte is set Boost");
		this.logMessage("    Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.hop.setMode(HotteMode.BOOST);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		
		this.logMessage("  Scenario: set the Hotte mode to null");
		this.logMessage("    Given the Hotte is on");
		try {
			resultState = this.hop.getState();
			if (!HotteState.ON.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Hotte mode is set to null");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.hop.setMode(null);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu : la precondition m != null a echoue
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: set the Hotte Vitesse2 when off");
		this.logMessage("    Given the Hotte is off");
		try {
			this.hop.turnOff();
			resultState = this.hop.getState();
			if (!HotteState.OFF.equals(resultState)) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Hotte is set Vitesse2");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.hop.setMode(HotteMode.VITESSE2);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu : la precondition getState() == ON a echoue
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();
	}



}
