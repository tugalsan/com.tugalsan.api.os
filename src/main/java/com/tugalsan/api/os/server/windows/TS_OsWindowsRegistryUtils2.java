package com.tugalsan.api.os.server.windows;

import com.tugalsan.api.union.client.TGS_Union;
import com.tugalsan.api.union.client.TGS_UnionUtils;
import java.lang.reflect.*;
import java.util.*;
import java.util.prefs.*;

public class TS_OsWindowsRegistryUtils2 {

    public static int HKEY_CURRENT_USER() {
        return 0x80000001;
    }

    public static int HKEY_LOCAL_MACHINE() {
        return 0x80000002;
    }

    public static int REG_SUCCESS() {
        return 0;
    }

    public static int REG_NOTFOUND() {
        return 2;
    }

    public static int REG_ACCESSDENIED() {
        return 5;
    }

    public static int KEY_WOW64_32KEY() {
        return 0x0200;
    }

    public static final int KEY_WOW64_64KEY() {
        return 0x0100;
    }

    private int KEY_ALL_ACCESS() {
        return 0xf003f;
    }

    private int KEY_READ() {
        return 0x20019;
    }

    private TS_OsWindowsRegistryUtils2() throws NoSuchMethodException {
        userRoot = Preferences.userRoot();
        systemRoot = Preferences.systemRoot();
        userClass = userRoot.getClass();
        regOpenKey = userClass.getDeclaredMethod("WindowsRegOpenKey", new Class[]{int.class, byte[].class, int.class});
        regOpenKey.setAccessible(true);
        regCloseKey = userClass.getDeclaredMethod("WindowsRegCloseKey", new Class[]{int.class});
        regCloseKey.setAccessible(true);
        regQueryValueEx = userClass.getDeclaredMethod("WindowsRegQueryValueEx", new Class[]{int.class, byte[].class});
        regQueryValueEx.setAccessible(true);
        regEnumValue = userClass.getDeclaredMethod("WindowsRegEnumValue", new Class[]{int.class, int.class, int.class});
        regEnumValue.setAccessible(true);
        regQueryInfoKey = userClass.getDeclaredMethod("WindowsRegQueryInfoKey1", new Class[]{int.class});
        regQueryInfoKey.setAccessible(true);
        regEnumKeyEx = userClass.getDeclaredMethod("WindowsRegEnumKeyEx", new Class[]{int.class, int.class, int.class});
        regEnumKeyEx.setAccessible(true);
        regCreateKeyEx = userClass.getDeclaredMethod("WindowsRegCreateKeyEx", new Class[]{int.class, byte[].class});
        regCreateKeyEx.setAccessible(true);
        regSetValueEx = userClass.getDeclaredMethod("WindowsRegSetValueEx", new Class[]{int.class, byte[].class, byte[].class});
        regSetValueEx.setAccessible(true);
        regDeleteValue = userClass.getDeclaredMethod("WindowsRegDeleteValue", new Class[]{int.class, byte[].class});
        regDeleteValue.setAccessible(true);
        regDeleteKey = userClass.getDeclaredMethod("WindowsRegDeleteKey", new Class[]{int.class, byte[].class});
        regDeleteKey.setAccessible(true);
    }
    private final Preferences userRoot;
    private final Preferences systemRoot;
    private final Class<? extends Preferences> userClass;
    private final Method regOpenKey;
    private final Method regCloseKey;
    private final Method regQueryValueEx;
    private final Method regEnumValue;
    private final Method regQueryInfoKey;
    private final Method regEnumKeyEx;
    private final Method regCreateKeyEx;
    private final Method regSetValueEx;
    private final Method regDeleteKey;
    private final Method regDeleteValue;

    public static TGS_Union<TS_OsWindowsRegistryUtils2> of() {
        try {
            return TGS_Union.of(new TS_OsWindowsRegistryUtils2());
        } catch (NoSuchMethodException ex) {
            return TGS_Union.ofThrowable(ex);
        }
    }

