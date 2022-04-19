package qwerty9118.sakuracast;

import java.time.LocalDate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Settings implements Screen {
	
	private final SakuraCast sk;
	private boolean visible = false;
	private Stage stage;
	private BitmapFont font;
	private static LocalDate date;
	private Slider dateSlider;
	
	public Settings(final SakuraCast sk) {
		
		this.sk = sk;
		this.stage = new Stage(sk.viewport);
		Gdx.input.setInputProcessor(stage);
		
		//initialise date as the current date, for convenience
		date = LocalDate.now();
		
		//slider
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		dateSlider = new Slider(1, 365, 1, false, skin);
		dateSlider.setBounds(stage.getWidth()/3, 10, stage.getHeight()/3, 25);
		dateSlider.setValue(120);
		dateSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				date = LocalDate.ofYearDay(2022, (int) dateSlider.getValue());
			}
		});
		
	}
	
	@Override
	public void show() {
		this.visible = true;
	}

	@Override
	public void hide() {
		this.visible = false;
	}
	
	public boolean visible() {
		return this.visible;
	}
	
	public static LocalDate date() {
		return date;
	}

	@Override
	public void render(float delta) {
//		Gdx.gl.glClearColor(.25f, .25f, .25f, 1f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		dateSlider.draw(sk.batch, 1);
		
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

}
