package microwave.microwave;

import microwave.ITimer;

public class MicrowaveStatemachine implements IMicrowaveStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private SCInterfaceOperationCallback operationCallback;
		
		public void setSCInterfaceOperationCallback(
				SCInterfaceOperationCallback operationCallback) {
			this.operationCallback = operationCallback;
		}
		private boolean high;
		
		public void raiseHigh() {
			high = true;
			runCycle();
		}
		
		private boolean low;
		
		public void raiseLow() {
			low = true;
			runCycle();
		}
		
		private boolean digit;
		
		private long digitValue;
		
		public void raiseDigit(long value) {
			digit = true;
			digitValue = value;
			runCycle();
		}
		
		protected long getDigitValue() {
			if (! digit ) 
				throw new IllegalStateException("Illegal event value access. Event Digit is not raised!");
			return digitValue;
		}
		
		private boolean timer;
		
		public void raiseTimer() {
			timer = true;
			runCycle();
		}
		
		private boolean start;
		
		public void raiseStart() {
			start = true;
			runCycle();
		}
		
		private boolean stop;
		
		public void raiseStop() {
			stop = true;
			runCycle();
		}
		
		private boolean open;
		
		public void raiseOpen() {
			open = true;
			runCycle();
		}
		
		private boolean close;
		
		public void raiseClose() {
			close = true;
			runCycle();
		}
		
		private String concat;
		
		public String getConcat() {
			return concat;
		}
		
		public void setConcat(String value) {
			this.concat = value;
		}
		
		private long power;
		
		public long getPower() {
			return power;
		}
		
		public void setPower(long value) {
			this.power = value;
		}
		
		private boolean isClosed;
		
		public boolean getIsClosed() {
			return isClosed;
		}
		
		public void setIsClosed(boolean value) {
			this.isClosed = value;
		}
		
		private boolean onUse;
		
		public boolean getOnUse() {
			return onUse;
		}
		
		public void setOnUse(boolean value) {
			this.onUse = value;
		}
		
		private long counter;
		
		public long getCounter() {
			return counter;
		}
		
		public void setCounter(long value) {
			this.counter = value;
		}
		
		protected void clearEvents() {
			high = false;
			low = false;
			digit = false;
			timer = false;
			start = false;
			stop = false;
			open = false;
			close = false;
		}
	}
	
	protected SCInterfaceImpl sCInterface;
	
	private boolean initialized = false;
	
	public enum State {
		microwave_Init,
		microwave_operation,
		microwave_operation_Puissance_Selection,
		microwave_operation_Puissance_high,
		microwave_operation_Puissance_low,
		microwave_operation_Puissance_Timer,
		microwave_operation_Puissance_displaytime,
		microwave_operation_Puissance_cooking,
		microwave_operation_Puissance_cooking_cooking_cooking,
		microwave_operation_Puissance_cooking_cooking_done,
		microwave_operation_Puissance_cooking_cooking_Blinking,
		microwave_operation_Puissance_cooking_counter_countdown,
		microwave_operation_Puissance_cooking_counter_discount,
		microwave_operation_Puissance_cooking_counter_stop,
		microwave_operation_Puissance_cooking_counter_reset,
		microwave_operation_Puissance_waiting,
		microwave_operation_fermer_ouvert_close,
		microwave_operation_fermer_ouvert_open,
		$NullState$
	};
	
	private final State[] stateVector = new State[3];
	
	private int nextStateIndex;
	
	
	private ITimer timer;
	
	private final boolean[] timeEvents = new boolean[5];
	private long modCount;
	
	protected void setModCount(long value) {
		modCount = value;
	}
	
	protected long getModCount() {
		return modCount;
	}
	
	public MicrowaveStatemachine() {
		sCInterface = new SCInterfaceImpl();
	}
	
	public void init() {
		this.initialized = true;
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		if (this.sCInterface.operationCallback == null) {
			throw new IllegalStateException("Operation callback for interface sCInterface must be set.");
		}
		
		for (int i = 0; i < 3; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
		sCInterface.setConcat("");
		
		sCInterface.setPower(0);
		
		sCInterface.setIsClosed(false);
		
		sCInterface.setOnUse(false);
		
		sCInterface.setCounter(0);
		
		setModCount(0);
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		if (timer == null) {
			throw new IllegalStateException("timer not set.");
		}
		enterSequence_Microwave_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case microwave_Init:
				microwave_Init_react(true);
				break;
			case microwave_operation_Puissance_Selection:
				microwave_operation_Puissance_Selection_react(true);
				break;
			case microwave_operation_Puissance_high:
				microwave_operation_Puissance_high_react(true);
				break;
			case microwave_operation_Puissance_low:
				microwave_operation_Puissance_low_react(true);
				break;
			case microwave_operation_Puissance_Timer:
				microwave_operation_Puissance_Timer_react(true);
				break;
			case microwave_operation_Puissance_displaytime:
				microwave_operation_Puissance_displaytime_react(true);
				break;
			case microwave_operation_Puissance_cooking_cooking_cooking:
				microwave_operation_Puissance_cooking_cooking_cooking_react(true);
				break;
			case microwave_operation_Puissance_cooking_cooking_done:
				microwave_operation_Puissance_cooking_cooking_done_react(true);
				break;
			case microwave_operation_Puissance_cooking_cooking_Blinking:
				microwave_operation_Puissance_cooking_cooking_Blinking_react(true);
				break;
			case microwave_operation_Puissance_cooking_counter_countdown:
				microwave_operation_Puissance_cooking_counter_countdown_react(true);
				break;
			case microwave_operation_Puissance_cooking_counter_discount:
				microwave_operation_Puissance_cooking_counter_discount_react(true);
				break;
			case microwave_operation_Puissance_cooking_counter_stop:
				microwave_operation_Puissance_cooking_counter_stop_react(true);
				break;
			case microwave_operation_Puissance_cooking_counter_reset:
				microwave_operation_Puissance_cooking_counter_reset_react(true);
				break;
			case microwave_operation_Puissance_waiting:
				microwave_operation_Puissance_waiting_react(true);
				break;
			case microwave_operation_fermer_ouvert_close:
				microwave_operation_fermer_ouvert_close_react(true);
				break;
			case microwave_operation_fermer_ouvert_open:
				microwave_operation_fermer_ouvert_open_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_Microwave();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$||stateVector[1] != State.$NullState$||stateVector[2] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
		for (int i=0; i<timeEvents.length; i++) {
			timeEvents[i] = false;
		}
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case microwave_Init:
			return stateVector[0] == State.microwave_Init;
		case microwave_operation:
			return stateVector[0].ordinal() >= State.
					microwave_operation.ordinal()&& stateVector[0].ordinal() <= State.microwave_operation_fermer_ouvert_open.ordinal();
		case microwave_operation_Puissance_Selection:
			return stateVector[0] == State.microwave_operation_Puissance_Selection;
		case microwave_operation_Puissance_high:
			return stateVector[0] == State.microwave_operation_Puissance_high;
		case microwave_operation_Puissance_low:
			return stateVector[0] == State.microwave_operation_Puissance_low;
		case microwave_operation_Puissance_Timer:
			return stateVector[0] == State.microwave_operation_Puissance_Timer;
		case microwave_operation_Puissance_displaytime:
			return stateVector[0] == State.microwave_operation_Puissance_displaytime;
		case microwave_operation_Puissance_cooking:
			return stateVector[0].ordinal() >= State.
					microwave_operation_Puissance_cooking.ordinal()&& stateVector[0].ordinal() <= State.microwave_operation_Puissance_cooking_counter_reset.ordinal();
		case microwave_operation_Puissance_cooking_cooking_cooking:
			return stateVector[0] == State.microwave_operation_Puissance_cooking_cooking_cooking;
		case microwave_operation_Puissance_cooking_cooking_done:
			return stateVector[0] == State.microwave_operation_Puissance_cooking_cooking_done;
		case microwave_operation_Puissance_cooking_cooking_Blinking:
			return stateVector[0] == State.microwave_operation_Puissance_cooking_cooking_Blinking;
		case microwave_operation_Puissance_cooking_counter_countdown:
			return stateVector[1] == State.microwave_operation_Puissance_cooking_counter_countdown;
		case microwave_operation_Puissance_cooking_counter_discount:
			return stateVector[1] == State.microwave_operation_Puissance_cooking_counter_discount;
		case microwave_operation_Puissance_cooking_counter_stop:
			return stateVector[1] == State.microwave_operation_Puissance_cooking_counter_stop;
		case microwave_operation_Puissance_cooking_counter_reset:
			return stateVector[1] == State.microwave_operation_Puissance_cooking_counter_reset;
		case microwave_operation_Puissance_waiting:
			return stateVector[0] == State.microwave_operation_Puissance_waiting;
		case microwave_operation_fermer_ouvert_close:
			return stateVector[2] == State.microwave_operation_fermer_ouvert_close;
		case microwave_operation_fermer_ouvert_open:
			return stateVector[2] == State.microwave_operation_fermer_ouvert_open;
		default:
			return false;
		}
	}
	
	/**
	* Set the {@link ITimer} for the state machine. It must be set
	* externally on a timed state machine before a run cycle can be correctly
	* executed.
	* 
	* @param timer
	*/
	public void setTimer(ITimer timer) {
		this.timer = timer;
	}
	
	/**
	* Returns the currently used timer.
	* 
	* @return {@link ITimer}
	*/
	public ITimer getTimer() {
		return timer;
	}
	
	public void timeElapsed(int eventID) {
		timeEvents[eventID] = true;
		runCycle();
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public void raiseHigh() {
		sCInterface.raiseHigh();
	}
	
	public void raiseLow() {
		sCInterface.raiseLow();
	}
	
	public void raiseDigit(long value) {
		sCInterface.raiseDigit(value);
	}
	
	public void raiseTimer() {
		sCInterface.raiseTimer();
	}
	
	public void raiseStart() {
		sCInterface.raiseStart();
	}
	
	public void raiseStop() {
		sCInterface.raiseStop();
	}
	
	public void raiseOpen() {
		sCInterface.raiseOpen();
	}
	
	public void raiseClose() {
		sCInterface.raiseClose();
	}
	
	public String getConcat() {
		return sCInterface.getConcat();
	}
	
	public void setConcat(String value) {
		sCInterface.setConcat(value);
	}
	
	public long getPower() {
		return sCInterface.getPower();
	}
	
	public void setPower(long value) {
		sCInterface.setPower(value);
	}
	
	public boolean getIsClosed() {
		return sCInterface.getIsClosed();
	}
	
	public void setIsClosed(boolean value) {
		sCInterface.setIsClosed(value);
	}
	
	public boolean getOnUse() {
		return sCInterface.getOnUse();
	}
	
	public void setOnUse(boolean value) {
		sCInterface.setOnUse(value);
	}
	
	public long getCounter() {
		return sCInterface.getCounter();
	}
	
	public void setCounter(long value) {
		sCInterface.setCounter(value);
	}
	
	/* Entry action for state 'Init'. */
	private void entryAction_Microwave_Init() {
		sCInterface.operationCallback.clearDisplay();
		
		sCInterface.operationCallback.closeDoor();
		
		sCInterface.operationCallback.display("Open to start");
		
		sCInterface.operationCallback.stopCook();
	}
	
	/* Entry action for state 'Selection'. */
	private void entryAction_Microwave_operation_Puissance_Selection() {
		sCInterface.operationCallback.display("Power Level ");
	}
	
	/* Entry action for state 'high'. */
	private void entryAction_Microwave_operation_Puissance_high() {
		sCInterface.operationCallback.display("High");
	}
	
	/* Entry action for state 'low'. */
	private void entryAction_Microwave_operation_Puissance_low() {
		sCInterface.operationCallback.display("Low");
	}
	
	/* Entry action for state 'Timer'. */
	private void entryAction_Microwave_operation_Puissance_Timer() {
		sCInterface.operationCallback.displayTime(0);
	}
	
	/* Entry action for state 'displaytime'. */
	private void entryAction_Microwave_operation_Puissance_displaytime() {
		sCInterface.operationCallback.stopCook();
		
		sCInterface.operationCallback.displayTime(sCInterface.getPower());
	}
	
	/* Entry action for state 'cooking'. */
	private void entryAction_Microwave_operation_Puissance_cooking_cooking_cooking() {
		sCInterface.operationCallback.cook();
	}
	
	/* Entry action for state 'done'. */
	private void entryAction_Microwave_operation_Puissance_cooking_cooking_done() {
		timer.setTimer(this, 0, (1 * 1000), false);
		
		sCInterface.operationCallback.stopCook();
		
		sCInterface.operationCallback.display("Finished");
	}
	
	/* Entry action for state 'Blinking'. */
	private void entryAction_Microwave_operation_Puissance_cooking_cooking_Blinking() {
		timer.setTimer(this, 1, (1 * 1000), false);
		
		sCInterface.operationCallback.clearDisplay();
	}
	
	/* Entry action for state 'countdown'. */
	private void entryAction_Microwave_operation_Puissance_cooking_counter_countdown() {
		timer.setTimer(this, 2, (1 * 1000), false);
		
		sCInterface.operationCallback.displayTime(sCInterface.getPower());
	}
	
	/* Entry action for state 'discount'. */
	private void entryAction_Microwave_operation_Puissance_cooking_counter_discount() {
		timer.setTimer(this, 3, (1 * 1000), false);
	}
	
	/* Entry action for state 'stop'. */
	private void entryAction_Microwave_operation_Puissance_cooking_counter_stop() {
		timer.setTimer(this, 4, (1 * 1000), true);
		
		sCInterface.operationCallback.beepOn();
	}
	
	/* Entry action for state 'reset'. */
	private void entryAction_Microwave_operation_Puissance_cooking_counter_reset() {
		sCInterface.operationCallback.beepOff();
	}
	
	/* Entry action for state 'waiting'. */
	private void entryAction_Microwave_operation_Puissance_waiting() {
		sCInterface.operationCallback.display("waiting");
		
		sCInterface.operationCallback.stopCook();
	}
	
	/* Entry action for state 'close'. */
	private void entryAction_Microwave_operation_fermer_ouvert_close() {
		sCInterface.operationCallback.closeDoor();
	}
	
	/* Entry action for state 'open'. */
	private void entryAction_Microwave_operation_fermer_ouvert_open() {
		sCInterface.operationCallback.openDoor();
	}
	
	/* Exit action for state 'Init'. */
	private void exitAction_Microwave_Init() {
		sCInterface.setOnUse(false);
		
		sCInterface.setCounter(0);
		
		setModCount(100);
	}
	
	/* Exit action for state 'displaytime'. */
	private void exitAction_Microwave_operation_Puissance_displaytime() {
		setModCount((modCount * 10));
	}
	
	/* Exit action for state 'done'. */
	private void exitAction_Microwave_operation_Puissance_cooking_cooking_done() {
		timer.unsetTimer(this, 0);
	}
	
	/* Exit action for state 'Blinking'. */
	private void exitAction_Microwave_operation_Puissance_cooking_cooking_Blinking() {
		timer.unsetTimer(this, 1);
	}
	
	/* Exit action for state 'countdown'. */
	private void exitAction_Microwave_operation_Puissance_cooking_counter_countdown() {
		timer.unsetTimer(this, 2);
	}
	
	/* Exit action for state 'discount'. */
	private void exitAction_Microwave_operation_Puissance_cooking_counter_discount() {
		timer.unsetTimer(this, 3);
		
		sCInterface.setPower((sCInterface.power - 1));
	}
	
	/* Exit action for state 'stop'. */
	private void exitAction_Microwave_operation_Puissance_cooking_counter_stop() {
		timer.unsetTimer(this, 4);
		
		sCInterface.setCounter((sCInterface.counter + 1));
	}
	
	/* Exit action for state 'close'. */
	private void exitAction_Microwave_operation_fermer_ouvert_close() {
		sCInterface.setIsClosed(false);
	}
	
	/* Exit action for state 'open'. */
	private void exitAction_Microwave_operation_fermer_ouvert_open() {
		sCInterface.setIsClosed(true);
	}
	
	/* 'default' enter sequence for state Init */
	private void enterSequence_Microwave_Init_default() {
		entryAction_Microwave_Init();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_Init;
	}
	
	/* 'default' enter sequence for state operation */
	private void enterSequence_Microwave_operation_default() {
		enterSequence_Microwave_operation_Puissance_default();
		enterSequence_Microwave_operation_fermer_ouvert_default();
	}
	
	/* 'default' enter sequence for state Selection */
	private void enterSequence_Microwave_operation_Puissance_Selection_default() {
		entryAction_Microwave_operation_Puissance_Selection();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_Selection;
	}
	
	/* 'default' enter sequence for state high */
	private void enterSequence_Microwave_operation_Puissance_high_default() {
		entryAction_Microwave_operation_Puissance_high();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_high;
	}
	
	/* 'default' enter sequence for state low */
	private void enterSequence_Microwave_operation_Puissance_low_default() {
		entryAction_Microwave_operation_Puissance_low();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_low;
	}
	
	/* 'default' enter sequence for state Timer */
	private void enterSequence_Microwave_operation_Puissance_Timer_default() {
		entryAction_Microwave_operation_Puissance_Timer();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_Timer;
	}
	
	/* 'default' enter sequence for state displaytime */
	private void enterSequence_Microwave_operation_Puissance_displaytime_default() {
		entryAction_Microwave_operation_Puissance_displaytime();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_displaytime;
	}
	
	/* 'default' enter sequence for state cooking */
	private void enterSequence_Microwave_operation_Puissance_cooking_default() {
		enterSequence_Microwave_operation_Puissance_cooking_cooking_default();
		enterSequence_Microwave_operation_Puissance_cooking_counter_default();
	}
	
	/* 'default' enter sequence for state cooking */
	private void enterSequence_Microwave_operation_Puissance_cooking_cooking_cooking_default() {
		entryAction_Microwave_operation_Puissance_cooking_cooking_cooking();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_cooking_cooking_cooking;
	}
	
	/* 'default' enter sequence for state done */
	private void enterSequence_Microwave_operation_Puissance_cooking_cooking_done_default() {
		entryAction_Microwave_operation_Puissance_cooking_cooking_done();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_cooking_cooking_done;
	}
	
	/* 'default' enter sequence for state Blinking */
	private void enterSequence_Microwave_operation_Puissance_cooking_cooking_Blinking_default() {
		entryAction_Microwave_operation_Puissance_cooking_cooking_Blinking();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_cooking_cooking_Blinking;
	}
	
	/* 'default' enter sequence for state countdown */
	private void enterSequence_Microwave_operation_Puissance_cooking_counter_countdown_default() {
		entryAction_Microwave_operation_Puissance_cooking_counter_countdown();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_operation_Puissance_cooking_counter_countdown;
	}
	
	/* 'default' enter sequence for state discount */
	private void enterSequence_Microwave_operation_Puissance_cooking_counter_discount_default() {
		entryAction_Microwave_operation_Puissance_cooking_counter_discount();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_operation_Puissance_cooking_counter_discount;
	}
	
	/* 'default' enter sequence for state stop */
	private void enterSequence_Microwave_operation_Puissance_cooking_counter_stop_default() {
		entryAction_Microwave_operation_Puissance_cooking_counter_stop();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_operation_Puissance_cooking_counter_stop;
	}
	
	/* 'default' enter sequence for state reset */
	private void enterSequence_Microwave_operation_Puissance_cooking_counter_reset_default() {
		entryAction_Microwave_operation_Puissance_cooking_counter_reset();
		nextStateIndex = 1;
		stateVector[1] = State.microwave_operation_Puissance_cooking_counter_reset;
	}
	
	/* 'default' enter sequence for state waiting */
	private void enterSequence_Microwave_operation_Puissance_waiting_default() {
		entryAction_Microwave_operation_Puissance_waiting();
		nextStateIndex = 0;
		stateVector[0] = State.microwave_operation_Puissance_waiting;
	}
	
	/* 'default' enter sequence for state close */
	private void enterSequence_Microwave_operation_fermer_ouvert_close_default() {
		entryAction_Microwave_operation_fermer_ouvert_close();
		nextStateIndex = 2;
		stateVector[2] = State.microwave_operation_fermer_ouvert_close;
	}
	
	/* 'default' enter sequence for state open */
	private void enterSequence_Microwave_operation_fermer_ouvert_open_default() {
		entryAction_Microwave_operation_fermer_ouvert_open();
		nextStateIndex = 2;
		stateVector[2] = State.microwave_operation_fermer_ouvert_open;
	}
	
	/* 'default' enter sequence for region Microwave */
	private void enterSequence_Microwave_default() {
		react_microwave_Microwave__entry_Default();
	}
	
	/* 'default' enter sequence for region Puissance */
	private void enterSequence_Microwave_operation_Puissance_default() {
		react_microwave_Microwave_operation_Puissance__entry_Default();
	}
	
	/* 'default' enter sequence for region cooking */
	private void enterSequence_Microwave_operation_Puissance_cooking_cooking_default() {
		react_microwave_Microwave_operation_Puissance_cooking_cooking__entry_Default();
	}
	
	/* 'default' enter sequence for region counter */
	private void enterSequence_Microwave_operation_Puissance_cooking_counter_default() {
		react_microwave_Microwave_operation_Puissance_cooking_counter__entry_Default();
	}
	
	/* 'default' enter sequence for region fermer/ouvert */
	private void enterSequence_Microwave_operation_fermer_ouvert_default() {
		react_microwave_Microwave_operation_fermer_ouvert__entry_Default();
	}
	
	/* Default exit sequence for state Init */
	private void exitSequence_Microwave_Init() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_Init();
	}
	
	/* Default exit sequence for state operation */
	private void exitSequence_Microwave_operation() {
		exitSequence_Microwave_operation_Puissance();
		exitSequence_Microwave_operation_fermer_ouvert();
	}
	
	/* Default exit sequence for state Selection */
	private void exitSequence_Microwave_operation_Puissance_Selection() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state high */
	private void exitSequence_Microwave_operation_Puissance_high() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state low */
	private void exitSequence_Microwave_operation_Puissance_low() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state Timer */
	private void exitSequence_Microwave_operation_Puissance_Timer() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state displaytime */
	private void exitSequence_Microwave_operation_Puissance_displaytime() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_displaytime();
	}
	
	/* Default exit sequence for state cooking */
	private void exitSequence_Microwave_operation_Puissance_cooking() {
		exitSequence_Microwave_operation_Puissance_cooking_cooking();
		exitSequence_Microwave_operation_Puissance_cooking_counter();
	}
	
	/* Default exit sequence for state cooking */
	private void exitSequence_Microwave_operation_Puissance_cooking_cooking_cooking() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state done */
	private void exitSequence_Microwave_operation_Puissance_cooking_cooking_done() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_cooking_cooking_done();
	}
	
	/* Default exit sequence for state Blinking */
	private void exitSequence_Microwave_operation_Puissance_cooking_cooking_Blinking() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_cooking_cooking_Blinking();
	}
	
	/* Default exit sequence for state countdown */
	private void exitSequence_Microwave_operation_Puissance_cooking_counter_countdown() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_cooking_counter_countdown();
	}
	
	/* Default exit sequence for state discount */
	private void exitSequence_Microwave_operation_Puissance_cooking_counter_discount() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_cooking_counter_discount();
	}
	
	/* Default exit sequence for state stop */
	private void exitSequence_Microwave_operation_Puissance_cooking_counter_stop() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
		
		exitAction_Microwave_operation_Puissance_cooking_counter_stop();
	}
	
	/* Default exit sequence for state reset */
	private void exitSequence_Microwave_operation_Puissance_cooking_counter_reset() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}
	
	/* Default exit sequence for state waiting */
	private void exitSequence_Microwave_operation_Puissance_waiting() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state close */
	private void exitSequence_Microwave_operation_fermer_ouvert_close() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_Microwave_operation_fermer_ouvert_close();
	}
	
	/* Default exit sequence for state open */
	private void exitSequence_Microwave_operation_fermer_ouvert_open() {
		nextStateIndex = 2;
		stateVector[2] = State.$NullState$;
		
		exitAction_Microwave_operation_fermer_ouvert_open();
	}
	
	/* Default exit sequence for region Microwave */
	private void exitSequence_Microwave() {
		switch (stateVector[0]) {
		case microwave_Init:
			exitSequence_Microwave_Init();
			break;
		case microwave_operation_Puissance_Selection:
			exitSequence_Microwave_operation_Puissance_Selection();
			break;
		case microwave_operation_Puissance_high:
			exitSequence_Microwave_operation_Puissance_high();
			break;
		case microwave_operation_Puissance_low:
			exitSequence_Microwave_operation_Puissance_low();
			break;
		case microwave_operation_Puissance_Timer:
			exitSequence_Microwave_operation_Puissance_Timer();
			break;
		case microwave_operation_Puissance_displaytime:
			exitSequence_Microwave_operation_Puissance_displaytime();
			break;
		case microwave_operation_Puissance_cooking_cooking_cooking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_cooking();
			break;
		case microwave_operation_Puissance_cooking_cooking_done:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_done();
			break;
		case microwave_operation_Puissance_cooking_cooking_Blinking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_Blinking();
			break;
		case microwave_operation_Puissance_waiting:
			exitSequence_Microwave_operation_Puissance_waiting();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case microwave_operation_Puissance_cooking_counter_countdown:
			exitSequence_Microwave_operation_Puissance_cooking_counter_countdown();
			break;
		case microwave_operation_Puissance_cooking_counter_discount:
			exitSequence_Microwave_operation_Puissance_cooking_counter_discount();
			break;
		case microwave_operation_Puissance_cooking_counter_stop:
			exitSequence_Microwave_operation_Puissance_cooking_counter_stop();
			break;
		case microwave_operation_Puissance_cooking_counter_reset:
			exitSequence_Microwave_operation_Puissance_cooking_counter_reset();
			break;
		default:
			break;
		}
		
		switch (stateVector[2]) {
		case microwave_operation_fermer_ouvert_close:
			exitSequence_Microwave_operation_fermer_ouvert_close();
			break;
		case microwave_operation_fermer_ouvert_open:
			exitSequence_Microwave_operation_fermer_ouvert_open();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region Puissance */
	private void exitSequence_Microwave_operation_Puissance() {
		switch (stateVector[0]) {
		case microwave_operation_Puissance_Selection:
			exitSequence_Microwave_operation_Puissance_Selection();
			break;
		case microwave_operation_Puissance_high:
			exitSequence_Microwave_operation_Puissance_high();
			break;
		case microwave_operation_Puissance_low:
			exitSequence_Microwave_operation_Puissance_low();
			break;
		case microwave_operation_Puissance_Timer:
			exitSequence_Microwave_operation_Puissance_Timer();
			break;
		case microwave_operation_Puissance_displaytime:
			exitSequence_Microwave_operation_Puissance_displaytime();
			break;
		case microwave_operation_Puissance_cooking_cooking_cooking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_cooking();
			break;
		case microwave_operation_Puissance_cooking_cooking_done:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_done();
			break;
		case microwave_operation_Puissance_cooking_cooking_Blinking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_Blinking();
			break;
		case microwave_operation_Puissance_waiting:
			exitSequence_Microwave_operation_Puissance_waiting();
			break;
		default:
			break;
		}
		
		switch (stateVector[1]) {
		case microwave_operation_Puissance_cooking_counter_countdown:
			exitSequence_Microwave_operation_Puissance_cooking_counter_countdown();
			break;
		case microwave_operation_Puissance_cooking_counter_discount:
			exitSequence_Microwave_operation_Puissance_cooking_counter_discount();
			break;
		case microwave_operation_Puissance_cooking_counter_stop:
			exitSequence_Microwave_operation_Puissance_cooking_counter_stop();
			break;
		case microwave_operation_Puissance_cooking_counter_reset:
			exitSequence_Microwave_operation_Puissance_cooking_counter_reset();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region cooking */
	private void exitSequence_Microwave_operation_Puissance_cooking_cooking() {
		switch (stateVector[0]) {
		case microwave_operation_Puissance_cooking_cooking_cooking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_cooking();
			break;
		case microwave_operation_Puissance_cooking_cooking_done:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_done();
			break;
		case microwave_operation_Puissance_cooking_cooking_Blinking:
			exitSequence_Microwave_operation_Puissance_cooking_cooking_Blinking();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region counter */
	private void exitSequence_Microwave_operation_Puissance_cooking_counter() {
		switch (stateVector[1]) {
		case microwave_operation_Puissance_cooking_counter_countdown:
			exitSequence_Microwave_operation_Puissance_cooking_counter_countdown();
			break;
		case microwave_operation_Puissance_cooking_counter_discount:
			exitSequence_Microwave_operation_Puissance_cooking_counter_discount();
			break;
		case microwave_operation_Puissance_cooking_counter_stop:
			exitSequence_Microwave_operation_Puissance_cooking_counter_stop();
			break;
		case microwave_operation_Puissance_cooking_counter_reset:
			exitSequence_Microwave_operation_Puissance_cooking_counter_reset();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region fermer/ouvert */
	private void exitSequence_Microwave_operation_fermer_ouvert() {
		switch (stateVector[2]) {
		case microwave_operation_fermer_ouvert_close:
			exitSequence_Microwave_operation_fermer_ouvert_close();
			break;
		case microwave_operation_fermer_ouvert_open:
			exitSequence_Microwave_operation_fermer_ouvert_open();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave__entry_Default() {
		enterSequence_Microwave_Init_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_operation_Puissance__entry_Default() {
		enterSequence_Microwave_operation_Puissance_Selection_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_operation_Puissance_cooking_cooking__entry_Default() {
		enterSequence_Microwave_operation_Puissance_cooking_cooking_cooking_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_operation_Puissance_cooking_counter__entry_Default() {
		enterSequence_Microwave_operation_Puissance_cooking_counter_discount_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_microwave_Microwave_operation_fermer_ouvert__entry_Default() {
		enterSequence_Microwave_operation_fermer_ouvert_open_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean microwave_Init_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCInterface.open) {
					exitSequence_Microwave_Init();
					enterSequence_Microwave_operation_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCInterface.stop) {
					exitSequence_Microwave_operation();
					enterSequence_Microwave_Init_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_Selection_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.low) {
					exitSequence_Microwave_operation_Puissance_Selection();
					enterSequence_Microwave_operation_Puissance_low_default();
				} else {
					if (sCInterface.high) {
						exitSequence_Microwave_operation_Puissance_Selection();
						enterSequence_Microwave_operation_Puissance_high_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_high_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.low) {
					exitSequence_Microwave_operation_Puissance_high();
					enterSequence_Microwave_operation_Puissance_low_default();
				} else {
					if (sCInterface.timer) {
						exitSequence_Microwave_operation_Puissance_high();
						enterSequence_Microwave_operation_Puissance_Timer_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_low_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.high) {
					exitSequence_Microwave_operation_Puissance_low();
					enterSequence_Microwave_operation_Puissance_high_default();
				} else {
					if (sCInterface.timer) {
						exitSequence_Microwave_operation_Puissance_low();
						enterSequence_Microwave_operation_Puissance_Timer_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_Timer_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.high) {
					exitSequence_Microwave_operation_Puissance_Timer();
					enterSequence_Microwave_operation_Puissance_high_default();
				} else {
					if (sCInterface.low) {
						exitSequence_Microwave_operation_Puissance_Timer();
						enterSequence_Microwave_operation_Puissance_low_default();
					} else {
						if (sCInterface.digit) {
							exitSequence_Microwave_operation_Puissance_Timer();
							sCInterface.setPower(sCInterface.getDigitValue());
							
							enterSequence_Microwave_operation_Puissance_displaytime_default();
						} else {
							did_transition = false;
						}
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_displaytime_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.digit) {
					exitSequence_Microwave_operation_Puissance_displaytime();
					sCInterface.setPower(((((sCInterface.power * 10) % modCount)) + sCInterface.getDigitValue()));
					
					enterSequence_Microwave_operation_Puissance_displaytime_default();
				} else {
					if (((sCInterface.start) && (sCInterface.getIsClosed()==true))) {
						exitSequence_Microwave_operation_Puissance_displaytime();
						enterSequence_Microwave_operation_Puissance_cooking_default();
					} else {
						did_transition = false;
					}
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.open) {
					exitSequence_Microwave_operation_Puissance_cooking();
					enterSequence_Microwave_operation_Puissance_waiting_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_cooking_cooking_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_Puissance_cooking_react(try_transition)==false) {
				if (sCInterface.getPower()==0) {
					exitSequence_Microwave_operation_Puissance_cooking_cooking_cooking();
					enterSequence_Microwave_operation_Puissance_cooking_cooking_done_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_cooking_done_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_Puissance_cooking_react(try_transition)==false) {
				if (timeEvents[0]) {
					exitSequence_Microwave_operation_Puissance_cooking_cooking_done();
					enterSequence_Microwave_operation_Puissance_cooking_cooking_Blinking_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_cooking_Blinking_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_Puissance_cooking_react(try_transition)==false) {
				if (timeEvents[1]) {
					exitSequence_Microwave_operation_Puissance_cooking_cooking_Blinking();
					enterSequence_Microwave_operation_Puissance_cooking_cooking_done_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_counter_countdown_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (((timeEvents[2]) && (sCInterface.getPower()>0))) {
				exitSequence_Microwave_operation_Puissance_cooking_counter_countdown();
				enterSequence_Microwave_operation_Puissance_cooking_counter_discount_default();
			} else {
				if (sCInterface.getPower()==0) {
					exitSequence_Microwave_operation_Puissance_cooking_counter_countdown();
					sCInterface.setCounter((sCInterface.counter + 1));
					
					enterSequence_Microwave_operation_Puissance_cooking_counter_stop_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_counter_discount_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (((timeEvents[3]) && (sCInterface.getPower()>0))) {
				exitSequence_Microwave_operation_Puissance_cooking_counter_discount();
				enterSequence_Microwave_operation_Puissance_cooking_counter_countdown_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_counter_stop_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (timeEvents[4]) {
				exitSequence_Microwave_operation_Puissance_cooking_counter_stop();
				enterSequence_Microwave_operation_Puissance_cooking_counter_stop_default();
			} else {
				if (sCInterface.getCounter()>5) {
					exitSequence_Microwave_operation_Puissance_cooking_counter_stop();
					enterSequence_Microwave_operation_Puissance_cooking_counter_reset_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_cooking_counter_reset_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			exitSequence_Microwave_operation();
			enterSequence_Microwave_Init_default();
		}
		return did_transition;
	}
	
	private boolean microwave_operation_Puissance_waiting_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (microwave_operation_react(try_transition)==false) {
				if (sCInterface.close) {
					exitSequence_Microwave_operation_Puissance_waiting();
					enterSequence_Microwave_operation_Puissance_displaytime_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_fermer_ouvert_close_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.open) {
				exitSequence_Microwave_operation_fermer_ouvert_close();
				enterSequence_Microwave_operation_fermer_ouvert_open_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean microwave_operation_fermer_ouvert_open_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.close) {
				exitSequence_Microwave_operation_fermer_ouvert_open();
				enterSequence_Microwave_operation_fermer_ouvert_close_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
}
