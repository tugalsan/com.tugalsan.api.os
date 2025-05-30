module com.tugalsan.api.os {
    requires java.prefs;
    requires jdk.management;
    requires com.github.oshi;
    requires com.sun.jna.platform;
    requires com.tugalsan.api.string;
    requires com.tugalsan.api.cast;
    requires com.tugalsan.api.union;
    requires com.tugalsan.api.charset;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.function;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.random;
    requires com.tugalsan.api.stream;
    exports com.tugalsan.api.os.server;
}
