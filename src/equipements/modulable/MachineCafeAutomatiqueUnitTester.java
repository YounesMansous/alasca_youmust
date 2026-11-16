package equipements.modulable;

import equipements.modulable.connections.MachineCafeAutomatiqueExternalControlOutboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueInternalControlOutboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueUserOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.utils.tests.TestsStatistics;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

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
public class MachineCafeAutomatiqueUnitTester
extends		AbstractComponent
{
	protected final boolean isUnitTest;
	/** URI of the user component interface inbound port.					*/
	protected String			MCAUserInboundPortURI;
	/** URI of the internal control component interface inbound port.		*/
	protected String			MCAInternalControlInboundPortURI;
	/** URI of the external control component interface inbound port.		*/
	protected String			MCAExternalControlInboundPortURI;

	/** user component interface inbound port.								*/
	protected MachineCafeAutomatiqueUserOutboundPort			mcaop;
	/** internal control component interface inbound port.					*/
	protected MachineCafeAutomatiqueInternalControlOutboundPort	mcaicop;
	/** external control component interface inbound port.					*/
	protected MachineCafeAutomatiqueExternalControlOutboundPort	mcaecop;

	/** collector of test statistics.										*/
	protected TestsStatistics	statistics;

	/** Si vrai, la machine affiche des traces. */
	public static boolean	VERBOSE = false;
	/** Position de la fenêtre de trace. */
	public static int		X_RELATIVE_POSITION = 0;
	
	public static int		Y_RELATIVE_POSITION = 0;
	
	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	/**
	 * return true if the implementation invariants are observed, false
	 * otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code mca != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param mca	instance to be tested.
	 * @return		true if the implementation invariants are observed, false otherwise.
	 */
	protected static boolean	implementationInvariants(MachineCafeAutomatiqueUnitTester mca)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.MCAUserInboundPortURI != null &&
									!mca.MCAUserInboundPortURI.isEmpty(),
									MachineCafeAutomatiqueUnitTester.class, mca,
				"mca.MCAUserInboundPortURI != null && "
							+ "!mca.MCAUserInboundPortURI.isEmpty()");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.MCAExternalControlInboundPortURI != null &&
							!mca.MCAExternalControlInboundPortURI.isEmpty(),
							MachineCafeAutomatiqueUnitTester.class, mca,
				"mca.MCAExternalControlInboundPortURI != null && "
						+ "!mca.MCAExternalControlInboundPortURI.isEmpty()");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.MCAInternalControlInboundPortURI != null &&
							!mca.MCAInternalControlInboundPortURI.isEmpty(),
							MachineCafeAutomatiqueUnitTester.class, mca,
				"mca.MCAInternalControlInboundPortURI != null &&"
						+ "!mca.MCAInternalControlInboundPortURI.isEmpty()");
		return ret;
	}
	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
						MachineCafeAutomatiqueUnitTester.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
						MachineCafeAutomatiqueUnitTester.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}
	
	protected static boolean	invariants(MachineCafeAutomatiqueUnitTester mca)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}
	
	

	protected MachineCafeAutomatiqueUnitTester(boolean isUnitTest) throws Exception{
		this(isUnitTest,
				MachineCafeAutomatique.USER_INBOUND_PORT_URI,
				MachineCafeAutomatique.INTERNAL_CONTROL_INBOUND_PORT_URI,
				MachineCafeAutomatique.EXTERNAL_CONTROL_INBOUND_PORT_URI);
		// TODO Auto-generated constructor stub
	}
	
	protected MachineCafeAutomatiqueUnitTester(
			boolean isUnitTest,
			String MCAUserInboundPortURI,
			String MCAInternalControlInboundPortURI,
			String MCAExternalControlInboundPortURI) throws Exception {
		super(1, 1);
		this.isUnitTest = isUnitTest;
		this.initialise(MCAUserInboundPortURI,
				MCAInternalControlInboundPortURI,
				MCAExternalControlInboundPortURI);
		// TODO Auto-generated constructor stub
	}
	
	protected MachineCafeAutomatiqueUnitTester(
			boolean isUnitTest,
			String URI,
			String reflectionInboundPortURI,
			String MCAUserInboundPortURI,
			String MCAInternalControlInboundPortURI,
			String MCAExternalControlInboundPortURI) throws Exception {
		super(reflectionInboundPortURI, 1, 1);
		this.isUnitTest = isUnitTest;
		this.initialise(MCAUserInboundPortURI,
				MCAInternalControlInboundPortURI,
				MCAExternalControlInboundPortURI);
		// TODO Auto-generated constructor stub
	}
	
	private void initialise(
			String MCAUserInboundPortURI,
			String MCAInternalControlInboundPortURI,
			String MCAExternalControlInboundPortURI) {
		// TODO Auto-generated method stub
		
	}

	
	
}
