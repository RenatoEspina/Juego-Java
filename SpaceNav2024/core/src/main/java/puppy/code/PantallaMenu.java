package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaMenu implements Screen {

    private SpaceNavigation game;
    private OrthographicCamera camera;
    private Texture fondo; // portada.png

    public PantallaMenu(SpaceNavigation game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        // Cargar la imagen de fondo (portada.png) desde assets
        fondo = new Texture(Gdx.files.internal("portada.png"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        // El fondo se estira solo, esto está bien:
        game.getBatch().draw(fondo, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.getFont().getData().setScale(2.5f); // Más grande para 1080p
        String texto = "Presiona cualquier tecla para comenzar ...";
        float x = camera.viewportWidth / 2 - 200;
        float y = camera.viewportHeight / 2;

        game.getFont().draw(game.getBatch(), texto, x, y);
        game.getBatch().end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            GerentePuntuacion.getInstance().resetScore();
            Screen ss = new PantallaJuego(game, 1, 10, 1, 1, 6);
            ss.resize((int) camera.viewportWidth, (int) camera.viewportHeight);
            game.setScreen(ss);
            dispose();
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
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
        if (fondo != null) fondo.dispose();
    }
}
