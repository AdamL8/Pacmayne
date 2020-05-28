package components;

public class InvincibleComponent implements IComponent{
	private final double immunityTimeInSeconds = 8.0;
	
	public double getImmunityTime() {
		return immunityTimeInSeconds;
	}
}
