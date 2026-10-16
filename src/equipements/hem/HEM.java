package equipements.hem;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import equipements.simple.hotte.Hotte;
import equipements.simple.hotte.HotteTester;
import equipements.variable.refrigerateur.Refrigerateur;
import equipements.variable.refrigerateur.RefrigerateurTester;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.exceptions.BCMRuntimeException;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.hem2026.bases.AdjustableCI;
import equipements.CVMIntegrationTest;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.Batteries;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.BatteriesCI;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.BatteriesUnitTester;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.connections.BatteriesConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.connections.BatteriesOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.Generator;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.GeneratorCI;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.GeneratorUnitTester;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.connections.GeneratorConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.connections.GeneratorOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.hem.AdjustableOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterCI;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeterUnitTester;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.connections.ElectricMeterConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.connections.ElectricMeterOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.SolarPanel;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.SolarPanelCI;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.SolarPanelUnitTester;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.connections.SolarPanelConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.connections.SolarPanelOutboundPort;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.WeatherStation;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.WeatherStationCI;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.WeatherStationUnitTester;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.connections.WeatherStationConnector;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.connections.WeatherStationOutboundPort;
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

@RequiredInterfaces(required = {ClocksServerCI.class,
		AdjustableCI.class,
		ElectricMeterCI.class,
		BatteriesCI.class,
		SolarPanelCI.class,
		GeneratorCI.class,
		WeatherStationCI.class})
