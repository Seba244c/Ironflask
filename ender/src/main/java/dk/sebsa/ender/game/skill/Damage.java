package dk.sebsa.ender.game.skill;

import dk.sebsa.ender.game.skill.type.DamageType;

public class Damage {
	public float value;
	public DamageType type;
	
	public Damage(DamageType type, float value) {
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value + " " + type.name + " Damage";
	}
}
