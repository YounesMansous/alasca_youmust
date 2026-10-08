package equipements.simple.hotte;

/*Plus tard, le connecteur sera généré automatiquement à partir d'un fichier XML, 
 * par Javassist. Or Javassist ne comprend que du Java très ancien : ni enums, ni 
 * génériques (Measure<Double>). Il faut donc une version de chaque méthode qui 
 * n'utilise que des types simples : int, double, boolean.*/

public interface HotteExternalControlJava4CI 
extends HotteExternalControlCI{
	
	/* Convention des modes en int (identique à AdjustableCI) :
	 * 1 = VITESSE1, 2 = VITESSE2, 3 = VITESSE3, 4 = BOOST */

	public int getModeJava4() throws Exception;
	
	public void setModeJava4(int im) throws Exception;
	
	public double	getModePowerJava4(int im) throws Exception;
	
}
