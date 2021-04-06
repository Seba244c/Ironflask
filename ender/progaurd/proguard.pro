-injars ender-0.0.1-SNAPSHOT.jar
-outjars ender-0.0.1-SNAPSHOT-pro.jar

-libraryjars 'C:\Program Files\Java\jdk-11.0.8\jmods\java.base.jmod'(!**.jar;!module-info.class)
-libraryjars './libs/'

-optimizationpasses 3
-allowaccessmodification
-printmapping myapplication.map
-overloadaggressively
-repackageclasses ''
-dontwarn java.awt.*



-keep public class dk.sebsa.ender.game.Main {
    public static void main(java.lang.String[]);
}
