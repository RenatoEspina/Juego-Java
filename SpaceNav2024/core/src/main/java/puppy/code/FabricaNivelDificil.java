package puppy.code;
import com.badlogic.gdx.graphics.Texture;
import java.util.Random;

public class FabricaNivelDificil implements FabricaEnemigos {
    // Definimos las 8 variables necesarias
    private Texture texNormal, texNormalDebil;
    private Texture texGrande, texGrandeDebil;
    private Texture texChico, texChicoDebil;
    private Texture texJefe, texJefeDebil;

    // EL CONSTRUCTOR DEBE RECIBIR LAS 8 TEXTURAS
    public FabricaNivelDificil(Texture tN, Texture tND, Texture tG, Texture tGD,
                               Texture tC, Texture tCD, Texture tJ, Texture tJD) {
        this.texNormal = tN;
        this.texNormalDebil = tND;
        this.texGrande = tG;
        this.texGrandeDebil = tGD;
        this.texChico = tC;
        this.texChicoDebil = tCD;
        this.texJefe = tJ;
        this.texJefeDebil = tJD;
    }

    // --- MÉTODOS DE CREACIÓN ---

    @Override
    public EnemigoBase crearEnemigoChico(float x, float y) {
        // Usa la textura correcta (tC y tCD)
        return new EnemigoChico(new MovimientoRebote(500, 500), texChico, texChicoDebil, (int)x, (int)y);
    }

    @Override
    public EnemigoBase crearEnemigoNormal(float x, float y) {
        return new EnemigoNormal(new MovimientoRebote(250, 250), texNormal, texNormalDebil, (int)x, (int)y);
    }

    @Override
    public EnemigoBase crearEnemigoGrande(float x, float y) {
        return new EnemigoGrande(new MovimientoRebote(150, 150), texGrande, texGrandeDebil, (int)x, (int)y);
    }

    @Override
    public EnemigoBase crearEnemigoAleatorio(float x, float y) {
        Random r = new Random();
        int suerte = r.nextInt(100);

        if (suerte < 50) return crearEnemigoNormal(x, y);
        else if (suerte < 80) return crearEnemigoChico(x, y);
        else return crearEnemigoGrande(x, y);
    }

    @Override
    public EnemigoBase crearEnemigoJefe(float x, float y, PantallaJuego juego) {
        // Usa la textura del Jefe que recibimos en el constructor
        return new EnemigoJefe(juego, this, texJefe, texJefeDebil, (int)x, (int)y);
    }
}
