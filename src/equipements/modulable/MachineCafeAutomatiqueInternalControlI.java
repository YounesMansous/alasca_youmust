package equipements.modulable;

import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.PreconditionException;

public interface MachineCafeAutomatiqueInternalControlI extends 
MachineCafeAutomatiqueStateI{

	
	public static boolean staticInvariant() {
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		return ret;
		
	}
	
	public static boolean	invariants(MachineCafeAutomatiqueInternalControlI instance)
	{
		assert instance != null : new PreconditionException("instance != null");

		boolean ret = true;
		ret &= MachineCafeAutomatiqueStateI.invariants(instance);
		ret &= AssertionChecking.checkInvariant(
				instance.isAwaiting() == (instance.getState() == MachineCafeAutomatiqueState.AWAITING),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isAwaiting() == (getState() == AWAITING)");
		ret &= AssertionChecking.checkInvariant(
				instance.isWarmingUp() == (instance.getState() == MachineCafeAutomatiqueState.WARMING_UP),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isWarmingUp() == (getState() == WARMING_UP)");
		ret &= AssertionChecking.checkInvariant(
				instance.isRinsing() == (instance.getState() == MachineCafeAutomatiqueState.RINSING),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isRinsing() == (getState() == RINSING)");
		ret &= AssertionChecking.checkInvariant(
				instance.isInfusing() == (instance.getState() == MachineCafeAutomatiqueState.PRE_INFUSION),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isInfusing() == (getState() == PRE_INFUSION)");
		ret &= AssertionChecking.checkInvariant(
				instance.isGrinding() == (instance.getState() == MachineCafeAutomatiqueState.GRINDING),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isGrinding() == (getState() == GRINDING)");
		ret &= AssertionChecking.checkInvariant(
				instance.isBrewing() == (instance.getState() == MachineCafeAutomatiqueState.BREWING),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isBrewing() == (getState() == BREWING)");
		ret &= AssertionChecking.checkInvariant(
				instance.isSteaming() == (instance.getState() == MachineCafeAutomatiqueState.STEAMING),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isSteaming() == (getState() == STEAMING)");
		ret &= AssertionChecking.checkInvariant(
				instance.isReady() == (instance.getState() == MachineCafeAutomatiqueState.READY_HOT),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isReady() == (getState() == READY_HOT)");
		ret &= AssertionChecking.checkInvariant(
				instance.isEcoStandBy() == (instance.getState() == MachineCafeAutomatiqueState.ECO_STANDBY),
						MachineCafeAutomatiqueInternalControlI.class, instance,
						"isEcoStandBy() == (getState() == ECO_STANDBY)");
		ret &= AssertionChecking.checkInvariant(
		        !instance.isHeating() || instance.isReady() || instance.isEcoStandBy(),
		        MachineCafeAutomatiqueInternalControlI.class, instance,
		        "isHeating() implique isReady() || isEcoStandBy()");
		ret &= AssertionChecking.checkInvariant(
				!instance.isSteaming() || instance.getSelectedDrink() ==  MachineCafeAutomatiqueDrink.LATTE || instance.getSelectedDrink() ==  MachineCafeAutomatiqueDrink.CAPPUCCINO, 
				MachineCafeAutomatiqueInternalControlI.class, instance,  
				"instance.isSteaming() implique instance.getSelectedDrink() ==  MachineCafeAutomatiqueDrink.LATTE || instance.getSelectedDrink() ==  MachineCafeAutomatiqueDrink.CAPPUCCINO");
		ret &= AssertionChecking.checkInvariant(
		        !(instance.isAwaiting() || instance.isWarmingUp() || instance.isRinsing() || instance.isGrinding() || instance.isInfusing() || instance.isSteaming() || instance.isBrewing()) || instance.getSelectedDrink() != null,
		        MachineCafeAutomatiqueInternalControlI.class, instance,
		        "(instance.isAwaiting() || instance.isWarmingUp() || instance.isRinsing() || instance.isGrinding() || instance.isInfusing() || instance.isSteaming() || instance.isBrewing()) implique instance.getSelectedDrink() != null");
		return ret;
	}
	
	public boolean isHeating();
	
	public boolean startHeating();
	
	public boolean endHeating();
	
	public boolean isAwaiting();
	
	public boolean startWarming();
	
	public boolean isWarmingUp();
	
	public boolean startRinsing();
	
	public boolean isRinsing();
	
	public boolean startGrinding();
	
	public boolean isGrinding();
	
	public boolean isInfusing();
	
	public boolean startInfusing();
	
	public boolean isBrewing();
	
	public boolean startBrewing();
	
	public boolean startSteaming();
	
	public boolean isSteaming();
	
	public boolean startReady();
	
	public boolean isReady();
	
	public boolean startEcoStandBy();
	
	public boolean isEcoStandBy();

}
