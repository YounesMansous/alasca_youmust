package equipements.modulable;

import equipements.modulable.connections.MachineCafeAutomatiqueExternalControlConnector;
import equipements.modulable.connections.MachineCafeAutomatiqueExternalControlOutboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueInternalControlConnector;
import equipements.modulable.connections.MachineCafeAutomatiqueInternalControlOutboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueUserConnector;
import equipements.modulable.connections.MachineCafeAutomatiqueUserOutboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.utils.tests.TestsStatistics;

/**
 * La classe <code>MachineCafeAutomatiqueTester</code> est un composant qui
 * teste la machine à café. Chaque test est écrit en Gherkin (Feature,
 * Scenario, Given, When, Then) dans les messages affichés.
 *
 * <p>
 * Le tester joue deux rôles : l'utilisateur (commande, capsule, réservoir)
 * et le contrôleur interne (il déclenche à la main les phases automatiques,
 * comme un "bouchon", car il n'y a pas encore de simulation).
 * </p>
 *
 * <p>Les tests s'exécutent dans l'ordre de {@link #runAllUnitTests()} : chaque
 * test part de l'état laissé par le précédent.</p>
 */
@RequiredInterfaces(required = {MachineCafeAutomatiqueUserCI.class,
								MachineCafeAutomatiqueInternalControlCI.class,
								MachineCafeAutomatiqueExternalControlCI.class})