public class HEM 
extends		AbstractComponent{

	public static boolean					VERBOSE = false;

	public static boolean					DEBUG = false;

	public static int						X_RELATIVE_POSITION = 0;

	public static int						Y_RELATIVE_POSITION = 0;

	protected final ElectricMeterOutboundPort		meterop;

	protected final BatteriesOutboundPort			batteriesop;

	protected final SolarPanelOutboundPort		solarPanelop;

	protected final GeneratorOutboundPort			generatorop;

	protected final WeatherStationOutboundPort	weatherStationOp;
	
	protected boolean						isPreFirstStep;

	protected AdjustableOutboundPort		hotteop;

	protected AdjustableOutboundPort		refrigerateurop;

	protected static int					NUMBER_OF_STANDARD_THREADS = 1;

	protected static int					NUMBER_OF_SCHEDULABLE_THREADS = 1;

	protected boolean						performTest;

	protected AcceleratedClock				ac;
	
	public static boolean	staticImplementationInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				NUMBER_OF_STANDARD_THREADS >= 0,
				HEM.class,
				"NUMBER_OF_STANDARD_THREADS >= 0");
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				NUMBER_OF_SCHEDULABLE_THREADS >= 0,
				HEM.class,
				"NUMBER_OF_SCHEDULABLE_THREADS");
		return ret;
	}

	protected static boolean	implementationInvariants(HEM hem)
	{
		assert	hem != null : new PreconditionException("hem != null");

		boolean ret = true;
		ret &= staticImplementationInvariants();
		return ret;
	}

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
				HEM.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				HEM.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(HEM hem)
	{
		assert	hem != null : new PreconditionException("hem != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}
	
	protected 			HEM() throws Exception
	{
		// by default, perform the tests planned in the method execute.
		this(true);
	}
	
	protected 			HEM(boolean performTest) throws Exception
	{
		// 1 standard thread to execute the method execute and 1 schedulable
		// thread that is used to perform the tests
		super(NUMBER_OF_STANDARD_THREADS, NUMBER_OF_SCHEDULABLE_THREADS);

		this.performTest = performTest;

		// by default, consider this execution as one in the pre-first step
		// and manage the hotte in a customised way.
		this.isPreFirstStep = true;

		this.meterop = new ElectricMeterOutboundPort(this);
		this.meterop.publishPort();
		this.batteriesop = new BatteriesOutboundPort(this);
		this.batteriesop.publishPort();
		this.solarPanelop = new SolarPanelOutboundPort(this);
		this.solarPanelop.publishPort();
		this.generatorop = new GeneratorOutboundPort(this);
		this.generatorop.publishPort();
		this.weatherStationOp = new WeatherStationOutboundPort(this);
		this.weatherStationOp.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Home Energy Manager component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		assert	HEM.implementationInvariants(this) :
				new ImplementationInvariantException(
						"HEM.implementationInvariants(this)");
		assert	HEM.invariants(this) :
				new InvariantException("HEM.invariants(this)");
	}
	
	@Override
	public synchronized void	start() throws ComponentStartException
	{
		super.start();

		try {
			this.doPortConnection(
					this.meterop.getPortURI(),
					ElectricMeter.ELECTRIC_METER_INBOUND_PORT_URI,
					ElectricMeterConnector.class.getCanonicalName());
			this.doPortConnection(
					batteriesop.getPortURI(),
					Batteries.STANDARD_INBOUND_PORT_URI,
					BatteriesConnector.class.getCanonicalName());
			this.doPortConnection(
					this.solarPanelop.getPortURI(),
					SolarPanel.STANDARD_INBOUND_PORT_URI,
					SolarPanelConnector.class.getCanonicalName());
			this.doPortConnection(
					this.generatorop.getPortURI(),
					Generator.STANDARD_INBOUND_PORT_URI,
					GeneratorConnector.class.getCanonicalName());
			this.doPortConnection(
					this.weatherStationOp.getPortURI(),
					WeatherStation.STANDARD_INBOUND_PORT_URI,
					WeatherStationConnector.class.getCanonicalName());

			if (this.isPreFirstStep) {
				// in this case, connect using the statically customised
				// hotte connector and keep a specific outbound port to
				// call the hotte.
				this.hotteop = new AdjustableOutboundPort(this);
				this.hotteop.publishPort();
				this.doPortConnection(
						this.hotteop.getPortURI(),
						Hotte.EXTERNAL_CONTROL_INBOUND_PORT_URI,
						HotteConnector.class.getCanonicalName());
				
				this.refrigerateurop = new AdjustableOutboundPort(this);
				this.refrigerateurop.publishPort();
				this.doPortConnection(
						this.refrigerateurop.getPortURI(),
						Refrigerateur.EXTERNAL_CONTROL_INBOUND_PORT_URI,
						RefrigerateurConnector.class.getCanonicalName());
			}
		} catch (Throwable e) {
			throw new ComponentStartException(e) ;
		}
	}

	@Override
	public synchronized void	execute() throws Exception
	{
		// First, get the clock and wait until the start time that it specifies.
		this.ac = null;
		ClocksServerOutboundPort clocksServerOutboundPort =
											new ClocksServerOutboundPort(this);
		clocksServerOutboundPort.publishPort();
		this.doPortConnection(
					clocksServerOutboundPort.getPortURI(),
					ClocksServer.STANDARD_INBOUNDPORT_URI,
					ClocksServerConnector.class.getCanonicalName());
		this.traceMessage("HEM gets the clock.\n");
		this.ac = clocksServerOutboundPort.getClock(CVMIntegrationTest.CLOCK_URI);
		this.doPortDisconnection(clocksServerOutboundPort.getPortURI());
		clocksServerOutboundPort.unpublishPort();
		this.traceMessage("HEM waits until start time.\n");
		this.ac.waitUntilStart();
		this.traceMessage("HEM starts.\n");

		if (this.performTest) {
			this.logMessage("Electric meter tests start.");
			this.testMeter();
			this.logMessage("Electric meter tests end.");
			this.logMessage("Batteries tests start.");
			this.testBatteries();
			this.logMessage("Batteries tests end.");
			this.logMessage("Solar Panel tests start.");
			this.testSolarPanel();
			this.logMessage("Solar Panel tests end.");
			this.logMessage("Generator tests start.");
			this.testGenerator();
			this.logMessage("Generator tests end.");
			this.logMessage("WeatherStation tests start.");
			this.testWeatherStation();
			this.logMessage("WeatherStation tests end.");
			if (this.isPreFirstStep) {
				this.scheduleTestHotte();
			}
			if (this.isPreFirstStep) {
				this.scheduleTestRefrigerateur();
			}
			
			
		}
	}
	
	@Override
	public synchronized void	finalise() throws Exception
	{
		this.doPortDisconnection(this.meterop.getPortURI());
		this.doPortDisconnection(this.batteriesop.getPortURI());
		this.doPortDisconnection(this.solarPanelop.getPortURI());
		this.doPortDisconnection(this.generatorop.getPortURI());
		this.doPortDisconnection(this.weatherStationOp.getPortURI());
		if (this.isPreFirstStep) {
			this.doPortDisconnection(this.hotteop.getPortURI());
			this.doPortDisconnection(this.refrigerateurop.getPortURI());
		}
		super.finalise();
	}
	
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.meterop.unpublishPort();
			this.batteriesop.unpublishPort();
			this.solarPanelop.unpublishPort();
			this.generatorop.unpublishPort();
			this.weatherStationOp.unpublishPort();
			if (this.isPreFirstStep) {
				this.hotteop.unpublishPort();
				this.refrigerateurop.unpublishPort();
			}
		} catch (Throwable e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	protected void		testMeter() throws Exception
	{
		ElectricMeterUnitTester.runAllTests(this, this.meterop,
											new TestsStatistics());
	}
	
	protected void		testBatteries() throws Exception
	{
		BatteriesUnitTester.runAllTests(this, this.batteriesop,
										new TestsStatistics());
	}

	protected void		testSolarPanel() throws Exception
	{
		SolarPanelUnitTester.runAllTests(this, this.solarPanelop,
										 new TestsStatistics());
	}
	
	protected void		testGenerator() throws Exception
	{
		GeneratorUnitTester.runAllTests(this, this.generatorop,
										 new TestsStatistics());
	}
	
	protected void		testWeatherStation()
	{
		TestsStatistics ts = new TestsStatistics();
		WeatherStationUnitTester.testCurrentInsideTemperature(
										this, this.weatherStationOp, ts);
		WeatherStationUnitTester.testCurrentOutsideTemperature(
										this, this.weatherStationOp, ts);
		ts.statisticsReport(this);
	}
	
	protected void		testHotte() throws Exception
	{
		this.logMessage("Hotte tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance mode management");
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    Given the hotte has just been turned on");
			this.logMessage("    When I call maxMode()");
			this.logMessage("    Then the result is its max mode index");
			final int maxMode = hotteop.maxMode();

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    Given the hotte has just been turned on");
			this.logMessage("    When I call currentMode()");
			this.logMessage("    Then the current mode is 1");
			int result = hotteop.currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the current mode index is 1");
			result = hotteop.currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then the method returns true");
			boolean bResult = hotteop.upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 2");
			result = hotteop.currentMode();
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();
			
			this.logMessage("  Scenario: going down one mode index");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the current mode index is 2");
			result = hotteop.currentMode();
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then the method returns true");
			bResult = hotteop.downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 1");
			result = hotteop.currentMode();
			if (result != 1) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode index");
			this.logMessage("    Given the hotte is turned on");
			int index = maxMode;
			this.logMessage("    And the max mode index is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(maxMode)");
			this.logMessage("    Then the method returns true");
			bResult = hotteop.setMode(maxMode);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is max mode");
			result = hotteop.currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("Feature: Getting the power consumption given a mode");
			this.logMessage("  Scenario: getting the power consumption of the maximum mode");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    When I get the power consumption of the maximum mode");
			double dResult = hotteop.getModeConsumption(maxMode);
			if (dResult != Hotte.BOOST_POWER.getData()) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}
			this.logMessage("    Then the result is the maximum power consumption of the hotte");

			statistics.updateStatistics();

			this.logMessage("Feature: suspending and resuming");
			this.logMessage("  Scenario: checking if suspended when not");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And it has not been suspended yet");
			this.logMessage("    When I check if suspended");
			bResult = hotteop.suspended();
			this.logMessage("    Then it is not");
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: suspending");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And it is not suspended");
			bResult = hotteop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call suspend()");
			bResult = hotteop.suspend();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the hotte is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index when suspended");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the hotte is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then a precondition exception is thrown");
			boolean old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				hotteop.downMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index when suspended");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the hotte is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				hotteop.upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode when suspended");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the hotte is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    And the mode index 1 is legitimate");
			if (1 > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				hotteop.setMode(1);
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode when suspended");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And the hotte is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I get the current mode");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				hotteop.currentMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: checking the emergency");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And it has just been suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call emergency()");
			dResult = hotteop.emergency();
			this.logMessage("    Then the emergency is between 0.0 and 1.0");
			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: resuming");
			this.logMessage("    Given the hotte is turned on");
			this.logMessage("    And it is suspended");
			bResult = hotteop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call resume()");
			bResult = hotteop.resume();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the hotte is not suspended");
			bResult = hotteop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);

		this.logMessage("hotte tests end.");
	}

	protected void		scheduleTestHotte()
	{
		// Test for the hotte
		Instant hotteTestStart =
				this.ac.getStartInstant().plusSeconds(
							(HotteTester.SWITCH_ON_DELAY +
									HotteTester.SWITCH_OFF_DELAY)/2);
		this.traceMessage("HEM schedules the hotte test.\n");
		long delay = this.ac.nanoDelayUntilInstant(hotteTestStart);

		// schedule the switch on hotte in one second
		this.scheduleTaskOnComponent(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							testHotte();
						} catch (Throwable e) {
							throw new BCMRuntimeException(e) ;
						}
					}
				}, delay, TimeUnit.NANOSECONDS);
	}
	
	protected void		testRefrigerateur() throws Exception
	{
		this.logMessage("Refrigerateur tests start.");
		TestsStatistics statistics = new TestsStatistics();
		try {
			this.logMessage("Feature: adjustable appliance mode management");
			this.logMessage("  Scenario: getting the max mode index");
			this.logMessage("    Given the Refrigerateur has just been switched on");
			this.logMessage("    When I call maxMode()");
			this.logMessage("    Then the result is its max mode index");
			final int maxMode = refrigerateurop.maxMode();

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode index");
			this.logMessage("    Given the refrigerateur has just been switched on");
			this.logMessage("    When I call currentMode()");
			this.logMessage("    Then the current mode is 2");
			int result = refrigerateurop.currentMode();
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the current mode index is 2");
			result = refrigerateurop.currentMode();
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then the method returns true");
			boolean bResult = refrigerateurop.upMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 3");
			result = refrigerateurop.currentMode();
			if (result != 3) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();
			
			this.logMessage("  Scenario: going down one mode index");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the current mode index is 3");
			result = refrigerateurop.currentMode();
			if (result != 3) {
				this.logMessage("      but was: " + result);
				statistics.failedCondition();
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then the method returns true");
			bResult = refrigerateurop.downMode();
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is 2");
			result = refrigerateurop.currentMode();
			if (result != 2) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode index");
			this.logMessage("    Given the refrigerateur is switched on");
			int index = maxMode;
			this.logMessage("    And the max mode index is legitimate");
			if (index > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(maxMode)");
			this.logMessage("    Then the method returns true");
			bResult = refrigerateurop.setMode(maxMode);
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the current mode is max mode");
			result = refrigerateurop.currentMode();
			if (result != maxMode) {
				this.logMessage("      but was: " + result);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("Feature: Getting the power consumption given a mode");
			this.logMessage("  Scenario: getting the power consumption of the maximum mode");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    When I get the power consumption of the maximum mode");
			double dResult = refrigerateurop.getModeConsumption(maxMode);
			if (dResult != Refrigerateur.RAPIDE_POWER.getData()) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}
			this.logMessage("    Then the result is the maximum power consumption of the refrigerateur");

			statistics.updateStatistics();

			this.logMessage("Feature: suspending and resuming");
			this.logMessage("  Scenario: checking if suspended when not");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And it has not been suspended yet");
			this.logMessage("    When I check if suspended");
			bResult = refrigerateurop.suspended();
			this.logMessage("    Then it is not");
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: suspending");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And it is not suspended");
			bResult = refrigerateurop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call suspend()");
			bResult = refrigerateurop.suspend();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the refrigerateur is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going down one mode index when suspended");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the refrigerateur is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call downMode()");
			this.logMessage("    Then a precondition exception is thrown");
			boolean old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				refrigerateurop.downMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: going up one mode index when suspended");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the refrigerateur is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call upMode()");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				refrigerateurop.upMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: setting the mode when suspended");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the refrigerateur is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    And the mode index 1 is legitimate");
			if (1 > maxMode) {
				this.logMessage("      but was not!");
				statistics.failedCondition();
			}
			this.logMessage("    When I call setMode(1)");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				refrigerateurop.setMode(1);
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: getting the current mode when suspended");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And the refrigerateur is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I get the current mode");
			this.logMessage("    Then a precondition exception is thrown");
			old = BCMException.VERBOSE;
			try {
				BCMException.VERBOSE = false;
				refrigerateurop.currentMode();
				this.logMessage("      but it was not!");
				statistics.incorrectResult();
			} catch (Throwable e) {
			} finally {
				BCMException.VERBOSE = old;
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: checking the emergency");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And it has just been suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call emergency()");
			dResult = refrigerateurop.emergency();
			this.logMessage("    Then the emergency is between 0.0 and 1.0");
			if (dResult < 0.0 || dResult > 1.0) {
				this.logMessage("      but was: " + dResult);
				statistics.incorrectResult();
			}

			statistics.updateStatistics();

			this.logMessage("  Scenario: resuming");
			this.logMessage("    Given the refrigerateur is switched on");
			this.logMessage("    And it is suspended");
			bResult = refrigerateurop.suspended();
			if (!bResult) {
				this.logMessage("      but it was not!");
				statistics.failedCondition();;
			}
			this.logMessage("    When I call resume()");
			bResult = refrigerateurop.resume();
			this.logMessage("    Then the method returns true");
			if (!bResult) {
				this.logMessage("      but was: " + bResult);
				statistics.incorrectResult();
			}
			this.logMessage("    And the refrigerateur is not suspended");
			bResult = refrigerateurop.suspended();
			if (bResult) {
				this.logMessage("      but it was!");
				statistics.incorrectResult();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

		statistics.updateStatistics();
		statistics.statisticsReport(this);

		this.logMessage("refrigerateur tests end.");
	}
	
	protected void		scheduleTestRefrigerateur()
	{
		// Test for the refrigerateur
		Instant refrigerateurTestStart =
				this.ac.getStartInstant().plusSeconds(
							(RefrigerateurTester.SWITCH_ON_DELAY +
									RefrigerateurTester.SWITCH_OFF_DELAY)/2);
		this.traceMessage("HEM schedules the refrigerateur test.\n");
		long delay = this.ac.nanoDelayUntilInstant(refrigerateurTestStart);

		// schedule the switch on refrigerateur in one second
		this.scheduleTaskOnComponent(
				new AbstractComponent.AbstractTask() {
					@Override
					public void run() {
						try {
							testRefrigerateur();
						} catch (Throwable e) {
							throw new BCMRuntimeException(e) ;
						}
					}
				}, delay, TimeUnit.NANOSECONDS);
	}
	

}
