Groups-organizer
================

Proyecto de CC5315 - Seguridad de Software: Groups organizer

Instrucciones de configuración del proyecto

1) Descargar IntelliJ Idea Community Edition de su pagina principal: http://www.jetbrains.com/idea/download/

2) Descargar el SDK de Android: http://developer.android.com/sdk/index.html?hl=sk en la sección "Use an existing IDE". Abrir Android SDK Manager (tools/android en linux) en la carpeta que se descarga y usarlo para descargar:

- Android SDK Tools
- Android SDK Platform-Tools
- Android SDK Build-Tools
- Android SDK Platform API 19
- Android SDK Platform API 14
- Android Support library
- Google USB Driver (Solo en Windows)

3) Abrir el proyecto con IntelliJ y esperar a que Maven descargue las dependencias del proyecto

4) Configurar el JDK 1.7 de java y el SDK de Android descargado para el proyecto. File -> Project Structure -> Menú izquierdo Project -> Edit y seleccionar primero el JDK de Java y luego el de Android (la carpeta donde estos se encuentran). Otra forma es File -> Project Structure -> Menú izquierdo SDK's -> Borrar todos los que salgan y agregar Primero el JDK de Java 1.7 y luego el de Android.

5) Ejecutar la aplicación en el menú Run -> Groups Organizer. La primera vez tomará mucho tiempo pues tiene que linkear recursos y generar archivos. En sucesivo debiera ser más rápido.

Notas: 

a) Android Annotations es una libreria usada en el proyecto y el tutorial explica como agregarla. La libreria procesa anotaciones echas en el codigo y genera código java en base a ellas.

b) Los archivos generados por las anotaciones quedan en la carpeta target/annotations y el editor muestra que tienen errores, sin embargo se puede ejecutar la aplicación.

c) Al ejecutar la aplicación se pide elegir un dispositivo, Si hay conectado un celular Android con los drivers instalados, aparecerá allí para seleccionarlo, de lo contrario se puede crear un dispositivo virtual con las herramientas del Android SDK.

d) Si se quiere ejecutar en un celular desde Linux puede que sea necesario:

- El punto 3 de "http://developer.android.com/tools/device.html" Señala como configurar el dispositivo.
- Ejecutar "sudo apt-get install lib32z1 lib32z1-dev lib32stdc++6" en un terminal si aparece el error "ADB not responding, wait more or restart"


###Sitio web:

En la carpeta *site/* se encuentra el sitio web escrito en PHP y testeado con PHP 5.4.12.
En la carpeta *site/internal/* se encuentra el script de creación de la base de datos *script_creacion.sql*. La base de datos usada en el desarrollo fue MySql.

###Link de aplicación a servidor web:

Para que la aplicación use un servidor web determinado, se debe cambiar el campo URL de la clase *cl.dcc.Groups_Organizer.connection.HttpConnection* a la url base de los scripts php.
