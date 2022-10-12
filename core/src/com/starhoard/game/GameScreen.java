package com.starhoard.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class GameScreen implements Screen{
    //screen
    Camera camera;
    Viewport viewport;

    //graphics
    SpriteBatch batch;

    //background
    Texture[] background;

    //timing
    float[] bgOffsets = {0,0,0,0,0,0,0,0};
    float bgMaxScrollingSpeed;

    //world parameters
    int WORLD_WIDTH = 2000;
    int WORLD_HEIGHT = 2000;

    GameScreen() {

        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH,WORLD_HEIGHT, camera);

        background = new Texture[8];
        background[0] = new Texture("bkgd_0.png");
        background[1] = new Texture("bkgd_1.png");
        background[2] = new Texture("bkgd_2.png");
        background[3] = new Texture("bkgd_3.png");
        background[4] = new Texture("bkgd_4.png");
        background[5] = new Texture("bkgd_5.png");
        background[6] = new Texture("bkgd_6.png");
        background[7] = new Texture("bkgd_7.png");

        bgMaxScrollingSpeed = (float) (WORLD_WIDTH/4);

        batch = new SpriteBatch();

    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        //scrolling background
       renderBackground(deltaTime);

        batch.end();

    }

    public void renderBackground(float deltaTime) {
        bgOffsets[0] += deltaTime * bgMaxScrollingSpeed / 10;
        bgOffsets[1] += deltaTime * bgMaxScrollingSpeed / 8;
        bgOffsets[2] += deltaTime * bgMaxScrollingSpeed / 6;
        bgOffsets[3] += deltaTime * bgMaxScrollingSpeed / 4;
        bgOffsets[4] += deltaTime * bgMaxScrollingSpeed / 3;
        bgOffsets[5] += deltaTime * bgMaxScrollingSpeed / 2;
        bgOffsets[6] += deltaTime * bgMaxScrollingSpeed / 2;
        bgOffsets[7] += deltaTime * bgMaxScrollingSpeed / 2;


        for (int i = 0; i < bgOffsets.length; i++) {
            if (bgOffsets[i] > WORLD_WIDTH) {
                bgOffsets[i] = 0;
            }

            batch.draw(background[i],

                    -bgOffsets[i], 0,
                    WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(background[i],

                    -bgOffsets[i] + WORLD_WIDTH, 0,
                    WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();

    }
    @Override
    public void show() {

    }
}
