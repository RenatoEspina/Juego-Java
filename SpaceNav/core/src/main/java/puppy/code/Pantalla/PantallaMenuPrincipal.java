package puppy.code.Pantalla;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import puppy.code.GerentePuntuacion;
import puppy.code.SpaceNavigation;
import puppy.code.Menu.*;

/**
 * Pantalla del menú principal con opciones navegables.
 *
 * Reemplaza a PantallaMenu con una interfaz interactiva:
 *   - Nueva Partida
 *   - Puntaje (muestra el High Score actual)
 *   - Salir
 *
 * Usa el componente MenuPrincipal del package menu para toda la lógica de
 * selección, delegando acciones mediante lambdas.
 */
public class PantallaMenuPrincipal implements Screen {

    private final SpaceNavigation game;
    private final OrthographicCamera camera;
    private final SpriteBatch batch;

    // Recurso gráfico
    private final Texture fondo;

    // Componente de menú
    private final MenuPrincipal menu;

    // Estado para la pantalla de puntaje (modal simple)
    private boolean mostrando_puntaje = false;
    private float tiempoMostrandoPuntaje = 0f;
    private static final float TIEMPO_PUNTAJE_SEG = 3f;

    public PantallaMenuPrincipal(SpaceNavigation game) {
        this.game = game;
        this.batch = game.getBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        fondo = new Texture(Gdx.files.internal("portada.png"));

        // Menú en la parte baja de la pantalla, debajo del título de la imagen
        float centroX = camera.viewportWidth / 2f;
        // Bajado a ~300 para quedar por debajo del título gráfico de la portada
        float inicioY = camera.viewportHeight / 2f - 200f;

        menu = new MenuPrincipal(game.getFont(), centroX, inicioY, 700f, 100f, 25f);

        menu.agregarOpcion("Nueva Partida", this::iniciarNuevaPartida);
        menu.agregarOpcion("High Score",    this::mostrarPuntaje);
        menu.agregarOpcion("Salir",         () -> Gdx.app.exit());
    }

    // --- Acciones de las opciones ---

    private void iniciarNuevaPartida() {
        GerentePuntuacion.getInstance().resetScore();
        Screen pantalla = new PantallaJuego(game, 1, 3, 1, 1, 6);
        pantalla.resize((int) camera.viewportWidth, (int) camera.viewportHeight);
        game.setScreen(pantalla);
        dispose();
    }

    private void mostrarPuntaje() {
        mostrando_puntaje = true;
        tiempoMostrandoPuntaje = 0f;
    }

    // --- Ciclo de vida Screen ---

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // Fondo
        batch.draw(fondo, 0, 0, camera.viewportWidth, camera.viewportHeight);

        if (mostrando_puntaje) {
            // Panel de puntaje temporal
            tiempoMostrandoPuntaje += delta;
            dibujarPantallaHighScore();

            // Vuelve al menú tras unos segundos o con cualquier tecla
            if (tiempoMostrandoPuntaje >= TIEMPO_PUNTAJE_SEG
                || Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ANY_KEY)) {
                mostrando_puntaje = false;
            }
        } else {
            // Instrucciones en la parte baja
            game.getFont().getData().setScale(2f);
            game.getFont().setColor(new Color(0.8f, 0.8f, 1f, 1f));
            game.getFont().draw(batch,
                "\u2191\u2193 para navegar  |  ENTER / clic para seleccionar",
                camera.viewportWidth / 2f - 380f,
                150f);

            // Opciones del menú
            menu.update();
            menu.draw(batch);

            // Restablecer fuente
            game.getFont().setColor(Color.WHITE);
            game.getFont().getData().setScale(2f);
        }

        batch.end();
    }

    private void dibujarPantallaHighScore() {
        int highScore = GerentePuntuacion.getInstance().getHighScore();

        float panelX = camera.viewportWidth / 2f - 400f;
        float panelY = camera.viewportHeight / 2f - 150f;

        game.getFont().getData().setScale(4f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "HIGH SCORE", panelX + 60f, camera.viewportHeight / 2f + 150f);

        game.getFont().getData().setScale(6f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, String.valueOf(highScore),
            camera.viewportWidth / 2f - 60f,
            camera.viewportHeight / 2f + 20f);

        game.getFont().getData().setScale(2f);
        game.getFont().setColor(new Color(0.7f, 0.7f, 0.7f, 1f));
        game.getFont().draw(batch, "Presiona cualquier tecla para continuar",
            panelX - 20f, panelY + 20f);
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (fondo != null) fondo.dispose();
    }
}