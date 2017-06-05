package com.me.catplaformer.managers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public abstract class ScreenManager implements Screen{

	public GameManager game;
	
	public ScreenManager(GameManager game) {
		this.game = game;
	}

	public abstract void render (float deltaTime);

	public abstract void resize (int width, int height);

	public abstract void show ();

	public abstract void hide ();

	public abstract void pause ();
	
	public abstract InputProcessor getInputProcessor ();
	
	public void resume () {
		ResourceManager.instance.init();
	}

	public void dispose () {
		ResourceManager.instance.dispose();
	}


}
