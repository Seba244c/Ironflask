package dk.sebsa.ender.game.skill.type;

public class Skill {
	public enum Type {
		ability,
		resistance
	}
	
	public final Type type;
	public final String name;
	
	public Skill(Type type, String name) {
		this.type = type;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
