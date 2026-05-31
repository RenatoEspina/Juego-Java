package puppy.code;
import com.badlogic.gdx.graphics.Texture;

public class FabricaNivelFacil implements FabricaEnemigos {
    // Agregamos variables para Chico y Jefe
    private Texture texNormal, texNormalDebil;
    private Texture texGrande, texGrandeDebil;
    private Texture texChico, texChicoDebil;
    private Texture texJefe, texJefeDebil; // Aunque no se use aquí, la estructura la pide

    // Constructor gigante actualizado
    public FabricaNivelFacil(Texture tN, Texture tND, Texture tG, Texture tGD,
                             Texture tC, Texture tCD, Texture tJ, Texture tJD) {
        this.texNormal = tN; this.texNormalDebil = tND;
        this.texGrande = tG; this.texGrandeDebil = tGD;
        this.texChico = tC;  this.texChicoDebil = tCD;
        this.texJefe = tJ;   this.texJefeDebil = tJD;
    }

    @Override
    public EnemigoBase crearEnemigoChico(float x, float y) {
        // ¡Ahora sí usa la textura correcta!
        return new EnemigoChico(new MovimientoRebote(300, 300), texChico, texChicoDebil, (int)x, (int)y);
    }

    @Override
    public EnemigoBase crearEnemigoNormal(float x, float y) {
        return new EnemigoNormal(new MovimientoRebote(150, 150), texNormal, texNormalDebil, (int)x, (int)y);
    }

    @Override
    public EnemigoBase crearEnemigoGrande(float x, float y) {
        return new EnemigoGrande(new MovimientoRebote(80, 80), texGrande, texGrandeDebil, (int)x, (int)y);
    }

    // --- MÉTODOS QUE NO SE DEBEN USAR EN NIVEL FÁCIL ---
    @Override public EnemigoBase crearEnemigoAleatorio(float x, float y) { return crearEnemigoNormal(x, y); }
    @Override public EnemigoBase crearEnemigoJefe(float x, float y, PantallaJuego j) { return null; }
}
