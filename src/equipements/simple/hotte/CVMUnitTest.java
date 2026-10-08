package equipements.simple.hotte;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.BCMException;

public class CVMUnitTest extends	AbstractCVM{
	
	public				CVMUnitTest() throws Exception
	{
		HotteTester.VERBOSE = true;
		HotteTester.X_RELATIVE_POSITION = 0;
		HotteTester.Y_RELATIVE_POSITION = 0;
		Hotte.VERBOSE = true;
		Hotte.X_RELATIVE_POSITION = 1;
		Hotte.Y_RELATIVE_POSITION = 0;
	}

	@Override
	public void			deploy() throws Exception
	{
		AbstractComponent.createComponent(
					Hotte.class.getCanonicalName(),
					new Object[]{});

		AbstractComponent.createComponent(
					HotteTester.class.getCanonicalName(),
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
