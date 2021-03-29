package dk.sebsa.ironflask.sandbox.components;

import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.math.Time;

public class Spin extends Component {
	@Override
	public void update() {
		float rotation = entity.getRotation().x + (100.5f*Time.getDeltaTime());
		if ( rotation > 360 ) {
		    rotation = 0;
		}
		entity.setLocalRotation(new Vector3f(rotation, rotation, rotation));
	}
}