public class MachineCafeAutomatiqueTester
extends		AbstractComponent
{
	// -------------------------------------------------------------------------
	// Types et variables
	// -------------------------------------------------------------------------

	/** Une action qui peut lever une exception (pour tester les préconditions). */
	protected interface Action
	{
		void run() throws Exception;
	}

	/** Affichage des messages dans une fenêtre de trace. */
	public static boolean	VERBOSE = false;
	/** Position de la fenêtre de trace. */
	public static int		X_RELATIVE_POSITION = 0;
	public static int		Y_RELATIVE_POSITION = 0;

	// Les trois ports sortants vers la machine
	protected MachineCafeAutomatiqueUserOutboundPort			userop;
	protected MachineCafeAutomatiqueInternalControlOutboundPort	internalop;
	protected MachineCafeAutomatiqueExternalControlOutboundPort	externalop;

	// URI des ports entrants de la machine
	protected String	userInboundPortURI;
	protected String	internalInboundPortURI;
	protected String	externalInboundPortURI;

	/** Compteur des résultats de tests. */
	protected TestsStatistics	statistics;

	// -------------------------------------------------------------------------
	// Constructeurs
	// -------------------------------------------------------------------------

	/** Crée un tester avec les URI par défaut de la machine. */
	protected MachineCafeAutomatiqueTester() throws Exception
	{
		this(MachineCafeAutomatique.USER_INBOUND_PORT_URI,
			 MachineCafeAutomatique.INTERNAL_CONTROL_INBOUND_PORT_URI,
			 MachineCafeAutomatique.EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	/** Crée un tester avec les URI des ports entrants de la machine. */
	protected MachineCafeAutomatiqueTester(
		String userInboundPortURI,
		String internalInboundPortURI,
		String externalInboundPortURI
		) throws Exception
	{
		super(1, 0);
		this.userInboundPortURI = userInboundPortURI;
		this.internalInboundPortURI = internalInboundPortURI;
		this.externalInboundPortURI = externalInboundPortURI;

		this.userop = new MachineCafeAutomatiqueUserOutboundPort(this);
		this.userop.publishPort();
		this.internalop =
				new MachineCafeAutomatiqueInternalControlOutboundPort(this);
		this.internalop.publishPort();
		this.externalop =
				new MachineCafeAutomatiqueExternalControlOutboundPort(this);
		this.externalop.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Coffee machine tester component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}
		this.statistics = new TestsStatistics();
	}

	// -------------------------------------------------------------------------
	// Outils pour écrire les tests
	// -------------------------------------------------------------------------

	/** Vérifie la condition d'un "Then". Sinon, compte une erreur. */
	protected void		check(boolean condition, String message)
	{
		if (!condition) {
			this.logMessage("     but " + message);
			this.statistics.incorrectResult();
		}
	}

	/** Vérifie la condition d'un "Given". Sinon, compte une condition non remplie. */
	protected void		given(boolean condition, String message)
	{
		if (!condition) {
			this.logMessage("     but " + message);
			this.statistics.failedCondition();
		}
	}

	/** Vérifie qu'une action lève bien une exception (précondition violée). */
	protected void		expectException(Action action)
	{
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			action.run();
			this.logMessage("     but no exception was thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu
		} finally {
			BCMException.VERBOSE = old;
		}
	}

	/** Vérifie qu'une transition n'a pas lieu : retour false ou exception. */
	protected void		expectRefused(Action action, String name)
	{
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			action.run();
		} catch (Throwable e) {
			// refus par exception : accepté
		} finally {
			BCMException.VERBOSE = old;
		}
	}

	/** Affiche l'exception inattendue et compte une erreur. */
	protected void		unexpected(Throwable e)
	{
		this.logMessage("     but the exception " + e + " has been raised");
		this.statistics.incorrectResult();
	}

	// -------------------------------------------------------------------------
	// Tests (Gherkin)
	// -------------------------------------------------------------------------

	/** La machine neuve est éteinte, sans capsule ni commande. */
	public void			testInitialState()
	{
		this.logMessage("Feature: Initial state of the coffee machine");
		this.logMessage("  Scenario: reading the state of a new machine");
		this.logMessage("    Given the coffee machine has not been used yet");
		try {
			this.logMessage("    When I read its state");
			MachineCafeAutomatiqueState s = this.internalop.getState();
			this.logMessage("    Then the machine is off");
			this.check(MachineCafeAutomatiqueState.OFF.equals(s),
					   "was: " + s);
			this.check(!this.internalop.on(), "on() was true");
			this.logMessage("    And there is no capsule");
			this.check(!this.internalop.isCapsulIn(), "a capsule was in");
			this.logMessage("    And no drink is selected");
			this.check(this.internalop.getSelectedOption() == null,
					   "a drink was selected");
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();
	}

	/** Commande sans capsule : attente, puis capsule insérée : prête. */
	public void			testOrderWithoutCapsule()
	{
		this.logMessage("Feature: ordering a coffee");
		this.logMessage("  Scenario: ordering without a capsule");
		this.logMessage("    Given the machine is off with a full tank");
		try {
			this.userop.refillTank();
			this.given(MachineCafeAutomatiqueState.OFF.equals(
								this.internalop.getState()), "not off");
			this.given(!this.internalop.isCapsulIn(), "a capsule was in");
			this.logMessage("    When the user orders an espresso");
			this.userop.orderCoffee(MachineCafeAutomatiqueOption.ESPRESSO);
			this.logMessage("    Then the machine is awaiting");
			this.check(this.internalop.isAwaiting(), "was not awaiting");
			this.logMessage("    And the espresso is the selected drink");
			this.check(MachineCafeAutomatiqueOption.ESPRESSO.equals(
							this.internalop.getSelectedOption()),
					   "selected drink was: "
								+ this.internalop.getSelectedOption());
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: inserting the missing capsule");
		this.logMessage("    Given the machine is awaiting a capsule");
		try {
			this.given(this.internalop.isAwaiting(), "was not awaiting");
			this.logMessage("    When the user inserts a capsule");
			this.userop.insertCapsule();
			this.logMessage("    Then a capsule is in");
			this.check(this.internalop.isCapsulIn(), "no capsule was in");
			this.logMessage("    And the machine is ready");
			this.check(this.internalop.isReady(),
					   "state was: " + this.internalop.getState());
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();
	}

	/** Les phases automatiques, déclenchées ici à la main (bouchons). */
	public void			testAutomaticPhases()
	{
		this.logMessage("Feature: automatic phases of the coffee machine");
		this.logMessage("  Scenario: from ready to heating");
		this.logMessage("    Given the machine is ready");
		try {
			this.given(this.internalop.isReady(), "was not ready");
			this.logMessage("    When the controller starts the heating");
			boolean r = this.internalop.startHeating();
			this.logMessage("    Then the transition is done");
			this.check(r, "startHeating() returned false");
			this.logMessage("    And the machine is heating");
			this.check(this.internalop.isHeating(),
					   "state was: " + this.internalop.getState());
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: from heating to brewing");
		this.logMessage("    Given the machine is heating");
		try {
			this.given(this.internalop.isHeating(), "was not heating");
			this.logMessage("    When the controller starts the brewing");
			boolean r = this.internalop.startBrewing();
			this.logMessage("    Then the transition is done");
			this.check(r, "startBrewing() returned false");
			this.logMessage("    And the machine is brewing");
			this.check(this.internalop.isBrewing(),
					   "state was: " + this.internalop.getState());
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: starting the heating when not ready");
		this.logMessage("    Given the machine is brewing");
		this.given(this.safeIsBrewing(), "was not brewing");
		this.logMessage("    When the controller starts the heating");
		this.logMessage("    Then the machine stays brewing");
		this.expectRefused(() -> this.internalop.startHeating(), "startHeating");
		this.check(this.safeIsBrewing(), "the machine left the brewing state");
		this.statistics.updateStatistics();
	}

	/** Pendant l'infusion, l'utilisateur ne peut rien changer. */
	public void			testUserCannotInterrupt()
	{
		this.logMessage("Feature: the user cannot interrupt a running cycle");
		this.logMessage("  Scenario: ordering while brewing");
		this.logMessage("    Given the machine is brewing");
		this.given(this.safeIsBrewing(), "was not brewing");
		this.logMessage("    When the user orders a coffee");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectException(() -> this.userop.orderCoffee(
									MachineCafeAutomatiqueOption.LONG_COFFEE));
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: inserting a capsule while brewing");
		this.logMessage("    Given the machine is brewing");
		this.logMessage("    When the user inserts a capsule");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectException(() -> this.userop.insertCapsule());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: refilling the tank while brewing");
		this.logMessage("    Given the machine is brewing");
		this.logMessage("    When the user refills the tank");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectException(() -> this.userop.refillTank());
		this.statistics.updateStatistics();
	}

	/** Le HEM lit et règle la puissance de la résistance. */
	public void			testExternalControl()
	{
		this.logMessage("Feature: external control of the coffee machine");
		this.logMessage("  Scenario: reading the maximum power level");
		this.logMessage("    Given the machine is on");
		try {
			this.given(this.externalop.on(), "on() was false");
			this.logMessage("    When the HEM reads the maximum power level");
			Measure<Double> max = this.externalop.getMaxPowerLevel();
			this.logMessage("    Then it is the one of the configuration");
			this.check(max != null && max.getData().equals(
						MachineCafeAutomatiqueConfigurationI.
											MAX_POWER_LEVEL.getData()),
					   "was: " + max);
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level");
		this.logMessage("    Given the machine is on");
		try {
			this.logMessage("    When the HEM sets the power level to 700 W");
			this.externalop.setCurrentPowerLevel(new Measure<Double>(700.0,
					MachineCafeAutomatiqueConfigurationI.POWER_UNIT));
			this.logMessage("    Then the current power level is 700 W");
			double p = this.externalop.getCurrentPowerLevel().
													getMeasure().getData();
			this.check(p == 700.0, "was: " + p);
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();
	}

	/** Arrêt par l'utilisateur, puis la machine n'est plus pilotable. */
	public void			testSwitchOff()
	{
		this.logMessage("Feature: switching off the coffee machine");
		this.logMessage("  Scenario: switching off while brewing");
		this.logMessage("    Given the machine is brewing");
		try {
			this.given(this.internalop.isBrewing(), "was not brewing");
			this.logMessage("    When the user switches it off");
			this.userop.switchOff();
			this.logMessage("    Then the machine is off");
			this.check(MachineCafeAutomatiqueState.OFF.equals(
								this.internalop.getState()),
					   "state was: " + this.internalop.getState());
			this.check(!this.externalop.on(), "on() was true");
		} catch (Throwable e) {
			this.unexpected(e);
		}
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: switching off when already off");
		this.logMessage("    Given the machine is off");
		this.logMessage("    When the user switches it off");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectException(() -> this.userop.switchOff());
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the power level when off");
		this.logMessage("    Given the machine is off");
		this.logMessage("    When the HEM sets the power level");
		this.logMessage("    Then a precondition exception is thrown");
		this.expectException(() -> this.externalop.setCurrentPowerLevel(
				new Measure<Double>(500.0,
					MachineCafeAutomatiqueConfigurationI.POWER_UNIT)));
		this.statistics.updateStatistics();
	}

	/** Lecture de isBrewing() sans propager d'exception. */
	protected boolean	safeIsBrewing()
	{
		try {
			return this.internalop.isBrewing();
		} catch (Throwable e) {
			return false;
		}
	}

	/** Lance tous les tests, dans l'ordre, puis affiche le bilan. */
	protected void		runAllUnitTests()
	{
		this.testInitialState();
		this.testOrderWithoutCapsule();
		this.testAutomaticPhases();
		this.testUserCannotInterrupt();
		this.testExternalControl();
		this.testSwitchOff();
		this.statistics.statisticsReport(this);
	}

	// -------------------------------------------------------------------------
	// Cycle de vie du composant
	// -------------------------------------------------------------------------

	/** Connecte les trois ports sortants à ceux de la machine. */
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();
		try {
			this.doPortConnection(
					this.userop.getPortURI(),
					this.userInboundPortURI,
					MachineCafeAutomatiqueUserConnector.class.
														getCanonicalName());
			this.doPortConnection(
					this.internalop.getPortURI(),
					this.internalInboundPortURI,
					MachineCafeAutomatiqueInternalControlConnector.class.
														getCanonicalName());
			this.doPortConnection(
					this.externalop.getPortURI(),
					this.externalInboundPortURI,
					MachineCafeAutomatiqueExternalControlConnector.class.
														getCanonicalName());
		} catch (Throwable e) {
			throw new ComponentStartException(e);
		}
	}

	/** Lance les tests. */
	@Override
	public synchronized void	execute() throws Exception
	{
		this.traceMessage("Coffee machine tester starts the tests.\n");
		this.runAllUnitTests();
		this.traceMessage("Coffee machine tester ends.\n");
	}

	/** Déconnecte les ports sortants. */
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.userop.getPortURI());
		this.doPortDisconnection(this.internalop.getPortURI());
		this.doPortDisconnection(this.externalop.getPortURI());
		super.finalise();
	}

	/** Dépublie les ports sortants. */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.userop.unpublishPort();
			this.internalop.unpublishPort();
			this.externalop.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
}
