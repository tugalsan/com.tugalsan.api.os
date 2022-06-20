package com.tugalsan.api.os.server;

import java.lang.reflect.*;
import java.util.*;
import java.util.prefs.*;

public class TS_RegistryUtils2 {

    public static final int HKEY_CURRENT_USER() {
        return 0x80000001;
    }

    public static final int HKEY_LOCAL_MACHINE() {
        return 0x80000002;
    }

    public static final int REG_SUCCESS() {
        return 0;
    }

    public static final int REG_NOTFOUND() {
        return 2;
    }

    public static final int REG_ACCESSDENIED() {
        return 5;
    }

    public static final int KEY_WOW64_32KEY() {
        return 0x0200;
    }

    public static final int KEY_WOW64_64KEY() {
        return 0x0100;
    }

    private static final int KEY_ALL_ACCESS() {
        return 0xf003f;
    }

    private static int KEY_READ() {
        return 0x20019;
    }
    private static Preferences userRoot = Preferences.userRoot();
    private static Preferences systemRoot = Preferences.systemRoot();
    private static Class<? extends Preferences> userClass = userRoot.getClass();
    private static Method regOpenKey = null;
    private static Method regCloseKey = null;
    private static Method regQueryValueEx = null;
    private static Method regEnumValue = null;
    private static Method regQueryInfoKey = null;
    private static Method regEnumKeyEx = null;
    private static Method regCreateKeyEx = null;
    private static Method regSetValueEx = null;
    private static Method regDeleteKey = null;
    private static Method regDeleteValue = null;

    static {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private TS_RegistryUtils2() {
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
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String readString(int hkey, CharSequence key, CharSequence valueName, int wow64) {
        try {
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                return readString(systemRoot, hkey, key, valueName, wow64);
            }

            if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                return readString(userRoot, hkey, key, valueName, wow64);
            }
            throw new IllegalArgumentException("hkey=" + hkey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Map<String, String> readStringValues(int hkey, String key, int wow64) {
        try {
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                return readStringValues(systemRoot, hkey, key, wow64);
            }
            if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                return readStringValues(userRoot, hkey, key, wow64);
            }
            throw new IllegalArgumentException("hkey=" + hkey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static List<String> readStringSubKeys(int hkey, String key, int wow64) {
        try {
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                return readStringSubKeys(systemRoot, hkey, key, wow64);
            }
            if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                return readStringSubKeys(userRoot, hkey, key, wow64);
            }
            throw new IllegalArgumentException("hkey=" + hkey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a key
     *
     * @param hkey HKEY_CURRENT_USER/HKEY_LOCAL_MACHINE
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void createKey(int hkey, String key) {
        try {
            int[] ret;
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                ret = createKey(systemRoot, hkey, key);
                regCloseKey.invoke(systemRoot, new Object[]{ret[0]});
            } else if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                ret = createKey(userRoot, hkey, key);
                regCloseKey.invoke(userRoot, new Object[]{ret[0]});
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
            if (ret[1] != REG_SUCCESS()) {
                throw new IllegalArgumentException("rc=" + ret[1] + "  key=" + key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void writeStringValue(int hkey, CharSequence key, CharSequence valueName, CharSequence value, int wow64) {
        try {
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                writeStringValue(systemRoot, hkey, key, valueName, value, wow64);
            } else if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                writeStringValue(userRoot, hkey, key, valueName, value, wow64);
            } else {
                throw new IllegalArgumentException("hkey=" + hkey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete a given key
     *
     * @param hkey
     * @param key
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteKey(int hkey, CharSequence key) {
        try {
            var rc = -1;
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                rc = deleteKey(systemRoot, hkey, key);
            } else if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                rc = deleteKey(userRoot, hkey, key);
            }
            if (rc != REG_SUCCESS()) {
                throw new IllegalArgumentException("rc=" + rc + "  key=" + key);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void deleteValue(int hkey, CharSequence key, CharSequence value, int wow64) {
        try {
            var rc = -1;
            if (Objects.equals(hkey, HKEY_LOCAL_MACHINE())) {
                rc = deleteValue(systemRoot, hkey, key, value, wow64);
            } else if (Objects.equals(hkey, HKEY_CURRENT_USER())) {
                rc = deleteValue(userRoot, hkey, key, value, wow64);
            }
            if (rc != REG_SUCCESS()) {
                throw new IllegalArgumentException("rc=" + rc + "  key=" + key + "  value=" + value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static int deleteValue(Preferences root, int hkey, CharSequence key, CharSequence value, int wow64) {
        try {
            var handles = (int[]) regOpenKey.invoke(root, new Object[]{
                hkey, toCstr(key), KEY_ALL_ACCESS() | wow64});
            if (handles[1] != REG_SUCCESS()) {
                return handles[1];  // can be REG_NOTFOUND, REG_ACCESSDENIED
            }
            var rc = ((Integer) regDeleteValue.invoke(root, new Object[]{
                handles[0], toCstr(value)
            }));
            regCloseKey.invoke(root, new Object[]{handles[0]});
            return rc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static int deleteKey(Preferences root, int hkey, CharSequence key) {
        try {
            var rc = ((Integer) regDeleteKey.invoke(root, new Object[]{
                hkey, toCstr(key)
            }));
            return rc;  // can REG_NOTFOUND, REG_ACCESSDENIED, REG_SUCCESS
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static String readString(Preferences root, int hkey, CharSequence key, CharSequence value, int wow64) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static Map<String, String> readStringValues(Preferences root, int hkey, CharSequence key, int wow64) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static List<String> readStringSubKeys(Preferences root, int hkey, CharSequence key, int wow64) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static int[] createKey(Preferences root, int hkey, CharSequence key) {
        try {
            return (int[]) regCreateKeyEx.invoke(root, new Object[]{
                hkey, toCstr(key)
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    private static void writeStringValue(Preferences root, int hkey, CharSequence key, CharSequence valueName, CharSequence value, int wow64) {
        try {
            var handles = (int[]) regOpenKey.invoke(root, new Object[]{
                hkey, toCstr(key), KEY_ALL_ACCESS() | wow64});
            regSetValueEx.invoke(root, new Object[]{
                handles[0], toCstr(valueName), toCstr(value)
            });
            regCloseKey.invoke(root, new Object[]{handles[0]});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //========================================================================
    // utility
    private static byte[] toCstr(CharSequence str) {
        try {
            var result = new byte[str.length() + 1];
            for (var i = 0; i < str.length(); i++) {
                result[i] = (byte) str.charAt(i);
            }
            result[str.length()] = 0;
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
