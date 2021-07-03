package dk.sebsa.ender.game.components;

import org.lwjgl.glfw.GLFW;

import dk.sebsa.ender.game.skill.Creature;
import dk.sebsa.ender.game.skill.Damage;
import dk.sebsa.ender.game.skill.SkillManager;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;

public class Test extends Component {
	private Application app;
	
	public Test(Application app) {
		this.app = app;
	}
	
	@Override
	public void awake() {
		LoggingUtil.appLog(app, Severity.Info, "Player has: " + SkillManager.skills.get(0));
		((Creature) this.entity).addSkill(SkillManager.skills.get(0));
	}	
	@Override
	public void update() {
		if(Component.assingedInput.isKeyPressed(GLFW.GLFW_KEY_Z)) {
			Damage damage = new Damage(SkillManager.damageTypes.get(1), 100);
			LoggingUtil.appLog(app, Severity.Info, "Dealing: " + damage.toString());
			((Creature) this.entity).stats.takeDamage(damage);
		}
	}
}
