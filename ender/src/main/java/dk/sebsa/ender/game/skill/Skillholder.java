package dk.sebsa.ender.game.skill;

import java.util.ArrayList;
import java.util.List;

import dk.sebsa.ender.game.skill.type.ResistanceSkill;
import dk.sebsa.ender.game.skill.type.Skill;

public class Skillholder {
	@SuppressWarnings("unused")
	private Creature creature;
	public List<Skill> skills = new ArrayList<>();
	public List<ResistanceSkill> resistanceSkills = new ArrayList<>();
	
	public Skillholder(Creature creature) {
		this.creature = creature;
	}
}
