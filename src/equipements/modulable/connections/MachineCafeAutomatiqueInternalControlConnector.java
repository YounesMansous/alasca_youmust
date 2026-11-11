package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueState;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * Connecteur de l'interface de contrôle interne : il relie le port sortant
 * du contrôleur au port entrant de la machine.
 */
public class MachineCafeAutomatiqueInternalControlConnector
extends		AbstractConnector
implements	MachineCafeAutomatiqueInternalControlCI
{
	// "offering" est le port entrant de la machine

	// Lectures de l'état
	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).on();
	}

	@Override
	public boolean isCapsulIn() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																isCapsulIn();
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																	getState();
	}

	@Override
	public MachineCafeAutomatiqueOption getSelectedOption() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															getSelectedOption();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
														getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getTankLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															getTankLevel();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
														getCurrentTemperature();
	}

	// Test de la phase courante
	@Override
	public boolean isAwaiting() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																isAwaiting();
	}

	@Override
	public boolean isReady() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																	isReady();
	}

	@Override
	public boolean isHeating() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																isHeating();
	}

	@Override
	public boolean isBrewing() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
																isBrewing();
	}

	// Passage à la phase suivante
	@Override
	public boolean startReadyState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															startReadyState();
	}

	@Override
	public boolean startHeating() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															startHeating();
	}

	@Override
	public boolean startBrewing() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.offering).
															startBrewing();
	}
}
