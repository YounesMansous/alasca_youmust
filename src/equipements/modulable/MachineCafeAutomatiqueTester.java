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

	protected MachineCafeAutomatiqueTester(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	protected MachineCafeAutomatiqueTester(String URI, int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	
}
