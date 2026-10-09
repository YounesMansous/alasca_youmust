package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurExternalControlCI;
import equipements.variable.refrigerateur.RefrigerateurExternalControlI;
import equipements.variable.refrigerateur.RefrigerateurMode;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

public class RefrigerateurExternalControlInboundPort 
extends		AbstractInboundPort
implements	RefrigerateurExternalControlCI{

	private static final long serialVersionUID = 1L;
	
	public				RefrigerateurExternalControlInboundPort(ComponentI owner)
	throws Exception
	{
		this(RefrigerateurExternalControlCI.class, owner);
	}
	
	public				RefrigerateurExternalControlInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			this(uri, RefrigerateurExternalControlCI.class, owner);
		}
	
	public				RefrigerateurExternalControlInboundPort(
			Class<? extends RefrigerateurExternalControlCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(implementedInterface, owner);

			assert	implementedInterface != null &&
					RefrigerateurExternalControlCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "RefrigerateurExternalControlCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof RefrigerateurExternalControlI :
					new PreconditionException(
							"owner instanceof RefrigerateurExternalControlI");
		}
	
	public				RefrigerateurExternalControlInboundPort(
			String uri,
			Class<? extends RefrigerateurExternalControlCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(uri, implementedInterface, owner);

			assert	implementedInterface != null &&
					RefrigerateurExternalControlCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "RefrigerateurExternalControlCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof RefrigerateurExternalControlI :
					new PreconditionException(
							"owner instanceof RefrigerateurExternalControlI");
		}

	@Override
	public boolean on() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).on());
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).getTargetTemperature());
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).getCurrentTemperature());
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).getMode());
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurExternalControlI)o).
					setMode(m);
						return null;
					 });		
	}

	@Override
	public Measure<Double> getModePower(RefrigerateurMode m) throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).getModePower(m));
	}

	@Override
	public void suspend() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurExternalControlI)o).
					suspend();
						return null;
					 });			
	}

	@Override
	public void resume() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurExternalControlI)o).
					resume();
						return null;
					 });		
	}

	@Override
	public boolean suspended() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurExternalControlI)o).suspended());
	}

}
