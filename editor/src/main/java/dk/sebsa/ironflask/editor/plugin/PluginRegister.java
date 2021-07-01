package dk.sebsa.ironflask.editor.plugin;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginRegister {
	String ID();
	String Name();
	String Version();;
	String Author();
}
