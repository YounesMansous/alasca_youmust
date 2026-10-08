package equipements.simple.hotte;

import fr.sorbonne_u.alasca.physical_data.MeasurementUnit;
import fr.sorbonne_u.exceptions.AssertionChecking;

public interface HotteConfigurationI {
	
	/** measurement unit for power used in this appliance.					*/
	public static final MeasurementUnit	POWER_UNIT = MeasurementUnit.WATTS;
	/** measurement unit for tension used in this appliance.				*/
	public static final MeasurementUnit	TENSION_UNIT = MeasurementUnit.VOLTS;

	
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticInvariant(
				POWER_UNIT != null,
				HotteConfigurationI.class,
				"POWER_UNIT != null");
		ret &= AssertionChecking.checkStaticInvariant(
				TENSION_UNIT != null,
				HotteConfigurationI.class,
				"TENSION_UNIT != null");
		return ret;
	}
}
