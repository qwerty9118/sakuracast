package qwerty9118.sakuracast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SakuraCast extends ApplicationAdapter implements InputProcessor {
	private Texture bgImage;
	private BitmapFont font;
	private Sound sfxSelect;
	private SpriteBatch batch;
	private Viewport viewport;
	private OrthographicCamera camera;
	private List<String> regionLoc;
	private List<Region> regions;
//	private List<Rectangle> regionHitboxes;
	private List<Polygon> regionHitpolys;
	private List<List<Integer>> regionPos;
	private List<Blossom> blossoms;
	private List<Texture> blossomTex;
	private float width;
	private float height;
//	private ShapeRenderer shaperend;
	private int DATE;
	private float zoom;
	private Vector3 zTextLoc;

	@Override
	public void create() {
		
		// load the background image & set internal size
		bgImage = new Texture(Gdx.files.internal("watrMap.png"));
		width = bgImage.getWidth();
		height = bgImage.getHeight();
		
		// load the rest of the images for the program
		regionLoc = new ArrayList<String>();
		blossomTex = new ArrayList<Texture>();
		populateRegionTex();
		populateBlossomTex();

		// load the font
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2f);
		
		//set zoom level label location
		zTextLoc = new Vector3(20, 20, 0);
		
		//set stored zoom level
		zoom = 1f;
		
		// load any sound effects
		sfxSelect = Gdx.audio.newSound(Gdx.files.internal("select.wav"));

		// create the camera, the viewport, and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		batch = new SpriteBatch();

		// create & populate the regions array
		regions = new ArrayList<Region>();
//		regionHitboxes = new ArrayList<Rectangle>();
		regionHitpolys = new ArrayList<Polygon>();
		regionPos = new ArrayList<List<Integer>>();
		populateRegions();
		
		//create & populate the blossoms array
		blossoms = new ArrayList<Blossom>();
		populateBlossoms();
		
//		shaperend = new ShapeRenderer();
		
		Gdx.input.setInputProcessor(this);
		
	}

	//this populates the region texture array with textures.
	private void populateRegionTex() {
		
		//loop through all files ("FileHandle"s) in the "regions" folder.
		for(FileHandle f : Gdx.files.internal("regions/").list()) {
			this.regionLoc.add(f.toString());
		}
		
		//Sort the list.
		Collections.sort(this.regionLoc);
		
	}

	//this populates the region array with regions.
	private void populateRegions() {
		
		//create some variables to store values for the loop
		Region region;
//		Rectangle rect;
		float x;
		float y;
		float texWidth;
		float texHeight;
		
		
		//add each position, scale, and rotation individually for each region.
		regionPos.add(Arrays.asList(582, 543, 214, 216, -7));//Chubu
		regionPos.add(Arrays.asList(317, 506, 193, 109, -4));//Chugoku
		regionPos.add(Arrays.asList(820, 870, 301, 239, -8));//Hokkaido
		regionPos.add(Arrays.asList(498, 482, 151, 133, -6));//Kansai
		regionPos.add(Arrays.asList(725, 560, 143, 120, -10));//Kanto
		regionPos.add(Arrays.asList(236, 372, 138, 150, -3));//Kyushu
		regionPos.add(Arrays.asList(135, 115, 37, 52, -14));//Okinawa
		regionPos.add(Arrays.asList(372, 453, 151, 89, -3));//Shikoku
		regionPos.add(Arrays.asList(800, 655, 140, 241, -12));//Tohoku
		
		//add each region's hitpolygon individually.
		//each polygon is made from a list of x-coordinate, y-coordinate pairs.
		regionHitpolys.add(new Polygon( new float[]{755,373,791,412,714,493,770,582,652,603,606,526,568,545,550,490} ));//Chubu
		regionHitpolys.add(new Polygon( new float[]{501,501,501,588,332,641,301,595} ));//Chugoku
		regionHitpolys.add(new Polygon( new float[]{906,46,1124,152,993,227,776,250} ));//Hokkaido
		regionHitpolys.add(new Polygon( new float[]{508,527,572,550,601,533,667,639,533,673} ));//Kansai
		regionHitpolys.add(new Polygon( new float[]{757,469,855,485,857,544,798,590,717,494} ));//Kanto
		regionHitpolys.add(new Polygon( new float[]{310,624,372,679,332,775,261,764,232,651} ));//Kyushu
		regionHitpolys.add(new Polygon( new float[]{184,972,141,1039,120,1002} ));//Okinawa
		regionHitpolys.add(new Polygon( new float[]{508,596,525,636,391,701,369,651} ));//Shikoku
		regionHitpolys.add(new Polygon( new float[]{792,258,938,243,856,482,761,459,797,411,771,382} ));//Tohoku
		
//		//scale the coordinates.
//		for(int i = 0; i < regionHitpolys.size(); i++) {
//			regionHitpolys.get(i).setScale((float) Gdx.graphics.getWidth() / width, (float) Gdx.graphics.getHeight() / height);
//		}
		
		
		
		//loop once for each location in regionLoc
		for(int i = 0; i < this.regionLoc.size(); i++) {
			
			//initialize the variables for this loop
			region = new Region(new Texture(this.regionLoc.get(i)));
			x = this.regionPos.get(i).get(0);
			y = this.regionPos.get(i).get(1);
			texWidth = this.regionPos.get(i).get(2);
			texHeight = this.regionPos.get(i).get(3);
			
			
			//set this loop's sprite's bounds & add it to the region array.
			region.setBounds(x, y, texWidth, texHeight);//texWidth, texHeight);
			region.setRotation(this.regionPos.get(i).get(4));
			this.regions.add(region);
			
//			//modify the variables to have the hitboxes be positioned correctly.
//			x *= (float) Gdx.graphics.getWidth() / width;
//			y = (float) Gdx.graphics.getHeight() - ((texHeight + y) * ((float) Gdx.graphics.getHeight() / height));
//			
//			texWidth *= (float) Gdx.graphics.getWidth() / width;
//			texHeight *= (float) Gdx.graphics.getHeight() / height;
//			
//			//set this loop's hitbox.
//			rect = new Rectangle(x, y, texWidth, texHeight);
//			this.regionHitboxes.add(rect);
		}
	}

	//this populates the region texture array with textures.
	private void populateBlossomTex() {
		
		List<String> blossomLoc = new ArrayList<String>();
		
		//loop through all files ("FileHandle"s) in the "regions" folder.
		for(FileHandle f : Gdx.files.internal("blossom/").list()) {
			blossomLoc.add(f.toString());
		}
		
		//Sort the list.
		Collections.sort(blossomLoc);
		
		//loops through those file handles and adds the textures at those locations to blossomTex.
		for(int i = 0; i < blossomLoc.size(); i++) {
			
//			System.out.println(blossomLoc.get(i));
			this.blossomTex.add(new Texture(blossomLoc.get(i)));
			
		}
		
	}

	//this adds a ton of blossom sprites.
	private void populateBlossoms() {
		
		Random r = new Random();
		int cellSize = 25;
		int texSize = 48;
		int gridWidth = (int) (Gdx.graphics.getWidth() / cellSize);
		int gridHeight = (int) (Gdx.graphics.getHeight() / cellSize);
		Blossom blossom;
		int xCoord;
		int yCoord;
		Vector3 hmm;
		
		for(int y = 0; y < gridHeight; y++) {
			
			for(int x = 0; x < gridWidth; x++) {
				
				xCoord = x*cellSize + r.nextInt(cellSize);
				yCoord = y*cellSize + r.nextInt(cellSize);
				
				hmm = guiPosOnWorld(xCoord+(texSize/2), yCoord+(texSize/2));
				
				if(hitboxCheck((int) hmm.x, (int) hmm.y)) {
					
					blossom = new Blossom(this.blossomTex);
					blossom.setBounds(xCoord, yCoord, texSize, texSize);
					blossom.setBloomLevel(2+r.nextInt(2));
					
					this.blossoms.add(blossom);
					
				}
				
			}
			
		}
		
		this.regionLoc = new ArrayList<String>();
		//loop through all files ("FileHandle"s) in the "regions" folder.
		for(FileHandle f : Gdx.files.internal("regions/").list()) {
			this.regionLoc.add(f.toString());
		}
		//Sort the list.
		Collections.sort(this.regionLoc);
	}
	
	//hitbox checking functions; they check for whether or not a point is inside of a certain hitbox (or all of them).
	private boolean hitboxCheck() {//checks all hitboxes for the mouse coordinates
		
		for(int i = 0; i < this.regionHitpolys.size(); i++) {
			
//			if(this.regionHitboxes.get(i).contains(Gdx.input.getX(), Gdx.input.getY())) {
				
				if(this.regionHitpolys.get(i).contains(Gdx.input.getX(), Gdx.input.getY())) {
					return true;
					
				}
				
//			}
			
		}
		
		return false;
	}
	
	private boolean hitboxCheck(int i) {//checks the [i]th hitbox for mouse coordinates
//		if(this.regionHitboxes.get(i).contains(Gdx.input.getX(), Gdx.input.getY())) {
			return this.regionHitpolys.get(i).contains(getX(mousePosOnWorld().x), getY(mousePosOnWorld().y));
//		}
//		return false;
	}
	
	private boolean hitboxCheck(int x, int y) {//checks all hitboxes for [x,y] coordinates.
		
//		x *= (float) Gdx.graphics.getWidth() / viewport.getWorldWidth();
//		y = (int) (Gdx.graphics.getHeight() - (y*((float) Gdx.graphics.getHeight() / height)));
		
		for(int i = 0; i < this.regionHitpolys.size(); i++) {

//			if(this.regionHitboxes.get(i).contains(x, y)) {
				
				if(this.regionHitpolys.get(i).contains(//x,y
						getX(x),
						getY(y)
						)) {
					
					return true;
					
				}
				
//			}
			
		}
		
		return false;
		
	}
	
	//translate the x from the window to the x in the world.
	private int getX(float x) {
		return (int) (x * ( Gdx.graphics.getWidth() / width ));
	}
	
	//translate the y from the window to the y in the world.
	private int getY(float y) {
		return (int) (Gdx.graphics.getHeight() - y * ( Gdx.graphics.getHeight() / height ));
	}
	
	//translates the mouse position on the window into the position where it appears that it is in the world.
	private Vector3 mousePosOnWorld() {
		return viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}
	
	//translates a position on the window into the position where it appears that it is in the world.
	private Vector3 guiPosOnWorld(int x, int y) {
		return camera.unproject(new Vector3(x, y, 0));
	}
	
	//refresh anything that needs to be after scaling the window.
	private void refreshScale() {
		
		//scale the coordinates.
		for(int i = 0; i < regionHitpolys.size(); i++) {
			
			regionHitpolys.get(i).setScale((float) Gdx.graphics.getWidth() / viewport.getWorldWidth(), (float) Gdx.graphics.getHeight() / viewport.getWorldHeight());
			
		}
		
		this.blossoms = new ArrayList<Blossom>();
		populateBlossoms();
		
		if(Gdx.graphics.getHeight() < Gdx.graphics.getWidth()) {
			font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());
		}
		else {
			font.getData().setScale(viewport.getWorldWidth() / Gdx.graphics.getWidth());
		}
		zTextLoc = guiPosOnWorld(20, 20);
		
	}
	
