package cpsc112.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import cpsc112.game.PlatformResolver;

public class AndroidResolver implements PlatformResolver {
	
	public String getDefaultLanguage() {
		return java.util.Locale.getDefault().toString();
	}
	
	public String format(String string, Object... args) {
		return String.format(string, args);
	}

	@Override
	public BitmapFont loadFont(String fntFile, String ttfFile, int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(ttfFile));
		BitmapFont font = generator.generateFont(size, FreeTypeFontGenerator.DEFAULT_CHARS, true);
		generator.dispose();
		return font;
	}
}
