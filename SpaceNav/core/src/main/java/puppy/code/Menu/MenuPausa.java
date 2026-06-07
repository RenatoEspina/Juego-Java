package puppy.code.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Menú de pausa en partida.
 *
 * Se muestra como overlay semitransparente sobre PantallaJuego.
 * El juego se detiene mientras está activo (no actualiza lógica).
 *
 * Uso en PantallaJuego:
 *   - Crear una instancia en el constructor.
 *   - En render(): si menuPausa.isVisible() → solo dibujar juego + llamar menuPausa.update()/draw()
 *   - Tecla ESC alterna la visibilidad.
 *
 * Opciones:
 *   1. Reanudar    → oculta el menú, el juego continúa
 *   2. Reiniciar   → vuelve a ronda 1 (callback externo)
 *   3. Menú Principal → vuelve a PantallaMenu (callback externo)
 *   4. Salir       → cierra la aplicación
 */
public class MenuPausa {

    // --- Estado ---
    private boolean visible = false;
    private boolean escPrevio = false;

    // --- Componente de menú reutilizable ---
    private final MenuPrincipal menu;

    // --- Callbacks (interfaces funcionales Java 8+) ---
    private final Runnable onReanudar;
    private final Runnable onReiniciar;
    private final Runnable onMenuPrincipal;

    // --- Cámara y fuente ---
    private final OrthographicCamera camera;
    private final BitmapFont font;

    /**
     * Construye el menú de pausa.
     *
     * @param font           Fuente compartida del juego
     * @param camera         Cámara del juego (para coordenadas correctas)
     * @param onReanudar     Acción al reanudar (oculta el menú)
     * @param onReiniciar    Acción al reiniciar la partida
     * @param onMenuPrincipal Acción al ir al menú principal
     */
    public MenuPausa(BitmapFont font, OrthographicCamera camera,
                     Runnable onReanudar, Runnable onReiniciar,
                     Runnable onMenuPrincipal) {
        this.font = font;
        this.camera = camera;
        this.onReanudar = onReanudar;
        this.onReiniciar = onReiniciar;
        this.onMenuPrincipal = onMenuPrincipal;

        // Centrar el menú en la pantalla de 1920x1080
        float centroX = camera.viewportWidth / 2f;
        float inicioY = camera.viewportHeight / 2f + 160f;

        menu = new MenuPrincipal(font, centroX, inicioY, 600f, 90f, 20f);
        construirOpciones();
    }

    private void construirOpciones() {
        menu.agregarOpcion("Reanudar", () -> {
            visible = false;
            onReanudar.run();
        });
        menu.agregarOpcion("Reiniciar Partida", () -> {
            visible = false;
            onReiniciar.run();
        });
        menu.agregarOpcion("Menu Principal", () -> {
            visible = false;
            onMenuPrincipal.run();
        });
        menu.agregarOpcion("Salir del Juego", () -> Gdx.app.exit());
    }

    /**
     * Alterna visibilidad con ESC y delega la lógica al MenuPrincipal interno.
     * Llamar cada frame en PantallaJuego.render() sin importar si está visible.
     */
    public void update() {
        boolean escActual = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);

        if (escActual && !escPrevio) {
            visible = !visible;
            if (visible) {
                // Restablecer selección al reanudar por si quedó en otra opción
                menu.setIndiceSeleccionado(0);
            }
        }
        escPrevio = escActual;

        if (visible) {
            menu.update();
        }
    }

    /**
     * Dibuja el overlay de pausa dentro del batch abierto de PantallaJuego.
     * Solo dibuja si está visible.
     */
    public void draw(SpriteBatch batch) {
        if (!visible) return;

        // Overlay oscuro semitransparente (simulado con texto de fondo)
        dibujarOverlayOscuro(batch);

        // Título "PAUSA"
        font.getData().setScale(6f);
        font.setColor(Color.YELLOW);
        float tituloX = camera.viewportWidth / 2f - 120f;
        float tituloY = camera.viewportHeight / 2f + 320f;
        font.draw(batch, "PAUSA", tituloX, tituloY);

        // Instrucciones
        font.getData().setScale(2f);
        font.setColor(new Color(0.8f, 0.8f, 0.8f, 1f));
        font.draw(batch, "↑↓ para navegar  |  ENTER para seleccionar  |  ESC para reanudar",
            camera.viewportWidth / 2f - 520f,
            camera.viewportHeight / 2f - 230f);

        // Opciones del menú
        menu.draw(batch);

        // Restablecer fuente
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
    }

    /**
     * Dibuja un overlay oscuro semitransparente que cubre toda la pantalla.
     * Usa el truco de color del batch para no cargar texturas extra.
     */
    private void dibujarOverlayOscuro(SpriteBatch batch) {
        Color prevColor = batch.getColor().cpy();
        batch.setColor(0f, 0f, 0.05f, 0.75f);
        // Dibujamos filas de espacios para simular el overlay
        font.getData().setScale(camera.viewportWidth / 800f, camera.viewportHeight / 100f);
        StringBuilder fila = new StringBuilder();
        for (int i = 0; i < 100; i++) fila.append(' ');
        String filaTxt = fila.toString();
        for (int fila2 = 0; fila2 < 15; fila2++) {
            font.draw(batch, filaTxt, 0, (fila2 + 1) * (camera.viewportHeight / 14f));
        }
        batch.setColor(prevColor);
    }

    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { visible = v; }
}