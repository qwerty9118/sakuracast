package qwerty9118.sakuracast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Region extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private boolean mouseOver;
	private Texture hoverTexture;
	
	public Region(Texture texture) {
		super(texture);
		//store the texture so that it can be temporarily changed
		this.hoverTexture = new Texture(Gdx.files.internal("folder2.png"));
		//Set mouseOver to false when created
		this.mouseOver = false;
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
	
	//Getter for mouseOver
	public boolean getMouseOver() {
		return this.mouseOver;
	}

	//Setter for mouseOver
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
	
}
