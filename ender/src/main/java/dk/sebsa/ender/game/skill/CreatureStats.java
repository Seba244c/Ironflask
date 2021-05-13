package dk.sebsa.ender.game.skill;

import dk.sebsa.ender.game.skill.type.ResistanceSkill;

public class CreatureStats {
	private Creature creature;
	private String title = "Commoner";
	
	public float taboo = 1;
	public float level = 4;
	public float exp = 1230;
	
	public float health = 30;
	public float maxHealth = 30;
	
	public float mana = 65;
	public float maxaMana = 80;
	
	public float statmina = 20;
	public float maxStamina = 70;
	
	public CreatureStats(Creature creature) {
		this.creature = creature;
	}
	
	public String appraisal() {
		String s = "\n--= Appraisal =--";
		s += "\n "+creature.getName();
		s += "\n<"+title+">";
		
		s += "\nLevel: " + level + ", EXP: " + exp;
		s += "\nTaboo: "+taboo + "/10";
		s += "\n\nHP: " + health + "/" + maxHealth;
		s += "\nMP: " + mana + "/" + maxaMana;
		s += "\nSP: " + statmina + "/" + maxStamina;
		return s;
	}
	
	public void takeDamage(Damage damage) {
		for(ResistanceSkill skill : creature.getSkills().resistanceSkills) {
			if(damage.type.equals(skill.damageType)) damage.value *= (1-skill.value);
		}
		
		health -= Math.ceil(damage.value);
	}
}
