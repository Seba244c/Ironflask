package dk.sebsa.ender.game.skill.type;

public class DamageType {
	public final String name;
	
	public DamageType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
