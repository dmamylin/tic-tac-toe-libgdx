package org.madbunny.tictactoe.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ScreenBase implements Screen {
    private static final float DEFAULT_BACKGROUND_RED = 0.5f;
    private static final float DEFAULT_BACKGROUND_GREEN = 0.5f;
    private static final float DEFAULT_BACKGROUND_BLUE = 0.5f;
    private static final float DEFAULT_BACKGROUND_ALPHA = 1.0f;
    private static final int DEFAULT_FONT_SIZE = 16;

    private final AtomicLong nextSpriteId = new AtomicLong(0);
    private final SpriteBatch spriteBatch = new SpriteBatch();
    private final ArrayList<Texture> textures = new ArrayList<>();
    private final Map<Long, Sprite> sprites = new HashMap<>();
    private final ArrayList<Sprite> renderQueue = new ArrayList<>();

    protected final long INVALID_ENTITY_ID = 0;

    public ScreenBase() {
        var viewMatrix = new Matrix4();
        viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(new Matrix4());
        spriteBatch.enableBlending();
    }

    @Override
    public final void show() {
        setBackgroundColor(DEFAULT_BACKGROUND_RED, DEFAULT_BACKGROUND_GREEN, DEFAULT_BACKGROUND_BLUE);
        onShow();
    }

    @Override
    public final void render(float v) {
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
    public final void resize(int i, int i1) {
        // Do nothing here. It is forbidden to resize a window as for now.
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

    @Override
    public final void dispose() {
        spriteBatch.dispose();
        for (var texture : textures) {
            texture.dispose();
        }
    }

    protected final boolean isValidEntityId(long id) {
        return id > 0;
    }

    protected final void scheduleForRender(long spriteId) {
        var sprite = sprites.get(spriteId);
        renderQueue.add(sprite);
    }

    protected final void setBackgroundColor(float red, float green, float blue) {
        Gdx.gl20.glClearColor(red, green, blue, DEFAULT_BACKGROUND_ALPHA);
    }

    protected final long createSprite(String path) {
        var spriteId = nextSpriteId.addAndGet(1);
        var texture = new Texture(path);
        var sprite = new Sprite(texture);
        textures.add(texture);
        sprites.put(spriteId, sprite);
        return spriteId;
    }

    protected final void deleteSprite(long spriteId) {
        sprites.remove(spriteId);
    }

    protected final Sprite getSprite(long spriteId) {
        return sprites.get(spriteId);
    }

    protected abstract void onShow();

    protected abstract void beforeRender();
}
