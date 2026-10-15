package equipements.variable.refrigerateur;

import equipements.variable.refrigerateur.connections.RefrigerateurExternalControlConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurExternalControlOutboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurInternalControlConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurInternalControlOutboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurUserConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurUserOutboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
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

@RequiredInterfaces(required = {RefrigerateurUserCI.class,
		RefrigerateurInternalControlCI.class,
		RefrigerateurExternalControlCI.class})
public class RefrigerateurTester extends		AbstractComponent{
	
	public static boolean				VERBOSE = false;

	public static int					X_RELATIVE_POSITION = 0;

	public static int					Y_RELATIVE_POSITION = 0;

	protected final boolean				isUnitTest;

	protected RefrigerateurUserOutboundPort ruop;

	protected RefrigerateurInternalControlOutboundPort riop;

	protected RefrigerateurExternalControlOutboundPort reop;

	protected String refrigerateurUserInboundPortURI;
	protected String refrigerateurInternalControlInboundPortURI;
	protected String refrigerateurExternalControlInboundPortURI;
	
	protected TestsStatistics			statistics;
	
	
	protected static boolean	implementationInvariants(RefrigerateurTester rt)
	{
		assert	rt != null : new PreconditionException("rt != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				rt.refrigerateurUserInboundPortURI != null &&
										!rt.refrigerateurUserInboundPortURI.isEmpty(),
										RefrigerateurTester.class, rt,
				"rt.refrigerateurUserInboundPortURI != null && "
								+ "!rt.refrigerateurUserInboundPortURI.isEmpty()");
		ret &= AssertionChecking.checkImplementationInvariant(
				rt.refrigerateurInternalControlInboundPortURI != null &&
										!rt.refrigerateurInternalControlInboundPortURI.isEmpty(),
										RefrigerateurTester.class, rt,
				"rt.refrigerateurInternalControlInboundPortURI != null && "
								+ "!rt.refrigerateurInternalControlInboundPortURI.isEmpty()");
		ret &= AssertionChecking.checkImplementationInvariant(
				rt.refrigerateurExternalControlInboundPortURI != null &&
										!rt.refrigerateurExternalControlInboundPortURI.isEmpty(),
										RefrigerateurTester.class, rt,
				"rt.refrigerateurExternalControlInboundPortURI != null && "
								+ "!rt.refrigerateurExternalControlInboundPortURI.isEmpty()");
		return ret;
	}
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
						RefrigerateurTester.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
						RefrigerateurTester.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(RefrigerateurTester rt)
	{
		assert	rt != null : new PreconditionException("rt != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	protected			RefrigerateurTester(boolean isUnitTest) throws Exception
	{
		this(isUnitTest, Refrigerateur.USER_INBOUND_PORT_URI, 
				Refrigerateur.INTERNAL_CONTROL_INBOUND_PORT_URI,
				Refrigerateur.EXTERNAL_CONTROL_INBOUND_PORT_URI
				);
	}
	
	protected			RefrigerateurTester(
			boolean isUnitTest,
			String refrigerateurUserInboundPortURI,
			String refrigerateurInternalControlInboundPortURI,
			String refrigerateurExternalControlInboundPortURI
			) throws Exception
		{
			super(1, 0);

			assert	refrigerateurUserInboundPortURI != null &&
											!refrigerateurUserInboundPortURI.isEmpty() :
					new PreconditionException(
							"refrigerateurUserInboundPortURI != null && "
							+ "!refrigerateurUserInboundPortURI.isEmpty()");
			
			assert	refrigerateurInternalControlInboundPortURI != null &&
					!refrigerateurInternalControlInboundPortURI.isEmpty() :
					new PreconditionException(
						"refrigerateurInternalControlInboundPortURI != null && "
						+ "!refrigerateurInternalControlInboundPortURI.isEmpty()");
			
			assert	refrigerateurExternalControlInboundPortURI != null &&
					!refrigerateurExternalControlInboundPortURI.isEmpty() :
					new PreconditionException(
						"refrigerateurExternalControlInboundPortURI != null && "
						+ "!refrigerateurExternalControlInboundPortURI.isEmpty()");
			

			this.isUnitTest = isUnitTest;
			this.initialise(refrigerateurUserInboundPortURI,
					refrigerateurInternalControlInboundPortURI,
					refrigerateurExternalControlInboundPortURI);
		}
	
	protected			RefrigerateurTester(
			boolean isUnitTest,
			String refrigerateurUserInboundPortURI,
			String refrigerateurInternalControlInboundPortURI,
			String refrigerateurExternalControlInboundPortURI,
			String reflectionInboundPortURI
			) throws Exception
		{
			super(reflectionInboundPortURI, 1, 0);

			this.isUnitTest = isUnitTest;
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
			this.refrigerateurUserInboundPortURI = refrigerateurUserInboundPortURI;
			this.refrigerateurInternalControlInboundPortURI = refrigerateurInternalControlInboundPortURI;
			this.refrigerateurExternalControlInboundPortURI = refrigerateurExternalControlInboundPortURI;
			
			this.ruop = new RefrigerateurUserOutboundPort(this);
			this.ruop.publishPort();
			
			this.riop = new RefrigerateurInternalControlOutboundPort(this);
			this.riop.publishPort();
			
			this.reop = new RefrigerateurExternalControlOutboundPort(this);
			this.reop.publishPort();

			if (VERBOSE) {
				this.tracer.get().setTitle("Refrigerateur tester component");
				this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
													  Y_RELATIVE_POSITION);
				this.toggleTracing();
			}

			this.statistics = new TestsStatistics();

			assert	RefrigerateurTester.implementationInvariants(this) :
					new ImplementationInvariantException(
							"RefrigerateurTester.implementationInvariants(this)");
			assert	RefrigerateurTester.invariants(this) :
					new InvariantException("RefrigerateurTester.invariants(this)");
		}


	@Override
	public synchronized void	start()
	throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
							this.ruop.getPortURI(),
							refrigerateurUserInboundPortURI,
							RefrigerateurUserConnector.class.getCanonicalName());
			this.doPortConnection(
					this.riop.getPortURI(),
					refrigerateurInternalControlInboundPortURI,
					RefrigerateurInternalControlConnector.class.getCanonicalName());
			this.doPortConnection(
					this.reop.getPortURI(),
					refrigerateurExternalControlInboundPortURI,
					RefrigerateurExternalControlConnector.class.getCanonicalName());
		} catch (Throwable e) {
			throw new ComponentStartException(e) ;
		}
	}
	
	@Override
	public synchronized void execute() throws Exception
	{
		this.traceMessage("Refrigerateur Tester starts the tests.\n");
		this.runAllUnitTests();
		this.traceMessage("Refrigerateur Tester ends.\n");
	}

	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.ruop.getPortURI());
		this.doPortDisconnection(this.riop.getPortURI());
		this.doPortDisconnection(this.reop.getPortURI());
		super.finalise();
	}

	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.ruop.unpublishPort();
			this.riop.unpublishPort();
			this.reop.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
	
	protected void		testSimple()
	{
		this.traceMessage("--- Test simple du refrigerateur ---\n");
		try {
			// 1. etat initial : eteint
			this.traceMessage("1. on() = " + this.ruop.on() + "\n");

			// 2. allumer (habitant)
			this.ruop.switchOn();
			this.traceMessage("2. apres switchOn : on() = " + this.ruop.on() + "\n");

			// 3. mode initial
			this.traceMessage("3. mode initial = " + this.ruop.getMode() + "\n");

			// 4. changer de mode (habitant)
			this.ruop.setMode(RefrigerateurMode.RAPIDE);
			this.traceMessage("4. apres setMode(RAPIDE) : mode = "
							  + this.ruop.getMode() + "\n");

			// 5. changer la temperature cible (habitant)
			this.ruop.setTargetTemperature(
						new Measure<Double>(3.0, MeasurementUnit.CELSIUS));
			this.traceMessage("5. cible = "
					+ this.ruop.getTargetTemperature().getMeasure().getData()
					+ " C\n");

			// 6. temperature courante (fausse a l'etape 1)
			this.traceMessage("6. temperature courante = "
					+ this.ruop.getCurrentTemperature().getMeasure().getData()
					+ " C\n");

			// 7. le thermostat demarre le compresseur
			this.riop.startCooling();
			this.traceMessage("7. apres startCooling : cooling() = "
							  + this.riop.cooling() + "\n");

			// 8. le HEM suspend le frigo
			this.reop.suspend();
			this.traceMessage("8. apres suspend : suspended() = "
							  + this.reop.suspended()
							  + ", cooling() = " + this.riop.cooling() + "\n");

			// 9. le HEM reprend le frigo
			this.reop.resume();
			this.traceMessage("9. apres resume : suspended() = "
							  + this.reop.suspended() + "\n");

			// 10. puissance d'un mode (HEM)
			this.traceMessage("10. puissance du mode ECO = "
					+ this.reop.getModePower(RefrigerateurMode.ECO).getData()
					+ " W\n");

			// 11. eteindre (habitant)
			this.ruop.switchOff();
			this.traceMessage("11. apres switchOff : on() = "
							  + this.ruop.on() + "\n");

		} catch (Throwable e) {
			this.traceMessage("ERREUR : " + e + "\n");
		}
		this.traceMessage("--- Fin du test simple ---\n");
	}

	protected void		runAllUnitTests()
	{
		this.testSwitchOnOff();
		this.testTemperature();
		this.testSetMode();
		this.testCooling();
		this.statistics.statisticsReport(this);
	
	}
	
	public void			testSwitchOnOff()
	{
		this.logMessage("Feature: switching the Refrigerateur on and off");
		this.logMessage("  Scenario: switching on when off");
		boolean resultState ;
		RefrigerateurMode resultMode = null;
		try {
			this.logMessage("    Given the Refrigerateur is off");
			resultState = this.ruop.on();
			if (resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
			this.logMessage("    When the Refrigerateur is turned on");
			this.ruop.switchOn();
			this.logMessage("    Then the Refrigerateur is on");
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
			this.logMessage("    And the Refrigerateur is in mode NORMAL");
			resultMode = this.ruop.getMode();
			if (!RefrigerateurMode.NORMAL.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: switching on when on");
		this.logMessage("    Given the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is turned on");
		this.logMessage("    Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.switchOn();
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch(Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: switching off when on");
		this.logMessage("    Given the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is turned off");
		try {
			this.ruop.switchOff();
			this.logMessage("    Then the Refrigerateur is off");
			resultState = this.ruop.on();
			if (resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: switching off when off");
		this.logMessage("    Given the Refrigerateur is off");
		try {
			resultState = this.ruop.on();
			if (resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is turned off");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.switchOff();
			this.logMessage("     but the precondition exception was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();
	}

	protected void		testTemperature()
	{
		this.logMessage("Feature: getting and setting the target temperature"
						+ " of the Refrigerateur");

		this.logMessage("  Scenario: getting the target temperature through the"
						+ " user interface when just initialised");
		this.logMessage("    Given the Refrigerateur is initialised");
		this.logMessage("    And the Refrigerateur has not been used yet");
		this.logMessage("    And the Refrigerateur is on");
		boolean resultState;
		TimedMeasure<Double> temperature = null;
		try {
			this.ruop.switchOn();
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: off");
				this.statistics.failedCondition();
			}
			this.logMessage("    When I get the target temperature through the "
							+ "user interface ");
			temperature = this.ruop.getTargetTemperature();
			if (temperature.getMeasure().getData() ==
					Refrigerateur.STANDARD_TARGET_TEMPERATURE.getData()
				&& temperature.getMeasure().getMeasurementUnit().equals(
													MeasurementUnit.CELSIUS)) {
				this.logMessage("    Then the target temperature of the Refrigerateur"
								+ " is the Refrigerateur standard target temperature");
			} else {
				this.logMessage("     but was: " + temperature.getMeasure());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: getting the target temperature through the internal control interface when just initialised");
		this.logMessage("    Given the Refrigerateur is initialised");
		this.logMessage("    And the Refrigerateur has not been used yet");
		this.logMessage("    And the Refrigerateur is on");
		try {
			this.logMessage("    When I get the target temperature through the internal control interface");
			temperature = this.riop.getTargetTemperature();
			if (temperature.getMeasure().getData() ==
					Refrigerateur.STANDARD_TARGET_TEMPERATURE.getData()
				&& temperature.getMeasure().getMeasurementUnit().equals(
													MeasurementUnit.CELSIUS)) {
				this.logMessage("    Then the target temperature of the Refrigerateur"
								+ " is the Refrigerateur standard target temperature");
			} else {
				this.logMessage("     but was: " + temperature.getMeasure());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the target temperature of the "
						+ "Refrigerateur when on");
		this.logMessage("    Given the Refrigerateur is initialised");
		this.logMessage("    And the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: off");
				this.statistics.failedCondition();
			}
			this.logMessage("    When I set the temperature at any given "
							+ "temperature between 2 and 8 Celsius inclusive");
			this.ruop.setTargetTemperature(
					new Measure<Double>(3.0, Refrigerateur.TEMPERATURE_UNIT));
			temperature = this.ruop.getTargetTemperature();
			if (temperature.getMeasure().getData() == 3.0 &&
					temperature.getMeasure().getMeasurementUnit().equals(
													MeasurementUnit.CELSIUS)) {
				this.logMessage("    Then the target temperature of the Refrigerateur"
								+ " is the given temperature");
			} else {
				this.statistics.incorrectResult();
				this.logMessage("     but was not: " + temperature.getMeasure());
			}

		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
		
		this.logMessage("  Scenario: getting the current temperature of the "
				+ "  Refrigerateur when on");
		this.logMessage("  Given the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: off");
				this.statistics.failedCondition();
			}
			this.logMessage("    When I get the current temperature through "
							+ " the user interface");
			temperature = this.ruop.getCurrentTemperature();
			this.logMessage("    Then the current temperature is the fake "
							+ " temperature 5 Celsius");
			if (temperature.getMeasure().getData() !=
						Refrigerateur.FAKE_CURRENT_TEMPERATURE.getMeasure().getData()
				|| !temperature.getMeasure().getMeasurementUnit().equals(
													MeasurementUnit.CELSIUS)) {
				this.logMessage("     but was: " + temperature.getMeasure());
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		
		this.statistics.updateStatistics();

		this.logMessage("  Scenario: setting the target temperature of the "
				+ "  Refrigerateurout of the range");
		this.logMessage("    And the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: off");
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When I set the temperature to 10 Celsius"
						+ "  Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.setTargetTemperature(
					new Measure<Double>(10.0, Refrigerateur.TEMPERATURE_UNIT));
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu : la precondition MIN <= target <= MAX a echoue
		} finally {
			BCMException.VERBOSE = old;
		}
		this.logMessage("    And the Refrigerateur is switched off");
		try {
			this.ruop.switchOff();
		} catch (Throwable e) {
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();
	}

	public void			testSetMode()
	{
		this.logMessage("Feature: switching the Refrigerateur ECO, NORMAL and RAPIDE.");
		this.logMessage("  Scenario: set the Refrigerateur RAPIDE from NORMAL");
		this.logMessage("    Given the Refrigerateur is on");
		boolean resultState;
		RefrigerateurMode resultMode = null;
		try {
			this.ruop.switchOn();
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And the Refrigerateur is NORMAL");
			resultMode = this.ruop.getMode();
			if (!RefrigerateurMode.NORMAL.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    When the Refrigerateur is set RAPIDE");
			this.logMessage("    Then the Refrigerateur is on");
			
			this.ruop.setMode(RefrigerateurMode.RAPIDE);
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And  the Refrigerateur is RAPIDE");
			resultMode = this.ruop.getMode();
			if (!RefrigerateurMode.RAPIDE.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: set the Refrigerateur RAPIDE from RAPIDE");
		this.logMessage("    Given the Refrigerateur is on");
		this.logMessage("    And the Refrigerateur is RAPIDE");
		this.logMessage("    When the Refrigerateur is set RAPIDE");
		this.logMessage("    Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.setMode(RefrigerateurMode.RAPIDE);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		
		this.logMessage("  Scenario: set the Refrigerateur mode to null");
		this.logMessage("    Given the Refrigerateur is on");
		try {
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur mode is set to null");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.setMode(null);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu : la precondition m != null a echoue
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: set the Refrigerateur ECO from RAPIDE while cooling");
		this.logMessage("    Given the Refrigerateur is on");
		this.logMessage("    and  the Refrigerateur is cooling");
		
		try {

			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		
		try {
			this.riop.startCooling();
			if (!this.riop.cooling()) {
				this.logMessage("    but was not cooling ");
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And the Refrigerateur is RAPIDE");
			resultMode = this.ruop.getMode();
			if (!RefrigerateurMode.RAPIDE.equals(resultMode)) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    When the Refrigerateur is set ECO");
			this.logMessage("    Then the Refrigerateur is on");
			
			this.ruop.setMode(RefrigerateurMode.ECO);
			resultState = this.ruop.on();
			if (!resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		try {
			this.logMessage("    And  the Refrigerateur is ECO");
			resultMode = this.ruop.getMode();
			if (!RefrigerateurMode.ECO.equals(resultMode) || !this.riop.cooling()) {
				this.logMessage("     but was: " + resultMode);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		
		this.logMessage("  Scenario: set the Refrigerateur NORMAL when off");
		this.logMessage("    Given the Refrigerateur is off");
		try {
			this.ruop.switchOff();
			resultState = this.ruop.on();
			if (resultState) {
				this.logMessage("     but was: " + resultState);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is set NORMAL");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.ruop.setMode(RefrigerateurMode.NORMAL);
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			// attendu : la precondition getState() == ON a echoue
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();
	}

	public void			testCooling()
	{
		this.logMessage("Feature: cooling the Refrigerateur on and off");
		this.logMessage("Scenario: starting cooling when not cooling ");
		boolean resultState ;
		boolean isCooling;
		try {
			this.logMessage("    Given the Refrigerateur is on and is not cooling");
			this.ruop.switchOn();
			resultState = this.ruop.on();
			isCooling = this.riop.cooling();
			if (!resultState || isCooling) {
				this.logMessage("     but was: on=" + resultState + "     /but was: cooling=" + isCooling );
				this.statistics.failedCondition();
			}
			this.logMessage("    When the Refrigerateur is starts cooling");
			this.riop.startCooling();
			this.logMessage("    Then the Refrigerateur is cooling");
			isCooling = this.riop.cooling();
			if (!isCooling) {
				this.logMessage("     but was: " + isCooling);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: starting cooling when cooling");
		this.logMessage("    Given the Refrigerateur is cooling");
		try {
			isCooling = this.riop.cooling();
			if (!isCooling) {
				this.logMessage("     but was: " + isCooling);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is starts cooling");
		this.logMessage("    Then a precondition exception is thrown");
		boolean old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.riop.startCooling();
			this.logMessage("     but it was not thrown");
			this.statistics.incorrectResult();
		} catch(Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: stopping cooling when cooling");
		this.logMessage("    Given the Refrigerateur is cooling");
		try {
			isCooling = this.riop.cooling();
			if (!isCooling) {
				this.logMessage("     but was: " + isCooling);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is stops cooling");
		try {
			this.riop.stopCooling();
			this.logMessage("    Then the Refrigerateur is not cooling");
			isCooling = this.riop.cooling();
			if (isCooling) {
				this.logMessage("     but was: " + isCooling);
				this.statistics.incorrectResult();
			}
		} catch (Throwable e) {
			this.statistics.incorrectResult();
			this.logMessage("     but the exception " + e + " has been raised");
		}

		this.statistics.updateStatistics();

		this.logMessage("  Scenario: stopping cooling when not cooling");
		this.logMessage("    Given the Refrigerateur is not cooling");
		try {
			isCooling = this.riop.cooling();
			if (isCooling) {
				this.logMessage("     but was: " + isCooling);
				this.statistics.failedCondition();
			}
		} catch (Throwable e) {
			this.statistics.failedCondition();
			this.logMessage("     but the exception " + e + " has been raised");
		}
		this.logMessage("    When the Refrigerateur is stops cooling");
		this.logMessage("    Then a precondition exception is thrown");
		old = BCMException.VERBOSE;
		try {
			BCMException.VERBOSE = false;
			this.riop.stopCooling();
			this.logMessage("     but the precondition exception was not thrown");
			this.statistics.incorrectResult();
		} catch (Throwable e) {
			
		} finally {
			BCMException.VERBOSE = old;
		}

		this.statistics.updateStatistics();
	}


}
