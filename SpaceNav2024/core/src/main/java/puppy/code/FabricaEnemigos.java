package puppy.code;

public interface FabricaEnemigos {
    EnemigoBase crearEnemigoNormal(float x, float y);
    EnemigoBase crearEnemigoGrande(float x, float y);
    EnemigoBase crearEnemigoChico(float x, float y);
    EnemigoBase crearEnemigoJefe(float x, float y, PantallaJuego juego);
    EnemigoBase crearEnemigoAleatorio(float x, float y);
}
