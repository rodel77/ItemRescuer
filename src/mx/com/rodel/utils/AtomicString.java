package mx.com.rodel.utils;

public class AtomicString {
	String value = "";
	
	public AtomicString(String value) {
		this.value = value;
	}
	
	public AtomicString() {}

	public String get(){
		return value;
	}
	
	public void set(String newValue){
		value = newValue;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
