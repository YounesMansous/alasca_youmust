package equipements.uncontrolable;

/**
 * The interface <code>AspirateurClassiqueImplementationI</code> defines the
 * signatures and contracts of the operations implemented by the classic
 * vacuum cleaner component.
 *
 * <p>Created on : 2026-10-09</p>
 *
 * @author	ALASCA youmust team
 */
public interface AspirateurClassiqueImplementationI
extends		AspirateurClassiqueConfigurationI
{
	public static boolean	staticInvariants()
	{
		boolean ret = true;
		ret &= AspirateurClassiqueConfigurationI.staticInvariants();
		return ret;
	}

	/**
	 * return the current state of the vacuum cleaner.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	the current state of the vacuum cleaner.
	 */
	public AspirateurClassiqueState	getState();

	/**
	 * return the current suction mode of the vacuum cleaner.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	the current suction mode of the vacuum cleaner.
	 */
	public AspirateurClassiqueMode	getMode();

	/**
	 * return true if the mopping function is active.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code true}	// no precondition.
	 * post	{@code true}	// no postcondition.
	 * </pre>
	 *
	 * @return	true if the mopping function is active.
	 */
	public boolean		isMopActive();

	/**
	 * turn on the vacuum cleaner, in low suction mode and without mopping.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.OFF}
	 * post	{@code getState() == AspirateurClassiqueState.ON}
	 * post	{@code getMode() == AspirateurClassiqueMode.LOW}
	 * post	{@code !isMopActive()}
	 * </pre>
	 */
	public void			turnOn();

	/**
	 * turn off the vacuum cleaner, which also stops the mopping function.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.ON}
	 * post	{@code getState() == AspirateurClassiqueState.OFF}
	 * post	{@code !isMopActive()}
	 * </pre>
	 */
	public void			turnOff();

	/**
	 * set the vacuum cleaner in high suction mode.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.ON}
	 * pre	{@code getMode() == AspirateurClassiqueMode.LOW}
	 * post	{@code getMode() == AspirateurClassiqueMode.HIGH}
	 * </pre>
	 */
	public void			setHigh();

	/**
	 * set the vacuum cleaner in low suction mode.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.ON}
	 * pre	{@code getMode() == AspirateurClassiqueMode.HIGH}
	 * post	{@code getMode() == AspirateurClassiqueMode.LOW}
	 * </pre>
	 */
	public void			setLow();

	/**
	 * activate the mopping function.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.ON}
	 * pre	{@code !isMopActive()}
	 * post	{@code isMopActive()}
	 * </pre>
	 */
	public void			activateMop();

	/**
	 * deactivate the mopping function.
	 *
	 * <p><strong>Contract</strong></p>
	 *
	 * <pre>
	 * pre	{@code getState() == AspirateurClassiqueState.ON}
	 * pre	{@code isMopActive()}
	 * post	{@code !isMopActive()}
	 * </pre>
	 */
	public void			deactivateMop();
}