    /**
     * Read a value from key and value name
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param valueName
     * @param wow64 0 for standard registry access (32-bits for 32-bit app,
     * 64-bits for 64-bits app) or KEY_WOW64_32KEY to force access to 32-bit
     * registry view, or KEY_WOW64_64KEY to force access to 64-bit registry view
     * @return the value
     *
     */
    public String readString(int hkey, CharSequence key, CharSequence valueName, int wow64) throws IllegalAccessException, InvocationTargetException {
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            return readString(systemRoot, hkey, key, valueName, wow64);
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            return readString(userRoot, hkey, key, valueName, wow64);
        }
        return TGS_UnionUtils.throwAsRuntimeException(
                TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                "readString",
                "IllegalArgumentException.hkey=" + hkey
        );
    }

    /**
     * Read value(s) and value name(s) form given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param wow64 0 for standard registry access (32-bits for 32-bit app,
     * 64-bits for 64-bits app) or KEY_WOW64_32KEY to force access to 32-bit
     * registry view, or KEY_WOW64_64KEY to force access to 64-bit registry view
     * @return the value name(s) plus the value(s)
     */
    public Map<String, String> readStringValues(int hkey, String key, int wow64) throws IllegalAccessException, InvocationTargetException {
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            return readStringValues(systemRoot, hkey, key, wow64);
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            return readStringValues(userRoot, hkey, key, wow64);
        }
        return TGS_UnionUtils.throwAsRuntimeException(
                TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                "readStringValues",
                "IllegalArgumentException.hkey=" + hkey
        );
    }

    /**
     * Read the value name(s) from a given key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @param wow64 0 for standard registry access (32-bits for 32-bit app,
     * 64-bits for 64-bits app) or KEY_WOW64_32KEY to force access to 32-bit
     * registry view, or KEY_WOW64_64KEY to force access to 64-bit registry view
     * @return the value name(s)
     */
    public List<String> readStringSubKeys(int hkey, String key, int wow64) throws IllegalAccessException, InvocationTargetException {
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            return readStringSubKeys(systemRoot, hkey, key, wow64);
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            return readStringSubKeys(userRoot, hkey, key, wow64);
        }
        return TGS_UnionUtils.throwAsRuntimeException(
                TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                "readStringSubKeys",
                "IllegalArgumentException.hkey=" + hkey
        );
    }

    /**
     * Create a key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     */
    public void createKey(int hkey, String key) throws IllegalAccessException, InvocationTargetException {
        int[] ret;
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            ret = createKey(systemRoot, hkey, key);
            regCloseKey.invoke(systemRoot, new Object[]{ret[0]});
            if (ret[1] != REG_SUCCESS()) {
                TGS_UnionUtils.throwAsRuntimeException(
                        TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                        "createKey",
                        "IllegalArgumentException.rc=" + ret[1] + "  key=" + key
                );
            }
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            ret = createKey(userRoot, hkey, key);
            regCloseKey.invoke(userRoot, new Object[]{ret[0]});
            if (ret[1] != REG_SUCCESS()) {
                TGS_UnionUtils.throwAsRuntimeException(
                        TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                        "createKey",
                        "IllegalArgumentException.rc=" + ret[1] + "  key=" + key);
            }
        }
    }

    /**
     * Write a value in a given key/value name
     *
     * @param hkey
     * @param key
     * @param valueName
     * @param value
     * @param wow64 0 for standard registry access (32-bits for 32-bit app,
     * 64-bits for 64-bits app) or KEY_WOW64_32KEY to force access to 32-bit
     * registry view, or KEY_WOW64_64KEY to force access to 64-bit registry view
     */
    public void writeStringValue(int hkey, CharSequence key, CharSequence valueName, CharSequence value, int wow64) throws IllegalAccessException, InvocationTargetException {
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            writeStringValue(systemRoot, hkey, key, valueName, value, wow64);
            return;
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            writeStringValue(userRoot, hkey, key, valueName, value, wow64);
            return;
        }
    }

    /**
     * Delete a given key
     *
     * @param hkey
     * @param key
     */
    public void deleteKey(int hkey, CharSequence key) throws IllegalAccessException, InvocationTargetException {
        var rc = -1;
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            rc = deleteKey(systemRoot, hkey, key);
        }
        if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            rc = deleteKey(userRoot, hkey, key);
        }
        if (rc != REG_SUCCESS()) {
            TGS_UnionUtils.throwAsRuntimeException(
                    TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                    "deleteKey",
                    "IllegalArgumentException.key=" + key
            );
        }
    }

    /**
     * delete a value from a given key/value name
     *
     * @param hkey
     * @param key
     * @param value
     * @param wow64 0 for standard registry access (32-bits for 32-bit app,
     * 64-bits for 64-bits app) or KEY_WOW64_32KEY to force access to 32-bit
     * registry view, or KEY_WOW64_64KEY to force access to 64-bit registry view
     */
    public void deleteValue(int hkey, CharSequence key, CharSequence value, int wow64) throws IllegalAccessException, InvocationTargetException {
        var rc = -1;
        if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
            rc = deleteValue(systemRoot, hkey, key, value, wow64);
        } else if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
            rc = deleteValue(userRoot, hkey, key, value, wow64);
        }
        if (rc != REG_SUCCESS()) {
            TGS_UnionUtils.throwAsRuntimeException(
                    TS_OsWindowsRegistryUtils2.class.getSimpleName(),
                    "deleteValue",
                    "IllegalArgumentException.rc=" + rc + "  key=" + key + "  value=" + value
            );
        }
    }

    //========================================================================
    private int deleteValue(Preferences root, int hkey, CharSequence key, CharSequence value, int wow64) throws IllegalAccessException, InvocationTargetException {
        var handles = (int[]) regOpenKey.invoke(root, new Object[]{
            hkey, toCstr(key), KEY_ALL_ACCESS() | wow64
        });
        if (handles[1] != REG_SUCCESS()) {
            return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
        }
        var rc = ((Integer) regDeleteValue.invoke(root, new Object[]{
            handles[0], toCstr(value)
        }));
        regCloseKey.invoke(root, new Object[]{handles[0]});
        return rc;
    }

    //========================================================================
    private int deleteKey(Preferences root, int hkey, CharSequence key) throws IllegalAccessException, InvocationTargetException {
        return ((Integer) regDeleteKey.invoke(root, new Object[]{
            hkey, toCstr(key)
        }));// can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
    }

    //========================================================================
    private String readString(Preferences root, int hkey, CharSequence key, CharSequence value, int wow64) throws IllegalAccessException, InvocationTargetException {
        var handles = (int[]) regOpenKey.invoke(root, new Object[]{
            hkey, toCstr(key), KEY_READ() | wow64});
        if (handles[1] != REG_SUCCESS()) {
            return null;
        }
        var valb = (byte[]) regQueryValueEx.invoke(root, new Object[]{
            handles[0], toCstr(value)
        });
        regCloseKey.invoke(root, new Object[]{handles[0]});
        return (valb != null ? new String(valb).trim() : null);
    }

    //========================================================================
    private Map<String, String> readStringValues(Preferences root, int hkey, CharSequence key, int wow64) throws IllegalAccessException, InvocationTargetException {
        var results = new HashMap<String, String>();
        var handles = (int[]) regOpenKey.invoke(root, new Object[]{
            hkey, toCstr(key), KEY_READ() | wow64});
        if (handles[1] != REG_SUCCESS()) {
            return null;
        }
        var info = (int[]) regQueryInfoKey.invoke(root, new Object[]{
            handles[0]
        });
        var count = info[2]; // count
        var maxlen = info[3]; // value length max
        for (var index = 0; index < count; index++) {
            var name = (byte[]) regEnumValue.invoke(root, new Object[]{
                handles[0], index, maxlen + 1}
            );
            var value = readString(hkey, key, new String(name), wow64);
            results.put(new String(name).trim(), value);
        }
        regCloseKey.invoke(root, new Object[]{handles[0]});
        return results;
    }

    //========================================================================
    private List<String> readStringSubKeys(Preferences root, int hkey, CharSequence key, int wow64) throws IllegalAccessException, InvocationTargetException {
        List<String> results = new ArrayList();
        var handles = (int[]) regOpenKey.invoke(root, new Object[]{
            hkey, toCstr(key), KEY_READ() | wow64
        });
        if (handles[1] != REG_SUCCESS()) {
            return null;
        }
        var info = (int[]) regQueryInfoKey.invoke(root, new Object[]{
            handles[0]});

        var count = info[0]; // Fix: info[2] was being used here with wrong results. Suggested by davenpcj, confirmed by Petrucio
        var maxlen = info[3]; // value length max
        for (var index = 0; index < count; index++) {
            var name = (byte[]) regEnumKeyEx.invoke(root, new Object[]{
                handles[0], index, maxlen + 1
            });
            results.add(new String(name).trim());
        }
        regCloseKey.invoke(root, new Object[]{handles[0]});
        return results;
    }

    //========================================================================
    private int[] createKey(Preferences root, int hkey, CharSequence key) throws IllegalAccessException, InvocationTargetException {
        return (int[]) regCreateKeyEx.invoke(root, new Object[]{
            hkey, toCstr(key)
        });
    }

    //========================================================================
    private void writeStringValue(Preferences root, int hkey, CharSequence key, CharSequence valueName, CharSequence value, int wow64) throws IllegalAccessException, InvocationTargetException {
        var handles = (int[]) regOpenKey.invoke(root, new Object[]{
            hkey, toCstr(key), KEY_ALL_ACCESS() | wow64});
        regSetValueEx.invoke(root, new Object[]{
            handles[0], toCstr(valueName), toCstr(value)
        });
        regCloseKey.invoke(root, new Object[]{handles[0]});
    }

    //========================================================================
    // utility
    private byte[] toCstr(CharSequence str) {
        var result = new byte[str.length() + 1];
        for (var i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
}
