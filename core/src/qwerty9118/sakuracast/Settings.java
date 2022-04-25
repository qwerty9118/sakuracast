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
	private TextField editTemp;
	private TextField editMaxDTS;
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
		dateSlider.setValue(100);//set the starting position for the slider.
		dateSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {//listen for when the slider is "slided"
				
				//set date to the slider's current value
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
			public void changed(ChangeEvent event, Actor actor) {//listen for when this is checked or unchecked.
				
				//set tris to this checkboxes current value.
				tris = showTris.isChecked();
				
			}
		});
		
		//create the test site setting fields:
		//Ds
		editDs = new TextField("", skin);
		editDs.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*32, stage.getWidth()/4, stage.getHeight()/40);
		editDs.setMessageText("Ds");//this sets the text that the text field displays when there's no value.
		editDs.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {//listen for any keystrokes while focused on this text field.
				if(sk.tsSel) {
					
					//this is try/catch-d to avoid crashing when an invalid value is input.
					try {
						
						//attempt to set the Ds of the currently selected site to the value of this text field.
						sk.testSites.get(selectedSite).setDs(LocalDate.parse(editDs.getText()).withYear(2022));
						
						//refresh the blossoms to update the gradients & stuff.
						sk.populateBlossoms();
						
					}
					catch(DateTimeParseException e) {
//						System.out.println(e);
					}
					
				}
			}
		});
		
		//BD
		editBD = new TextField("", skin);
		editBD.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*31, stage.getWidth()/4, stage.getHeight()/40);
		editBD.setMessageText("BD");//this sets the text that the text field displays when there's no value.
		editBD.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {//listen for any keystrokes while focused on this text field.
				if(sk.tsSel) {
					
					//this is try/catch-d to avoid crashing when an invalid value is input.
					try {
						
						//attempt to set the BD of the currently selected site to the value of this text field.
						sk.testSites.get(selectedSite).setBD(LocalDate.parse(editBD.getText()).withYear(2022));
						
						//refresh the blossoms to update the gradients & stuff.
						sk.populateBlossoms();
						
					}
					catch(DateTimeParseException e) {
//						System.out.println(e);
					}
					
				}
			}
		});
		
		//Temp.
		editTemp = new TextField("", skin);
		editTemp.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*30, stage.getWidth()/4, stage.getHeight()/40);
		editTemp.setMessageText("Avg. temp. from Ds to BD, in degrees Celcius");//this sets the text that the text field displays when there's no value.
		editTemp.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {//listen for any keystrokes while focused on this text field.
				if(sk.tsSel) {
					
					//this is try/catch-d to avoid crashing when an invalid value is input.
					try {
						
						//attempt to set the temp. of the currently selected site to the value of this text field.
						sk.testSites.get(selectedSite).setTemp(Double.valueOf(editTemp.getText()));
						
						//refresh the blossoms to update the gradients & stuff.
						sk.populateBlossoms();
						
					}
					catch(NumberFormatException e) {
//						System.out.println(e);
					}
					
				}
			}
		});
		
		//Max DTS
		editMaxDTS = new TextField("", skin);
		editMaxDTS.setBounds(stage.getWidth()/40, (stage.getHeight()/40)*29, stage.getWidth()/4, stage.getHeight()/40);
		editMaxDTS.setMessageText("Total DTS needed to bloom");//this sets the text that the text field displays when there's no value.
		editMaxDTS.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {//listen for any keystrokes while focused on this text field.
				if(sk.tsSel) {
					
					//this is try/catch-d to avoid crashing when an invalid value is input.
					try {
						
						//attempt to set the max DTS of the currently selected site to the value of this text field.
						sk.testSites.get(selectedSite).setMaxDTS(Double.valueOf(editBD.getText()));
						
						//refresh the blossoms to update the gradients & stuff.
						sk.populateBlossoms();
						
					}
					catch(NumberFormatException e) {
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
		stage.addActor(editTemp);
		stage.addActor(editMaxDTS);
		
		//add the stage as a processor to the input multiplexer.
		sk.inputM.addProcessor(stage);
		
	}
	
	@Override
	public void show() {//show settings & enable all actors so that they can't be interacted with.
		
		this.visible = true;
		this.showTris.setDisabled(false);
		this.editDs.setDisabled(false);
		this.editBD.setDisabled(false);
		this.editTemp.setDisabled(false);
		this.editMaxDTS.setDisabled(false);
		
	}

	@Override
	public void hide() {//hide settings & disable all actors so that they can't be interacted with.
		this.visible = false;
		this.showTris.setDisabled(true);
		this.editDs.setDisabled(true);
		this.editBD.setDisabled(true);
		this.editTemp.setDisabled(true);
		this.editMaxDTS.setDisabled(true);
	}
	
	public boolean visible() {//return if settings are visible or not.
		return this.visible;
	}
	
	public static LocalDate date() {//return the current date, defined by the date slider.
		return date;
	}
	
	public void selectedSite(int i) {
		
		this.selectedSite = i;
		
		this.editDs.setText(sk.testSites.get(i).getDs().toString());
		this.editBD.setText(sk.testSites.get(i).getBD().toString());
		this.editTemp.setText(String.valueOf(sk.testSites.get(i).getTemp()));
		this.editMaxDTS.setText(String.valueOf(sk.testSites.get(i).getMaxDTS()));
		
	}
	
	public int getSelSite() {
		
		return this.selectedSite;
		
	}

	public void siteDesel() {
		
		this.editDs.setText("");
		this.editBD.setText("");
		this.editTemp.setText("");
		this.editMaxDTS.setText("");
		
	}

	@Override
	public void render(float delta) {
		
		dateSlider.draw(sk.batch, 1);
		
		if(this.visible) {
			
			showTris.draw(sk.batch, 1);
			editDs.draw(sk.batch, 1);
			editBD.draw(sk.batch, 1);
			editTemp.draw(sk.batch, 1);
			editMaxDTS.draw(sk.batch, 1);
			
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
