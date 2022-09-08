module com.tugalsan.api.os {
    requires java.prefs;
    requires java.management;
    requires jdk.management;
    requires com.sun.jna.platform;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.charset;
    requires com.tugalsan.api.executable;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.compiler;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.stream;
    exports com.tugalsan.api.os.server;
}
