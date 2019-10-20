package de.brabb3l.endustry;

import java.lang.reflect.*;
import java.util.*;

/**
 * Generic static methods related Java Reflection. 
 * @author javaguides.net
 *
 */
public class Reflections {

    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied
     * name and parameter types. Searches all superclasses up to {@code Object}.
     * <p>
     * Returns {@code null} if no {@link Method} can be found.
     * 
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the method
     * @param paramTypes
     *            the parameter types of the method (may be {@code null} to
     *            indicate any signature)
     * @return the Method object, or {@code null} if none found
     */

    public static Method findMethod(Class < ? > clazz, String name) {
        Class < ? > searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() :
                searchType.getDeclaredMethods());
            for (Method method: methods) {
                if (name.equals(method.getName())) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Invoke the specified {@link Method} against the supplied target object
     * with no arguments. The target object can be {@code null} when invoking a
     * static {@link Method}.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException}.
     * 
     * @param method
     *            the method to invoke
     * @param target
     *            the target object to invoke the method on
     * @return the invocation result, if any
     * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
     */

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, new Object[0]);
    }

    /**
     * Get Method by passing class and method name.
     * 
     * @param clazz
     * @param methodName
     * @return
     */
    public static Method getMethod(Class clazz, String methodName) {
        final Method[] methods = clazz.getMethods();
        for (Method method: methods) {
            if (method.getName().equals(methodName)) {
                method.setAccessible(true);
                return method;
            }
        }
        return null;
    }

    /**
     * Invoke the specified {@link Method} against the supplied target object
     * with the supplied arguments. The target object can be {@code null} when
     * invoking a static {@link Method}.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException}.
     * 
     * @param method
     *            the method to invoke
     * @param target
     *            the target object to invoke the method on
     * @param args
     *            the invocation arguments (may be {@code null})
     * @return the invocation result, if any
     */

    public static Object invokeMethod(Method method, Object target, Object...args) {
        try {
            return method.invoke(target, args);
        } catch (Exception ex) {
            handleReflectionException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }

    /**
     * Determine whether the given method is an "equals" method.
     * 
     * @see java.lang.Object#equals(Object)
     */
    public static boolean isEqualsMethod(Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class < ? > [] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
     * Determine whether the given method is a "hashCode" method.
     * 
     * @see java.lang.Object#hashCode()
     */
    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode") && method.getParameterCount() == 0);
    }

    /**
     * Determine whether the given method is a "toString" method.
     * 
     * @see java.lang.Object#toString()
     */
    public static boolean isToStringMethod(Method method) {
        return (method != null && method.getName().equals("toString") && method.getParameterCount() == 0);
    }

    /**
     * Determine whether the given method is originally declared by
     * {@link java.lang.Object}.
     */
    public static boolean isObjectMethod(Method method) {
        if (method == null) {
            return false;
        }
        try {
            Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Make the given method accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * 
     * @param method
     *            the method to make accessible
     * @see java.lang.reflect.Method#setAccessible
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) &&
            !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * Finds all setters in the given class and super classes.
     */
    public static List < Method > getSetters(Class < ? > clazz) {
        Method[] methods = clazz.getMethods();

        List < Method > list = new ArrayList < Method > ();

        for (Method method: methods) {
            if (isSetter(method)) {
                list.add(method);
            }
        }

        return list;
    }

    public static boolean isSetter(Method method) {
        return method.getReturnType().equals(Void.TYPE) &&
            !Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 1;
    }

    /**
     * Finds a public method of the given name, regardless of its parameter
     * definitions,
     */
    public static Method getPublicMethodNamed(Class c, String methodName) {
        for (Method m: c.getMethods())
            if (m.getName().equals(methodName))
                return m;
        return null;
    }

    public static ClassLoader getDefaultClassLoader() {
        try {
            return Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            /* ignore */
        }
        return ClassLoader.getSystemClassLoader();
    }

    public static boolean isPublic(Class < ? > clazz) {
        return Modifier.isPublic(clazz.getModifiers());
    }

    public static boolean isPublic(Member member) {
        return Modifier.isPublic(member.getModifiers());
    }

    public static boolean isPrivate(Class < ? > clazz) {
        return Modifier.isPrivate(clazz.getModifiers());
    }

    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(member.getModifiers());
    }

    public static boolean isNotPrivate(Member member) {
        return !isPrivate(member);
    }

    public static boolean isAbstract(Class < ? > clazz) {
        return Modifier.isAbstract(clazz.getModifiers());
    }

    public static boolean isAbstract(Member member) {
        return Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isStatic(Class < ? > clazz) {
        return Modifier.isStatic(clazz.getModifiers());
    }

    public static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    public static boolean isNotStatic(Member member) {
        return !isStatic(member);
    }

    /**
     * Determine if the supplied class is an <em>inner class</em> (i.e., a
     * non-static member class).
     *
     * <p>
     * Technically speaking (i.e., according to the Java Language
     * Specification), "an inner class may be a non-static member class, a local
     * class, or an anonymous class." However, this method does not return
     * {@code true} for a local or anonymous class.
     *
     * @param clazz
     *            the class to check; never {@code null}
     * @return {@code true} if the class is an <em>inner class</em>
     */
    public static boolean isInnerClass(Class < ? > clazz) {
        return !isStatic(clazz) && clazz.isMemberClass();
    }

    public static boolean returnsVoid(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    /**
     * Determine if the supplied object is an array.
     *
     * @param obj
     *            the object to test; potentially {@code null}
     * @return {@code true} if the object is an array
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with
     * the supplied {@code name} and/or {@link Class type}. Searches all
     * superclasses up to {@link Object}.
     * 
     * @param clazz
     *            the class to introspect
     * @param name
     *            the name of the field (may be {@code null} if type is
     *            specified)
     * @param type
     *            the type of the field (may be {@code null} if name is
     *            specified)
     * @return the corresponding Field object, or {@code null} if not found
     */

    public static Field findField(Class < ? > clazz, String name) {
        Class < ? > searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field: fields) {
                if ((name == null || name.equals(field.getName()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * Set the field represented by the supplied {@link Field field object} on
     * the specified {@link Object target object} to the specified
     * {@code value}. In accordance with {@link Field#set(Object, Object)}
     * semantics, the new value is automatically unwrapped if the underlying
     * field has a primitive type.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException(Exception)}.
     * 
     * @param field
     *            the field to set
     * @param target
     *            the target object on which to set the field
     * @param value
     *            the value to set (may be {@code null})
     */
    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * Get the field represented by the supplied {@link Field field object} on
     * the specified {@link Object target object}. In accordance with
     * {@link Field#get(Object)} semantics, the returned value is automatically
     * wrapped if the underlying field has a primitive type.
     * <p>
     * Thrown exceptions are handled via a call to
     * {@link #handleReflectionException(Exception)}.
     * 
     * @param field
     *            the field to get
     * @param target
     *            the target object from which to get the field
     * @return the field's current value
     */

    public static Object getField(Field field, Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }

    /**
     * Handle the given reflection exception. Should only be called if no
     * checked exception is expected to be thrown by the target method.
     * <p>
     * Throws the underlying RuntimeException or Error in case of an
     * InvocationTargetException with such a root cause. Throws an
     * IllegalStateException with an appropriate message or
     * UndeclaredThrowableException otherwise.
     * 
     * @param ex
     *            the reflection exception to handle
     */
    public static void handleReflectionException(Exception ex) {
        if (ex instanceof NoSuchMethodException) {
            throw new IllegalStateException("Method not found: " + ex.getMessage());
        }
        if (ex instanceof IllegalAccessException) {
            throw new IllegalStateException("Could not access method: " + ex.getMessage());
        }
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        }
        throw new UndeclaredThrowableException(ex);
    }

    /**
     * Get field from class.
     * 
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getField(Class clazz, String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException ignored) {}
        return null;
    }

    /**
     * Make the given field accessible, explicitly setting it accessible if
     * necessary. The {@code setAccessible(true)} method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * 
     * @param field
     *            the field to make accessible
     * @see java.lang.reflect.Field#setAccessible
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * Determine whether the given field is a "public static final" constant.
     * 
     * @param field
     *            the field to check
     */
    public static boolean isPublicStaticFinal(Field field) {
        int modifiers = field.getModifiers();
        return (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) &&
            Modifier.isFinal(modifiers));
    }

    /**
     * This variant retrieves {@link Class#getDeclaredFields()} from a local
     * cache in order to avoid the JVM's SecurityManager check and defensive
     * array copying.
     * 
     * @param clazz
     *            the class to introspect
     * @return the cached array of fields
     * @throws IllegalStateException
     *             if introspection fails
     * @see Class#getDeclaredFields()
     */
    private static Field[] getDeclaredFields(Class < ? > clazz) {
        return clazz.getDeclaredFields();
    }

    public static Field getFieldByNameIncludingSuperclasses(String fieldName, Class < ? > clazz) {
        Field retValue = null;

        try {
            retValue = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class < ? > superclass = clazz.getSuperclass();

            if (superclass != null) {
                retValue = getFieldByNameIncludingSuperclasses(fieldName, superclass);
            }
        }

        return retValue;
    }

    public static List < Field > getFieldsIncludingSuperclasses(Class < ? > clazz) {
        List < Field > fields = new ArrayList < Field > (Arrays.asList(clazz.getDeclaredFields()));

        Class < ? > superclass = clazz.getSuperclass();

        if (superclass != null) {
            fields.addAll(getFieldsIncludingSuperclasses(superclass));
        }

        return fields;
    }

    /**
     * Make the given constructor accessible, explicitly setting it accessible
     * if necessary. The {@code setAccessible(true)} method is only called when
     * actually necessary, to avoid unnecessary conflicts with a JVM
     * SecurityManager (if active).
     * 
     * @param ctor
     *            the constructor to make accessible
     * @see java.lang.reflect.Constructor#setAccessible
     */
    public static void makeAccessible(Constructor < ? > ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) &&
            !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    /**
     * Obtain an accessible constructor for the given class and parameters.
     * 
     * @param clazz
     *            the clazz to check
     * @param parameterTypes
     *            the parameter types of the desired constructor
     * @return the constructor reference
     * @throws NoSuchMethodException
     *             if no such constructor exists
     * @since 5.0
     */
    public static < T > Constructor < T > accessibleConstructor(Class < T > clazz, Class < ? > ...parameterTypes)
    throws NoSuchMethodException {

        Constructor < T > ctor = clazz.getDeclaredConstructor(parameterTypes);
        makeAccessible(ctor);
        return ctor;
    }
}