package equipements.modulable;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;

/**
 * La classe <code>CVMUnitTest</code> déploie la machine à café et son tester
 * pour exécuter les tests unitaires dans une seule JVM.
 *
 * <p>Created on : 2026-10-13</p>
 *
 * @author	ALASCA youmust team
 */
public class CVMUnitTest
extends		AbstractCVM
{
	/** Active l'affichage des traces et place les fenêtres. */
	public CVMUnitTest() throws Exception
	{
		super();
		MachineCafeAutomatiqueUnitTester.VERBOSE = true;
		MachineCafeAutomatiqueUnitTester.X_RELATIVE_POSITION = 0;
		MachineCafeAutomatiqueUnitTester.Y_RELATIVE_POSITION = 0;
		MachineCafeAutomatique.VERBOSE = true;
		MachineCafeAutomatique.X_RELATIVE_POSITION = 1;
		MachineCafeAutomatique.Y_RELATIVE_POSITION = 0;
	}

	/** Crée la machine et son tester. */
	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
				MachineCafeAutomatique.class.getCanonicalName(),
				new Object[]{});
		AbstractComponent.createComponent(
				MachineCafeAutomatiqueUnitTester.class.getCanonicalName(),
				new Object[]{});

		super.deploy();
	}

	public static void	main(String[] args)
	{
		BCMException.VERBOSE = true;
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(2000L);
			Thread.sleep(19000L);
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
