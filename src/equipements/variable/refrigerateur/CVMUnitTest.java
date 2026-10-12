package equipements.variable.refrigerateur;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;

public class CVMUnitTest extends	AbstractCVM{
	
	public				CVMUnitTest() throws Exception
	{
		RefrigerateurTester.VERBOSE = true;
		RefrigerateurTester.X_RELATIVE_POSITION = 0;
		RefrigerateurTester.Y_RELATIVE_POSITION = 0;
		Refrigerateur.VERBOSE = true;
		Refrigerateur.X_RELATIVE_POSITION = 1;
		Refrigerateur.Y_RELATIVE_POSITION = 0;
	}
	
	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
				Refrigerateur.class.getCanonicalName(),
					new Object[]{});

		AbstractComponent.createComponent(
				RefrigerateurTester.class.getCanonicalName(),
					new Object[]{true});

		super.deploy();
	}
	
	public static void		main(String[] args)
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
