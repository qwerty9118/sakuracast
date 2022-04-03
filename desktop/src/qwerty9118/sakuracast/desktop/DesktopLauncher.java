package qwerty9118.sakuracast.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import qwerty9118.sakuracast.SakuraCast;

public class DesktopLauncher {
	public static void main(String[] arg) {
		//set up the config
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SakuraCast";//window title is set to "SakuraCast"
		config.width = 573;//width and height are initially set to my phone screen dimensions.
		config.height = 574;
		new LwjglApplication(new SakuraCast(), config);
	}
}
