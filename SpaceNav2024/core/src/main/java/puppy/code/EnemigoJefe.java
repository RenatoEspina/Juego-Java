package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class EnemigoJefe extends EnemigoBase {
    private PantallaJuego juego;
    private FabricaEnemigos fabrica;

    private float tiempoSpawn = 0;
    private float intervaloSpawn = 3f;

    public EnemigoJefe(PantallaJuego juego, FabricaEnemigos fabrica, Texture tNormal, Texture tDebil, int x, int y) {
        super(new MovimientoEstatico(), 100, tNormal, tDebil, x, y, 350);
        this.juego = juego;
        this.fabrica = fabrica;
    }

    // AQUÍ IMPLEMENTAMOS EL PASO 3 DEL TEMPLATE METHOD
    @Override
    public void realizarComportamientoEspecifico() {
        // 1. Rotación visual (Efecto Boss)
        spr.rotate(15 * Gdx.graphics.getDeltaTime());

        // 2. Lógica de Invocación
        tiempoSpawn += Gdx.graphics.getDeltaTime();

        // Si está débil, invoca más rápido (Furia)
        float intervaloActual = debil ? 1.5f : 3f;

        if (tiempoSpawn >= intervaloActual) {
            invocarEsbirros();
            tiempoSpawn = 0;
        }
    }

    private void invocarEsbirros() {
        // Calcular el CENTRO exacto del Jefe
        float centroX = this.getX() + this.getArea().width / 2;
        float centroY = this.getY() + this.getArea().height / 2;

        // Ajuste: Restamos la mitad del tamaño aprox de un esbirro (ej. 50)
        // para que nazcan exactamente centrados
        float spawnX = centroX - 50;
        float spawnY = centroY - 50;

        // 1. Esbirro normal (Sale del centro)
        EnemigoBase esbirro = fabrica.crearEnemigoAleatorio(spawnX, spawnY);
        juego.agregarEnemigo(esbirro);

        // 2. Refuerzo en modo Furia (Vida baja) -> También sale del centro
        if (debil) {
            EnemigoBase refuerzo = fabrica.crearEnemigoAleatorio(spawnX, spawnY);
            juego.agregarEnemigo(refuerzo);
        }
    }
}
