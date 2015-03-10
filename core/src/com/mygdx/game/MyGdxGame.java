package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MyGdxGame extends ApplicationAdapter implements GestureDetector.GestureListener {
    int COL_COUNT, ROW_COUNT;
	SpriteBatch batch;
    ArrayList<GameObject> gameObjects;

	@Override
	public void create () {
        COL_COUNT = ROW_COUNT = 10;
        GameObject temp;
        Gdx.input.setInputProcessor(new GestureDetector(this));
		batch = new SpriteBatch();
        gameObjects = new ArrayList<GameObject>();
        for(int i = 0; i < 10; i++) {
            gameObjects.add(setPosition(normalize(new Grass()), i, 1));
            gameObjects.add(setPosition(normalize(new Dirt()), i, 0));
        }
	}

    private GameObject normalize(GameObject obj) {
        obj.setSize(
                Gdx.graphics.getWidth() / COL_COUNT,
                Gdx.graphics.getHeight() / ROW_COUNT
        );
        return obj;
    }

    private GameObject setPosition(GameObject obj, int x, int y) {
        obj.setPosition(x*obj.getWidth(), y*obj.getHeight());
        return obj;
    }

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        for(GameObject obj : gameObjects) {
            obj.draw(batch);
        }
		batch.end();

        float dt = Gdx.graphics.getDeltaTime();
        for(GameObject obj : gameObjects) {
            obj.update(dt);
        }
        ArrayList<GameObject> dead = new ArrayList<GameObject>();
        for(int i = 0; i < gameObjects.size(); i++) {
            for(int j = i+1; j < gameObjects.size(); j++) {
                if(gameObjects.get(i) != gameObjects.get(j) && gameObjects.get(i).getBoundingRectangle().overlaps(gameObjects.get(j).getBoundingRectangle())) {
                    int damage = (int)(gameObjects.get(i).getVelocity().cpy().sub(gameObjects.get(j).getVelocity()).len()/
                    50);
                    if(gameObjects.get(i).takeDamage(damage)) {
                        gameObjects.get(i).kill();
                        dead.add(gameObjects.get(i));
                    }
                    if(gameObjects.get(j).takeDamage(damage)) {
                        gameObjects.get(j).kill();
                        dead.add(gameObjects.get(j));
                    }
                    gameObjects.get(j).takeDamage(damage);
                    gameObjects.get(i).getVelocity().scl(-1.0f);
                    gameObjects.get(j).getVelocity().scl(-1.0f);
                    gameObjects.get(i).update(dt);
                    gameObjects.get(j).update(dt);
                }
            }
        }

        gameObjects.removeAll(dead);
	}

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        GameObject temp = new Tnt();
        temp.setAcceleration(0, -200);
        normalize(temp);
        temp.setPosition(x-temp.getWidth()/2f, Gdx.graphics.getHeight()-y);
        gameObjects.add(temp);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        System.out.println("longPress");
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        System.out.println("fling");
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        System.out.println("pan");
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        System.out.println("panStop");
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        System.out.println("zoom");
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        System.out.println("pinch");
        return false;
    }
}
