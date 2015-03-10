package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by haakonkaurel on 10/03/15.
 */
public class GameObject extends Sprite {
    protected Vector2 velocity, acceleration;
    protected int health;

    public GameObject(Texture tex) {
        super(tex);
        this.velocity = new Vector2(0, 0);
        this.acceleration = new Vector2(0, 0);
        this.health = 100;
    }

    public void update(float dt) {
        velocity.add(acceleration.cpy().scl(dt));
        setX(getX() + velocity.x * dt);
        setY(getY()+velocity.y*dt);
    }

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float x, float y) {
        acceleration.set(x, y);
    }

    public boolean takeDamage(int damage) {
        health -= damage;
        return health <= 0;
    }

    public void kill() {
    }
}

class Grass extends GameObject {
    public Grass() {
        super(new Texture("Grass.PNG"));
    }
}

class Dirt extends GameObject {
    public Dirt() {
        super(new Texture("Dirt.PNG"));
    }
}

class Tnt extends  GameObject {
    public Tnt() { super(new Texture("Tnt.PNG"));}

    @Override
    public void update(float dt) {
        super.update(dt);
        if(getY() > Gdx.graphics.getHeight() || getY() < 0 ) {
            velocity.y *= -0.9;
        }
    }

    @Override
    public void kill() {
        Gdx.audio.newSound(Gdx.files.internal("bang.mp3")).play();
    }
}
