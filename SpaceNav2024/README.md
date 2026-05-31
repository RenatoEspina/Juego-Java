# Heaven Piercing SpaceShip

## Descripción

Heaven Piercing SpaceShip es un juego de acción espacial desarrollado en Java utilizando la librería LibGDX. El jugador controla una nave-taladro que debe enfrentarse a diferentes tipos de enemigos, esquivar obstáculos y disparar para sobrevivir el mayor tiempo posible. Esta nave puede conectarse y controlar partes enemigas para mejorar temporalmente su poder y subir el contador de vidas con cada absorción enemiga. El objetivo es ascender hacia el espacio, derrotando enemigos y destruyendo naves nodrizas en distintos niveles.

## Cómo compilar y ejecutar

Este juego ha sido desarrollado en Java y puede ejecutarse descomprimiendo el archivo .zip del proyecto y ejectutando el archivo "SpaceNav2024-1.0.0.jar". 

### Requisitos previos:
- Tener java 17 o 21.

### Pasos para compilar y ejecutar:

1. **Descarga y descomprime el** archivo `.zip` en una carpeta de tu elección.
2. Ejecuta el archivo "SpaceNav2024-1.0.0.jar",

#### **En caso de error por java desactualizado seguir con el paso 3:**
3. Ingresa a este link y continúa con la instalación:
    - "https://download.oracle.com/java/21/archive/jdk-21.0.8_windows-x64_bin.exe"
        
3. **Ejecuta el programa**

## Funcionalidades

### Funcionando correctamente:

- Controlar la nave del jugador con movimientos horizontales y verticales.
- Disparar proyectiles para atacar enemigos.
- Enemigos con distintos tipos y comportamientos.
- Gestión de vidas y colisiones con enemigos y balas.

### Problemas conocidos:

- En ocasiones los enemigos pueden aparecer fuera de la pantalla, impidiendo la continuidad del nivel y forzando a reiniciar el juego.

### A mejorar:

- Implementar un menú de pausa.
- Añadir que enemigos específicos puedan disparar.

## Ejemplo de uso

**Iniciar el juego**

- El juego presenta una pantalla de inicio donde debe presionar cualquier botón para comenzar.

**Pantalla del nivel**

- En esta pantalla se encuentra nuestra nave y los distintos enemigos correspondientes al primer nivel, además en el HUD se encuentran las vidas, ronda (nivel), highscore (corresponde al puntaje más alto obtenido mientras el juego este ejecutado) y score (puntaje de la partida actual).

**Controles y Enfrentamiento**

- Usa las teclas de dirección para mover la nave y la barra espaciadora para disparar, los enemigos aparecen automáticamente y se mueven con distintos patrones, además, luego de dejarlos a poca vida, cambiará su Sprite y el jugador podrá acoplarse a la nave enemiga acercandoce a esta, subiendo 2 vidas con cada fusión y mejorando la velocidad o el tipo de disparo del jugador, dependiendo del enemigo. 

**Fin del juego**

- El juego continúa generando enemigos y proyectiles hasta consumas todas tus vidas o hasta que llegues a la etapa del jefe final en donde dependiendo de cómo derrotes el jefe podrás llegar a uno de los 2 posibles finales.
