package puppy.code.Menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente de menú genérico y reutilizable.
 * Maneja una lista de opciones navegables con teclado (↑↓ + ENTER) o ratón.
 * Diseñado para ser embebido en cualquier Screen de LibGDX.
 */
public class MenuPrincipal {

    // --- Estructura de una opción de menú ---
    public static class OpcionMenu {
        public final String texto;
        public final Runnable accion;

        public OpcionMenu(String texto, Runnable accion) {
            this.texto = texto;
            this.accion = accion;
        }
    }

    // --- Estado ---
    private final List<OpcionMenu> opciones = new ArrayList<>();
    private int indiceSeleccionado = 0;
    private boolean keyDownPrevio = false;
    private boolean keyUpPrevio = false;
    private boolean keyEnterPrevio = false;

    // --- Layout ---
    private final float x;
    private final float y;
    private final float anchoBoton;
    private final float altoBoton;
    private final float espaciado;

    // --- Fuente y colores ---
    private final BitmapFont font;
    private final Color colorNormal;
    private final Color colorSeleccionado;
    private final Color colorFondoNormal;
    private final Color colorFondoSeleccionado;

    /**
     * Construye el menú.
     *
     * @param font      Fuente de LibGDX a usar para el texto
     * @param x         Coordenada X del centro del menú
     * @param y         Coordenada Y del botón superior
     * @param ancho     Ancho de cada botón
     * @param alto      Alto de cada botón
     * @param espaciado Separación vertical entre botones
     */
    public MenuPrincipal(BitmapFont font, float x, float y,
                         float ancho, float alto, float espaciado) {
        this.font = font;
        this.x = x;
        this.y = y;
        this.anchoBoton = ancho;
        this.altoBoton = alto;
        this.espaciado = espaciado;

        // Paleta de colores espacial
        this.colorNormal          = new Color(0.7f, 0.7f, 1.0f, 1f);
        this.colorSeleccionado    = new Color(1f,   1f,   0.2f, 1f);
        this.colorFondoNormal     = new Color(0f,   0f,   0.3f, 0.7f);
        this.colorFondoSeleccionado = new Color(0.1f, 0.1f, 0.6f, 0.9f);
    }

    /** Agrega una opción al final de la lista. */
    public void agregarOpcion(String texto, Runnable accion) {
        opciones.add(new OpcionMenu(texto, accion));
    }

    /**
     * Actualiza la lógica de selección: navegación con teclas y clic de ratón.
     * Debe llamarse cada frame antes de draw().
     */
    public void update() {
        if (opciones.isEmpty()) return;

        // --- Navegación con teclado ---
        boolean downActual  = Gdx.input.isKeyPressed(Input.Keys.DOWN)  || Gdx.input.isKeyPressed(Input.Keys.S);
        boolean upActual    = Gdx.input.isKeyPressed(Input.Keys.UP)    || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean enterActual = Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.SPACE);

        if (downActual && !keyDownPrevio) {
            indiceSeleccionado = (indiceSeleccionado + 1) % opciones.size();
        }
        if (upActual && !keyUpPrevio) {
            indiceSeleccionado = (indiceSeleccionado - 1 + opciones.size()) % opciones.size();
        }
        if (enterActual && !keyEnterPrevio) {
            opciones.get(indiceSeleccionado).accion.run();
        }

        keyDownPrevio  = downActual;
        keyUpPrevio    = upActual;
        keyEnterPrevio = enterActual;

        // --- Clic de ratón ---
        if (Gdx.input.justTouched()) {
            // LibGDX: Y del ratón está invertida respecto a la cámara ortográfica
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int i = 0; i < opciones.size(); i++) {
                Rectangle area = obtenerAreaBoton(i);
                if (area.contains(mouseX, mouseY)) {
                    indiceSeleccionado = i;
                    opciones.get(i).accion.run();
                    return;
                }
            }
        }

        // Hover del ratón: actualiza selección visual sin activar acción
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        for (int i = 0; i < opciones.size(); i++) {
            if (obtenerAreaBoton(i).contains(mouseX, mouseY)) {
                indiceSeleccionado = i;
                break;
            }
        }
    }

    /**
     * Dibuja los botones del menú dentro de un batch ya abierto.
     * El batch debe estar en begin() cuando se llama a este método.
     */
    public void draw(SpriteBatch batch) {
        if (opciones.isEmpty()) return;

        for (int i = 0; i < opciones.size(); i++) {
            Rectangle area = obtenerAreaBoton(i);
            boolean seleccionado = (i == indiceSeleccionado);

            // Fondo del botón (dibujado como rectángulo de color)
            Color fondoColor = seleccionado ? colorFondoSeleccionado : colorFondoNormal;
            dibujarRectangulo(batch, area, fondoColor);

            // Texto centrado dentro del botón
            font.getData().setScale(seleccionado ? 3.0f : 2.5f);
            font.setColor(seleccionado ? colorSeleccionado : colorNormal);

            // Indicador de selección con flechas
            String textoFinal = seleccionado
                ? "► " + opciones.get(i).texto + " ◄"
                : "  " + opciones.get(i).texto + "  ";

            // Centrar texto horizontalmente
            float textX = area.x + (area.width / 2f) - estimarAnchoTexto(textoFinal) / 2f;
            float textY = area.y + area.height / 2f + 20f; // +20 por baseline de libgdx

            font.draw(batch, textoFinal, textX, textY);
        }

        // Restablecer escala de fuente para no afectar otros dibujados
        font.getData().setScale(2f);
        font.setColor(Color.WHITE);
    }

    // --- Métodos auxiliares ---

    /** Calcula el Rectangle de pantalla del botón i. */
    private Rectangle obtenerAreaBoton(int i) {
        float bx = x - anchoBoton / 2f;
        float by = y - i * (altoBoton + espaciado);
        return new Rectangle(bx, by, anchoBoton, altoBoton);
    }

    /**
     * Dibuja un rectángulo sólido usando el batch.
     * Reutiliza la textura white pixel de la fuente BitmapFont si existe;
     * si no, usa el color directamente sobre el pixel de la región actual.
     * Hack limpio para LibGDX sin cargar texturas adicionales.
     */
    private void dibujarRectangulo(SpriteBatch batch, Rectangle r, Color color) {
        // Usamos el glyph blanco de la BitmapFont que siempre existe (carácter ' ')
        Color prevColor = batch.getColor().cpy();
        batch.setColor(color);
        // Dibujamos el glyph de espacio escalado como fondo (pixmap 1x1 blanco)
        font.getData().setScale(1f);
        // Dibujamos mediante la región de la fuente (el glyph de espacio es un pixel blanco)
        // Alternativa portable: dibujar con ShapeRenderer requiere flush del batch.
        // Para evitar cambios de estado, llenamos con texto de espacios:
        StringBuilder sb = new StringBuilder();
        for (int col = 0; col < 60; col++) sb.append(' ');
        String fila = sb.toString();
        font.getData().setScale(r.width / (60f * 14f), r.height / 30f);
        font.draw(batch, fila, r.x, r.y + r.height);
        batch.setColor(prevColor);
    }

    /** Estimación de ancho de texto (aproximada para BitmapFont con scale variable). */
    private float estimarAnchoTexto(String texto) {
        // Aproximación: ~18px por carácter con scale 3
        return texto.length() * 18f;
    }

    public int getIndiceSeleccionado() { return indiceSeleccionado; }
    public void setIndiceSeleccionado(int i) { this.indiceSeleccionado = i; }
}