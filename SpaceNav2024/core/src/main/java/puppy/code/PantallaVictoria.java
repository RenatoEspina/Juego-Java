package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaVictoria implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture fondoVictoria;

    public PantallaVictoria(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        // Mantenemos la resolución Full HD
        this.camera.setToOrtho(false, 1920, 1080);

        // Cargar la imagen de victoria normal
        fondoVictoria = new Texture(Gdx.files.internal("pantallavictoria.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Dibujar el fondo estirado a toda la pantalla
        batch.draw(fondoVictoria, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // Opcional: Texto parpadeante indicando que hacer
        game.getFont().getData().setScale(2f);
        game.getFont().draw(batch, "Haz clic para volver al menú", camera.viewportWidth/2 - 200, 100);
        batch.end();

        // Al hacer clic o presionar tecla, volver al menú
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaMenu(game));
            dispose();
        }
    }

    @Override
    public void dispose() {
        if (fondoVictoria != null) fondoVictoria.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
