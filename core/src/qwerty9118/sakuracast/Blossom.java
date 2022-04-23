package qwerty9118.sakuracast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Blossom extends Sprite {

	static final int VERTEX_SIZE = 2 + 1 + 2;
	static final int SPRITE_SIZE = 4 * VERTEX_SIZE;
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
		this.closestSite = closestSite;
		
		this.Ds = SakuraCast.testSites.get(this.closestSite).getDs().plusDays(dateVariance);
		this.BD = SakuraCast.testSites.get(this.closestSite).getBD().plusDays(dateVariance);
	}
	
	public Blossom(List<Texture> blossomTex, ArrayList<Double> closestSite) {
		super(blossomTex.get(0));
		this.textures = blossomTex;
		this.variant = new Random().nextBoolean();
		this.dateVariance = new Random().nextInt(5)-2;
		this.closestSite = closestSite.get(0).intValue();
		
		this.Ds = LocalDate.ofYearDay(2022, (int) (
				(SakuraCast.testSites.get(closestSite.get(0).intValue()).getDs().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(1))
				+SakuraCast.testSites.get(closestSite.get(2).intValue()).getDs().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(3))
				+SakuraCast.testSites.get(closestSite.get(4).intValue()).getDs().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(5)))
				/ (1/closestSite.get(1) + 1/closestSite.get(3) + 1/closestSite.get(5))));
		
		this.BD = LocalDate.ofYearDay(2022, (int) (
				(SakuraCast.testSites.get(closestSite.get(0).intValue()).getBD().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(1))
				+SakuraCast.testSites.get(closestSite.get(2).intValue()).getBD().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(3))
				+SakuraCast.testSites.get(closestSite.get(4).intValue()).getBD().plusDays(dateVariance).getDayOfYear() * (1/closestSite.get(5)))
				/ (1/closestSite.get(1) + 1/closestSite.get(3) + 1/closestSite.get(5))));
		
	}
	
	@Override
	public void draw (Batch batch) {
		batch.draw(getTexture(), getVertices(), 0, SPRITE_SIZE);
	}
	
	@Override
	public Texture getTexture () {
		
		double bloomStageLvl = 14/3;
		
		if(Settings.date().isBefore(this.Ds)) {
			return this.textures.get(0);
		}
		else if(Settings.date().isBefore(this.BD)) {
			return ( this.variant ? this.textures.get(1) : this.textures.get(2) );
		}
		else if(Settings.date().isBefore(this.BD.plusDays((long) bloomStageLvl))) {
			return ( this.variant ? this.textures.get(3) : this.textures.get(4) );
		}
		else if(Settings.date().isBefore(this.BD.plusDays((long) bloomStageLvl*2 + dateVariance/2))) {
			return ( this.variant ? this.textures.get(5) : this.textures.get(6) );
		}
		else if(Settings.date().isBefore(this.BD.plusDays((long) bloomStageLvl*3 + dateVariance))) {
			return ( this.variant ? this.textures.get(7) : this.textures.get(8) );
		}
		else {
			return this.textures.get(0);
		}
		
	}
	
}