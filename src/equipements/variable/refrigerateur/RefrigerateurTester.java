package equipements.variable.refrigerateur;

import equipements.variable.refrigerateur.connections.RefrigerateurExternalControlConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurExternalControlOutboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurInternalControlConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurInternalControlOutboundPort;
import equipements.variable.refrigerateur.connections.RefrigerateurUserConnector;
import equipements.variable.refrigerateur.connections.RefrigerateurUserOutboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
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
		this.testSimple();
	}


}
