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
 * invariant	{@code 0 <= watertankLevel <= WATER_TANK_CAPACITY}
 * invariant	{@code 0 <= milktankLevel <= MILK_TANK_CAPACITY}
 * invariant	{@code 0 <= grainTankLevel <= BEANS_TANK_CAPACITY}
 * invariant	{@code 0 <= currentPowerLevel <= MAX_POWER_LEVEL}
 * invariant	{@code MIN_TARGET_TEMPERATURE <= targetTemperature <= MAX_TARGET_TEMPERATURE}
 * invariant	{@code selectedIntensity != null && selectedSize != null}
 * invariant	{@code currentState == OFF implique !heating && selectedDrink == null}
 * invariant	{@code heating implique currentState == READY_HOT || ECO_STANDBY}
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
	
	protected MachineCafeAutomatiqueState initialState = MachineCafeAutomatiqueState.OFF;

	/** État de la machine. */
	protected MachineCafeAutomatiqueState	currentState;
	/** Boisson commandée (null quand la machine est éteinte). */
	protected MachineCafeAutomatiqueDrink selectedDrink;
	/** Intensité choisie. */
	protected MachineCafeAutomatiqueIntensity selectedIntensity;
	/** Taille choisie. */
	protected MachineCafeAutomatiqueSize selectedSize;
	
	/** Niveau d'eau du réservoir, en litres (LEQUID_UNIT). */
	protected TimedMeasure<Double>			watertankLevel;
	/** Niveau de lait du réservoir, en litres (LEQUID_UNIT). */
	protected TimedMeasure<Double>			milktankLevel;
	/** Niveau de grains de la trémie, en kilogrammes (WEIGHT_UNIT). */
	protected TimedMeasure<Double>			grainTankLevel;
	/** Puissance de chauffe permise par le HEM, en watts (POWER_UNIT). */
	protected TimedMeasure<Double>			currentPowerLevel;
	/** Température cible, en degrés Celsius (TEMPERATURE_UNIT). */
	protected Measure<Double>				targetTemperature;
	/** Température courante de l'eau de la chaudière (TEMPERATURE_UNIT). */
	protected TimedMeasure<Double>			boilerTemperature;
	
	protected boolean 						heating = false;
	
	
	// -------------------------------------------------------------------------
	// Doses par boisson (mêmes unités que les capacités de la configuration)
	// -------------------------------------------------------------------------

	/** Eau consommée par le rinçage, en litres. */
	public static final Measure<Double>	RINSE_WATER =
								new Measure<Double>(0.03, LEQUID_UNIT);

	// Eau par boisson et par taille, en litres
	public static final Measure<Double>	ESPRESSO_WATER_SMALL =
								new Measure<Double>(0.04, LEQUID_UNIT);
	public static final Measure<Double>	ESPRESSO_WATER_LARGE =
								new Measure<Double>(0.06, LEQUID_UNIT);
	public static final Measure<Double>	LUNGO_WATER_SMALL =
								new Measure<Double>(0.11, LEQUID_UNIT);
	public static final Measure<Double>	LUNGO_WATER_LARGE =
								new Measure<Double>(0.15, LEQUID_UNIT);
	public static final Measure<Double>	CAPPUCCINO_WATER_SMALL =
								new Measure<Double>(0.04, LEQUID_UNIT);
	public static final Measure<Double>	CAPPUCCINO_WATER_LARGE =
								new Measure<Double>(0.06, LEQUID_UNIT);
	public static final Measure<Double>	LATTE_WATER_SMALL =
								new Measure<Double>(0.04, LEQUID_UNIT);
	public static final Measure<Double>	LATTE_WATER_LARGE =
								new Measure<Double>(0.06, LEQUID_UNIT);
	public static final Measure<Double>	HOT_WATER_SMALL =
								new Measure<Double>(0.20, LEQUID_UNIT);
	public static final Measure<Double>	HOT_WATER_LARGE =
								new Measure<Double>(0.30, LEQUID_UNIT);

	// Lait par boisson, en litres (aucun pour les boissons sans lait)
	public static final Measure<Double>	CAPPUCCINO_MILK =
								new Measure<Double>(0.10, LEQUID_UNIT);
	public static final Measure<Double>	LATTE_MILK =
								new Measure<Double>(0.18, LEQUID_UNIT);

	// Grains par intensité, en kilogrammes comme BEANS_TANK_CAPACITY
	// (aucun pour HOT_WATER)
	public static final Measure<Double>	BEANS_DOUCE =
								new Measure<Double>(0.007, WEIGHT_UNIT);
	public static final Measure<Double>	BEANS_NORMALE =
								new Measure<Double>(0.009, WEIGHT_UNIT);
	public static final Measure<Double>	BEANS_FORTE =
								new Measure<Double>(0.011, WEIGHT_UNIT);

	// -------------------------------------------------------------------------
	// Températures (entre MIN_TARGET_TEMPERATURE et MAX_TARGET_TEMPERATURE)
	// -------------------------------------------------------------------------

	/** Température de travail de la chaudière. */
	public static final Measure<Double>	HOT_TEMPERATURE =
								new Measure<Double>(92.0, TEMPERATURE_UNIT);
	/** Température de maintien réduit en ECO_STANDBY. */
	public static final Measure<Double>	ECO_TEMPERATURE =
								new Measure<Double>(80.0, TEMPERATURE_UNIT);
	/** Température de l'eau froide au démarrage (ce n'est pas une cible). */
	public static final Measure<Double>	INITIAL_TEMPERATURE =
								new Measure<Double>(20.0, TEMPERATURE_UNIT);


	// Les trois ports entrants
	protected MachineCafeAutomatiqueUserInboundPort				userIp;
	protected MachineCafeAutomatiqueInternalControlInboundPort	internalIp;
	protected MachineCafeAutomatiqueExternalControlJava4InboundPort	externalIp;
	
	// -------------------------------------------------------------------------
	// Constructeurs
	// -------------------------------------------------------------------------

	/**
	 * crée la machine avec les URI par défaut de ses ports.
	 *
	 * @throws Exception	<i>to do</i>.
	 */
	protected MachineCafeAutomatique() throws Exception
	{
		this(USER_INBOUND_PORT_URI,
			 INTERNAL_CONTROL_INBOUND_PORT_URI,
			 EXTERNAL_CONTROL_INBOUND_PORT_URI);
	}

	/**
	 * crée la machine avec les URI de ses trois ports entrants.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code userInboundPortURI != null && !userInboundPortURI.isEmpty()}
	 * pre	{@code internalControlInboundPortURI != null && !internalControlInboundPortURI.isEmpty()}
	 * pre	{@code externalControlInboundPortURI != null && !externalControlInboundPortURI.isEmpty()}
	 * post	{@code getState() == OFF}
	 * </pre>
	 *
	 * @param userInboundPortURI				URI du port utilisateur.
	 * @param internalControlInboundPortURI		URI du port de contrôle interne.
	 * @param externalControlInboundPortURI		URI du port de contrôle externe.
	 * @throws Exception						<i>to do</i>.
	 */
	protected MachineCafeAutomatique(
		String userInboundPortURI,
		String internalControlInboundPortURI,
		String externalControlInboundPortURI
		) throws Exception
	{
		this(REFLECTION_INBOUND_PORT_URI, userInboundPortURI,
			 internalControlInboundPortURI, externalControlInboundPortURI);
	}

	/**
	 * crée la machine avec l'URI de son port de réflexion et celles de ses
	 * trois ports entrants.
	 *
	 * @param reflectionInboundPortURI			URI du port de réflexion.
	 * @param userInboundPortURI				URI du port utilisateur.
	 * @param internalControlInboundPortURI		URI du port de contrôle interne.
	 * @param externalControlInboundPortURI		URI du port de contrôle externe.
	 * @throws Exception						<i>to do</i>.
	 */
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

	/**
	 * initialise l'état de la machine et publie ses trois ports entrants.
	 *
	 * @param userInboundPortURI				URI du port utilisateur.
	 * @param internalControlInboundPortURI		URI du port de contrôle interne.
	 * @param externalControlInboundPortURI		URI du port de contrôle externe.
	 * @throws Exception						<i>to do</i>.
	 */
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

		// État de départ : éteinte, sans commande, réservoirs pleins,
		// chaudière froide, puissance maximale permise
		this.currentState = this.initialState;
		this.selectedDrink = null;
		this.selectedIntensity = MachineCafeAutomatiqueIntensity.NORMALE;
		this.selectedSize = MachineCafeAutomatiqueSize.PETITE;
		this.watertankLevel = liquid(WATER_TANK_CAPACITY.getData());
		this.milktankLevel = liquid(MILK_TANK_CAPACITY.getData());
		this.grainTankLevel = weight(BEANS_TANK_CAPACITY.getData());
		this.currentPowerLevel = power(MAX_POWER_LEVEL.getData());
		this.targetTemperature = HOT_TEMPERATURE;
		this.boilerTemperature = temperature(INITIAL_TEMPERATURE.getData());
		this.heating = false;

		// Création et publication des trois ports entrants
		this.userIp = new MachineCafeAutomatiqueUserInboundPort(
											userInboundPortURI, this);
		this.userIp.publishPort();
		this.internalIp = new MachineCafeAutomatiqueInternalControlInboundPort(
											internalControlInboundPortURI, this);
		this.internalIp.publishPort();
		this.externalIp =
				new MachineCafeAutomatiqueExternalControlJava4InboundPort(
											externalControlInboundPortURI, this);
		this.externalIp.publishPort();

		if (VERBOSE) {
			this.tracer.get().setTitle("Coffee machine component");
			this.tracer.get().setRelativePosition(X_RELATIVE_POSITION,
												  Y_RELATIVE_POSITION);
			this.toggleTracing();
		}

		this.checkInvariants();
	}

	// -------------------------------------------------------------------------
	// Cycle de vie du composant
	// -------------------------------------------------------------------------

	/**
	 * dépublie les ports entrants avant d'arrêter le composant.
	 *
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.userIp.unpublishPort();
			this.internalIp.unpublishPort();
			this.externalIp.unpublishPort();
		} catch (Throwable e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Méthodes internes d'aide
	// -------------------------------------------------------------------------

	/** fabrique un niveau de liquide (eau ou lait), daté de maintenant. */
	protected static TimedMeasure<Double>	liquid(double litres)
	{
		return new TimedMeasure<Double>(
						new Measure<Double>(Math.max(0.0, litres), LEQUID_UNIT));
	}

	/** fabrique un niveau de grains, daté de maintenant. */
	protected static TimedMeasure<Double>	weight(double quantity)
	{
		return new TimedMeasure<Double>(
						new Measure<Double>(Math.max(0.0, quantity), WEIGHT_UNIT));
	}

	/** fabrique une puissance, datée de maintenant. */
	protected static TimedMeasure<Double>	power(double watts)
	{
		return new TimedMeasure<Double>(new Measure<Double>(watts, POWER_UNIT));
	}

	/** fabrique une température, datée de maintenant. */
	protected static TimedMeasure<Double>	temperature(double celsius)
	{
		return new TimedMeasure<Double>(
						new Measure<Double>(celsius, TEMPERATURE_UNIT));
	}

	/** vrai si la boisson commandée contient du lait. */
	protected boolean	isMilkDrink()
	{
		return this.selectedDrink == MachineCafeAutomatiqueDrink.CAPPUCCINO ||
			   this.selectedDrink == MachineCafeAutomatiqueDrink.LATTE;
	}

	/** vrai si l'eau de la chaudière est à la température de travail. */
	protected boolean	isHot()
	{
		return this.boilerTemperature.getData() >= HOT_TEMPERATURE.getData();
	}

	/** eau nécessaire pour la boisson commandée et la taille choisie, en litres. */
	protected double	waterNeeded()
	{
		boolean small = this.selectedSize == MachineCafeAutomatiqueSize.PETITE;
		switch (this.selectedDrink) {
		case ESPRESSO:
			return small ? ESPRESSO_WATER_SMALL.getData()
						 : ESPRESSO_WATER_LARGE.getData();
		case LUNGO:
			return small ? LUNGO_WATER_SMALL.getData()
						 : LUNGO_WATER_LARGE.getData();
		case CAPPUCCINO:
			return small ? CAPPUCCINO_WATER_SMALL.getData()
						 : CAPPUCCINO_WATER_LARGE.getData();
		case LATTE:
			return small ? LATTE_WATER_SMALL.getData()
						 : LATTE_WATER_LARGE.getData();
		default:	// HOT_WATER
			return small ? HOT_WATER_SMALL.getData()
						 : HOT_WATER_LARGE.getData();
		}
	}

	/** lait nécessaire pour la boisson commandée, en litres. */
	protected double	milkNeeded()
	{
		if (this.selectedDrink == MachineCafeAutomatiqueDrink.CAPPUCCINO) {
			return CAPPUCCINO_MILK.getData();
		}
		if (this.selectedDrink == MachineCafeAutomatiqueDrink.LATTE) {
			return LATTE_MILK.getData();
		}
		return 0.0;
	}

	/** grains nécessaires pour la boisson commandée et l'intensité choisie. */
	protected double	beansNeeded()
	{
		if (this.selectedDrink == MachineCafeAutomatiqueDrink.HOT_WATER) {
			return 0.0;
		}
		switch (this.selectedIntensity) {
		case DOUCE:		return BEANS_DOUCE.getData();
		case FORTE:		return BEANS_FORTE.getData();
		default:		return BEANS_NORMALE.getData();
		}
	}

	/**
	 * vrai si les réservoirs contiennent assez pour la boisson commandée, en
	 * comptant le rinçage si la machine est froide.
	 */
	protected boolean	resourcesAvailable()
	{
		double water = this.waterNeeded() +
								(this.isHot() ? 0.0 : RINSE_WATER.getData());
		return this.watertankLevel.getData() >= water &&
			   this.milktankLevel.getData() >= this.milkNeeded() &&
			   this.grainTankLevel.getData() >= this.beansNeeded();
	}

	/** vrai si la machine est au repos (pas de cycle en cours). */
	protected boolean	isAtRest()
	{
		return this.currentState == MachineCafeAutomatiqueState.OFF ||
			   this.currentState == MachineCafeAutomatiqueState.AWAITING ||
			   this.currentState == MachineCafeAutomatiqueState.READY_HOT ||
			   this.currentState == MachineCafeAutomatiqueState.ECO_STANDBY;
	}

	/** vrai si la machine est en maintien au chaud (normal ou réduit). */
	protected boolean	isKeepingWarm()
	{
		return this.currentState == MachineCafeAutomatiqueState.READY_HOT ||
			   this.currentState == MachineCafeAutomatiqueState.ECO_STANDBY;
	}

	/** vérifie les invariants ; appelé à la fin de chaque modification. */
	protected void		checkInvariants()
	{
		assert	MachineCafeAutomatique.implementationInvariants(this) :
				new ImplementationInvariantException(
						"MachineCafeAutomatique.implementationInvariants(this)");
		assert	MachineCafeAutomatique.invariants(this) :
				new InvariantException("MachineCafeAutomatique.invariants(this)");
	}

	/** affiche un message si les traces sont actives. */
	protected void		trace(String message)
	{
		if (VERBOSE) {
			this.traceMessage(message);
		}
	}
	
	
	// -------------------------------------------------------------------------
	// Invariants
	// -------------------------------------------------------------------------
	
	/**
	 * retourne vrai si les constantes internes du composant (doses et
	 * températures) sont cohérentes.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	vrai si les invariants statiques d'implémentation sont respectés.
	 */
	public static boolean	staticImplementationInvariants()
	{
		boolean ret = true;
		// Les doses d'eau et de lait sont en litres, les grains dans l'unité
		// de la trémie et les températures en degrés Celsius
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				RINSE_WATER.getMeasurementUnit().equals(LEQUID_UNIT) &&
				ESPRESSO_WATER_SMALL.getMeasurementUnit().equals(LEQUID_UNIT) &&
				ESPRESSO_WATER_LARGE.getMeasurementUnit().equals(LEQUID_UNIT) &&
				LUNGO_WATER_SMALL.getMeasurementUnit().equals(LEQUID_UNIT) &&
				LUNGO_WATER_LARGE.getMeasurementUnit().equals(LEQUID_UNIT) &&
				CAPPUCCINO_WATER_SMALL.getMeasurementUnit().equals(LEQUID_UNIT) &&
				CAPPUCCINO_WATER_LARGE.getMeasurementUnit().equals(LEQUID_UNIT) &&
				LATTE_WATER_SMALL.getMeasurementUnit().equals(LEQUID_UNIT) &&
				LATTE_WATER_LARGE.getMeasurementUnit().equals(LEQUID_UNIT) &&
				HOT_WATER_SMALL.getMeasurementUnit().equals(LEQUID_UNIT) &&
				HOT_WATER_LARGE.getMeasurementUnit().equals(LEQUID_UNIT),
				MachineCafeAutomatique.class,
				"doses d'eau en LEQUID_UNIT");
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				CAPPUCCINO_MILK.getMeasurementUnit().equals(LEQUID_UNIT) &&
				LATTE_MILK.getMeasurementUnit().equals(LEQUID_UNIT),
				MachineCafeAutomatique.class,
				"doses de lait en LEQUID_UNIT");
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				BEANS_DOUCE.getMeasurementUnit().equals(WEIGHT_UNIT) &&
				BEANS_NORMALE.getMeasurementUnit().equals(WEIGHT_UNIT) &&
				BEANS_FORTE.getMeasurementUnit().equals(WEIGHT_UNIT),
				MachineCafeAutomatique.class,
				"doses de grains en WEIGHT_UNIT");
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				HOT_TEMPERATURE.getMeasurementUnit().equals(TEMPERATURE_UNIT) &&
				ECO_TEMPERATURE.getMeasurementUnit().equals(TEMPERATURE_UNIT) &&
				INITIAL_TEMPERATURE.getMeasurementUnit().equals(TEMPERATURE_UNIT),
				MachineCafeAutomatique.class,
				"températures en TEMPERATURE_UNIT");
		// Les intensités sont dans le bon ordre
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				BEANS_DOUCE.getData() > 0.0 &&
					BEANS_DOUCE.getData() < BEANS_NORMALE.getData() &&
					BEANS_NORMALE.getData() < BEANS_FORTE.getData(),
				MachineCafeAutomatique.class,
				"0 < BEANS_DOUCE < BEANS_NORMALE < BEANS_FORTE");
		// Une trémie pleine permet au moins un café fort
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				BEANS_FORTE.getData() <= BEANS_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class,
				"BEANS_FORTE <= BEANS_TANK_CAPACITY");
		// La plus grosse dose d'eau, avec le rinçage, tient dans le réservoir
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				RINSE_WATER.getData() > 0.0 &&
					HOT_WATER_LARGE.getData() + RINSE_WATER.getData() <=
											WATER_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class,
				"RINSE_WATER > 0 && HOT_WATER_LARGE + RINSE_WATER <= "
				+ "WATER_TANK_CAPACITY");
		// La plus grosse dose de lait tient dans le réservoir
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				CAPPUCCINO_MILK.getData() > 0.0 && LATTE_MILK.getData() > 0.0 &&
					LATTE_MILK.getData() <= MILK_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class,
				"CAPPUCCINO_MILK > 0 && LATTE_MILK > 0 && "
				+ "LATTE_MILK <= MILK_TANK_CAPACITY");
		// Les températures sont dans le bon ordre
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				INITIAL_TEMPERATURE.getData() < ECO_TEMPERATURE.getData() &&
					ECO_TEMPERATURE.getData() < HOT_TEMPERATURE.getData(),
				MachineCafeAutomatique.class,
				"INITIAL_TEMPERATURE < ECO_TEMPERATURE < HOT_TEMPERATURE");
		// Les températures de travail et de maintien sont des cibles valides
		ret &= AssertionChecking.checkStaticImplementationInvariant(
				ECO_TEMPERATURE.getData() >= MIN_TARGET_TEMPERATURE.getData() &&
					HOT_TEMPERATURE.getData() <= MAX_TARGET_TEMPERATURE.getData(),
				MachineCafeAutomatique.class,
				"MIN_TARGET_TEMPERATURE <= ECO_TEMPERATURE < HOT_TEMPERATURE "
				+ "<= MAX_TARGET_TEMPERATURE");
		return ret;
	}

	/**
	 * retourne vrai si les attributs de la machine sont cohérents.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code mca != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param mca	instance à vérifier.
	 * @return		vrai si les invariants d'implémentation sont respectés.
	 */
	protected static boolean	implementationInvariants(
		MachineCafeAutomatique mca
		)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= staticImplementationInvariants();
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.currentState != null,
				MachineCafeAutomatique.class, mca,
				"currentState != null");
		// Les réservoirs existent, ont la bonne unité et restent entre 0 et
		// leur capacité
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.watertankLevel != null &&
					mca.watertankLevel.getMeasurementUnit().equals(LEQUID_UNIT) &&
					mca.watertankLevel.getData() >= 0.0 &&
					mca.watertankLevel.getData() <= WATER_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class, mca,
				"watertankLevel en LEQUID_UNIT et "
				+ "0 <= watertankLevel <= WATER_TANK_CAPACITY");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.milktankLevel != null &&
					mca.milktankLevel.getMeasurementUnit().equals(LEQUID_UNIT) &&
					mca.milktankLevel.getData() >= 0.0 &&
					mca.milktankLevel.getData() <= MILK_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class, mca,
				"milktankLevel en LEQUID_UNIT et "
				+ "0 <= milktankLevel <= MILK_TANK_CAPACITY");
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.grainTankLevel != null &&
					mca.grainTankLevel.getMeasurementUnit().equals(WEIGHT_UNIT) &&
					mca.grainTankLevel.getData() >= 0.0 &&
					mca.grainTankLevel.getData() <= BEANS_TANK_CAPACITY.getData(),
				MachineCafeAutomatique.class, mca,
				"grainTankLevel en WEIGHT_UNIT et "
				+ "0 <= grainTankLevel <= BEANS_TANK_CAPACITY");
		// La puissance permise par le HEM reste dans les bornes
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.currentPowerLevel != null &&
					mca.currentPowerLevel.getMeasurementUnit().equals(POWER_UNIT) &&
					mca.currentPowerLevel.getData() >= 0.0 &&
					mca.currentPowerLevel.getData() <= MAX_POWER_LEVEL.getData(),
				MachineCafeAutomatique.class, mca,
				"currentPowerLevel en POWER_UNIT et "
				+ "0 <= currentPowerLevel <= MAX_POWER_LEVEL");
		// La consigne de température reste dans ses bornes
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.targetTemperature != null &&
					mca.targetTemperature.getMeasurementUnit().equals(
														TEMPERATURE_UNIT) &&
					mca.targetTemperature.getData() >=
										MIN_TARGET_TEMPERATURE.getData() &&
					mca.targetTemperature.getData() <=
										MAX_TARGET_TEMPERATURE.getData(),
				MachineCafeAutomatique.class, mca,
				"targetTemperature en TEMPERATURE_UNIT et MIN_TARGET_TEMPERATURE "
				+ "<= targetTemperature <= MAX_TARGET_TEMPERATURE");
		// La température de la chaudière existe et a la bonne unité
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.boilerTemperature != null &&
					mca.boilerTemperature.getMeasurementUnit().equals(
														TEMPERATURE_UNIT),
				MachineCafeAutomatique.class, mca,
				"boilerTemperature != null et en TEMPERATURE_UNIT");
		// La taille et l'intensité ont toujours une valeur
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.selectedIntensity != null && mca.selectedSize != null,
				MachineCafeAutomatique.class, mca,
				"selectedIntensity != null && selectedSize != null");
		// Éteinte : pas de chauffe et pas de commande
		ret &= AssertionChecking.checkImplementationInvariant(
				mca.currentState != MachineCafeAutomatiqueState.OFF ||
					(!mca.heating && mca.selectedDrink == null),
				MachineCafeAutomatique.class, mca,
				"currentState == OFF implique !heating && selectedDrink == null");
		// La chaudière thermostatée ne chauffe qu'au maintien au chaud
		ret &= AssertionChecking.checkImplementationInvariant(
				!mca.heating ||
					mca.currentState == MachineCafeAutomatiqueState.READY_HOT ||
					mca.currentState == MachineCafeAutomatiqueState.ECO_STANDBY,
				MachineCafeAutomatique.class, mca,
				"heating implique currentState == READY_HOT || ECO_STANDBY");
		return ret;
	}

	/**
	 * retourne vrai si les invariants statiques sont respectés.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	vrai si les invariants statiques sont respectés.
	 */
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= MachineCafeAutomatiqueStateI.staticInvariants();
		ret &= AssertionChecking.checkStaticInvariant(
				REFLECTION_INBOUND_PORT_URI != null &&
									!REFLECTION_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"REFLECTION_INBOUND_PORT_URI != null && "
								+ "!REFLECTION_INBOUND_PORT_URI.isEmpty()");
		ret &= AssertionChecking.checkStaticInvariant(
				USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty(),
				MachineCafeAutomatique.class,
				"USER_INBOUND_PORT_URI != null && !USER_INBOUND_PORT_URI.isEmpty()");
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
				MachineCafeAutomatique.class,
				"X_RELATIVE_POSITION >= 0");
		ret &= AssertionChecking.checkStaticInvariant(
				Y_RELATIVE_POSITION >= 0,
				MachineCafeAutomatique.class,
				"Y_RELATIVE_POSITION >= 0");
		return ret;
	}

	/**
	 * retourne vrai si les invariants vus de l'extérieur sont respectés :
	 * ceux du composant et ceux de ses interfaces.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code mca != null}
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @param mca	instance à vérifier.
	 * @return		vrai si les invariants sont respectés.
	 */
	protected static boolean	invariants(MachineCafeAutomatique mca)
	{
		assert	mca != null : new PreconditionException("mca != null");

		boolean ret = true;
		ret &= staticInvariants();
		ret &= MachineCafeAutomatiqueInternalControlI.invariants(mca);
		ret &= MachineCafeAutomatiqueExternalControlI.invariants(mca);
		return ret;
	}
	
	
	// -------------------------------------------------------------------------
	// Lectures de l'état (StateI)
	// -------------------------------------------------------------------------

	@Override
	public MachineCafeAutomatiqueState	getState()
	{
		this.trace("Coffee machine returns its state : "
												+ this.currentState + ".\n");
		return this.currentState;
	}

	@Override
	public MachineCafeAutomatiqueDrink	getSelectedDrink()
	{
		return this.selectedDrink;
	}

	@Override
	public MachineCafeAutomatiqueIntensity	getSelectedIntensity()
	{
		return this.selectedIntensity;
	}

	@Override
	public MachineCafeAutomatiqueSize	getSelectedSize()
	{
		return this.selectedSize;
	}

	@Override
	public Measure<Double>	getTargetTemperature()
	{
		return this.targetTemperature;
	}

	@Override
	public TimedMeasure<Double>	getCurrentTemperature()
	{
		return this.boilerTemperature;
	}

	@Override
	public TimedMeasure<Double>	getWaterTankLevel()
	{
		return this.watertankLevel;
	}

	@Override
	public TimedMeasure<Double>	getMilkTankLevel()
	{
		return this.milktankLevel;
	}

	@Override
	public TimedMeasure<Double>	getBeansTanklevel()
	{
		return this.grainTankLevel;
	}

	@Override
	public boolean		on()
	{
		return this.currentState != MachineCafeAutomatiqueState.OFF;
	}

	// -------------------------------------------------------------------------
	// Interface utilisateur (UserI)
	// -------------------------------------------------------------------------

	@Override
	public void			chooseCoffeeType(MachineCafeAutomatiqueDrink choice)
	{
		this.trace("Coffee machine receives the order : " + choice + ".\n");

		assert	choice != null : new PreconditionException("choice != null");
		assert	this.currentState == MachineCafeAutomatiqueState.OFF ||
					this.isKeepingWarm() :
				new PreconditionException(
						"getState() == OFF || READY_HOT || ECO_STANDBY");

		// La commande est mémorisée et la machine attend que le contrôleur
		// lance le cycle (ressources et énergie disponibles)
		this.selectedDrink = choice;
		this.heating = false;
		this.targetTemperature = HOT_TEMPERATURE;
		this.currentState = MachineCafeAutomatiqueState.AWAITING;

		this.checkInvariants();
	}

	@Override
	public void			chooseCoffeeIntensity(
		MachineCafeAutomatiqueIntensity choice
		)
	{
		this.trace("Coffee machine intensity is set to " + choice + ".\n");

		assert	choice != null : new PreconditionException("choice != null");
		assert	this.isAtRest() :
				new PreconditionException(
						"getState() == OFF || AWAITING || READY_HOT || ECO_STANDBY");

		this.selectedIntensity = choice;

		this.checkInvariants();
	}

	@Override
	public void			chooseCoffeeSize(MachineCafeAutomatiqueSize choice)
	{
		this.trace("Coffee machine size is set to " + choice + ".\n");

		assert	choice != null : new PreconditionException("choice != null");
		assert	this.isAtRest() :
				new PreconditionException(
						"getState() == OFF || AWAITING || READY_HOT || ECO_STANDBY");

		this.selectedSize = choice;

		this.checkInvariants();
	}

	@Override
	public void			refillWater()
	{
		this.trace("Coffee machine water tank is refilled.\n");

		assert	this.isAtRest() :
				new PreconditionException(
						"getState() == OFF || AWAITING || READY_HOT || ECO_STANDBY");

		this.watertankLevel = liquid(WATER_TANK_CAPACITY.getData());

		this.checkInvariants();
	}

	@Override
	public void			refillMilk()
	{
		this.trace("Coffee machine milk tank is refilled.\n");

		assert	this.isAtRest() :
				new PreconditionException(
						"getState() == OFF || AWAITING || READY_HOT || ECO_STANDBY");

		this.milktankLevel = liquid(MILK_TANK_CAPACITY.getData());

		this.checkInvariants();
	}

	@Override
	public void			refillBeans()
	{
		this.trace("Coffee machine beans tank is refilled.\n");

		assert	this.isAtRest() :
				new PreconditionException(
						"getState() == OFF || AWAITING || READY_HOT || ECO_STANDBY");

		this.grainTankLevel = weight(BEANS_TANK_CAPACITY.getData());

		this.checkInvariants();
	}

	@Override
	public void			switchOff()
	{
		this.trace("Coffee machine is switched off.\n");

		assert	this.on() : new PreconditionException("on()");

		// La commande en cours est annulée, la chaudière s'arrête et l'eau
		// refroidit (bouchon : pas de simulation à cette étape)
		this.currentState = MachineCafeAutomatiqueState.OFF;
		this.selectedDrink = null;
		this.heating = false;
		this.targetTemperature = HOT_TEMPERATURE;
		this.boilerTemperature = temperature(INITIAL_TEMPERATURE.getData());

		this.checkInvariants();
	}

	// -------------------------------------------------------------------------
	// Interface de contrôle interne (InternalControlI)
	// -------------------------------------------------------------------------

	@Override
	public boolean		isHeating()
	{
		return this.heating;
	}

	@Override
	public boolean		startHeating()
	{
		this.trace("Coffee machine thermostat starts heating.\n");

		// La chaudière thermostatée ne chauffe qu'au maintien au chaud
		if (!this.isKeepingWarm() || this.heating) {
			return false;
		}
		this.heating = true;

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		endHeating()
	{
		this.trace("Coffee machine thermostat stops heating.\n");

		if (!this.isKeepingWarm() || !this.heating) {
			return false;
		}
		this.heating = false;

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isAwaiting()
	{
		return this.currentState == MachineCafeAutomatiqueState.AWAITING;
	}

	@Override
	public boolean		startWarming()
	{
		this.trace("Coffee machine tries to start warming up.\n");

		// Depuis l'attente, machine froide, ressources et énergie disponibles
		if (this.currentState != MachineCafeAutomatiqueState.AWAITING ||
				this.isHot() || !this.resourcesAvailable() ||
				this.currentPowerLevel.getData() <= 0.0) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.WARMING_UP;

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isWarmingUp()
	{
		return this.currentState == MachineCafeAutomatiqueState.WARMING_UP;
	}

	@Override
	public boolean		startRinsing()
	{
		this.trace("Coffee machine starts rinsing.\n");

		if (this.currentState != MachineCafeAutomatiqueState.WARMING_UP) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.RINSING;
		// Bouchon : la température de travail est atteinte
		this.boilerTemperature = temperature(HOT_TEMPERATURE.getData());
		this.watertankLevel = liquid(this.watertankLevel.getData() -
												RINSE_WATER.getData());

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isRinsing()
	{
		return this.currentState == MachineCafeAutomatiqueState.RINSING;
	}

	@Override
	public boolean		startGrinding()
	{
		this.trace("Coffee machine starts grinding.\n");

		// Après le rinçage, ou directement depuis l'attente si la machine est
		// déjà chaude (nouvelle commande depuis le maintien au chaud)
		boolean afterRinsing =
				this.currentState == MachineCafeAutomatiqueState.RINSING;
		boolean hotOrder =
				this.currentState == MachineCafeAutomatiqueState.AWAITING &&
				this.isHot() && this.resourcesAvailable() &&
				this.currentPowerLevel.getData() > 0.0;
		if (!afterRinsing && !hotOrder) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.GRINDING;
		this.heating = false;
		this.grainTankLevel = weight(this.grainTankLevel.getData() -
												this.beansNeeded());

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isGrinding()
	{
		return this.currentState == MachineCafeAutomatiqueState.GRINDING;
	}

	@Override
	public boolean		isInfusing()
	{
		return this.currentState == MachineCafeAutomatiqueState.PRE_INFUSION;
	}

	@Override
	public boolean		startInfusing()
	{
		this.trace("Coffee machine starts pre-infusion.\n");

		if (this.currentState != MachineCafeAutomatiqueState.GRINDING) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.PRE_INFUSION;

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isBrewing()
	{
		return this.currentState == MachineCafeAutomatiqueState.BREWING;
	}

	@Override
	public boolean		startBrewing()
	{
		this.trace("Coffee machine starts brewing.\n");

		if (this.currentState != MachineCafeAutomatiqueState.PRE_INFUSION) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.BREWING;
		this.watertankLevel = liquid(this.watertankLevel.getData() -
												this.waterNeeded());

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		startSteaming()
	{
		this.trace("Coffee machine starts steaming.\n");

		// La vapeur seulement après l'infusion d'une boisson au lait
		if (this.currentState != MachineCafeAutomatiqueState.BREWING ||
				!this.isMilkDrink()) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.STEAMING;
		this.milktankLevel = liquid(this.milktankLevel.getData() -
												this.milkNeeded());

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isSteaming()
	{
		return this.currentState == MachineCafeAutomatiqueState.STEAMING;
	}

	@Override
	public boolean		startReady()
	{
		this.trace("Coffee machine is ready and keeps warm.\n");

		// Après la vapeur, ou après l'infusion d'une boisson sans lait
		boolean afterSteaming =
				this.currentState == MachineCafeAutomatiqueState.STEAMING;
		boolean afterBrewing =
				this.currentState == MachineCafeAutomatiqueState.BREWING &&
				!this.isMilkDrink();
		if (!afterSteaming && !afterBrewing) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.READY_HOT;
		// La boisson est servie : la commande est terminée
		this.selectedDrink = null;
		this.targetTemperature = HOT_TEMPERATURE;

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isReady()
	{
		return this.currentState == MachineCafeAutomatiqueState.READY_HOT;
	}

	@Override
	public boolean		startEcoStandBy()
	{
		this.trace("Coffee machine goes to eco standby.\n");

		if (this.currentState != MachineCafeAutomatiqueState.READY_HOT) {
			return false;
		}
		this.currentState = MachineCafeAutomatiqueState.ECO_STANDBY;
		this.targetTemperature = ECO_TEMPERATURE;
		// Bouchon : l'eau redescend à la température de maintien réduit
		this.boilerTemperature = temperature(ECO_TEMPERATURE.getData());

		this.checkInvariants();
		return true;
	}

	@Override
	public boolean		isEcoStandBy()
	{
		return this.currentState == MachineCafeAutomatiqueState.ECO_STANDBY;
	}

	// -------------------------------------------------------------------------
	// Interface de contrôle externe (ExternalControlI)
	// -------------------------------------------------------------------------

	@Override
	public Measure<Double>	getMaxPowerLevel()
	{
		return MAX_POWER_LEVEL;
	}

	@Override
	public TimedMeasure<Double>	getCurrentPowerLevel()
	{
		return this.currentPowerLevel;
	}

	@Override
	public void			setCurrentPowerLevel(Measure<Double> powerLevel)
	{
		this.trace("Coffee machine power level is set to "
												+ powerLevel + ".\n");

		assert	this.on() : new PreconditionException("on()");
		assert	powerLevel != null :
				new PreconditionException("powerLevel != null");
		assert	powerLevel.getMeasurementUnit().equals(POWER_UNIT) :
				new PreconditionException(
						"powerLevel.getMeasurementUnit().equals(POWER_UNIT)");
		assert	powerLevel.getData() >= 0.0 &&
						powerLevel.getData() <= MAX_POWER_LEVEL.getData() :
				new PreconditionException(
						"0 <= powerLevel <= MAX_POWER_LEVEL");
		assert	this.isSuspendable() || powerLevel.getData() > 0.0 :
				new PreconditionException("isSuspendable() || powerLevel > 0");

		this.currentPowerLevel = power(powerLevel.getData());

		this.checkInvariants();
	}

	@Override
	public boolean		isSuspendable()
	{
		// Couper la chaudière ne gâche rien en attente ou au maintien au chaud
		return this.currentState == MachineCafeAutomatiqueState.AWAITING ||
			   this.isKeepingWarm();
	}
}
