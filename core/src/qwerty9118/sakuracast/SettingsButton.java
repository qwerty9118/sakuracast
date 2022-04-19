package qwerty9118.sakuracast;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SettingsButton extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private List<Texture> textures;
	private boolean mouseOver;
	private boolean mouseDown;
	
	public SettingsButton(List<Texture> texs) {
		super(texs.get(0));
		this.textures = texs;
	}
	
	@Override
	public void draw (Batch batch) {
		batch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);
	}
	
	@Override
	public Texture getTexture () {
		
		if(mouseOver) {
			
			if(mouseDown) {
				
				return textures.get(2);
				
			}
			
			return textures.get(1);
			
		}
		
		return textures.get(0);
		
	}
	
	//Getter for mouseOver
	public boolean getMouseOver() {
		return this.mouseOver;
	}

	//Setter for mouseOver
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}
	
	//Getter for mouseOver
	public boolean getMouseDown() {
		return this.mouseDown;
	}

	//Setter for mouseOver
	public void setMouseDown(boolean mouseDown) {
		this.mouseDown = mouseDown;
	}

	public void dispose() {
		for(Texture t : textures) {
			t.dispose();
		}
	}
	
}