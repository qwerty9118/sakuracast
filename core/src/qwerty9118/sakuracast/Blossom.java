package qwerty9118.sakuracast;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Blossom extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
	private int bloomLevel;
	private List<Texture> textures;
	private boolean variant;
	private int dateVariance;
	private int closestSite;
	private LocalDate Ds;
	private LocalDate BD;
	
	public Blossom(List<Texture> blossomTex, int closestSite) {
		super(blossomTex.get(0));
		this.textures = blossomTex;
		this.variant = new Random().nextBoolean();
		this.dateVariance = new Random().nextInt(5)-2;
		this.bloomLevel = 0;
		this.closestSite = closestSite;
		
		this.Ds = SakuraCast.testSites.get(this.closestSite).getDs().plusDays(dateVariance);
		this.BD = SakuraCast.testSites.get(this.closestSite).getBD().plusDays(dateVariance);
	}
	
	@Override
	public void draw (Batch batch) {
		batch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);
	}
	
	@Override
	public Texture getTexture () {
		
		double bloomStageLvl = 14/3;
		
		if(SakuraCast.date.isBefore(this.Ds)) {
			bloomLevel = 0;
		}
		else if(SakuraCast.date.isBefore(this.BD)) {
			bloomLevel = 1;
		}
		else if(SakuraCast.date.isBefore(this.BD.plusDays((long) bloomStageLvl))) {
			bloomLevel = 2;
		}
		else if(SakuraCast.date.isBefore(this.BD.plusDays((long) bloomStageLvl*2 + dateVariance/2))) {
			bloomLevel = 3;
		}
		else if(SakuraCast.date.isBefore(this.BD.plusDays((long) bloomStageLvl*3 + dateVariance))) {
			bloomLevel = 4;
		}
		else {
			bloomLevel = 0;
		}
		
		
		
		
		//displays a different texture depending on bloom level.
		switch(bloomLevel) {
		
		case 0://blank texture (for unbroken dormancy bud)
			return this.textures.get(0);
		
		case 1://broken dormancy bud (1st image on the example diagram used)
			return ( this.variant ? this.textures.get(1) : this.textures.get(2) );
		
		case 2://growing flower (small blossom) (2nd image on the example diagram)
			return ( this.variant ? this.textures.get(3) : this.textures.get(4) );
		
		case 3://flowering (medium blossom) (3rd image on the example diagram)
			return ( this.variant ? this.textures.get(5) : this.textures.get(6) );
		
		case 4://flowered (large blossom)
			return ( this.variant ? this.textures.get(7) : this.textures.get(8) );
		
		default:
			return this.textures.get(0);
		
		}
		
	}
	
//	@Override
//	public void draw (Batch batch, float alphaModulation) {
//		float oldAlpha = getColor().a;
//		setAlpha(oldAlpha * alphaModulation);
//		draw(batch);
//		setAlpha(oldAlpha);
//	}
	
	//Getter for bloomLevel
	public int getBloomLevel() {
		return this.bloomLevel;
	}

	//Setter for bloomLevel
	public void setBloomLevel(int bloomLevel) {
		this.bloomLevel = bloomLevel;
	}

	//Incrementer for bloomLevel
	public void nextBloomLevel() {
		this.bloomLevel++;
	}
	
}