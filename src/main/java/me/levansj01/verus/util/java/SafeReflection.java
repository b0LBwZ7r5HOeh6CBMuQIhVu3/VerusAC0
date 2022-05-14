package me.levansj01.verus.util.java;

import com.google.common.base.*;
import org.bukkit.command.*;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.*;

public class SafeReflection
{
    public static Object getLocalField(final String s, final Object o, final String s2) {
        return fetch(access(Class.forName(s), s2), o);
    }
    
    public static Object fetch(final Field field, final Object o, final Class clazz) {
        return field.get(o);
    }
    
    public static Field access(final Class clazz, final String... array) {
        if (0 < array.length) {
            final Field declaredField = clazz.getDeclaredField(array[0]);
            declaredField.setAccessible(true);
            return declaredField;
        }
        throw new IllegalArgumentException(clazz.getSimpleName() + ":" + Joiner.on(",").join((Object[])array));
    }
    
    public static Map getKnownCommands(final SimpleCommandMap simpleCommandMap) {
        return (Map)getLocalField(SimpleCommandMap.class, simpleCommandMap, "knownCommands");
    }
    
    public static Method access(final Class clazz, final String s, final Class... array) {
        final Method declaredMethod = clazz.getDeclaredMethod(s, (Class[])array);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
    
    public static Object fetch(final Field field, final Object o) {
        return field.get(o);
    }
    
    public static void set(final Field field, final Object o, final Object o2) {
        field.set(o, o2);
    }
    
    public static Class findClass(final String... array) {
        if (0 < array.length) {
            return Class.forName(array[0]);
        }
        throw new IllegalArgumentException("Could not find a class: " + Joiner.on(", ").join((Object[])array));
    }
    
    public static Field access(final Class clazz, final String s) {
        final Field declaredField = clazz.getDeclaredField(s);
        declaredField.setAccessible(true);
        return declaredField;
    }
    
    public static Constructor constructor(final Class clazz, final Class... array) {
        final Constructor declaredConstructor = clazz.getDeclaredConstructor((Class[])array);
        declaredConstructor.setAccessible(true);
        return declaredConstructor;
    }
    
    public static Object fetchConstructor(final Constructor constructor, final Object... array) {
        return constructor.newInstance(array);
    }
    
    public static PacketPlayOutNamedEntitySpawn spawn(final int n, final UUID uuid, final int n2, final int n3, final int n4, final byte b, final byte b2, final int n5, final DataWatcher dataWatcher, final List list) {
        throw new UnsupportedOperationException();
    }
    
    public static Object getLocalField(final Class clazz, final Object o, final String... array) {
        return fetch(access(clazz, array), o);
    }
    
    public static Method access(final String[] array, final String s, final Class... array2) {
        final Throwable t = null;
        if (0 < array.length) {
            final Method declaredMethod = Class.forName(array[0]).getDeclaredMethod(s, (Class<?>[])array2);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        }
        throw new IllegalArgumentException(String.join(",", (CharSequence[])array) + ":" + s + "(", t);
    }
    
    public static Object execute(final Method method, final Object o, final Object... array) {
        return method.invoke(o, array);
    }
    
    public static Object getLocalField(final String[] array, final Object o, final String s) {
        final Throwable t = null;
        if (0 < array.length) {
            final Field declaredField = Class.forName(array[0]).getDeclaredField(s);
            declaredField.setAccessible(true);
            return declaredField.get(o);
        }
        throw new IllegalArgumentException(String.join(",", (CharSequence[])array) + ":" + s, t);
    }
    
    public static void setLocalField(final Class clazz, final Object o, final String s, final Object o2) {
        set(access(clazz, s), o, o2);
    }
    
    public static SimpleCommandMap getCommandMap() {
        return (SimpleCommandMap)getLocalField(Bukkit.getServer().getClass(), Bukkit.getServer(), "commandMap");
    }
}
