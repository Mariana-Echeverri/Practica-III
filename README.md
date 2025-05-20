ANALIZADOR SINTÁCTICO DE PARTIDAS DE AJEDREZ CON VISUALIZACIÓN DE ÁRBOL BINARIO

DESCRIPCIÓN
Este proyecto implementa un analizador sintáctico para partidas de ajedrez escritas en notación algebraica estándar (SAN). La aplicación valida que cada movimiento cumpla con las reglas definidas en la gramática BNF proporcionada y, si la partida es válida, la representa visualmente como un árbol binario entrelazado por turnos.

INTEGRANTES DEL EQUIPO
- Mariana Echeverri Ramirez
- Nawal Oriana

ESPECIFICACIONES TÉCNICAS
- Lenguaje: Java
- Versión del Compilador: Java SE 17
- IDE Utilizado: IntelliJ IDEA

CARACTERÍSTICAS PRINCIPALES
- Análisis sintáctico de partidas de ajedrez en notación SAN
- Validación de movimientos según la gramática BNF
- Visualización interactiva del árbol binario de jugadas
- Interfaz gráfica intuitiva con sistema de login
- Visualización de tablero de ajedrez
- Exportación del árbol como imagen

REQUISITOS DEL SISTEMA
- Java Runtime Environment (JRE) 17 o superior
- Resolución de pantalla mínima recomendada: 1024x768

INSTALACIÓN Y EJECUCIÓN
1. Clone el repositorio:
   git clone https://github.com/[usuario]/[nombre-repositorio].git

2. Navegue al directorio del proyecto:
   cd [nombre-repositorio]

3. Compile el proyecto:
   javac -d bin src/main/*.java src/modelo/*.java src/analizador/*.java src/util/*.java

4. Ejecute la aplicación:
   java -cp bin main.AplicacionAjedrezGUI

ESTRUCTURA DEL PROYECTO
src/
├── main/
│   ├── AjedrezArbolVisualizer.java  # Visualizador interactivo del árbol
│   ├── AplicacionAjedrezGUI.java    # Clase principal con la interfaz gráfica
│   ├── Arbol.java                   # Implementación del árbol binario
│   └── InterfazAjedrez.java         # Interfaz alternativa simplificada
├── modelo/
│   ├── Jugada.java                  # Representa una jugada individual
│   ├── Partida.java                 # Representa una partida completa
│   ├── ResultadoAnalisis.java       # Almacena resultados del análisis
│   └── Turno.java                   # Representa un turno (jugada blanca y negra)
├── analizador/
│   ├── AnalizadorGramatico.java     # Analiza la gramática y genera el árbol
│   └── AnalizadorSintactico.java    # Valida la sintaxis de las jugadas
└── util/
    └── ValidadorSAN.java            # Utilidades para validar notación SAN

USO DE LA APLICACIÓN

Inicio de Sesión
- Usuario: Entre 3 y 15 caracteres (letras, números y guión bajo)
- Contraseña: Mínimo 6 caracteres (debe incluir letras y números)

Análisis de Partidas
1. Ingrese la notación de la partida en el área de texto
2. Haga clic en "Analizar Partida"
3. Si la partida es válida, se mostrará el árbol binario en una ventana separada
4. Los resultados del análisis se mostrarán en el área inferior

Visualización del Árbol
- Use la rueda del ratón para hacer zoom
- Arrastre para mover el árbol
- Haga clic en un nodo para ver detalles de la jugada

Ejemplo de Partida Válida
1. e4 e5 2. Nf3 Nc6 3. Bb5 a6 4. Ba4 Nf6 5. O-O Be7

GRAMÁTICA BNF IMPLEMENTADA
La aplicación implementa la siguiente gramática BNF para la notación SAN:

<partida> ::= { <turno> }
<turno> ::= <numero_turno> "." <jugada_blanca> [<jugada_negra>]
<jugada_blanca> ::= <jugada>
<jugada_negra> ::= <jugada>
<jugada> ::= <enroque> | <movimiento_pieza> | <movimiento_peon>
<enroque> ::= "O-O" | "O-O-O"
<movimiento_pieza> ::= <pieza> <desambiguacion>? <captura>? <casilla> <promocion>? <jaque_mate>?
<movimiento_peon> ::= <peon_captura> | <peon_avance>
<peon_avance> ::= <casilla> <promocion>? <jaque_mate>?
<peon_captura> ::= <letra> "x" <casilla> <promocion>? <jaque_mate>?
<desambiguacion> ::= <letra> | <numero> | <letra><numero>
<captura> ::= "x"
<casilla> ::= <letra><numero>
<letra> ::= "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h"
<numero> ::= "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8"
<promocion> ::= "=" <pieza>
<jaque_mate> ::= "+" | "#"
<pieza> ::= "K" | "Q" | "R" | "B" | "N"
<numero_turno> ::= <digito> {<digito>}
<digito> ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"

CARACTERÍSTICAS ADICIONALES
- Visualización del Tablero: Representación gráfica del tablero de ajedrez con piezas
- Exportación de Imágenes: Capacidad para guardar el árbol como imagen PNG
- Interfaz Intuitiva: Diseño amigable con colores temáticos de ajedrez
- Zoom y Navegación: Herramientas interactivas para explorar árboles grandes
- Detalles de Jugadas: Panel informativo que muestra detalles de cada movimiento

Link al video:


