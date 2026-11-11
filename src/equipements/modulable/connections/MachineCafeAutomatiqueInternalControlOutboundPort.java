package equipements.modulable.connections;

import equipements.modulable.MachineCafeAutomatiqueInternalControlCI;
import equipements.modulable.MachineCafeAutomatiqueOption;
import equipements.modulable.MachineCafeAutomatiqueState;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * Port sortant de l'interface de contrôle interne : c'est par lui que le
 * contrôleur local appelle la machine.
 */
public class MachineCafeAutomatiqueInternalControlOutboundPort
extends		AbstractOutboundPort
implements	MachineCafeAutomatiqueInternalControlCI
{
	private static final long serialVersionUID = 1L;

	// Constructeur sans URI
	public MachineCafeAutomatiqueInternalControlOutboundPort(ComponentI owner)
	throws Exception
	{
		super(MachineCafeAutomatiqueInternalControlCI.class, owner);
	}

	// Constructeur avec URI
	public MachineCafeAutomatiqueInternalControlOutboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, MachineCafeAutomatiqueInternalControlCI.class, owner);
	}

	// Lectures de l'état
	@Override
	public boolean on() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																		on();
	}

	@Override
	public boolean isCapsulIn() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																isCapsulIn();
	}

	@Override
	public MachineCafeAutomatiqueState getState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																	getState();
	}

	@Override
	public MachineCafeAutomatiqueOption getSelectedOption() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															getSelectedOption();
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
														getTargetTemperature();
	}

	@Override
	public TimedMeasure<Double> getTankLevel() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															getTankLevel();
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
														getCurrentTemperature();
	}

	// Test de la phase courante
	@Override
	public boolean isAwaiting() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																isAwaiting();
	}

	@Override
	public boolean isReady() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																	isReady();
	}

	@Override
	public boolean isHeating() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																isHeating();
	}

	@Override
	public boolean isBrewing() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
																isBrewing();
	}

	// Passage à la phase suivante
	@Override
	public boolean startReadyState() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															startReadyState();
	}

	@Override
	public boolean startHeating() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															startHeating();
	}

	@Override
	public boolean startBrewing() throws Exception
	{
		return ((MachineCafeAutomatiqueInternalControlCI)this.getConnector()).
															startBrewing();
	}
}
