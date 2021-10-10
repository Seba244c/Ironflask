-injars editor-0.0.1-SNAPSHOT.jar
-outjars editor-0.0.1-SNAPSHOT-pro.jar

-libraryjars '..\4j\target\jdk-11.0.8\jmods\java.base.jmod'(!**.jar;!module-info.class)
-libraryjars .\libs

-optimizationpasses 3
-allowaccessmodification
-printmapping myapplication.map
-overloadaggressively
-repackageclasses ''
-dontwarn java.awt.*
-dontwarn java.awt.image.*

-keep public class dk.sebsa.ironflask.editor.Main {
    public static void main(java.lang.String[]);
}
