package puppy.code.Enemigo;

import com.badlogic.gdx.graphics.Texture;

import puppy.code.Factory.FabricaEnemigos;
import puppy.code.MovementStrategy.EstrategiaMovimiento;
import puppy.code.Pantalla.PantallaJuego;

public class EnemigoBuilder {

    // Parámetros obligatorios
    private EstrategiaMovimiento estrategia;
    private Texture texNormal;
    private Texture texDebil;
    private int x;
    private int y;

    // Parámetros con valores por defecto
    private int vidas = 5;
    private int tamano = 100;

    // Tipo de enemigo a construir
    private TipoEnemigo tipo = TipoEnemigo.NORMAL;

    // Referencia al juego (solo necesaria para el Jefe)
    private PantallaJuego juego;
    private FabricaEnemigos fabrica;

    public enum TipoEnemigo {
        CHICO, NORMAL, GRANDE, JEFE
    }

    // --- MÉTODOS DE CONFIGURACIÓN (fluent interface) ---

    public EnemigoBuilder tipo(TipoEnemigo tipo) {
        this.tipo = tipo;
        return this;
    }

    public EnemigoBuilder conEstrategia(EstrategiaMovimiento estrategia) {
        this.estrategia = estrategia;
        return this;
    }

    public EnemigoBuilder conTexturas(Texture texNormal, Texture texDebil) {
        this.texNormal = texNormal;
        this.texDebil = texDebil;
        return this;
    }

    public EnemigoBuilder enPosicion(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
        return this;
    }

    public EnemigoBuilder conVidas(int vidas) {
        this.vidas = vidas;
        return this;
    }

    public EnemigoBuilder conTamano(int tamano) {
        this.tamano = tamano;
        return this;
    }

    public EnemigoBuilder conJuego(PantallaJuego juego) {
        this.juego = juego;
        return this;
    }

    public EnemigoBuilder conFabrica(FabricaEnemigos fabrica) {
        this.fabrica = fabrica;
        return this;
    }

    // --- BUILD ---

    /**
     * Construye y retorna el EnemigoBase correspondiente al tipo configurado.
     * Lanza IllegalStateException si faltan parámetros obligatorios.
     */
    public EnemigoBase build() {
        validar();

        switch (tipo) {
            case CHICO:
                return new EnemigoChico(estrategia, texNormal, texDebil, x, y);

            case NORMAL:
                return new EnemigoNormal(estrategia, texNormal, texDebil, x, y);

            case GRANDE:
                return new EnemigoGrande(estrategia, texNormal, texDebil, x, y);

            case JEFE:
                if (juego == null || fabrica == null) {
                    throw new IllegalStateException(
                        "EnemigoBuilder: el Jefe requiere conJuego() y conFabrica()");
                }
                return new EnemigoJefe(juego, fabrica, texNormal, texDebil, x, y);

            default:
                throw new IllegalStateException("EnemigoBuilder: tipo desconocido");
        }
    }

    private void validar() {
        if (estrategia == null) {
            throw new IllegalStateException("EnemigoBuilder: falta conEstrategia()");
        }
        if (texNormal == null || texDebil == null) {
            throw new IllegalStateException("EnemigoBuilder: falta conTexturas()");
        }
    }
}