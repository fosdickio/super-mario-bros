package com.mario.game.Tools;

import com.mario.game.MarioGame;
import com.mario.game.Screens.AbstractScreen;
import com.mario.game.Screens.GameOverScreen;
import com.mario.game.Screens.MainMenuScreen;
import com.mario.game.Screens.PlayScreen;

public class ScreenManager {
	public enum ScreenEnum {MAIN_MENU, LEVEL_1_1, LEVEL_1_2, GAME_OVER};
	
	private static ScreenManager screenManager;
	
	private MarioGame game;
	private String level;
	
	// Singleton: private constructor
	private ScreenManager() {
		super();
	}
	
	public static ScreenManager getInstance() {
		if(screenManager == null){
			screenManager = new ScreenManager();
		}
		return screenManager;
	}
	
	public void initalize(MarioGame game){
		this.game = game;
	}
	
	public void showScreen(ScreenEnum screenEnum){
		//Screen currentScreen = game.getScreen();
		
		AbstractScreen newScreen;
		
		switch(screenEnum){
		case LEVEL_1_1:
			level = "level_1-1.tmx";
			newScreen = new PlayScreen(game, level);
			break;
		case LEVEL_1_2:
			level = "level_1-2.tmx";
			newScreen = new PlayScreen(game, level);
			break;
		case GAME_OVER:
			newScreen = new GameOverScreen(game);
			break;
		case MAIN_MENU:
		default:
			newScreen = new MainMenuScreen(game);
			break;
		}
		
		game.setScreen(newScreen);
		
		
	}
	
}
