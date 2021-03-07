package dk.sebsa.ironflask.sandbox.components;

import org.joml.Vector3f;

import dk.sebsa.ironflask.engine.ecs.Component;

public class Spin extends Component {
	@Override
	public void update() {
		float rotation = entity.getRotation().x + 1.5f;
		if ( rotation > 360 ) {
		    rotation = 0;
		}
		entity.setRotation(new Vector3f(rotation, rotation, rotation));
	}
}
