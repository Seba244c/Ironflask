package dk.sebsa.ender.game.skill.type;

public class ResistanceSkill extends Skill {
	public DamageType damageType;
	public final float value;
	
	public ResistanceSkill(float level, DamageType damageType) {
		super(Type.resistance, generateName(level, damageType));
		this.damageType = damageType;
		this.value = level;
	}
	
	private static String generateName(float level, DamageType damageType) {
		String firstLetter = damageType.name.substring(0, 1);
	    String remainingLetters = damageType.name.substring(1, damageType.name.length());
	    firstLetter = firstLetter.toUpperCase();
	    String name = firstLetter + remainingLetters;

	    if(level == 0.25f) name = name + " Resistance";
	    else if(level == 0.5f) name = "Strong " + name + " Resistance";
	    else if(level == 0.9f) name = name + " Immunity";
	    else if(level == 1f) name += " Nullification";
	    
	    return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
