package equipements.variable.refrigerateur.connections;

import equipements.variable.refrigerateur.RefrigerateurMode;
import equipements.variable.refrigerateur.RefrigerateurUserCI;
import equipements.variable.refrigerateur.RefrigerateurUserI;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.exceptions.PreconditionException;

public class RefrigerateurUserInboundPort 
extends		AbstractInboundPort
implements	RefrigerateurUserCI{

	private static final long serialVersionUID = 1L;

	public				RefrigerateurUserInboundPort(ComponentI owner)
	throws Exception
	{
		this(RefrigerateurUserCI.class, owner);
	}
	
	public				RefrigerateurUserInboundPort(
			String uri,
			ComponentI owner
			) throws Exception
		{
			this(uri, RefrigerateurUserCI.class, owner);
		}
	
	public				RefrigerateurUserInboundPort(
			Class<? extends RefrigerateurUserCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(implementedInterface, owner);

			assert	implementedInterface != null &&
					RefrigerateurUserCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "RefrigerateurUserCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof RefrigerateurUserI :
					new PreconditionException(
							"owner instanceof RefrigerateurUserI");
		}
	
	public				RefrigerateurUserInboundPort(
			String uri,
			Class<? extends RefrigerateurUserCI> implementedInterface,
			ComponentI owner
			) throws Exception
		{
			super(uri, implementedInterface, owner);

			assert	implementedInterface != null &&
					RefrigerateurUserCI.class.isAssignableFrom(
															implementedInterface)  :
					new PreconditionException(
							"implementedInterface != null && "
							+ "RefrigerateurUserCI.class.isAssignableFrom("
							+ "implementedInterface)");
			assert	owner instanceof RefrigerateurUserI :
					new PreconditionException(
							"owner instanceof RefrigerateurUserI");
		}

	@Override
	public void switchOn() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurUserI)o).
					switchOn();
						return null;
					 });		
	}

	@Override
	public void switchOff() throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurUserI)o).
					switchOff();
						return null;
					 });			
	}

	@Override
	public void setTargetTemperature(Measure<Double> target) throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurUserI)o).
					setTargetTemperature(target);
						return null;
					 });			
	}

	@Override
	public void setMode(RefrigerateurMode m) throws Exception {
		this.getOwner().handleRequest(
				o -> {	((RefrigerateurUserI)o).
					setMode(m);
						return null;
					 });		
	}

	@Override
	public boolean on() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurUserI)o).on());
	}

	@Override
	public TimedMeasure<Double> getTargetTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurUserI)o).getTargetTemperature());
	}

	@Override
	public TimedMeasure<Double> getCurrentTemperature() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurUserI)o).getCurrentTemperature());
	}

	@Override
	public RefrigerateurMode getMode() throws Exception {
		return this.getOwner().handleRequest(
				o -> ((RefrigerateurUserI)o).getMode());
	}
}
