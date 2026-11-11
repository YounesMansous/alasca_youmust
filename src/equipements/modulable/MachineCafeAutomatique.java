package equipements.modulable;

import equipements.modulable.connections.MachineCafeAutomatiqueExternalControlJava4InboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueInternalControlInboundPort;
import equipements.modulable.connections.MachineCafeAutomatiqueUserInboundPort;
import fr.sorbonne_u.alasca.physical_data.Measure;
import fr.sorbonne_u.alasca.physical_data.TimedMeasure;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.exceptions.AssertionChecking;
import fr.sorbonne_u.exceptions.ImplementationInvariantException;
import fr.sorbonne_u.exceptions.InvariantException;
import fr.sorbonne_u.exceptions.PreconditionException;

/**
 * La classe <code>MachineCafeAutomatique</code> implémente une machine à café
 * automatique à capsules, de type A (ses phases s'enchaînent seules) et
 * modulable (le HEM règle la puissance de sa résistance).
 *
 * <p><strong>Cycle de la machine</strong></p>
 *
 * <pre>
 * OFF --orderCoffee--> READY (capsule et eau OK) ou AWAITING (il manque l'un des deux)
 * AWAITING --insertCapsule / refillTank--> READY (quand plus rien ne manque)
 * READY --startHeating--> HEATING --startBrewing--> BREWING --switchOff--> OFF
 * </pre>
 *
 * <p>
 * La capsule et l'eau de la boisson sont consommées au début de l'infusion
 * (<code>startBrewing</code>). Il n'existe pas encore d'opération pour la fin
 * de l'infusion : à cette étape, on revient à <code>OFF</code> avec
 * <code>switchOff</code>.
 * </p>
 *
 * <p><strong>Implementation Invariants</strong></p>
 *
 * <pre>
 * invariant	{@code currentState != null}
 * invariant	{@code (currentState == OFF) == (selectedOption == null)}
 * invariant	{@code tankLevel >= 0.0 && tankLevel <= TANK_CAPACITY}
 * invariant	{@code currentPowerLevel <= MAX_POWER_LEVEL}
 * </pre>
 *
 * <p>Created on : 2026-10-13</p>
 *
 * @author	ALASCA youmust team
 */
@OfferedInterfaces(offered={MachineCafeAutomatiqueUserCI.class,
		MachineCafeAutomatiqueInternalControlCI.class,
		MachineCafeAutomatiqueExternalControlJava4CI.class})
