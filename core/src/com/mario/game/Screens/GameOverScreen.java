package com.mario.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;
import com.mario.game.Tools.ScreenManager;
import com.mario.game.Tools.ScreenManager.ScreenEnum;

public class GameOverScreen extends AbstractScreen{

	//private Viewport viewport;
	private Stage stage;
	//private MarioGame game;

	public GameOverScreen(MarioGame game) {
		super(game);
		//this.game = game;
		//viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(gameport, ((MarioGame) game).batch);
		
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
		
		Table table = new Table();
		table.center();
		table.setFillParent(true);
		
		Label gameOverLabel = new Label("GAME OVER", font);
		Label playAgainLabel = new Label("PLAY AGAIN?", font);
		
		table.add(gameOverLabel).expandX();
		table.row();
		table.add(playAgainLabel).expandX().padTop(10f);
		
		stage.addActor(table);
		
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		if(Gdx.input.justTouched()){
			ScreenManager.getInstance().showScreen(ScreenEnum.LEVEL_1_1);
			dispose();
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}

}
