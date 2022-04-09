package qwerty9118.sakuracast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Region extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private boolean mouseOver;
	private boolean zoomedInto;
	private Texture hoverTexture;
	
	public Region(Texture texture, Texture hoverTexture) {
		super(texture);
		//store the texture so that it can be temporarily changed
		this.hoverTexture = hoverTexture;
		//Set mouseOver & zoomedInto to false when created
		this.mouseOver = false;
		this.zoomedInto = false;
	}
	
//	public Texture getHoverTexture () {
//		return super.getTexture();
//	}
	
	@Override
	public void draw(Batch batch) {
		
		//will display a temporary image instead of the sprite's texture when hovered over
		if(mouseOver) {
			batch.draw(this.hoverTexture, getVertices(), 0, SPRITE_SIZE);
		}
		else {
			batch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);
		}
	}
	
//	@Override
//	public void draw (Batch batch, float alphaModulation) {
//		float oldAlpha = getColor().a;
//		setAlpha(oldAlpha * alphaModulation);
//		draw(batch);
//		setAlpha(oldAlpha);
//	}
	
	public float getMidX() {
		return this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2);
	}
	
	public float getMidY() {
		return this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2);
	}
	
	//Getter for mouseOver
	public boolean getMouseOver() {
		return this.mouseOver;
	}

	//Setter for mouseOver
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
	
	//Getter for zoomedInto
	public boolean getZoomedInto() {
		return this.zoomedInto;
	}

	//Setter for zoomedInto
	public void setZoomedInto(boolean zoomedInto) {
		this.zoomedInto = zoomedInto;
	}
	
	//Getter for hover texture, for disposing
	public Texture getHoverTexture() {
		return this.hoverTexture;
	}
	
}
