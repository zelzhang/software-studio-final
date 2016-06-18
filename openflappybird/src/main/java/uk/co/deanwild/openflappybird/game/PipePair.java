package uk.co.deanwild.openflappybird.game;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;


public class PipePair {


	float PIPE_WIDTH = MainActivity.CAMERA_WIDTH * 0.18f;
	float PIPE_HEIGHT = PIPE_WIDTH * 0.46f;

	// upper pipe
	private static TextureRegion mUpperPipeTexture;
	private static TextureRegion mUpperPipeSectionTexture;

	//lower pipe
	private static TextureRegion mLowerPipeTexture;
	private static TextureRegion mLowerPipeSectionTexture;

	public static void onCreateResources(SimpleBaseGameActivity activity){

		// upper pipe
		BitmapTextureAtlas upperPipeSectionTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 120, 1, TextureOptions.BILINEAR);
		mUpperPipeSectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(upperPipeSectionTextureAtlas, activity, "pipesectionupper.png", 0, 0);
		upperPipeSectionTextureAtlas.load();


		// lower pipe
		BitmapTextureAtlas lowerPipeSectionTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 120, 1, TextureOptions.BILINEAR);
		mLowerPipeSectionTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lowerPipeSectionTextureAtlas, activity, "pipesectionlower.png", 0, 0);
		lowerPipeSectionTextureAtlas.load();
	}

	private float mOpeningHeight;
	private float mCurrentPosition;

	private VertexBufferObjectManager mVertexBufferObjectManager;
	private Scene mScene;

	private Sprite mUpperPipeSection;
	private Sprite mLowerPipeSection;


	private static final float PIPE_Y_OFFSET = MainActivity.CAMERA_WIDTH + 200; // make sure they always spawn way off screen
	
	public PipePair(int mOpeningHeight, int space,
			VertexBufferObjectManager mVertexBufferObjectManager, Scene mScene) {
		super();
		this.mOpeningHeight = mOpeningHeight;
		this.mVertexBufferObjectManager = mVertexBufferObjectManager;
		this.mScene = mScene;

		// lower pipe
		mUpperPipeSection = new Sprite(PIPE_Y_OFFSET + 3, 0, 82, mOpeningHeight-space/2, mUpperPipeSectionTexture, mVertexBufferObjectManager);
		mUpperPipeSection.setZIndex(1);
		mScene.attachChild(mUpperPipeSection);

        Log.d("a", "upper len = " + (mOpeningHeight-space/2));

		//upper pipe

		mLowerPipeSection = new Sprite(PIPE_Y_OFFSET + 3, MainActivity.CAMERA_HEIGHT, 82, MainActivity.CAMERA_HEIGHT-mOpeningHeight+space/2, mLowerPipeSectionTexture, mVertexBufferObjectManager);
		mLowerPipeSection.setZIndex(1);
		mScene.attachChild(mLowerPipeSection);

        Log.d("a", "lower len = " + (mOpeningHeight+space/2));

		mScene.sortChildren();

	}


	public void move(float offset){
		mUpperPipeSection.setPosition(mUpperPipeSection.getX() - offset, mUpperPipeSection.getY());

		mLowerPipeSection.setPosition(mLowerPipeSection.getX() - offset, mLowerPipeSection.getY());

	}

	public boolean isOnScreen(){

		if(mUpperPipeSection.getX() < -200){
			return false;
		}

		return true;
	}
	
	boolean counted = false;
	
	public boolean isCleared(float birdXOffset){
		
		if(!counted){
			if(mUpperPipeSection.getX()<(birdXOffset - (Bird.BIRD_WIDTH/2))){
				counted = true; // make sure we don't count this again
				return true;
			}
		}		
		
		return false;
	
	}


	public void destroy(){
		mScene.detachChild(mUpperPipeSection);
		mScene.detachChild(mLowerPipeSection);

	}

	public boolean collidesWith(Sprite bird){

		if(mUpperPipeSection.collidesWith(bird)) return true;
		if(mLowerPipeSection.collidesWith(bird)) return true;
		return false;

	}

	private boolean collidesWithCircle(float centerX, float centerY,
			float centerX1, float centerY1, float radius) {

		// pythagorus
		double a = centerX - centerX1;
		double b = centerY - centerY1;
		double c = (a * a) + (b * b);
		double distance = Math.sqrt(c);

		//radius*2 - because its the center of both circles
		if (distance <= radius*2){  
			return true;
		}
		else {
			return false;
		}
	}





}
