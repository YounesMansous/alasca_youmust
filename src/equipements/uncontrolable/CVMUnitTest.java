package equipements.uncontrolable;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;

/**
 * The class <code>CVMUnitTest</code> deploys the classic vacuum cleaner and
 * its tester to execute the unit tests in a single JVM.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public class CVMUnitTest
extends		AbstractCVM
{
	public CVMUnitTest() throws Exception
	{
		AspirateurClassiqueTester.VERBOSE = true;
		AspirateurClassiqueTester.X_RELATIVE_POSITION = 0;
		AspirateurClassiqueTester.Y_RELATIVE_POSITION = 0;
		AspirateurClassique.VERBOSE = true;
		AspirateurClassique.X_RELATIVE_POSITION = 1;
		AspirateurClassique.Y_RELATIVE_POSITION = 0;
	}

	/**
	 * @see fr.sorbonne_u.components.cvm.AbstractCVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
				AspirateurClassique.class.getCanonicalName(),
				new Object[]{});
		AbstractComponent.createComponent(
				AspirateurClassiqueTester.class.getCanonicalName(),
				new Object[]{});

		super.deploy();
	}

	public static void	main(String[] args)
	{
		BCMException.VERBOSE = true;
		try {
			CVMUnitTest cvm = new CVMUnitTest();
			cvm.startStandardLifeCycle(2000L);
			Thread.sleep(10000L);
			System.exit(0);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