//	public int sakuraFront(float temp) {
//		
//		return e^( ( 9.5 * 10^3 ) * ( ( temp - 288.2 ) / ( 288.2 * temp ) ) );
//		
//	}

	@Override
	public void render() {
//		System.out.println(mousePosOnWorld().x + ", " + mousePosOnWorld().y);
//		Blossom blossom1 = new Blossom(this.blossomTex);
//		blossom1.setBounds(mousePosOnWorld().x, mousePosOnWorld().y, 20, 20);
//		blossom1.setBloomLevel(4);
//		this.blossoms.add(blossom1);
		
		
		//loop through all hitboxes and play a sound if a hitbox is hovered over
		for(int i = 0; i < this.regions.size(); i++) {
			//check if hitbox contains mouse
			if(hitboxCheck(i)) {
				
				if(this.regions.get(i).getMouseOver() == false) {
					this.sfxSelect.play(0.5f);
					this.regions.get(i).setMouseOver(true);
					
					if(i == 5 || i == 6) {
						this.regions.get(5).setMouseOver(true);
						this.regions.get(6).setMouseOver(true);
					}
				}
				
			}
			//if not, and if sprite's mouseOver is true, turn off the sprite's mouseover, with an exception for okinawa & kyushu, as okinawa is part of the kyushu region.
			else if(
					this.regions.get(i).getMouseOver() == true
					&& !(i == 5 && hitboxCheck(6))
					&& !(i == 6 && hitboxCheck(5))
					) {
				
				this.regions.get(i).setMouseOver(false);
				
			}
			
//			System.out.println(this.regions.get(i).getBoundingRectangle().contains(Gdx.input.getX(), Gdx.input.getY()));
		}
		
		
		// clear the screen with a dark blue colour. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		
//		shaperend.setProjectionMatrix(camera.combined);
		
		
		// begin a new batch and draw everything.
		batch.begin();
		
		batch.draw(bgImage, 0, 0);
		
		font.draw(batch, "Zoom level:"+zoom, zTextLoc.x, zTextLoc.y);
		
		for (Region region : regions) {
			region.draw(batch);
		}
		
		for (Blossom blossom : blossoms) {
			blossom.draw(batch);
		}
		
		batch.end();
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		//TODO: check to see if anything else needs to be disposed properly/differently.
		for(Region region : regions) {
			region.getTexture().dispose();
		}
		for(Texture t : blossomTex) {
			t.dispose();
		}
		font.dispose();
		sfxSelect.dispose();
		batch.dispose();
	}
	
	//Resize the viewport when the window is resized.
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		refreshScale();
	}
	
	@Override
	public void pause() {
		
	}
	
	@Override
	public void resume() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println(camera.zoom);
		if(button == Input.Buttons.LEFT) {
//			System.out.println(viewport.getWorldHeight());
//			viewport.setWorldSize(viewport.getWorldWidth() -100, viewport.getWorldHeight() -100);
//			camera.zoom += 0.5;
			
			for(int i = 0; i < regions.size(); i++) {
				
				if(hitboxCheck(i)) {
					
					camera.position.x = regions.get(i).getMidX();
					camera.position.y = regions.get(i).getMidY();
					
					if( (regions.get(i).getWidth()/viewport.getWorldWidth()) > (regions.get(i).getHeight()/viewport.getWorldHeight()) ) {
						camera.zoom = (regions.get(i).getWidth() + 20)/viewport.getWorldWidth();
					}
					else {
						camera.zoom = (regions.get(i).getHeight() + 20)/viewport.getWorldHeight();
					}
					
					break;
					
				}
				
			}
			
			viewport.apply();
			refreshScale();
			
			return true;
		}
		
		if(button == Input.Buttons.RIGHT) {
//			System.out.println(viewport.getWorldHeight());
//			viewport.setWorldSize(viewport.getWorldWidth() +100, viewport.getWorldHeight() +100);
//			camera.zoom -= 0.1;
			
			camera.position.x = viewport.getWorldWidth() / 2;
			camera.position.y = viewport.getWorldHeight() / 2;
			camera.zoom = 1;
			
			viewport.apply();
			refreshScale();
			
			return true;
		}
		
		zoom = camera.zoom;
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
}
