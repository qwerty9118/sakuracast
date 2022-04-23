package qwerty9118.sakuracast;

import java.time.LocalDate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public class TestSite extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
//	private Vector2 position;
	private boolean mouseOver;
	private boolean selected;
	private Texture selectedTexture;
	private boolean visible;
	private LocalDate Ds;
	private LocalDate BD;
	//temp in degrees celcius
	private double temp = 15;
	
	public TestSite() {
		
		super();
//		this.position = new Vector2(
//				this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2),
//				this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2));
//		this.Ds = ;
//		this.BD = ;
		this.visible = true;
		
	}
	
	public TestSite(Texture tex, Texture texS) {
		
		super(tex);
		this.selectedTexture = texS;
//		this.position = new Vector2(
//				this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2),
//				this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2));
//		this.Ds = ;
//		this.BD = ;
		this.visible = true;
		
	}
	
	@Override
	public void draw(Batch batch) {
		
		//will display a temporary image instead of the sprite's texture when hovered over
		if(selected) {
			batch.draw(this.selectedTexture, getVertices(), 0, SPRITE_SIZE);
		}
		else {
			batch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);
		}
	}
	
	
	
	//Getter for temperature
	public double getTemp() {
		return this.temp;
	}

	//Setter for temperature
	public void setTemp(double temp) {
		this.temp = temp;
	}
	
	
	
	//Getter for starting date
	public LocalDate getDs() {
		return this.Ds;
	}

	//Setter for starting date
	public void setDs(LocalDate Ds) {
		this.Ds = Ds;
	}
	
	
	
	//Getter for bloom date
	public LocalDate getBD() {
		return this.BD;
	}
	//Setter for bloom date
	public void setBD(LocalDate BD) {
		this.BD = BD;
	}
	
//	//Getter for bloom date of year j
//	public LocalDate getBD(int j) {
//		return this.BDj;
//	}
//	//Setter for bloom date of year j
//	public void setBD(int j, LocalDate BD) {
//		this.BDj = BDj;
//	}
	
	
	
	//Getter for bloom date
	public boolean selected() {
		return this.selected;
	}
	//Setter for bloom date
	public void select(boolean select) {
		this.selected = select;
	}
	
	
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void visible(boolean visible) {
		this.visible = visible;
	}
	
	public float posX() {
//		return this.position.x;
		return this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2);
	}
	
	public float posY() {
//		return this.position.y;
		return this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2);
	}
	
	public Vector2 pos() {
//		return this.position;
		return new Vector2(posX(), posY());
	}

	/** Sets the position so that the sprite is centered on (x, y) */
	public void setCenter (float x, float y) {
		setPosition(x - getWidth() / 2, y - getHeight() / 2);
	}
	
	public void set(TestSite ts) {
		
		super.set(ts);
//		position = ts.position;
		mouseOver = ts.mouseOver;
		selected = ts.selected;
		selectedTexture = ts.selectedTexture;
		visible = ts.visible;
		Ds = ts.Ds;
		BD = ts.BD;
		temp = ts.temp;
		
	}
	
//	public void set(TestSite ts) {
//		
//		sprite.set(ts.sprite);
//		setBounds(ts.getX(), ts.getY(), ts.getWidth(), ts.getHeight());
////		position = ts.position;
//		mouseOver = ts.mouseOver;
//		selected = ts.selected;
//		visible = ts.visible;
//		Ds = ts.Ds;
//		BD = ts.BD;
//		temp = ts.temp;
//		
//		setStage(ts.getStage());
//		setParent(ts.getParent());
//		for(EventListener l : ts.getListeners()) {
//			addListener(l);
//		}
//		for(EventListener cl : ts.getCaptureListeners()) {
//			addCaptureListener(cl);
//		}
//		for(Action a : ts.getActions()) {
//			addAction(a);
//		}
//		setName(ts.getName());
//		setTouchable(ts.getTouchable());
//		setDebug(ts.getDebug());
//		setOrigin(ts.getOriginX(), ts.getOriginY());
//		setScale(ts.getScaleX(), ts.getScaleY());
//		setRotation(ts.getRotation());
//		setColor(ts.getColor());
//		setUserObject(ts.getUserObject());
//		
//	}
	
}