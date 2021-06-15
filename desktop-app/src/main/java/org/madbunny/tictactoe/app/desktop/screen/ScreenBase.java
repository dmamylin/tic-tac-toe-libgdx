package org.madbunny.tictactoe.app.desktop.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import java.util.ArrayList;

public abstract class ScreenBase implements Screen {
    private static final float DEFAULT_BACKGROUND_RED = 0.5f;
    private static final float DEFAULT_BACKGROUND_GREEN = 0.5f;
    private static final float DEFAULT_BACKGROUND_BLUE = 0.5f;
    private static final float DEFAULT_BACKGROUND_ALPHA = 1.0f;

    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final ArrayList<Texture> textures = new ArrayList<>();
    private final ArrayList<Sprite> renderQueue = new ArrayList<>();

    public ScreenBase() {
        var viewMatrix = new Matrix4();
        viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(new Matrix4());
        spriteBatch.enableBlending();
    }

    @Override
    public final void show() {
        Gdx.gl20.glClearColor(DEFAULT_BACKGROUND_RED, DEFAULT_BACKGROUND_GREEN, DEFAULT_BACKGROUND_BLUE, DEFAULT_BACKGROUND_ALPHA);
    }

    @Override
    public final void render(float dt) {
        beforeRender();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        for (var sprite : renderQueue) {
            sprite.draw(spriteBatch);
        }
        spriteBatch.end();
        renderQueue.clear();
    }

    @Override
    public final void dispose() {
        spriteBatch.dispose();
        for (var texture : textures) {
            texture.dispose();
        }
    }

    protected final void scheduleForRender(Sprite sprite) {
        renderQueue.add(sprite);
    }

    protected final Texture createTexture(String path) {
        var texture = new Texture(path);
        textures.add(texture);
        return texture;
    }

    protected final Sprite createSprite(Texture texture) {
        return new Sprite(texture);
    }

    @Override
    public final void resize(int i, int i1) {
    }

    @Override
    public final void pause() {
    }

    @Override
    public final void resume() {
    }

    @Override
    public final void hide() {
    }

    protected abstract void beforeRender();
}
