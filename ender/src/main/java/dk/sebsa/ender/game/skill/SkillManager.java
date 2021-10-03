package dk.sebsa.ender.game.skill;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import dk.sebsa.ender.game.skill.type.DamageType;
import dk.sebsa.ender.game.skill.type.ResistanceSkill;
import dk.sebsa.ender.game.skill.type.Skill;
import dk.sebsa.ironflask.engine.Application;
import dk.sebsa.ironflask.engine.ecs.Entity;
import dk.sebsa.ironflask.engine.ecs.WorldManager;
import dk.sebsa.ironflask.engine.enums.Severity;
import dk.sebsa.ironflask.engine.io.LoggingUtil;
import dk.sebsa.ironflask.engine.utils.Command;
import dk.sebsa.ironflask.engine.utils.CommandUtils;
import dk.sebsa.ironflask.engine.utils.FileUtil;

public class SkillManager {
	public static List<DamageType> damageTypes = new ArrayList<>();
	public static List<Skill> skills = new ArrayList<>();
	private static Application application;
	
	public static void init(Application app) {
		String skillsFolder = Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/skill/";
		
		// Load damage types
		JSONArray damage = FileUtil.loadJsonArray(skillsFolder+"damage.json");
		for(int i = 0; i < damage.size(); i++) {
			JSONObject j = (JSONObject) damage.get(i);
			
			DamageType d = new DamageType((String) j.get("name"));
			damageTypes.add(d);
			
			JSONArray resitances = (JSONArray) j.get("resistances");
			if(resitances != null) {
				for(Object o : resitances.toArray()) {
					double level;
					if(o.getClass().equals(Long.class)) level = ((Long) o).doubleValue();
					else level = (Double) o;
					
					ResistanceSkill skill = new ResistanceSkill((float) level, d);
					skills.add(skill);
				}
			}
		}
		
		// Print
		LoggingUtil.appLog(app, Severity.Trace, "-- SkillManager Init --");
		LoggingUtil.appLog(app, Severity.Trace, "- Damage types: " + damageTypes.toString());
		LoggingUtil.appLog(app, Severity.Trace, "- Skills: " + skills.toString());
		LoggingUtil.appLog(app, Severity.Trace, "-- END --");
		
		// Add commands
		application = app;
		CommandUtils.addCommand(new Command(SkillManager::appraiseCommand, "apraise"));
	}
	
	public static void appraiseCommand(String[] args) {
		Entity e = WorldManager.getEntity(args[0]);
		if(e == null) {
			LoggingUtil.appLog(application, Severity.Warning, "Entity(" + args[0] + ") does not exist!");
			return;
		} else if (!e.getClass().equals(Creature.class)) {
			LoggingUtil.appLog(application, Severity.Warning, "Entity(" + args[0] + ") is not a creature!");
			return;
		}
		
		Creature creature = (Creature) e;
		LoggingUtil.appLog(application, Severity.Info, creature.stats.appraisal()+"\n");
	}
}