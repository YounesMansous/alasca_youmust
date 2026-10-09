package equipements.variable.refrigerateur;

import fr.sorbonne_u.exceptions.PreconditionException;

public enum RefrigerateurMode {
	ECO,			
	NORMAL,
	RAPIDE;
	
	public static RefrigerateurMode	intToMode(int im)
	{
		assert	im >= 1 && im <= 3 :
				new PreconditionException("im >= 1 && im <= 3");
		switch (im) {
		case 1:		return ECO;
		case 2:		return NORMAL;
		case 3:		return RAPIDE;
		default:	throw new IllegalArgumentException("mode inconnu : " + im);
		}
	}

	public static int	modeToInt(RefrigerateurMode m)
	{
		assert	m != null : new PreconditionException("m != null");
		switch (m) {
		case ECO:		return 1;
		case NORMAL:	return 2;
		case RAPIDE:	return 3;
		default:		throw new IllegalArgumentException("mode inconnu : " + m);
		}
	}
}
