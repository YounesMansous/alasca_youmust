package equipements.uncontrolable;

import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.exceptions.AssertionChecking;

public interface AspirateurClassiqueConfigurationI {
	/** measurement unit for power used in this appliance.					*/
	public static final MeasurementUnit	POWER_UNIT = MeasurementUnit.WATTS;
	/** measurement unit for tension used in this appliance.				*/
	public static final MeasurementUnit	TENSION_UNIT = MeasurementUnit.VOLTS;

	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	/**
	 * return true if the static invariants are observed, false otherwise.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	true if the static invariants are observed, false otherwise.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				POWER_UNIT != null,
				AspirateurClassiqueConfigurationI.class,
				"POWER_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION_UNIT != null,
				AspirateurClassiqueConfigurationI.class,
				"TENSION_UNIT != null");
		return ret;
	}
}
