package qwerty9118.sakuracast;

import java.time.LocalDate;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TestSite extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private LocalDate Ds;
	private LocalDate BD;
	//temp in degrees celcius
	private double temp = 15;
	
	public TestSite() {
		super();
//		this.Ds = ;
//		this.BD = ;
	}
	
	public TestSite(Texture tex) {
		super(tex);
//		this.Ds = ;
//		this.BD = ;
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
	
	
	
	public float getCentreX() {
		return this.getBoundingRectangle().getX() + (this.getBoundingRectangle().getWidth() / 2);
	}
	
	public float getCentreY() {
		return this.getBoundingRectangle().getY() + (this.getBoundingRectangle().getHeight() / 2);
	}
	
}