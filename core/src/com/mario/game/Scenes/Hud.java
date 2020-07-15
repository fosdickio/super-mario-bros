package com.mario.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mario.game.MarioGame;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Hud implements Disposable{
	public Stage stage;
	
	private Viewport viewport;
	private Integer worldTimer;
	private float timeCount;
	private static Integer score;
	
	private Label countdownLabel;
	private static Label scoreLabel;
	private Label timeLabel;
	private Label levelLabel;
	private Label worldLabel;
	private Label marioLabel;
	
	public Hud(SpriteBatch sb) {
		worldTimer = 300;
		timeCount = 0;
		score = 0;
		
		viewport = new FitViewport(MarioGame.V_WIDTH, MarioGame.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		
		Label.LabelStyle labelstyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
		countdownLabel = new Label(String.format("%03d", worldTimer), labelstyle);
		scoreLabel = new Label(String.format("%06d", score), labelstyle);
		timeLabel = new Label("TIME", labelstyle);
		levelLabel = new Label("1-1", labelstyle);
		worldLabel = new Label("WORLD", labelstyle);
		marioLabel = new Label("MARIO", labelstyle);
		
		table.add(marioLabel).expandX().padTop(10);
		table.add(worldLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(countdownLabel).expandX();
		
		stage.addActor(table);
	}
	
	public void update(float dt) {
		timeCount += dt;
		if(timeCount >= 1) {
			worldTimer--;
			countdownLabel.setText(String.format("%03d", worldTimer));
			timeCount = 0;
		}
	}
	
	public static void addScore(int value) {
		score += value;
		scoreLabel.setText(String.format("%06d", score));
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}
}
