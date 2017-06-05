package com.me.catplaformer.managers;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.me.catplaformer.screens.transitions.ScreenTransition;

public abstract class GameManager implements ApplicationListener{
	
	private boolean init;
	private ScreenManager currScreen;
	private ScreenManager nextScreen;
	private FrameBuffer currFbo;
	private FrameBuffer nextFbo;
	private SpriteBatch batch;
	private float t;
	private ScreenTransition screenTransition;

	public void setScreen(ScreenManager screen) {
		setScreen(screen);
	}
	
	public void setScreen (ScreenManager screen, ScreenTransition screenTransition) {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		if (!init) {
			currFbo = new FrameBuffer(Format.RGB888, w, h, false);
			nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
			batch = new SpriteBatch();
			init = true;
		}
		// start new transition
		nextScreen = screen;
		nextScreen.show(); // activate next screen
		nextScreen.resize(w, h);
		nextScreen.render(0); // let next screen update() once
		if (currScreen != null) currScreen.pause();
		nextScreen.pause();
		Gdx.input.setInputProcessor(null); // disable input
		this.screenTransition = screenTransition;
		t = 0;
	}
	
	@Override
	public void dispose() {
		
	}	

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f / 60.0f);
		if (nextScreen == null) {
			// no ongoing transition
			if (currScreen != null) currScreen.render(deltaTime);
		} else {
			// ongoing transition
			float duration = 0;
			if (screenTransition != null) duration = screenTransition.getDuration();
			t = Math.min(t + deltaTime, duration);
			if (screenTransition == null || t >= duration) {
				// no transition effect set or transition has just finished
				if (currScreen != null) currScreen.hide();
				nextScreen.resume();
				// enable input for next screen
				Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
				// switch screens
				currScreen = nextScreen;
				nextScreen = null;
				screenTransition = null;
			} else {
				// render screens to FBOs
				currFbo.begin();
				if (currScreen != null) currScreen.render(deltaTime);
				currFbo.end();
				nextFbo.begin();
				nextScreen.render(deltaTime);
				nextFbo.end();
				// render transition effect to screen
				float alpha = t / duration;
				screenTransition.render(batch, currFbo.getColorBufferTexture(), nextFbo.getColorBufferTexture(), alpha);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}


}
