package dk.sebsa.ender.game.components;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import dk.sebsa.ironflask.engine.ecs.Component;
import dk.sebsa.ironflask.engine.math.Time;

public class PlayerMovement extends Component {
	public float speed = 10.0f;
	
	@Override
	public void update() {
		Vector3f movement = new Vector3f(0, 0, 0);
		if(Component.assingedInput.isKeyDown(GLFW.GLFW_KEY_W)) movement.z -= speed*Time.getDeltaTime();
		if(Component.assingedInput.isKeyDown(GLFW.GLFW_KEY_A)) movement.x -= speed*Time.getDeltaTime();
		if(Component.assingedInput.isKeyDown(GLFW.GLFW_KEY_S)) movement.z += speed*Time.getDeltaTime();
		if(Component.assingedInput.isKeyDown(GLFW.GLFW_KEY_D)) movement.x += speed*Time.getDeltaTime();
		entity.movePosition(movement);
	}
}
