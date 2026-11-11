package equipements.uncontrolable;

import equipements.uncontrolable.connections.AspirateurClassiqueConnector;
import equipements.uncontrolable.connections.AspirateurClassiqueOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.utils.tests.TestsStatistics;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * The class <code>AspirateurClassiqueTester</code> implements a component
 * performing the unit tests of the classic vacuum cleaner, each test being
 * specified as a Gherkin feature and its scenarios.
 *
 * <p><strong>Description</strong></p>
 *
 * <p>
 * The tester calls the vacuum cleaner through an outbound port, as a user
 * would do, and checks the results of the operations as well as the
 * exceptions raised when preconditions are violated.
 * </p>
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
@RequiredInterfaces(required = {AspirateurClassiqueUserCI.class})
public class AspirateurClassiqueTester
extends		AbstractComponent
{
	// -------------------------------------------------------------------------
	// Inner types
	// -------------------------------------------------------------------------

	/** an action that may throw, used to test violated preconditions.		*/
	protected interface Action
	{
		void run() throws Exception;
	}

	// -------------------------------------------------------------------------
	// Constants and variables
	// -------------------------------------------------------------------------

	/** when true, methods trace their execution.							*/
	public static boolean					VERBOSE = false;
	/** x coordinate of the trace window relative position.					*/
	public static int						X_RELATIVE_POSITION = 0;
	/** y coordinate of the trace window relative position.					*/
	public static int						Y_RELATIVE_POSITION = 0;

	/** outbound port used to call the vacuum cleaner.						*/
	protected AspirateurClassiqueOutboundPort	acop;
	/** URI of the inbound port of the vacuum cleaner to be tested.			*/
	protected String						aspirateurInboundPortURI;
	/** statistics on the tests results.									*/
	protected TestsStatistics				statistics;

	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	protected static boolean	implementationInvariants(
		AspirateurClassiqueTester act
		)
	{
		assert	act != null : new PreconditionException("act != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				act.aspirateurInboundPortURI != null &&
								!act.aspirateurInboundPortURI.isEmpty(),
				AspirateurClassiqueTester.class, act,
				"aspirateurInboundPortURI != null && "
								+ "!aspirateurInboundPortURI.isEmpty()");
		return ret;
	}

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
				AspirateurClassiqueTester.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				AspirateurClassiqueTester.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	protected static boolean	invariants(AspirateurClassiqueTester act)
	{
		assert	act != null : new PreconditionException("act != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * create a tester for the vacuum cleaner with the default inbound port URI.
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	protected AspirateurClassiqueTester() throws Exception
	{
		this(AspirateurClassique.INBOUND_PORT_URI);
	}

	/**
	 * create a tester for the vacuum cleaner.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code aspirateurInboundPortURI != null}
	 * pre	{@code !aspirateurInboundPortURI.isEmpty()}
	 * </pre>
	 *
	 * @param aspirateurInboundPortURI	URI of the inbound port of the vacuum cleaner.
	 * @throws Exception				<i>to do</i>.
	 */
	protected AspirateurClassiqueTester(String aspirateurInboundPortURI)
	throws Exception
	{
		super(1, 0);

		assert	aspirateurInboundPortURI != null &&
								!aspirateurInboundPortURI.isEmpty() :
				new PreconditionException(
						"aspirateurInboundPortURI != null && "
						+ "!aspirateurInboundPortURI.isEmpty()");

		this.aspirateurInboundPortURI = aspirateurInboundPortURI;
		this.acop = new AspirateurClassiqueOutboundPort(this);
		this.acop.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Classic vacuum cleaner tester component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}
		this.statistics = new TestsStatistics();

		assert	AspirateurClassiqueTester.implementationInvariants(this) :
				new ImplementationInvariantException(
					"AspirateurClassiqueTester.implementationInvariants(this)");
		assert	AspirateurClassiqueTester.invariants(this) :
				new InvariantException(
					"AspirateurClassiqueTester.invariants(this)");
	}

	// -------------------------------------------------------------------------
	// Helpers
	// -------------------------------------------------------------------------

	/**
	 * check that the vacuum cleaner is in the expected state, counting a
	 * failed condition (a "Given" that does not hold) otherwise.
	 */
	protected void		given(AspirateurClassiqueState expected)
	{
		try {
			AspirateurClassiqueState s = this.acop.getState();
			if (!expected.equals(s)) {
				this.logMessage("     but was: " + s);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
	}

	/**
	 * check that the action raises an exception, which is the way a violated
	 * precondition shows up through the ports.
	 */
	protected void		expectPreconditionViolation(Action action)
	{
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			action.run();
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// expected
		} finally {
			BCMException.VERBOSE = old;
		}
	}

	// -------------------------------------------------------------------------
	// Tests
	// -------------------------------------------------------------------------

	public void			testGetState()
	{
		this.logMessage("Feature: Getting the state of the vacuum cleaner");
		this.logMessage("  Scenario: getting the state when off");
		this.logMessage("    Given the vacuum cleaner has not been used yet");
		try {
			this.logMessage("    When I test the state of the vacuum cleaner");
			AspirateurClassiqueState result = this.acop.getState();
			this.logMessage("    Then the vacuum cleaner is in its initial state");
			if (!AspirateurClassique.INITIAL_STATE.equals(result)) {
				this.statistics.incorrectResult();
				this.logMessage("     but was: " + result);
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();
	}

	public void			testGetMode()
	{
		this.logMessage("Feature: Getting the mode of the vacuum cleaner");
		this.logMessage("  Scenario: getting the mode when off");
		this.logMessage("    Given the vacuum cleaner is off");
		this.given(AspirateurClassiqueState.OFF);
		try {
			this.logMessage("    When I test the mode of the vacuum cleaner");
			AspirateurClassiqueMode result = this.acop.getMode();
			this.logMessage("    Then the vacuum cleaner is in its initial mode");
			if (!AspirateurClassique.INITIAL_MODE.equals(result)) {
				this.logMessage("     but was: " + result);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();
	}

	public void			testTurnOnOff()
	{
		this.logMessage("Feature: turning the vacuum cleaner on and off");

		this.logMessage("  Scenario: turning on when off");
		this.logMessage("    Given the vacuum cleaner is off");
		this.given(AspirateurClassiqueState.OFF);
		try {
			this.logMessage("    When the vacuum cleaner is turned on");
			this.acop.turnOn();
			this.logMessage("    Then the vacuum cleaner is on");
			if (!AspirateurClassiqueState.ON.equals(this.acop.getState())) {
				this.logMessage("     but was: " + this.acop.getState());
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the vacuum cleaner is in mode low");
			if (!AspirateurClassiqueMode.LOW.equals(this.acop.getMode())) {
				this.logMessage("     but was: " + this.acop.getMode());
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the mopping function is not active");
			if (this.acop.isMopActive()) {
				this.logMessage("     but it was active");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning on when on");
		this.logMessage("    Given the vacuum cleaner is on");
		this.given(AspirateurClassiqueState.ON);
		this.logMessage("    When the vacuum cleaner is turned on");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.turnOn());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning off when on");
		this.logMessage("    Given the vacuum cleaner is on");
		this.given(AspirateurClassiqueState.ON);
		try {
			this.logMessage("    When the vacuum cleaner is turned off");
			this.acop.turnOff();
			this.logMessage("    Then the vacuum cleaner is off");
			if (!AspirateurClassiqueState.OFF.equals(this.acop.getState())) {
				this.logMessage("     but was: " + this.acop.getState());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning off when off");
		this.logMessage("    Given the vacuum cleaner is off");
		this.given(AspirateurClassiqueState.OFF);
		this.logMessage("    When the vacuum cleaner is turned off");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.turnOff());
		this.statistics.updateStatistics();
	}

	public void			testSetLowHigh()
	{
		this.logMessage("Feature: setting the suction mode of the vacuum cleaner");

		this.logMessage("  Scenario: setting high when low");
		this.logMessage("    Given the vacuum cleaner is on and in mode low");
		try {
			this.acop.turnOn();
			this.logMessage("    When the vacuum cleaner is set high");
			this.acop.setHigh();
			this.logMessage("    Then the vacuum cleaner is in mode high");
			if (!AspirateurClassiqueMode.HIGH.equals(this.acop.getMode())) {
				this.logMessage("     but was: " + this.acop.getMode());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting high when high");
		this.logMessage("    Given the vacuum cleaner is on and in mode high");
		this.logMessage("    When the vacuum cleaner is set high");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.setHigh());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting low when high");
		this.logMessage("    Given the vacuum cleaner is on and in mode high");
		try {
			this.logMessage("    When the vacuum cleaner is set low");
			this.acop.setLow();
			this.logMessage("    Then the vacuum cleaner is in mode low");
			if (!AspirateurClassiqueMode.LOW.equals(this.acop.getMode())) {
				this.logMessage("     but was: " + this.acop.getMode());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting low when low");
		this.logMessage("    Given the vacuum cleaner is on and in mode low");
		this.logMessage("    When the vacuum cleaner is set low");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.setLow());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the mode when off");
		this.logMessage("    Given the vacuum cleaner is off");
		try {
			this.acop.turnOff();
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the vacuum cleaner is set high");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.setHigh());
		this.statistics.updateStatistics();
	}

	public void			testMop()
	{
		this.logMessage("Feature: mopping with the vacuum cleaner");

		this.logMessage("  Scenario: activating the mop when on");
		this.logMessage("    Given the vacuum cleaner is on without mopping");
		try {
			this.acop.turnOn();
			this.logMessage("    When the mopping function is activated");
			this.acop.activateMop();
			this.logMessage("    Then the mopping function is active");
			if (!this.acop.isMopActive()) {
				this.logMessage("     but it was not active");
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the vacuum cleaner is still on");
			if (!AspirateurClassiqueState.ON.equals(this.acop.getState())) {
				this.logMessage("     but was: " + this.acop.getState());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: activating the mop when already active");
		this.logMessage("    Given the mopping function is active");
		this.logMessage("    When the mopping function is activated");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.activateMop());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: changing the suction mode while mopping");
		this.logMessage("    Given the mopping function is active");
		try {
			this.logMessage("    When the vacuum cleaner is set high");
			this.acop.setHigh();
			this.logMessage("    Then the vacuum cleaner is in mode high");
			if (!AspirateurClassiqueMode.HIGH.equals(this.acop.getMode())) {
				this.logMessage("     but was: " + this.acop.getMode());
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the mopping function is still active");
			if (!this.acop.isMopActive()) {
				this.logMessage("     but it was not active");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: deactivating the mop when active");
		this.logMessage("    Given the mopping function is active");
		try {
			this.logMessage("    When the mopping function is deactivated");
			this.acop.deactivateMop();
			this.logMessage("    Then the mopping function is not active");
			if (this.acop.isMopActive()) {
				this.logMessage("     but it was active");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: deactivating the mop when not active");
		this.logMessage("    Given the mopping function is not active");
		this.logMessage("    When the mopping function is deactivated");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.deactivateMop());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: turning off while mopping");
		this.logMessage("    Given the vacuum cleaner is on and mopping");
		try {
			this.acop.activateMop();
			this.logMessage("    When the vacuum cleaner is turned off");
			this.acop.turnOff();
			this.logMessage("    Then the mopping function is not active");
			if (this.acop.isMopActive()) {
				this.logMessage("     but it was active");
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: activating the mop when off");
		this.logMessage("    Given the vacuum cleaner is off");
		this.given(AspirateurClassiqueState.OFF);
		this.logMessage("    When the mopping function is activated");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectPreconditionViolation(() -> this.acop.activateMop());
		this.statistics.updateStatistics();
	}

	/**
	 * run all the unit tests, in the order required by the state they leave
	 * the vacuum cleaner in, and print the statistics.
	 */
	protected void		runAllUnitTests()
	{
		this.testGetState();
		this.testGetMode();
		this.testTurnOnOff();
		this.testSetLowHigh();
		this.testMop();
		this.statistics.statisticsReport(this);
	}

	// -------------------------------------------------------------------------
	// Component life-cycle
	// -------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.acop.getPortURI(),
					this.aspirateurInboundPortURI,
					AspirateurClassiqueConnector.class.getCanonicalName());
		} catch (Throwable e) {
			throw new ComponentStartException(e);
		}
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#execute()
	 */
	@Override
	public synchronized void	execute() throws Exception
	{
		this.traceMessage("Vacuum cleaner tester starts the tests.\n");
		this.runAllUnitTests();
		this.traceMessage("Vacuum cleaner tester ends.\n");
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.acop.getPortURI());
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.acop.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
}
