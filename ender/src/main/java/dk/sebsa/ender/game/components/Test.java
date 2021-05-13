package dk.sebsa.ender.game.components;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.skill.Creature;
import dk.sebsa.ender.game.skill.Damage;
import dk.sebsa.ender.game.skill.SkillManager;
import dk.sebsa.ironflask.engine.ecs.Component;

public class Test extends Component {
	@Override
	public void awake() {
		System.out.println("Player has: " + SkillManager.skills.get(0));
		((Creature) this.entity).addSkill(SkillManager.skills.get(0));
	}	
	@Override
	public void update() {
		if(Component.assingedInput.isKeyPressed(GLFW.GLFW_KEY_Z)) {
			Damage damage = new Damage(SkillManager.damageTypes.get(1), 100);
			System.out.println("Dealing: " + damage.toString());
			((Creature) this.entity).stats.takeDamage(damage);
		}
	}
}
