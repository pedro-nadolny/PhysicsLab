package robert.peter.physicslab;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FixedResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

import static org.andengine.extension.physics.box2d.PhysicsFactory.createBoxBody;

public class MechanicsActivity extends SimpleBaseGameActivity implements SensorEventListener {

    private int width, height;
    private PhysicsWorld mPhysicsWorld;
    private SensorManager mSensorManager;
    private Sensor mSensor = null;
    private Body boxBody;

    @Override
    public EngineOptions onCreateEngineOptions() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        final Camera c = new Camera(0, 0, width, height);
        final FixedResolutionPolicy p = new FixedResolutionPolicy(width, height);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, p, c);
    }

    @Override
    protected void onCreateResources() throws IOException {

    }

    @Override
    protected Scene onCreateScene() {
        Scene scene = new Scene();

        mPhysicsWorld = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH), false);

        scene.registerUpdateHandler(new IUpdateHandler() {

            @Override
            public void reset() {}

            @Override
            public void onUpdate(float pSecondsElapsed) {
                mPhysicsWorld.onUpdate(pSecondsElapsed);
            }
        });

        Rectangle bg = new Rectangle(width/2, height/2, width, height, this.getVertexBufferObjectManager());
        bg.setColor(0.75f,0.75f,0.75f);
        scene.attachChild(bg);

        Rectangle box1 = new Rectangle(width/4, height/2, width/7, height/7, this.getVertexBufferObjectManager());
        box1.setColor(0.1f, 0.8f, 0.2f);
        scene.attachChild(box1);

        Rectangle box2 = new Rectangle(width/2, height/2, height/7, width/7, this.getVertexBufferObjectManager());
        box2.setColor(0.8f, 0.2f, 0.1f);
        scene.attachChild(box2);

        Rectangle box3 = new Rectangle(3*width/4, height/2, height/7, height/7, this.getVertexBufferObjectManager());
        box3.setColor(0.2f, 0.1f, 0.8f);
        scene.attachChild(box3);

        final Body boxBody1 = createBoxBody(mPhysicsWorld, box1, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1,0,0));
        final Body boxBody2 = createBoxBody(mPhysicsWorld, box2, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1,0,0));
        final Body boxBody3 = createBoxBody(mPhysicsWorld, box3, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1,0,0));

        final PhysicsConnector boxConnector1 = new PhysicsConnector(box1, boxBody1, true, true);
        final PhysicsConnector boxConnector2 = new PhysicsConnector(box2, boxBody2, true, true);
        final PhysicsConnector boxConnector3 = new PhysicsConnector(box3, boxBody3, true, true);

        mPhysicsWorld.registerPhysicsConnector(boxConnector1);
        mPhysicsWorld.registerPhysicsConnector(boxConnector2);
        mPhysicsWorld.registerPhysicsConnector(boxConnector3);

        createEdges();

        return scene;
    }

    private void createEdges() {
        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,0,0);

        //bottom
        Body b = PhysicsFactory.createBoxBody(mPhysicsWorld, width/2, height, width, 1, BodyDef.BodyType.StaticBody, wallFixtureDef);

        //top
        Body t = PhysicsFactory.createBoxBody(mPhysicsWorld, width/2, 0, width, 1, BodyDef.BodyType.StaticBody, wallFixtureDef);

        //left
        Body l = PhysicsFactory.createBoxBody(mPhysicsWorld, 0, height/2, 1, height, BodyDef.BodyType.StaticBody, wallFixtureDef);

        //right
        Body r = PhysicsFactory.createBoxBody(mPhysicsWorld, width, height/2, 1, height, BodyDef.BodyType.StaticBody, wallFixtureDef);
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mPhysicsWorld == null) return;

        double x = event.values[1];
        double y = -event.values[0];
        double z = event.values[2];

        double r = Math.hypot(x, y);
        float newX = 40 * (float)Math.asin(x/r) * 2;
        float newY = 40 * (float)Math.asin(y/r) * 2;

        Vector2 g = new Vector2(newX, newY);
        mPhysicsWorld.setGravity(g);
    }
}