public class MachineCafeAutomatique
extends		AbstractComponent
implements	MachineCafeAutomatiqueUserI,
			MachineCafeAutomatiqueInternalControlI,
			MachineCafeAutomatiqueExternalControlI
{
	// -------------------------------------------------------------------------
	// Constantes et variables
	// -------------------------------------------------------------------------

	/** URI du port de réflexion du composant. */
	public static final String	REFLECTION_INBOUND_PORT_URI =
										"MACHINE-CAFE-AUTOMATIQUE-RIP-URI";
	/** URI du port entrant de l'interface utilisateur. */
	public static final String	USER_INBOUND_PORT_URI =
										"MACHINE-CAFE-AUTOMATIQUE-USER-URI";
	/** URI du port entrant de l'interface de contrôle interne. */
	public static final String	INTERNAL_CONTROL_INBOUND_PORT_URI =
									"MACHINE-CAFE-AUTOMATIQUE-INTERNAL-URI";
	/** URI du port entrant de l'interface de contrôle externe. */
	public static final String	EXTERNAL_CONTROL_INBOUND_PORT_URI =
									"MACHINE-CAFE-AUTOMATIQUE-EXTERNAL-URI";

	/** Si vrai, la machine affiche des traces. */
	public static boolean	VERBOSE = false;
	/** Position de la fenêtre de trace. */
	public static int		X_RELATIVE_POSITION = 0;
	public static int		Y_RELATIVE_POSITION = 0;

	/** Volume d'eau d'un expresso, en litres. */
	public static final double	ESPRESSO_VOLUME = 0.04;
	/** Volume d'eau d'un café long, en litres. */
	public static final double	LONG_COFFEE_VOLUME = 0.12;
	/** Température cible au départ, en degrés Celsius. */
	public static final double	INITIAL_TARGET_TEMPERATURE = 90.0;
	/** Température courante (valeur fixe, pas de simulation à cette étape). */
	public static final double	FAKE_CURRENT_TEMPERATURE = 20.0;

	/** État de la machine. */
	protected MachineCafeAutomatiqueState	currentState;
	/** Boisson commandée (null quand la machine est éteinte). */
	protected MachineCafeAutomatiqueOption	selectedOption;
	/** Vrai si une capsule est dans la machine. */
	protected boolean						capsuleIn;
	/** Niveau d'eau du réservoir, en litres. */
	protected double						tankLevel;
	/** Puissance courante de la résistance, en watts. */
	protected double						currentPowerLevel;
	/** Température cible, en degrés Celsius. */
	protected double						targetTemperature;

	// Les trois ports entrants
	protected MachineCafeAutomatiqueUserInboundPort				userip;
	protected MachineCafeAutomatiqueInternalControlInboundPort	internalip;
	protected MachineCafeAutomatiqueExternalControlJava4InboundPort	externalip;

	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------

	/** Invariants statiques d'implémentation. */
	public static boolean	staticImplementationInvariants()
	{
		boolean ret = true;
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				ESPRESSO_VOLUME > 0.0 && LONG_COFFEE_VOLUME > ESPRESSO_VOLUME
				&& LONG_COFFEE_VOLUME <= TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class,
				"ESPRESSO_VOLUME > 0.0 && LONG_COFFEE_VOLUME > ESPRESSO_VOLUME "
				+ "&& LONG_COFFEE_VOLUME <= TANK_CAPACITY.getData()");
		return ret;
	}

	/** Invariants d'implémentation. */
	protected static boolean	implementationInvariants(
		MachineCafeAutomatique mca
		)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= staticImplementationInvariants();
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.currentState != null, MachineCafeAutomatique.class, mca,
				"currentState != null");
		ret &= AssertionChecking.checkImplementationInvariant(
				(mca.currentState == MachineCafeAutomatiqueState.OFF) ==
												(mca.selectedOption == null),
				MachineCafeAutomatique.class, mca,
				"(currentState == OFF) == (selectedOption == null)");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.tankLevel >= 0.0 && mca.tankLevel <= TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class, mca,
				"tankLevel >= 0.0 && tankLevel <= TANK_CAPACITY.getData()");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.currentPowerLevel >= 0.0 &&
					mca.currentPowerLevel <= MAX_POWER_LEVEL.getData(),
				MachineCafeAutomatique.class, mca,
				"currentPowerLevel >= 0.0 && currentPowerLevel <= "
				+ "MAX_POWER_LEVEL.getData()");
		return ret;
	}

	/** Invariants statiques. */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= MachineCafeAutomatiqueConfigurationI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				REFLECTION_INBOUND_PORT_URI != null &&
									!REFLECTION_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"REFLECTION_INBOUND_PORT_URI != null && "
								+ "!REFLECTION_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"USER_INBOUND_PORT_URI != null && "
								+ "!USER_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				INTERNAL_CONTROL_INBOUND_PORT_URI != null &&
								!INTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"INTERNAL_CONTROL_INBOUND_PORT_URI != null && "
							+ "!INTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				EXTERNAL_CONTROL_INBOUND_PORT_URI != null &&
								!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"EXTERNAL_CONTROL_INBOUND_PORT_URI != null && "
							+ "!EXTERNAL_CONTROL_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				X_RELATIVE_POSITION >= 0,
				MachineCafeAutomatique.class, "X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				MachineCafeAutomatique.class, "Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	/** Invariants. */
	protected static boolean	invariants(MachineCafeAutomatique mca)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= staticInvariants();
		return ret;
	}

	// -------------------------------------------------------------------------
	// Constructeurs
	// -------------------------------------------------------------------------

	/** Crée la machine avec les URI par défaut. */
	protected MachineCafeAutomatique() throws Exception
	{
		this(USER_INBOUND_PORT_URI,
			 INTERNAL_CONTROL_INBOUND_PORT_URI,
			 EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	/** Crée la machine avec les URI de ses trois ports entrants. */
	protected MachineCafeAutomatique(
		String userInboundPortURI,
		String internalControlInboundPortURI,
		String externalControlInboundPortURI
		) throws Exception
	{
		this(REFLECTION_INBOUND_PORT_URI, userInboundPortURI,
			 internalControlInboundPortURI, externalControlInboundPortURI);
	}

	/** Crée la machine avec l'URI de réflexion et celles de ses trois ports. */
	protected MachineCafeAutomatique(
		String reflectionInboundPortURI,
		String userInboundPortURI,
		String internalControlInboundPortURI,
		String externalControlInboundPortURI
		) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0);
		this.initialise(userInboundPortURI, internalControlInboundPortURI,
						externalControlInboundPortURI);
	}

	/** Initialise l'état de la machine et publie ses ports entrants. */
	protected void		initialise(
		String userInboundPortURI,
		String internalControlInboundPortURI,
		String externalControlInboundPortURI
		) throws Exception
	{
		assert	userInboundPortURI != null && !userInboundPortURI.isEmpty() :
				new PreconditionException(
						"userInboundPortURI != null && "
						+ "!userInboundPortURI.isEmpty()");
		assert	internalControlInboundPortURI != null &&
								!internalControlInboundPortURI.isEmpty() :
				new PreconditionException(
						"internalControlInboundPortURI != null && "
						+ "!internalControlInboundPortURI.isEmpty()");
		assert	externalControlInboundPortURI != null &&
								!externalControlInboundPortURI.isEmpty() :
				new PreconditionException(
						"externalControlInboundPortURI != null && "
						+ "!externalControlInboundPortURI.isEmpty()");

		// État de départ : éteinte, sans capsule ni commande, réservoir plein
		this.currentState = MachineCafeAutomatiqueState.OFF;
		this.selectedOption = null;
		this.capsuleIn = false;
		this.tankLevel = TANK_CAPACITY.getData();
		this.currentPowerLevel = 0.0;
		this.targetTemperature = INITIAL_TARGET_TEMPERATURE;

		// Création et publication des trois ports entrants
		this.userip = new MachineCafeAutomatiqueUserInboundPort(
											userInboundPortURI, this);
		this.userip.publishPort();
		this.internalip = new MachineCafeAutomatiqueInternalControlInboundPort(
											internalControlInboundPortURI, this);
		this.internalip.publishPort();
		this.externalip =
			new MachineCafeAutomatiqueExternalControlJava4InboundPort(
											externalControlInboundPortURI, this);
		this.externalip.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Coffee machine component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		assert	MachineCafeAutomatique.implementationInvariants(this) :
				new ImplementationInvariantException(
						"MachineCafeAutomatique.implementationInvariants(this)");
		assert	MachineCafeAutomatique.invariants(this) :
				new InvariantException("MachineCafeAutomatique.invariants(this)");
	}

	// -------------------------------------------------------------------------
	// Cycle de vie du composant
	// -------------------------------------------------------------------------

	/** Dépublie les ports entrants avant d'arrêter le composant. */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.userip.unpublishPort();
			this.internalip.unpublishPort();
			this.externalip.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Outils internes
	// -------------------------------------------------------------------------

	/** Retourne le volume d'eau nécessaire pour une boisson, en litres. */
	protected static double		volumeOf(MachineCafeAutomatiqueOption option)
	{
		if (option == MachineCafeAutomatiqueOption.ESPRESSO) {
			return ESPRESSO_VOLUME;
		}
		return LONG_COFFEE_VOLUME;
	}

	/** Retourne vrai si la capsule et l'eau suffisent pour la boisson commandée. */
	protected boolean	resourcesAvailable()
	{
		return this.capsuleIn &&
				this.tankLevel >= volumeOf(this.selectedOption);
	}

	/** Si la machine attend et que plus rien ne manque, elle passe à READY. */
	protected void		leaveAwaitingIfPossible()
	{
		if (this.currentState == MachineCafeAutomatiqueState.AWAITING &&
													this.resourcesAvailable()) {
			this.currentState = MachineCafeAutomatiqueState.READY;
		}
	}

	/** Affiche un message si les traces sont actives. */
	protected void		trace(String message)
	{
		if (VERBOSE) {
			this.traceMessage(message);
		}
	}

	// -------------------------------------------------------------------------
	// Lectures de l'état
	// -------------------------------------------------------------------------

	@Override
	public boolean		on()
	{
		this.trace("Coffee machine returns whether it is on : "
										+ (this.currentState !=
										MachineCafeAutomatiqueState.OFF) + ".\n");
		return this.currentState != MachineCafeAutomatiqueState.OFF;
	}

	@Override
	public boolean		isCapsulIn()
	{
		this.trace("Coffee machine returns whether a capsule is in : "
														+ this.capsuleIn + ".\n");
		return this.capsuleIn;
	}

	@Override
	public MachineCafeAutomatiqueState	getState()
	{
		this.trace("Coffee machine returns its state : "
													+ this.currentState + ".\n");
		return this.currentState;
	}

	@Override
	public MachineCafeAutomatiqueOption	getSelectedOption()
	{
		this.trace("Coffee machine returns the selected drink : "
													+ this.selectedOption + ".\n");
		return this.selectedOption;
	}

	@Override
	public TimedMeasure<Double>	getTargetTemperature()
	{
		return new TimedMeasure<Double>(
				new Measure<Double>(this.targetTemperature, TEMPERATURE_UNIT));
	}

	@Override
	public TimedMeasure<Double>	getTankLevel()
	{
		return new TimedMeasure<Double>(
				new Measure<Double>(this.tankLevel, LEQUID_UNIT));
	}

	@Override
	public TimedMeasure<Double>	getCurrentTemperature()
	{
		return new TimedMeasure<Double>(
				new Measure<Double>(FAKE_CURRENT_TEMPERATURE, TEMPERATURE_UNIT));
	}

	@Override
	public boolean		isAwaiting()
	{
		return this.currentState == MachineCafeAutomatiqueState.AWAITING;
	}

	@Override
	public boolean		isReady()
	{
		return this.currentState == MachineCafeAutomatiqueState.READY;
	}

	@Override
	public boolean		isHeating()
	{
		return this.currentState == MachineCafeAutomatiqueState.HEATING;
	}

	@Override
	public boolean		isBrewing()
	{
		return this.currentState == MachineCafeAutomatiqueState.BREWING;
	}

	// -------------------------------------------------------------------------
	// Interface utilisateur
	// -------------------------------------------------------------------------

	@Override
	public void			orderCoffee(MachineCafeAutomatiqueOption choice)
	{
		this.trace("Coffee machine receives the order : " + choice + ".\n");

		assert	this.currentState == MachineCafeAutomatiqueState.OFF :
				new PreconditionException("getState() == OFF");
		assert	choice != null : new PreconditionException("choice != null");

		// La commande est mémorisée, puis la machine vérifie ses ressources
		this.selectedOption = choice;
		this.currentState = MachineCafeAutomatiqueState.AWAITING;
		this.leaveAwaitingIfPossible();
	}

	@Override
	public void			insertCapsule()
	{
		this.trace("Coffee machine receives a capsule.\n");

		assert	this.currentState == MachineCafeAutomatiqueState.OFF ||
				this.currentState == MachineCafeAutomatiqueState.AWAITING :
				new PreconditionException("getState() == OFF || AWAITING");
		assert	!this.capsuleIn : new PreconditionException("!isCapsulIn()");

		this.capsuleIn = true;
		this.leaveAwaitingIfPossible();
	}

	@Override
	public void			refillTank()
	{
		this.trace("Coffee machine tank is refilled.\n");

		assert	this.currentState == MachineCafeAutomatiqueState.OFF ||
				this.currentState == MachineCafeAutomatiqueState.AWAITING :
				new PreconditionException("getState() == OFF || AWAITING");

		this.tankLevel = TANK_CAPACITY.getData();
		this.leaveAwaitingIfPossible();
	}

	@Override
	public void			switchOff()
	{
		this.trace("Coffee machine is switched off.\n");

		assert	this.currentState != MachineCafeAutomatiqueState.OFF :
				new PreconditionException("getState() != OFF");

		// La capsule reste dans la machine, la commande est annulée
		this.currentState = MachineCafeAutomatiqueState.OFF;
		this.selectedOption = null;
		this.currentPowerLevel = 0.0;
	}

	// -------------------------------------------------------------------------
	// Interface de contrôle interne (phases automatiques)
	// -------------------------------------------------------------------------

	@Override
	public boolean		startReadyState()
	{
		this.trace("Coffee machine tries to become ready.\n");

		this.leaveAwaitingIfPossible();
		return this.isReady();
	}

	@Override
	public boolean		startHeating()
	{
		this.trace("Coffee machine starts heating.\n");

		// Seule la phase READY peut passer à HEATING
		if (this.currentState != MachineCafeAutomatiqueState.READY) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.HEATING;
		return true;
	}

	@Override
	public boolean		startBrewing()
	{
		this.trace("Coffee machine starts brewing.\n");

		// Seule la phase HEATING peut passer à BREWING
		if (this.currentState != MachineCafeAutomatiqueState.HEATING) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.BREWING;
		// La capsule et l'eau sont consommées au début de l'infusion
		this.capsuleIn = false;
		this.tankLevel -= volumeOf(this.selectedOption);
		if (this.tankLevel < 0.0) {
			this.tankLevel = 0.0;
		}
		return true;
	}

	// -------------------------------------------------------------------------
	// Interface de contrôle externe (HEM)
	// -------------------------------------------------------------------------

	@Override
	public Measure<Double>	getMaxPowerLevel()
	{
		return MAX_POWER_LEVEL;
	}

	@Override
	public TimedMeasure<Double>	getCurrentPowerLevel()
	{
		return new TimedMeasure<Double>(
				new Measure<Double>(this.currentPowerLevel, POWER_UNIT));
	}

	@Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	{
		this.trace("Coffee machine power level is set to " + powerLevel + ".\n");

		assert	this.on() : new PreconditionException("on()");
		assert	powerLevel != null :
				new PreconditionException("powerLevel != null");
		assert	powerLevel.getMeasurementUnit().equals(POWER_UNIT) :
				new PreconditionException(
						"powerLevel.getMeasurementUnit().equals(POWER_UNIT)");
		assert	powerLevel.getData() >= 0.0 &&
							powerLevel.getData() <= MAX_POWER_LEVEL.getData() :
				new PreconditionException(
						"powerLevel.getData() >= 0.0 && "
						+ "powerLevel.getData() <= MAX_POWER_LEVEL.getData()");

		this.currentPowerLevel = powerLevel.getData();
	}
}
