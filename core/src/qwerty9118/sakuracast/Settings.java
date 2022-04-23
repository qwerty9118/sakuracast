package qwerty9118.sakuracast;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Settings implements Screen {
	
	private final SakuraCast sk;
	private boolean visible = false;
	private Stage stage;
	private Vector3 pos;
	private Vector3 dim;
	public static LocalDate date;
	public Slider dateSlider;
	public static boolean tris;
	public CheckBox showTris;
//	public Label zoom;
//	private boolean showTrisLastValue;
	private TextField editDs;
	private TextField editBD;
	private int selectedSite;
	
	public Settings(final SakuraCast sk) {
		
		this.sk = sk;
		this.stage = new Stage(sk.viewportGui);
		Gdx.input.setInputProcessor(stage);
		
		//initialise date as the current date, for convenience
		date = LocalDate.now();
		
		pos = sk.guiPosOnWorld(stage.getWidth()/3, (stage.getHeight()/20) * 19);
		dim = sk.guiPosOnWorld(stage.getWidth()/3, stage.getHeight()/20);
		
		//set up the skin for UI elements. This uses an example skin from libgdx.
		Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
		
		//create the slider for date
		dateSlider = new Slider(1, 180, 1, false, skin);
		dateSlider.setBounds(stage.getWidth()/3, (stage.getHeight()/20) * 19, stage.getWidth()/3, stage.getHeight()/20);
		dateSlider.setValue(100);
		dateSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				date = LocalDate.ofYearDay(2022, (int) dateSlider.getValue());
				
			}
		});
		date = LocalDate.ofYearDay(2022, (int) dateSlider.getValue());
		
		//create a checkbox to toggle the triangles between test sites
		showTris = new CheckBox("Show triangles connecting test sites", skin);
		showTris.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*33, stage.getWidth()/4, stage.getHeight()/40);
		showTris.setChecked(false);
		showTris.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
//				if(visible) {
					tris = showTris.isChecked();
//				}
//				else {
//					showTris.setChecked(showTrisLastValue);
//				}
			}
		});
		
		//create the test site setting fields
		editDs = new TextField("", skin);
		editDs.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*32, stage.getWidth()/4, stage.getHeight()/40);
		editDs.setMessageText("Ds");
		editDs.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				if(sk.tsSel) {
					
					try {
						
						sk.testSites.get(selectedSite).setDs(LocalDate.parse(editDs.getText()).withYear(2022));
						
						sk.populateBlossoms();
						
					}
					catch(DateTimeParseException e) {
//						System.out.println(e);
					}
					
				}
			}
		});
		
		editBD = new TextField("", skin);
		editBD.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*31, stage.getWidth()/4, stage.getHeight()/40);
		editBD.setMessageText("BD");
		editBD.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				if(sk.tsSel) {
					
					try {
						
						sk.testSites.get(selectedSite).setBD(LocalDate.parse(editBD.getText()).withYear(2022));
						
						sk.populateBlossoms();
						
					}
					catch(DateTimeParseException e) {
//						System.out.println(e);
					}
					
				}
			}
		});
		
		
		//add all actors to the stage.
		stage.addActor(dateSlider);
		stage.addActor(showTris);
		stage.addActor(editDs);
		stage.addActor(editBD);
		
		//add the stage as a processor to the input multiplexer.
		sk.inputM.addProcessor(stage);
		
	}
	
	@Override
	public void show() {
		
		this.visible = true;
		this.showTris.setDisabled(false);
		this.editDs.setDisabled(false);
		this.editBD.setDisabled(false);
		
	}

	@Override
	public void hide() {
		this.visible = false;
//		this.showTrisLastValue = showTris.isChecked();
		this.showTris.setDisabled(true);
		this.editDs.setDisabled(true);
		this.editBD.setDisabled(true);
	}
	
	public boolean visible() {
		return this.visible;
	}
	
	public static LocalDate date() {
		return date;
	}
	
	public void selectedSite(int i) {
		
		this.selectedSite = i;
		
		this.editDs.setText(sk.testSites.get(i).getDs().toString());
		this.editBD.setText(sk.testSites.get(i).getBD().toString());
		
	}
	
	public int getSelSite() {
		
		return this.selectedSite;
		
	}

	@Override
	public void render(float delta) {
		
		dateSlider.draw(sk.batch, 1);
		
		if(this.visible) {
			
			showTris.draw(sk.batch, 1);
			editDs.draw(sk.batch, 1);
			editBD.draw(sk.batch, 1);
			
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
		//re-find position
		pos = sk.guiPosOnWorld(stage.getWidth()/3, (stage.getHeight()/20) * 19);
		dim = sk.guiPosOnWorld(stage.getWidth()/3, stage.getHeight()/20);
		System.out.println(stage.getWidth()+", "+stage.getHeight());
		
		//re-calculate size & move to said position
		dateSlider.setBounds(stage.getWidth()/3, (stage.getHeight()/20) * 19, stage.getWidth()/3, stage.getHeight()/20);
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

}
