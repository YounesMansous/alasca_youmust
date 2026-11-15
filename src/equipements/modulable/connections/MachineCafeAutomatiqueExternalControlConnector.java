package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueExternalControlCI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * Connecteur de l'interface de contrôle externe : il relie le port sortant
 * du client au port entrant de la machine.
 */
public class MachineCafeAutomatiqueExternalControlConnector
extends		AbstractConnector
implements	MachineCafeAutomatiqueExternalControlCI
{
	// "offering" est le port entrant de la machine
	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).on();
	}

	@Override
	public Measure<Double> getMaxPowerLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).
															getMaxPowerLevel();
	}

	@Override
	public TimedMeasure<Double> getCurrentPowerLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).
														getCurrentPowerLevel();
	}

	@Override
	public void setCurrentPowerLevel(Measure<Double> powerLevel)
	throws Exception
	{
		((MachineCafeAutomatiqueExternalControlCI)this.offering).
												setCurrentPowerLevel(powerLevel);
	}

	@Override
	public boolean isSuspendable() throws Exception {
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).isSuspendable();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).getCurrentTemperature();
	}

	@Override
	public Measure<Double> getTargetTemperature() throws Exception {
		// TODO Auto-generated method stub
		return ((MachineCafeAutomatiqueExternalControlCI)this.offering).getTargetTemperature();
	}
}
