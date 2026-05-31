package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaGameOver implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;
	private Sound gameOverSound;

	public PantallaGameOver(SpaceNavigation game) {
		this.game = game;

		camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

		// cargar efecto de sonido para Game Over
		gameOverSound = Gdx.audio.newSound(Gdx.files.internal("game-over.mp3"));
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		game.getFont().draw(game.getBatch(), "Fin del Juego", 420, 350,400,1,true);
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado para reiniciar ...", 350, 250);

		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            GerentePuntuacion.getInstance().resetScore();
			Screen ss = new PantallaJuego(game,1,3,1,1,6);
			ss.resize(1920, 1080);
			game.setScreen(ss);
			dispose();
		}
	}


	@Override
	public void show() {
		// reproducir el sonido de Game Over al mostrar la pantalla
		if (gameOverSound != null) {
			gameOverSound.play(0.8f);
		}

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// liberar recurso de sonido
		if (gameOverSound != null) {
			gameOverSound.dispose();
			gameOverSound = null;
		}

	}

}
