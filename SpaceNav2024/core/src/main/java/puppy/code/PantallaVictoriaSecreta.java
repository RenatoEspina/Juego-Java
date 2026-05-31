package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PantallaVictoriaSecreta implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture fondoSecreto;

    public PantallaVictoriaSecreta(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        // Mantenemos la resolución Full HD
        this.camera.setToOrtho(false, 1920, 1080);

        // Cargar la imagen de victoria secreta
        fondoSecreto = new Texture(Gdx.files.internal("pantallavictoriasecreto.png"));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1); // Un fondo azulado oscuro mientras carga
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        // Dibujar el fondo secreto estirado
        batch.draw(fondoSecreto, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // Opcional: Texto explicativo
        game.getFont().getData().setScale(2.5f);
        game.getFont().draw(batch, "¡FUSIÓN COMPLETA!", camera.viewportWidth/2 - 200, camera.viewportHeight - 100);

        batch.end();

        // Al hacer clic, volver al menú
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            game.setScreen(new PantallaMenu(game));
            dispose();
        }
    }

    @Override
    public void dispose() {
        if (fondoSecreto != null) fondoSecreto.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
