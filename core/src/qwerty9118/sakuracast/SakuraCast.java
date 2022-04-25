package qwerty9118.sakuracast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.ShortArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SakuraCast extends Game implements InputProcessor {//ApplicationAdapter implements InputProcessor {
	public InputMultiplexer inputM;
	private Texture bgImage;
	public BitmapFont font;
	private Sound sfxSelect;
	public SpriteBatch batch;
	public Viewport viewport;
	public Viewport viewportGui;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGui;
	private Settings settings;
	private SettingsButton settingsBtn;
	private List<String> regionLoc;
	private List<String> regionLocS;
	private List<Region> regions;
	private List<Polygon> regionHitpolys;
	private List<List<Integer>> regionPos;
	private List<Blossom> blossoms;
	private List<Texture> blossomTex;
	public static List<TestSite> testSites;
	private List<List<Integer>> testSiteTriPoints;
	private List<Polygon> testSiteTris;
	private TestSite templateSite;
	private Texture jmaTex;
	private Texture jmaSelTex;
	private float width;
	private float height;
	public float zoom;
	private ShapeRenderer shaperend;
	public boolean tsSel;

	@Override
	public void create() {
		
		//initialise the input multiplexer, to allow handling of two input processors at once - the SakuraCast one, and the Settings one.
		inputM = new InputMultiplexer();
		
		// load the background image & set internal size
		bgImage = new Texture(Gdx.files.internal("watrMap.png"));
		width = bgImage.getWidth();
		height = bgImage.getHeight();
		
		// load the rest of the images for the program
		jmaTex = new Texture(Gdx.files.internal("jma.png"));
		jmaSelTex = new Texture(Gdx.files.internal("jmaS.png"));
		regionLoc = new ArrayList<String>();
		regionLocS = new ArrayList<String>();
		blossomTex = new ArrayList<Texture>();
		populateRegionTex();
		populateBlossomTex();

		// load the font
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(2f);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		//set stored zoom level
		zoom = 1f;
		
		// load any sound effects
		sfxSelect = Gdx.audio.newSound(Gdx.files.internal("select.wav"));

		// create the cameras, the viewports, and the SpriteBatch
		// (there's multiple cameras & viewports so that I can do the settings menu GUI)
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		cameraGui = new OrthographicCamera();
		cameraGui.setToOrtho(false, width, height);
		viewport = new FitViewport(width, height, camera);
		viewportGui = new FitViewport(width, height, cameraGui);
		batch = new SpriteBatch();

		// create & populate the regions array
		populateRegions();
		
		//create & populate the testSites array (BEFORE blossoms, as blossoms take values from test sites)
		populateTestSites();
		
		//create & populate the blossoms array
		populateBlossoms();
		
		shaperend = new ShapeRenderer();
		
		//set up the settings screen
		settings = new Settings(this);
		this.setScreen(settings);
		settings.hide();
		//& button, along with it's textures
		List<Texture> settingsBtnTex = new ArrayList<Texture>();
		settingsBtnTex.add(new Texture(Gdx.files.internal("settings.png")));
		settingsBtnTex.add(new Texture(Gdx.files.internal("settingsH.png")));
		settingsBtnTex.add(new Texture(Gdx.files.internal("settingsC.png")));
		settingsBtn = new SettingsButton(settingsBtnTex);
		settingsBtn.setBounds(0, (height/20)*19, width/20, height/20);
		
		//add this to inputM
		inputM.addProcessor(this);
		
		//set the input processor to the input multiplexer.
		Gdx.input.setInputProcessor(inputM);
		
//		System.out.println(sakuraFront(635));
		
	}

	//this populates the region texture array with textures.
	private void populateRegionTex() {
		
		//loop through all files ("FileHandle"s) in the "regions" folder.
		for(FileHandle f : Gdx.files.internal("regions/").list()) {
			//if it's a 
			if(f.toString().endsWith("S.png")) {
				this.regionLocS.add(f.toString());
			}
			else {
				this.regionLoc.add(f.toString());
			}
		}
		
		//Sort the lists.
		Collections.sort(this.regionLoc);
		Collections.sort(this.regionLocS);
		
	}

	//this populates the region array with regions.
	private void populateRegions() {
		
		//initialise the relevant arrays.
		regions = new ArrayList<Region>();
		regionHitpolys = new ArrayList<Polygon>();
		regionPos = new ArrayList<List<Integer>>();
		
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
		
		//add each region's hitpolygon individually.751,460
		//each polygon is made from a list of x-coordinate, y-coordinate pairs.
		regionHitpolys.add(new Polygon( new float[]{755,373, 770,386, 797,411, 751,460, 717,494, 769,580, 644,604, 600,533, 572,549, 550,490} ));//Chubu
		regionHitpolys.add(new Polygon( new float[]{427,536, 501,528, 500,587, 332,641,301,595} ));//Chugoku
		regionHitpolys.add(new Polygon( new float[]{906,46,1124,152,993,227,776,250} ));//Hokkaido
		regionHitpolys.add(new Polygon( new float[]{501,528, 543,523, 572,549, 600,533, 644,604, 667,639,533,673, 524,618, 500,587} ));//Kansai
		regionHitpolys.add(new Polygon( new float[]{751,460, 854,483, 857,544,798,590, 769,580, 717,494} ));//Kanto
		regionHitpolys.add(new Polygon( new float[]{310,624,372,679,332,775,261,764,232,651} ));//Kyushu
		regionHitpolys.add(new Polygon( new float[]{184,972,141,1039,120,1002} ));//Okinawa
		regionHitpolys.add(new Polygon( new float[]{501,599, 525,636,391,701,369,651} ));//Shikoku
		regionHitpolys.add(new Polygon( new float[]{792,258,938,243, 921,353, 854,483, 751,460, 797,411, 771,382, 770,386} ));//Tohoku
		
		
		//loop once for each location in regionLoc
		for(int i = 0; i < this.regionLoc.size(); i++) {
			
			//initialize the variables for this loop
			region = new Region(new Texture(this.regionLoc.get(i)), new Texture(this.regionLocS.get(i)));
			x = this.regionPos.get(i).get(0);
			y = this.regionPos.get(i).get(1);
			texWidth = this.regionPos.get(i).get(2);
			texHeight = this.regionPos.get(i).get(3);
			
			
			//set this loop's sprite's bounds & add it to the region array.
			region.setBounds(x, y, texWidth, texHeight);//texWidth, texHeight);
			region.setRotation(this.regionPos.get(i).get(4));
			this.regions.add(region);
			
		}
		
	}

	//this populates the blossom texture array with textures.
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

	//this generates a ton of blossom sprites in the region hitpolygons.
	public void populateBlossoms() {
		
		//create and/or reset blossoms
		this.blossoms = new ArrayList<Blossom>();
		
		Random r = new Random();
		int cellSize = 16;
		int texSize = (int) (32 * zoom);
		int gridWidth = (int) (width / cellSize)+1;
		int gridHeight = (int) (height / cellSize)+1;
		Blossom blossom;
		int xCoord;
		int yCoord;
		Vector3 pos;
		
		//this creates a bunch of triangles that are made from the midpoints
		//of all the test sites so that the blossoms can check the corners of
		//the triangles and get a gradient out of them.
		calcSiteTriForSmooth();//uncomment when closestTestSiteSmooth or closestTestSiteSmooth2 are being used.
		
		//loop through a grid to evenly distribute the blossoms
		for(int y = 0; y < gridHeight; y++) {
			
			for(int x = 0; x < gridWidth; x++) {
				
				//get the coordinates from the grid cell we are currently on (corrected to actual coordinates) plus
				//a random value to make everything look nicer.
				xCoord = (int) ( ( ( x / width ) * Gdx.graphics.getWidth() ) * cellSize + r.nextInt(cellSize) );
				yCoord = (int) ( ( ( y / height ) * Gdx.graphics.getHeight() ) * cellSize + r.nextInt(cellSize) );
				
				pos = guiPosOnWorld(xCoord, yCoord);
				
				//check if the coordinates are inside any of the hitpolygons of the regions.
				if(hitboxCheck((int) pos.x, (int) pos.y)) {
					
//					pos = guiPosOnWorld(xCoord-(texSize/2), yCoord-(texSize/2));
					
					//create a new blossom & pass in it's textures and closest point/s
//					blossom = new Blossom(this.blossomTex, closestTestSite(pos.x-(texSize/2), pos.y-(texSize/2)));
//					blossom = new Blossom(this.blossomTex, closestTestSiteSmooth(pos.x-(texSize/2), pos.y-(texSize/2)));
					blossom = new Blossom(this.blossomTex, closestTestSiteSmooth2(pos.x-(texSize/2), pos.y-(texSize/2)));
					
					blossom.setBounds(pos.x-(texSize/2), pos.y-(texSize/2), texSize, texSize);
					
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
	
	//returns the index of the closest test site to the given coordinates.
	private int closestTestSite(float x, float y) {
		
		int closest = 0;
		double dist = 500;
		double tempDist;
		
		for(int i = 0; i < testSites.size(); i++) {
			
			tempDist = testSites.get(i).pos().dst(x, y);
			
			if(tempDist < dist) {
				closest = i;
				dist = tempDist;
			}
			
		}
		
		return closest;
		
	}
	
	//returns the indexes of the three closest test sites to the given coordinates.
	private ArrayList<Double> closestTestSiteSmooth(float x, float y) {
		
		double closest = 0;
		double closest2 = 0;
		double closest3 = 0;
		double dist = 500;
		double dist2 = 500;
		double dist3 = 500;
		double tempDist;
		
		//loop through all test sites and store the three closest values & their indexes.
		for(int i = 0; i < testSites.size(); i++) {
			
			tempDist = testSites.get(i).pos().dst(x, y);
			
			if(tempDist < dist) {
				closest3 = closest2;
				closest2 = closest;
				closest = i;
				
				dist3 = dist2;
				dist2 = dist;
				dist = tempDist;
			}
			else if(tempDist < dist2) {
				closest3 = closest2;
				closest2 = i;
				
				dist3 = dist2;
				dist2 = tempDist;
			}
			else if(tempDist < dist3) {
				closest3 = i;
				
				dist3 = tempDist;
			}
			
		}
		
		//add all those stored values to an array.
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(closest);
		list.add(dist);
		list.add(closest2);
		list.add(dist2);
		list.add(closest3);
		list.add(dist3);
		
		return list;
		
	}
	
	//returns the indexes of the test sites that make up the points of the triangle that the given coordinates are in,
	//unless the given coordinates are not in any of the triangles, in which case it falls back on closestTestSiteSmooth.
	private ArrayList<Double> closestTestSiteSmooth2(float x, float y) {
		
		for(int i = 0; i < testSiteTris.size(); i++) {
			
			if(testSiteTris.get(i).contains(x, y)) {
				
				ArrayList<Double> list = new ArrayList<Double>();
				list.add(testSiteTriPoints.get(i).get(0).doubleValue());
				list.add((double) testSites.get( testSiteTriPoints.get(i).get(0) ).pos().dst(x, y));
				list.add(testSiteTriPoints.get(i).get(1).doubleValue());
				list.add((double) testSites.get( testSiteTriPoints.get(i).get(1) ).pos().dst(x, y));
				list.add(testSiteTriPoints.get(i).get(2).doubleValue());
				list.add((double) testSites.get( testSiteTriPoints.get(i).get(2) ).pos().dst(x, y));
				
				return list;
				
			}
			
		}
		
//		float dist = 500;
//		int index = 0;
//		for(int i = 0; i < testSites.size(); i++) {
//			if(testSites.get(i).pos().dst(x,y) < dist) {
//				dist = testSites.get(i).pos().dst(x,y);
//				index = i;
//			}
//		}
//		
//		ArrayList<Double> list = new ArrayList<Double>();
//		list.add(testSiteTriPoints.get(index).get(0).doubleValue());
//		list.add((double) testSites.get( testSiteTriPoints.get(index).get(0) ).pos().dst(x, y));
//		list.add(testSiteTriPoints.get(index).get(1).doubleValue());
//		list.add((double) testSites.get( testSiteTriPoints.get(index).get(1) ).pos().dst(x, y));
//		list.add(testSiteTriPoints.get(index).get(2).doubleValue());
//		list.add((double) testSites.get( testSiteTriPoints.get(index).get(2) ).pos().dst(x, y));
//		
//		return list;
		
		return closestTestSiteSmooth(x, y);
		
	}
	
	private void calcSiteTriForSmooth() {
		
		System.out.println("Triangulating test sites...");
		
		testSiteTriPoints = new ArrayList<List<Integer>>();
		testSiteTris = new ArrayList<Polygon>();
		
		List<Float> coords = new ArrayList<Float>();
		DelaunayTriangulator delTri = new DelaunayTriangulator();
		
		for(TestSite ts : testSites) {
			
			coords.add(ts.posX());
			coords.add(ts.posY());
			
		}
		
		float[] coords2 = new float[coords.size()];
		for(int i = 0; i < coords.size(); i++) {
			coords2[i] = coords.get(i);
		}
		
		ShortArray triGroups = new ShortArray();
		triGroups = delTri.computeTriangles(coords2, false);
		
		short[] triGroups2 = triGroups.toArray();
		
		for(int i = 0; i < triGroups2.length; i+=3) {
			testSiteTriPoints.add(Arrays.asList((int) triGroups2[i], (int) triGroups2[i+1], (int) triGroups2[i+2]));
			testSiteTris.add(new Polygon(new float[] {
					testSites.get(triGroups2[i]).posX(),
					testSites.get(triGroups2[i]).posY(),
					testSites.get(triGroups2[i+1]).posX(),
					testSites.get(triGroups2[i+1]).posY(),
					testSites.get(triGroups2[i+2]).posX(),
					testSites.get(triGroups2[i+2]).posY()
			}));
//			System.out.println(i);
		}
		
//		for(Polygon p : regionHitpolys) {
//			
//			for(int i = 0; i < p.getVertices().length; i+=2) {
//				
//				if(hitboxCheckTestTris(p.getVertices()[i], height - p.getVertices()[i+1])) {
//					
//					TestSite ts = new TestSite();
//					ts.set(templateSite);//make the new object identical to the template object that is stored globally.
//					ts.setCenter(p.getVertices()[i], height - p.getVertices()[i+1]);//pass in the coordinates to the new object.
////					ts.visible(false);
//					
//					float dist = 500;
//					int index = 0;
//					
//					for(int j = 0; j < testSites.size(); j++) {
//						if(testSites.get(j).pos().dst(ts.pos()) < dist) {
//							dist = testSites.get(j).pos().dst(ts.pos());
//							index = j;
//						}
//					}
//					
//					ts.setDs(testSites.get(index).getDs());
//					ts.setBD(testSites.get(index).getBD());
//					
//					testSites.add(ts);
//					
////					System.out.println(i);
//					
//				}
//				
//			}
//			
//		}
		
	}
	
	private void populateTestSites() {
		
		testSites = new ArrayList<TestSite>();
		
		int texSize = 8;//(int) (16 * zoom);
		templateSite = new TestSite(jmaTex, jmaSelTex);
		templateSite.setSize(texSize, texSize);
		
		//this will be painful to look at, but i'm rapidly running out of time and i need to do this fast, so i'm taking the copy-paste-code route for these coordinates.
		// (used as template for replace function in notepad++) => );\npopulateTestSite(
		//create & add all test sites to the array:
		
		populateTestSite(328,1035);
		populateTestSite(135,1015);
		populateTestSite(134,1017);
		populateTestSite(006,1081);
		populateTestSite(200,607);
		populateTestSite(201,675);
		populateTestSite(254,686);
		populateTestSite(278,665);
		populateTestSite(286,648);
		populateTestSite(313,625);
		populateTestSite(341,617);
		populateTestSite(342,662);
		populateTestSite(304,679);
		populateTestSite(287,744);
		populateTestSite(334,731);
		populateTestSite(408,683);
		populateTestSite(402,662);
		populateTestSite(418,630);
		populateTestSite(455,647);
		populateTestSite(505,620);
		populateTestSite(395,606);
		populateTestSite(383,575);
		populateTestSite(423,552);
		populateTestSite(443,552);
		populateTestSite(444,506);
		populateTestSite(493,549);
		populateTestSite(484,569);
		populateTestSite(481,608);
		populateTestSite(479,591);
		populateTestSite(484,569);
		populateTestSite(529,547);
		populateTestSite(554,555);
		populateTestSite(519,582);
		populateTestSite(532,598);
		populateTestSite(522,610);
		populateTestSite(543,611);
		populateTestSite(555,592);
		populateTestSite(566,594);
		populateTestSite(577,591);
		populateTestSite(564,581);
		populateTestSite(575,649);
		populateTestSite(602,619);
		populateTestSite(616,588);
		populateTestSite(598,563);
		populateTestSite(590,547);
		populateTestSite(600,525);
		populateTestSite(630,556);
		populateTestSite(640,564);
		populateTestSite(677,591);
		populateTestSite(762,688);
		populateTestSite(622,498);
		populateTestSite(652,490);
		populateTestSite(640,453);
		populateTestSite(654,521);
		populateTestSite(678,551);
		populateTestSite(697,524);
		populateTestSite(692,517);
		populateTestSite(702,498);
		populateTestSite(703,474);
		populateTestSite(712,421);
		populateTestSite(747,431);
		populateTestSite(795,452);
		populateTestSite(749,500);
		populateTestSite(731,539);
		populateTestSite(721,577);
		populateTestSite(744,569);
		populateTestSite(756,572);
		populateTestSite(759,523);
		populateTestSite(769,517);
		populateTestSite(794,504);
		populateTestSite(799,540);
		populateTestSite(789,551);
		populateTestSite(815,546);
		populateTestSite(822,565);
		populateTestSite(799,576);
		populateTestSite(850,537);
		populateTestSite(834,504);
		populateTestSite(857,475);
		populateTestSite(809,475);
		populateTestSite(832,448);
		populateTestSite(825,414);
		populateTestSite(862,408);
		populateTestSite(880,401);
		populateTestSite(902,371);
		populateTestSite(913,342);
		populateTestSite(866,336);
		populateTestSite(825,379);
		populateTestSite(794,378);
		populateTestSite(810,335);
		populateTestSite(801,294);
		populateTestSite(844,282);
		populateTestSite(883,293);
		populateTestSite(876,260);
		populateTestSite(848,232);
		populateTestSite(859,205);
		populateTestSite(899,182);
		populateTestSite(883,171);
		populateTestSite(936,135);
		populateTestSite(897,050);
		populateTestSite(980,177);
		populateTestSite(1045,172);
		populateTestSite(1038,122);
		
	}
	
	//populate one test site
	private void populateTestSite(int x, int y) {
		
		//correct the friggin height value
		y = (int) (height - y);
		
		//generate a new TestSite object to avoid problems with passing by reference.
		TestSite ts = new TestSite();
		ts.set(templateSite);//make the new object identical to the template object that is stored globally.
		ts.setCenter(x,y);//pass in the coordinates to the new object.
//		ts.setTemp(10+new Random().nextDouble()*10);//give it a random temperature.
		
		//values are from Sendai from the pdf that I'm using as a main source.
		//this is temporary.
		ts.setDs(LocalDate.ofYearDay(2022, 15 + new Random().nextInt(26)));//LocalDate.of(2022, 2, 12));
		ts.setBD(LocalDate.ofYearDay(2022, 88 + new Random().nextInt(32)));//LocalDate.of(2022, 4, 29));
		testSites.add(ts);//add it to the array.
		
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
			
			if(this.regionHitpolys.get(i).contains(//x,y
					getX(x),
					getY(y)
					)) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	private boolean hitboxCheckTestTris(float x, float y) {//checks all hitboxes for [x,y] world coordinates.
		
//		System.out.println(f+","+g);
		
		System.out.println(testSiteTris.size());
		
		for(int i = 0; i < this.testSiteTris.size(); i++) {
			
			if(this.testSiteTris.get(i).contains(//x,y
					getX(x),
					getY(y)
					)) {
				
//				System.out.println(i);
				
				return false;
				
			}
			
		}
		
		return true;
		
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
	
	//translates the mouse position on the window into the position where it appears that it is in the world.
	private Vector3 mousePosOnGui() {
		return viewportGui.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
	}
	
	//translates a position on the window into the position where it appears that it is in the world.
	public Vector3 guiPosOnWorld(float x, float y) {
		return camera.unproject(new Vector3(x, y, 0));
	}
	
	public boolean testSiteCheckSelected() {
		
		for(TestSite ts : testSites) {
			
			if(ts.selected()) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	//refresh anything that needs to be after scaling the window.
	private void refreshScale() {
		
		//scale the coordinates.
		for(int i = 0; i < regionHitpolys.size(); i++) {
			
			regionHitpolys.get(i).setScale((float) Gdx.graphics.getWidth() / viewport.getWorldWidth(), (float) Gdx.graphics.getHeight() / viewport.getWorldHeight());
			
		}
		
		//this bit is pretty much the same as the initial creation of test sites and blossoms.
//		this.testSites = new ArrayList<TestSite>();
//		populateTestSites();
		
		populateBlossoms();
		
	}

	@Override
	public void render() {
		
		//loop through all hitboxes and play a sound if a hitbox is hovered over
		for(int i = 0; i < this.regions.size(); i++) {
			//check if hitbox contains mouse
			if(hitboxCheck(i) && !this.regions.get(i).getZoomedInto()) {
				
				if(!this.regions.get(i).getMouseOver()) {
					this.sfxSelect.play(0.1f);
					this.regions.get(i).setMouseOver(true);
					
					if(i == 5 || i == 6) {
						this.regions.get(5).setMouseOver(true);
						this.regions.get(6).setMouseOver(true);
					}
				}
				
			}
			//if not, and if sprite's mouseOver is true, turn off the sprite's mouseover, with an exception for okinawa & kyushu, as okinawa is part of the kyushu region.
			else if(
					this.regions.get(i).getMouseOver()
					&& !(i == 5 && hitboxCheck(6))
					&& !(i == 6 && hitboxCheck(5))
					) {
				
				this.regions.get(i).setMouseOver(false);
				
			}
			
		}
		
		//check if mouse is over/has clicked the settings button, and does a similar thing to the region hitboxes.
		if(settingsBtn.getBoundingRectangle().contains(mousePosOnGui().x, mousePosOnGui().y)) {
			
			if(!settingsBtn.getMouseOver()) {
				
				this.sfxSelect.play(0.1f);
				settingsBtn.setMouseOver(true);
				
			}
			
			if(Gdx.input.isTouched()) {
				
				if(!settingsBtn.getMouseDown()) {
					
					this.sfxSelect.play(0.1f);
					settingsBtn.setMouseDown(true);
					
				}
				
			}
			else if(settingsBtn.getMouseDown()) {
				
				settingsBtn.setMouseDown(false);
				
			}
			
		}
		else if(settingsBtn.getMouseOver()) {
			
			settingsBtn.setMouseOver(false);
			settingsBtn.setMouseDown(false);
			
		}
		
		
		// clear the screen with a dark blue colour. The
		// arguments to clear are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		ScreenUtils.clear(0, 0, 0.2f, 1);
		ScreenUtils.clear(Color.valueOf("243e6e"));

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);
		viewport.apply();
		
		
		// begin a new batch and draw everything.
		batch.begin();
		
		batch.draw(bgImage, 0, 0);
		
		for (Region region : regions) {
			region.draw(batch);
		}
		
		for (Blossom blossom : blossoms) {
			blossom.draw(batch);
		}
		
		for (TestSite ts : testSites) {
//			if(ts.isVisible()) {
				ts.draw(batch);
//			}
		}
		
		//set up stuff for the settings rendering
		batch.setProjectionMatrix(cameraGui.combined);
		viewportGui.apply(true);
		
		//render the settings button
		settingsBtn.draw(batch);
		
		//call for the settings to render themselves
		settings.render(Gdx.graphics.getDeltaTime());

		//if the settings are visible, render them.
		//(settings.render handles itself)
		if(settings.visible()) {
			
			font.draw(batch, "Zoom level: "+zoom, width/40, (height/40) * 37);
			font.draw(batch, "Date: "+Settings.date.toString(), width/40, (height/40) * 36);
			font.draw(batch, "Cursor position: ["+(int) mousePosOnWorld().x+", "+(int) mousePosOnWorld().y+"]", width/40, (height/40) * 35);
			
		}
		
		batch.end();
		
		
		shaperend.setAutoShapeType(true);
		shaperend.begin();
		
		java.awt.Color clr;
		
		shaperend.setProjectionMatrix(camera.combined);
		
		if(settings.tris) {
			
			for(int i = 0; i < testSiteTris.size(); i++) {
				
				clr = java.awt.Color.getHSBColor((float) i/testSiteTris.size(), 1, 1);
				
				shaperend.setColor((float) clr.getRed()/255, (float) clr.getGreen()/255, (float) clr.getBlue()/255, 1f);
				
				shaperend.polygon(testSiteTris.get(i).getVertices());
				
			}
			
		}
		
		shaperend.end();
		
		
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		//TODO: check to see if anything else needs to be disposed properly/differently.
		
		for(Region region : regions) {
			region.getTexture().dispose();
			region.getHoverTexture().dispose();
		}
		for(Texture t : blossomTex) {
			t.dispose();
		}
		for(TestSite ts : testSites) {
			ts.getTexture().dispose();
		}
		settingsBtn.dispose();
		font.dispose();
		sfxSelect.dispose();
		settings.dispose();
		batch.dispose();
	}
	
	//Resize the viewport when the window is resized.
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		viewportGui.update(width, height);
		settings.resize(width, height);
		refreshScale();
	}
	
	@Override
	public void pause() {
		settings.pause();
	}
	
	@Override
	public void resume() {
		settings.resume();
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
		
		if(button == Input.Buttons.LEFT) {
//			System.out.println(viewport.getWorldHeight());
//			viewport.setWorldSize(viewport.getWorldWidth() -100, viewport.getWorldHeight() -100);
//			camera.zoom += 0.5;
			
			boolean overSettings = false;
			
			if(settingsBtn.getBoundingRectangle().contains(mousePosOnGui().x, mousePosOnGui().y)) {
				
				if(settings.visible()) {
					settings.hide();
				}
				else {
					settings.show();
				}
				
				overSettings = true;
				
			}
			
			if(/*!testSiteCheckSelected() &&*/ zoom != 1 && !overSettings) {
				
				for(int i = 0; i < testSites.size(); i++) {
					
					if(testSites.get(i).getBoundingRectangle().contains(mousePosOnWorld().x, mousePosOnWorld().y)) {
						
						testSites.get(settings.getSelSite()).select(false);
						
						testSites.get(i).select(true);
						tsSel = true;
						settings.selectedSite(i);
						
					}
					
				}
				
			}
			
			if(!tsSel && !overSettings) {
				
				for(int i = 0; i < regions.size(); i++) {
					
					if(hitboxCheck(i) && !this.regions.get(i).getZoomedInto()) {
						
						camera.position.x = regions.get(i).getMidX();
						camera.position.y = regions.get(i).getMidY();
						
						if( (regions.get(i).getWidth()/viewport.getWorldWidth()) > (regions.get(i).getHeight()/viewport.getWorldHeight()) ) {
							camera.zoom = (regions.get(i).getWidth() + 20)/viewport.getWorldWidth();
						}
						else {
							camera.zoom = (regions.get(i).getHeight() + 20)/viewport.getWorldHeight();
						}
						
						zoom = camera.zoom;
						
						//tell the regions they aren't zoomed into.
						for(Region r : regions) {
							r.setZoomedInto(false);
						}
						
						//tell the current region that actually it is zoomed into.
						regions.get(i).setZoomedInto(true);
						
						//special magic that lets everything render correctly.
						viewport.apply();
						viewportGui.apply();
						refreshScale();
						
						break;
						
					}
					
				}
				
			}
			
			return true;
			
		}
		
		if(button == Input.Buttons.RIGHT) {
			
			boolean tsDesel = false;
			
			//check for selected test sites & deselect them
			for(TestSite ts : testSites) {
				
				if(ts.selected()) {
					ts.select(false);
					tsDesel = true;
				}
				
				settings.siteDesel();
				
				tsSel = false;
				
			}
			
			if(zoom != 1 && !tsDesel) {
				
				camera.position.x = viewport.getWorldWidth() / 2;
				camera.position.y = viewport.getWorldHeight() / 2;
				camera.zoom = 1;
				zoom = 1;
				
				//tell the regions they aren't zoomed into.
				for(Region r : regions) {
					r.setZoomedInto(false);
				}
				
				//special magic that lets everything render correctly.
				viewport.apply();
				viewportGui.apply();
				refreshScale();
				
				return true;
				
			}
			
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
