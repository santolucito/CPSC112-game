package cpsc112.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import cpsc112.game.Freegemas;

public class GwtLauncher extends GwtApplication {
	
	public GwtLauncher() {
		super();
		Freegemas.setPlatformResolver(new WebGLResolver());
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(960, 540);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new Freegemas();
	}
}