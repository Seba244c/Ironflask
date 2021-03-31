package dk.sebsa.ironflask.engine.gui;

import dk.sebsa.ironflask.engine.graph.Shader;

public abstract class Modifier {
	public abstract void apply(Shader shader);
	public abstract void remove(Shader shader);
}
