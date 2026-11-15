package equipements.modulable;

public enum MachineCafeAutomatiqueState {
	OFF, //éteint
	AWAITING, //allumée mais en attente d'un choix
	WARMING_UP, // chaudière à fond
	RINSING, // chaudière et pompe (rinçage automatique au démarrage)
	GRINDING, // mise en marche du broyeur
	PRE_INFUSION, // pompe à faible débit
	STEAMING, // chaudière vapeur pour les boissons lactées
	READY_HOT, // maintien au chaud (thermostat)
	BREWING, // pompe et chaudière
	ECO_STANDBY, // maintien à température réduite
}
