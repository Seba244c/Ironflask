package dk.sebsa.ender.game.skill;

import dk.sebsa.ender.game.skill.type.ResistanceSkill;
import dk.sebsa.ender.game.skill.type.Skill;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.World;

public class Creature extends Entity {
	private final Skillholder skill = new Skillholder(this);
	public final CreatureStats stats = new CreatureStats(this);
	
	public Creature(World world, String name) {
		super(world);
		this.name = name;
	}

	public Creature(String name) {
		super(name);
	}

	public Creature(boolean addToMaster, String name) {
		super(addToMaster);
		this.name = name;
	}
	
	public void addSkill(Skill newSkill) {
		skill.skills.add(newSkill);
		if(newSkill.getClass().equals(ResistanceSkill.class)) skill.resistanceSkills.add((ResistanceSkill) newSkill);
	}

	public Skillholder getSkills() {
		return skill;
	}
}
