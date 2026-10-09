package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurInternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurInternalControlI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

public class RefrigerateurInternalControlInboundPort 
extends		AbstractInboundPort
implements RefrigerateurInternalControlCI{
	
	private static final long serialVersionUID = 1L;

	public				RefrigerateurInternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		super(RefrigerateurInternalControlCI.class, owner);
		assert	owner instanceof RefrigerateurInternalControlI :
				new PreconditionException(
						"owner instanceof RefrigerateurInternalControlI");
	}
	
	public				RefrigerateurInternalControlInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			super(uri, RefrigerateurInternalControlCI.class, owner);
			assert	owner instanceof RefrigerateurInternalControlI :
					new PreconditionException(
							"owner instanceof RefrigerateurInternalControlI");
		}

	@Override
	public boolean		on() throws Exception
	{
		return this.getOwner().handleRequest(
								o -> ((RefrigerateurInternalControlI)o).on());
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurInternalControlI)o).getTargetTemperature());
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurInternalControlI)o).getCurrentTemperature());
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurInternalControlI)o).getMode());
	}

	@Override
	public boolean cooling() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurInternalControlI)o).cooling());
	}

	@Override
	public void startCooling() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurInternalControlI)o).startCooling();
						return null;
					 });		
	}

	@Override
	public void stopCooling() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurInternalControlI)o).stopCooling();
						return null;
					 });			
	}
}
