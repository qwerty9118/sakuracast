package qwerty9118.sakuracast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TestSite extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	//temp in degrees celcius
	private float temp = 15f;
	
	public TestSite() {
		super();
	}
	
	public TestSite(Texture tex) {
		super(tex);
	}
	
	//Getter for bloomLevel
	public float getTemp() {
		return this.temp;
	}

	//Setter for bloomLevel
	public void setTemp(int temp) {
		this.temp = temp;
	}
	
	public float getCentreX() {
		return this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2);
	}
	
	public float getCentreY() {
		return this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2);
	}
	
}