package uk.co.deanwild.openflappybird.game;

import android.util.Log;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;

public class SceneManager {

	private SimpleBaseGameActivity mContext;
	private ResourceManager mResourceManager;
	private ParallaxBackground mParallaxBackground;

	// text objects
	Text mScoreText;
	Text mGetReadyText;
	Sprite mInstructionsSprite;
	Text mCopyText;
	Text mOverText;

	Bird mBird;

	public SceneManager(SimpleBaseGameActivity context, ResourceManager resourceManager, ParallaxBackground parallaxBackground){
		this.mContext = context;
		this.mResourceManager = resourceManager;
		this.mParallaxBackground = parallaxBackground;
	}

	public Scene createScene(){

		float w = MainActivity.CAMERA_WIDTH;
		float h = MainActivity.CAMERA_HEIGHT;

		Scene mScene = new Scene();
		VertexBufferObjectManager vbo = mContext.getVertexBufferObjectManager();

		Sprite backgroundSprite = new Sprite(0, 0 , mResourceManager.mBackgroundTextureRegion, vbo);
		mParallaxBackground.attachParallaxEntity(new ParallaxEntity(1, backgroundSprite));

		mScene.setBackground(mParallaxBackground);
		mScene.setBackgroundEnabled(true);


		// bird		
		float birdStartXOffset = w/3 - (Bird.BIRD_WIDTH / 4);
		float birdYOffset = h/2 - (Bird.BIRD_HEIGHT / 4);
		mBird = new Bird(birdStartXOffset, birdYOffset, mContext.getVertexBufferObjectManager(), mScene);


		//score
		mScoreText = new Text(w/2, h/6, mResourceManager.mScoreFont, "        ", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
		mScoreText.setZIndex(3);
		mScene.attachChild(mScoreText);
		Log.d("abc", "w = " + MainActivity.CAMERA_WIDTH + "; h = "+MainActivity.CAMERA_HEIGHT);
		// get ready text
		mGetReadyText = new Text(w/2, h*2/6, mResourceManager.mGetReadyFont, "Get Ready!", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
		mGetReadyText.setZIndex(3);
		mScene.attachChild(mGetReadyText);
		centerText(mGetReadyText);

		// instructions image
		mInstructionsSprite = new Sprite(w, h, 200, 172, mResourceManager.mInstructionsTexture, mContext.getVertexBufferObjectManager());
		mInstructionsSprite.setZIndex(3);
		mScene.attachChild(mInstructionsSprite);
		centerSprite(mInstructionsSprite);
		mInstructionsSprite.setY(mInstructionsSprite.getY() + 20);

		// copy text
		mCopyText = new Text(0, 750, mResourceManager.mCopyFont, "(c) Dean Wild 2014", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
		mCopyText.setZIndex(3);
		mScene.attachChild(mCopyText);
		centerText(mCopyText);


		// you suck text		
		mOverText = new Text(w/2, MainActivity.CAMERA_HEIGHT / 2 - 100, mResourceManager.mYouSuckFont, "Game Over!", new TextOptions(HorizontalAlign.CENTER), mContext.getVertexBufferObjectManager());
		mOverText.setZIndex(3);
		centerText(mOverText);

		return mScene;
	}

	public static void centerSprite(Sprite sprite){
		sprite.setX(MainActivity.CAMERA_WIDTH / 2);
		sprite.setY(MainActivity.CAMERA_HEIGHT / 2);
	}

	public void displayCurrentScore(int score){
		mScoreText.setText("" + score);
	}

	public void displayBestScore(int score){
		mScoreText.setText("Best - " + score);
	}

	private void centerText(Text text){
		text.setX(MainActivity.CAMERA_WIDTH / 2);
	}
}