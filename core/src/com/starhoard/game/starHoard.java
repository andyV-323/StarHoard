package com.starhoard.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class starHoard extends Game {

	GameScreen gameScreen;
	Music music;
	Sound engine;
	Sound star;
	Sound explosion;

	public static Random random = new Random();

	SpriteBatch batch;
	Texture ship;
	Texture exp;

	float gravity = 0.03f;
	float velocity = 0;
	int shipY = 0;
	Rectangle shipRectangle;

	BitmapFont font;
	int score = 0;
	int gameState = 0;

	//star objects
	ArrayList<Integer> starXs = new ArrayList<>();
	ArrayList<Integer> starYs = new ArrayList<>();
	ArrayList<Rectangle> starRectangles = new ArrayList<>();
	Texture stars;
	int starCount;

	// asteroids objects
	ArrayList<Integer> asteroidXs = new ArrayList<>();
	ArrayList<Integer> asteroidYs = new ArrayList<>();
	ArrayList<Rectangle> asteroidRectangles = new ArrayList<>();
	Texture asteroid;
	int asteroidCount;

	//batch drawing, texture image
	@Override
	public void create () {
		gameScreen = new GameScreen();
		setScreen(gameScreen);

		batch = new SpriteBatch();

		ship = new Texture("spaceship.png");
		shipY = Gdx.graphics.getHeight()/ 2;
		shipRectangle = new Rectangle();
		stars = new Texture("star.png");
		asteroid = new Texture("asteroid.png");
		exp = new Texture("explosion0.png");
		random = new Random();

		explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		engine = Gdx.audio.newSound(Gdx.files.internal("engine.ogg"));
		star = Gdx.audio.newSound(Gdx.files.internal("coin.ogg"));
		music = Gdx.audio.newMusic(Gdx.files.internal("race.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();


		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}

	public void makeStars() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		starYs.add((int) height);
		starXs.add(Gdx.graphics.getWidth());
	}

	public void makeAsteroids() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		asteroidYs.add((int) height);
		asteroidXs.add(Gdx.graphics.getWidth());
	}

	//drawing
	@Override
	public void render () {
		super.render();
		batch.begin();

		if (gameState == 1) {
			//GAME IS LIVE
			//ASTEROID
			if (asteroidCount < 250) {
				asteroidCount++;
			} else {
				asteroidCount = 0;
				makeAsteroids();
			}

			asteroidRectangles.clear();
			for (int i = 0; i < asteroidXs.size(); i++) {
				batch.draw(asteroid, asteroidXs.get(i), asteroidYs.get(i));
				asteroidXs.set(i, asteroidXs.get(i) - 8);
				asteroidRectangles.add(new Rectangle(asteroidXs.get(i), asteroidYs.get(i),
						asteroid.getWidth(), asteroid.getHeight()));
			}
			//STARS
			if (starCount < 200) {
				starCount++;
			} else {
				starCount = 0;
				makeStars();
			}

			starRectangles.clear();
			for (int i = 0; i < starXs.size(); i++) {
				batch.draw(stars, starXs.get(i), starYs.get(i));
				starXs.set(i, starXs.get(i) - 4);
				starRectangles.add(new Rectangle(starXs.get(i), starYs.get(i),
						stars.getWidth(), stars.getHeight()));
			}


			if (Gdx.input.justTouched()) {
				velocity = -3;
				engine.play(0.4f);

			}

			velocity += gravity;
			shipY -= velocity;
			if (shipY <= 0) {
				shipY = 0;
			}
		} else if (gameState == 0) {
			//Waiting to start
			if (Gdx.input.justTouched()) {
				gameState = 1;

			}
		} else if (gameState == 2) {
			//GAME OVER
			if (Gdx.input.justTouched()) {
				gameState = 1;
				shipY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				starXs.clear();
				starYs.clear();
				starRectangles.clear();
				starCount = 0;

				asteroidXs.clear();
				asteroidYs.clear();
				asteroidRectangles.clear();
				asteroidCount = 0;
			}

		}

		if (gameState == 2) {
			batch.draw(exp, Gdx.graphics.getWidth()/ 2 - ship.getWidth()/ 2, shipY);
		} else {
			//man drawn in the screens center by dividing x by 2 from screen and man[0]
			batch.draw(ship, Gdx.graphics.getWidth()/2, shipY);

		}
		shipRectangle = new Rectangle(Gdx.graphics.getWidth()/ 2 - ship.getWidth()/ 2,shipY,
				ship.getWidth()/2, ship.getHeight()/ 2);

		for (int i = starRectangles.size() - 2; i >= 0; i--) {
			if (Intersector.overlaps(shipRectangle, starRectangles.get(i))) {
				score++;
				star.play(0.3f);

				starRectangles.remove(i);
				starXs.remove(i);
				starYs.remove(i);
				break;
			}
		}
		for (int i = asteroidRectangles.size() - 1; i >= 0; i--) {
			if (Intersector.overlaps(shipRectangle, asteroidRectangles.get(i))) {
				explosion.play(0.3f);
				gameState = 2;

			}
		}

		font.draw(batch, String.valueOf(score), 100, 200);

		batch.end();
	}


	@Override
	public void resize(int width, int height){

		gameScreen.resize(width,height);
	}

	@Override
	public void dispose () {
		gameScreen.dispose();
		music.dispose();
		star.dispose();
		explosion.dispose();
	}
}


