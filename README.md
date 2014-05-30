Groups-organizer
================

Proyecto de CC5315 - Seguridad de Software: Groups organizer

Instrucciones de configuración del proyecto

1) Descargar IntelliJ Idea Community Edition de su pagina principal: http://www.jetbrains.com/idea/download/

2) Descargar el SDK de Android: http://developer.android.com/sdk/index.html?hl=sk en la sección "Use an existing IDE". Abrir Android SDK Manager en la carpeta que se descarga y usarlo para descargar los archivos de la API nivel 14 y 19. Toma su tiempo hacer esto.

3) Importar el proyecto a IntelliJ y configurar el SDK de Android en el. File -> Project Struncture -> Project SDK -> Edit y seleccionar la ruta al SDK descargado.

4) Tener conexión a internet para que Maven descargue las dependencias del proyecto

5) Configurar las anotaciones en IntelliJ Idea siguiendo el siguiente tutorial partiendo desde la sección "2.3 Annotation Processors" a la 3: http://hintdesk.com/android-introduction-to-androidannotations-maven-in-intellij-idea/

6) Al ejecutar la aplicación se pide elegir un dispositivo, Si hay conectado un celular Android con los drivers intstalados, aparecerá allí para seleccionarlo, de lo contrario se puede crear un dispositivo virtual con las herramientas del Android SDK.

Notas: 
a) Android Annotations es una libreria usada en el proyecto y el tutorial explica como agregarla. La libreria procesa anotaciones echas en el codigo y genera código java en base a ellas.

b) Los archivos generados por las anotaciones quedan en la carpeta target/annotations y el editor muestra que tienen errores, sin embargo se puede ejecutar la aplicación.

c) La configuración de IntelliJ puede ser algo tediosa y "error-prone". A veces puede ser necesario cerrar el editor y abrirlo denuevo o usar el comando Build Project para que se vuelvan a compilar los archivos.


###Sitio web:

En la carpeta *site/* se encuentra el sitio web escrito en PHP y testeado con PHP 5.4.12.
En la carpeta *site/internal/* se encuentra el script de creación de la base de datos *script_creacion.sql*. La base de datos usada en el desarrollo fue MySql.

###Link de aplicación a servidor web:

Para que la aplicación use un servidor web determinado, se debe cambiar el campo URL de la clase *cl.dcc.Groups_Organizer.connection.HttpConnection* a la url base de los scripts php.
