package ro.luca1152.balloon.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ro.luca1152.balloon.MyGame;

class Balloon extends Image {
    // Constants
    private final float WIDTH = 1.2f, HEIGHT = 1.6f;

    // Collisions
    private Rectangle collisionBox;

    // Box2D
    private Body body;

    Balloon(World world, Rectangle rectangle) {
        super(MyGame.manager.get("textures/player.png", Texture.class));

        // Image
        this.setSize(WIDTH, HEIGHT);
        this.setPosition(rectangle.x / MyGame.PPM + WIDTH / 2f, rectangle.y / MyGame.PPM + HEIGHT / 2f - .35f);
        this.getColor().a = .2f;

        // Collisions
        collisionBox = rectangle;
        collisionBox.set(getX(), getY(), getWidth(), getHeight());

        // Box2D
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = createEllipse(WIDTH / 2f, HEIGHT / 2f);
        body.createFixture(fixtureDef);
        body.setTransform(rectangle.x, rectangle.y, 0f);
        body.setLinearDamping(.15f);
    }

    private ChainShape createEllipse(float width, float height) {
        int STEPS = 8;

        ChainShape ellipse = new ChainShape();
        Vector2[] vertices = new Vector2[STEPS];

        for (int i = 0; i < STEPS; i++) {
            float t = (float) (i * 2 * Math.PI) / STEPS;
            vertices[i] = new Vector2(width * (float) Math.cos(t), height * (float) Math.sin(t));
        }

        ellipse.createLoop(vertices);
        return ellipse;
    }

    public Rectangle getCollisionBox() {
        collisionBox.set(getX(), getY(), getWidth(), getHeight());
        return collisionBox;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        body.applyForce(new Vector2(0, 10f), body.getWorldCenter(), true);
        setPosition(body.getWorldCenter().x - WIDTH / 2f, body.getWorldCenter().y - HEIGHT / 2f);
        setRotation(body.getAngle() * MathUtils.radDeg);
    }
}
