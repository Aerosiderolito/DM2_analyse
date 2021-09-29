package microwave.microwave;

import microwave.IStatemachine;
import microwave.ITimerCallback;

public interface IMicrowaveStatemachine extends ITimerCallback,IStatemachine {
	public interface SCInterface {
	
		public void raiseHigh();
		
		public void raiseLow();
		
		public void raiseDigit(long value);
		
		public void raiseTimer();
		
		public void raiseStart();
		
		public void raiseStop();
		
		public void raiseOpen();
		
		public void raiseClose();
		
		public String getConcat();
		
		public void setConcat(String value);
		
		public long getPower();
		
		public void setPower(long value);
		
		public boolean getIsClosed();
		
		public void setIsClosed(boolean value);
		
		public boolean getOnUse();
		
		public void setOnUse(boolean value);
		
		public long getCounter();
		
		public void setCounter(long value);
		
		public void setSCInterfaceOperationCallback(SCInterfaceOperationCallback operationCallback);
	
	}
	
	public interface SCInterfaceOperationCallback {
	
		public void displayTime(long t);
		
		public void display(String msg);
		
		public void clearDisplay();
		
		public void cook();
		
		public void stopCook();
		
		public void beepOn();
		
		public void beepOff();
		
		public void closeDoor();
		
		public void openDoor();
		
	}
	
	public SCInterface getSCInterface();
	
}
