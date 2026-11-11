package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueConfigurationI;
import equipements.modulable.MachineCafeAutomatiqueExternalControlJava4CI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.components.ComponentI;

/**
 * Port entrant de la version "Java4" du contrôle externe. Il reprend le port
 * normal et ajoute les méthodes qui utilisent des types simples (double).
 */
public class MachineCafeAutomatiqueExternalControlJava4InboundPort
extends		MachineCafeAutomatiqueExternalControlInboundPort
implements	MachineCafeAutomatiqueExternalControlJava4CI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueExternalControlJava4InboundPort(
		ComponentI owner
		) throws Exception
	{
		super(MachineCafeAutomatiqueExternalControlJava4CI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueExternalControlJava4InboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, MachineCafeAutomatiqueExternalControlJava4CI.class, owner);
	}

	// Les méthodes Java4 appellent les méthodes normales et convertissent
	@Override
	public double getMaxPowerLevelJava4() throws Exception
	{
		return this.getMaxPowerLevel().getData();
	}

	@Override
	public double getCurrentPowerLevelJava4() throws Exception
	{
		return this.getCurrentPowerLevel().getMeasure().getData();
	}

	@Override
	public void setCurrentPowerLevelJava4(double powerLevel) throws Exception
	{
		this.setCurrentPowerLevel(new Measure<Double>(
							powerLevel,
							MachineCafeAutomatiqueConfigurationI.POWER_UNIT));
	}
}
