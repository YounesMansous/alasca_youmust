package equipements;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import equipements.hem.HEM;
import equipements.simple.hotte.Hotte;
import equipements.simple.hotte.HotteTester;
import equipements.variable.refrigerateur.Refrigerateur;
import equipements.variable.refrigerateur.RefrigerateurTester;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;
import fr.sorbonne_u.components.hem2026e1.equipments.batteries.Batteries;
import fr.sorbonne_u.components.hem2026e1.equipments.generator.Generator;
import fr.sorbonne_u.components.hem2026e1.equipments.meter.ElectricMeter;
import fr.sorbonne_u.components.hem2026e1.equipments.solar_panel.SolarPanel;
import fr.sorbonne_u.components.hem2026e1.equipments.weather_station.WeatherStation;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;
import fr.sorbonne_u.utils.aclocks.ClocksServer;

public class CVMIntegrationTest 
extends		AbstractCVM{
	
	public static final String	CLOCK_URI = "test-clock";

	public static final long	DELAY_TO_START_IN_MILLIS = 3000;

	public static final double	ACCELERATION_FACTOR = 1.0;

	public static final Instant	START_INSTANT =
									Instant.parse("2024-09-18T14:00:00.00Z");

	public static final int		NB_OF_SQUARE_METERS = 10;

	public static boolean	staticImplementationInvariants()
	{
		boolean ret = true;
		return ret;
	}

	protected static boolean	implementationInvariants(CVMIntegrationTest cvm)
	{
		assert	cvm != null : new PreconditionException("cvm != null");

		boolean ret = true;
		ret &= staticImplementationInvariants();
		return ret;
	}

	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				CLOCK_URI != null && !CLOCK_URI.isEmpty(),
				CVMIntegrationTest.class,
				"CLOCK_URI != null && !CLOCK_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				DELAY_TO_START_IN_MILLIS >= 0,
				CVMIntegrationTest.class,
				"DELAY_TO_START_IN_MILLIS >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				ACCELERATION_FACTOR > 0.0,
				CVMIntegrationTest.class,
				"ACCELERATION_FACTOR > 0.0");
		ret &= AssertionChecking.checkStaticInvariant(
				START_INSTANT != null,
				CVMIntegrationTest.class,
				"START_INSTANT != null");
		return ret;
	}
	
	protected static boolean	invariants(CVMIntegrationTest cvm)
	{
		assert	cvm != null : new PreconditionException("cvm != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	public				CVMIntegrationTest() throws Exception
	{
		// Trace and trace window positions
		ClocksServer.VERBOSE = true;
		ClocksServer.X_RELATIVE_POSITION = 0;
		ClocksServer.Y_RELATIVE_POSITION = 0;
		HEM.VERBOSE = true;
		HEM.X_RELATIVE_POSITION = 0;
		HEM.Y_RELATIVE_POSITION = 1;
		ElectricMeter.VERBOSE = true;
		ElectricMeter.X_RELATIVE_POSITION = 1;
		ElectricMeter.Y_RELATIVE_POSITION = 0;
		Batteries.VERBOSE = true;
		Batteries.X_RELATIVE_POSITION = 1;
		Batteries.Y_RELATIVE_POSITION = 1;
		SolarPanel.VERBOSE = true;
		SolarPanel.X_RELATIVE_POSITION = 2;
		SolarPanel.Y_RELATIVE_POSITION = 1;
		Generator.VERBOSE = true;
		Generator.X_RELATIVE_POSITION = 3;
		Generator.Y_RELATIVE_POSITION = 1;
		WeatherStation.VERBOSE = true;
		WeatherStation.X_RELATIVE_POSITION = 3;
		WeatherStation.Y_RELATIVE_POSITION = 0;
		HotteTester.VERBOSE = true;
		HotteTester.X_RELATIVE_POSITION = 0;
		HotteTester.Y_RELATIVE_POSITION = 2;
		Hotte.VERBOSE = true;
		Hotte.X_RELATIVE_POSITION = 1;
		Hotte.Y_RELATIVE_POSITION = 2;
		RefrigerateurTester.VERBOSE = true;
		RefrigerateurTester.X_RELATIVE_POSITION = 0;
		RefrigerateurTester.Y_RELATIVE_POSITION = 3;
		Refrigerateur.VERBOSE = true;
		Refrigerateur.X_RELATIVE_POSITION = 1;
		Refrigerateur.Y_RELATIVE_POSITION = 3;

		assert	CVMIntegrationTest.implementationInvariants(this) :
				new InvariantException(
						"CVMIntegrationTest.glassBoxInvariants(this)");
		assert	CVMIntegrationTest.invariants(this) :
				new InvariantException(
						"CVMIntegrationTest.blackBoxInvariants(this)");
	}
	
	@Override
	public void			deploy() throws Exception
	{
		// start time in Unix epoch time in nanoseconds.
		long unixEpochStartTimeInMillis = 
				System.currentTimeMillis() + DELAY_TO_START_IN_MILLIS;

		AbstractComponent.createComponent(
				ClocksServer.class.getCanonicalName(),
				new Object[]{
						// URI of the clock to retrieve it
						CLOCK_URI,
						// start time in Unix epoch time
						TimeUnit.MILLISECONDS.toNanos(
										 		unixEpochStartTimeInMillis),
						START_INSTANT,
						ACCELERATION_FACTOR});

		AbstractComponent.createComponent(
				ElectricMeter.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				Batteries.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				SolarPanel.class.getCanonicalName(),
				new Object[]{NB_OF_SQUARE_METERS});

		AbstractComponent.createComponent(
				Generator.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				WeatherStation.class.getCanonicalName(),
				new Object[]{});

		AbstractComponent.createComponent(
				Hotte.class.getCanonicalName(),
				new Object[]{});
		// At this stage, the tester for the Hotte is added only
		// to show the Hotte functioning; later on, it will be replaced
		// by a simulation of users' actions.
		AbstractComponent.createComponent(
				HotteTester.class.getCanonicalName(),
				new Object[]{false});

		AbstractComponent.createComponent(
				Refrigerateur.class.getCanonicalName(),
				new Object[]{});

		// At this stage, the tester for the Refrigerateur is added only
		// to switch on and off the Refrigerateur; later on, it will be replaced
		// by a simulation of users' actions.
		AbstractComponent.createComponent(
				RefrigerateurTester.class.getCanonicalName(),
				new Object[]{false});

		AbstractComponent.createComponent(
				HEM.class.getCanonicalName(),
				new Object[]{});

		super.deploy();
	}

	public static void	main(String[] args)
	{
		BCMException.VERBOSE = true;
		try {
			CVMIntegrationTest cvm = new CVMIntegrationTest();
			cvm.startStandardLifeCycle(12000L);
			Thread.sleep(100000L);
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
