/**
 * Copyright 2010 Google Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package com.admob.rocksteady.util;

import java.lang.reflect.*;
import java.util.*;
import java.io.InputStream;
import java.io.FileInputStream;

public class ClassHelper {

  public static ClassLoader getClassLoader(Class clazz) {
    ClassLoader loader = null;
    try {
      loader = Thread.currentThread().getContextClassLoader();
      if (loader != null) {
        return loader;
      }
    } catch (Throwable t) {
      // Noop
    }
    if (clazz != null) {
      try {
        loader = clazz.getClassLoader();
        if (loader != null) {
          return loader;
        }
      } catch (Throwable t) {
        // Noop
      }
    }
    try {
      loader = ClassLoader.getSystemClassLoader();
      if (loader != null) {
        return loader;
      }
    } catch (Throwable t) {
      // Noop
    }
    return loader;
  }

  public static Class loadClass(String classname, Class clazz) throws ClassNotFoundException {
    return getClassLoader(clazz).loadClass(classname);
  }

  public static Field findField(Class clazz, String fieldName) {
    Vector<Field> fields = new Vector<Field>();
    getField(fields, clazz, fieldName);
    if (fields.size() == 0) {
      throw new RuntimeException(
          "ClassHelper.findField: Could not find Field with name for given Class -- ClassName: "
              + clazz.getName() + " -- FieldName: " + fieldName);
    }
    if (fields.size() != 1) {
      throw new RuntimeException(
          "ClassHelper.findField: Could not find Field with name for given Class -- ClassName: "
              + clazz.getClass().getName() + " -- FieldName: " + fieldName);
    }
    return (fields.get(0));
  }

  private static void getField(Vector<Field> inVector, Class clazz, String fieldName) {
    if (clazz == null) {
      return;
    }
    getField(inVector, clazz.getSuperclass(), fieldName);
    Field[] classFields = clazz.getDeclaredFields();
    for (Field field : classFields) {
      if (!(field.getName().equals(fieldName) && isStaticFinal(field))) {
        continue;
      }
      field.setAccessible(true);
      inVector.add(field);
    }
  }

  private static boolean isStaticFinal(Field f) {
    int mods = f.getModifiers();
    return Modifier.isStatic(mods) || Modifier.isFinal(mods);
  }

  public static Properties getPropertiesFile(String name) throws Exception {
    Properties props = new Properties();
    InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    if (ins == null) {
      ins = ClassLoader.getSystemResourceAsStream(name);
    }
    if (ins != null) {
      props.load(ins);
      return props;
    } else {
      try {
        props.load(new FileInputStream(name));
        return props;
      } catch (Exception ex) {
        throw new ROCKSTEADYSystemException("Could not find " + name + " in classpath", ex);
      }
    }
  }

  /**
   * Get the underlying class for a type, or null if the type is a variable
   * type.
   *
   * @param type the type
   * @return the underlying class
   */
  public static Class<?> getClass(Type type) {
    if (type instanceof Class) {
      return (Class) type;
    } else if (type instanceof ParameterizedType) {
      return getClass(((ParameterizedType) type).getRawType());
    } else if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      Class<?> componentClass = getClass(componentType);
      if (componentClass != null) {
        return Array.newInstance(componentClass, 0).getClass();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Get the actual type arguments a child class has used to extend a generic
   * base class.
   *
   * @param baseClass the base class
   * @param childClass the child class
   * @return a list of the raw classes for the actual type arguments.
   */
  public static <T> List<Class<?>> getTypeArguments(
      Class<T> baseClass, Class<? extends T> childClass) {
    Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
    Type type = childClass;
    // start walking up the inheritance hierarchy until we hit baseClass
    while (!getClass(type).equals(baseClass)) {
      if (type instanceof Class) {
        // there is no useful information for us in raw types, so just keep
        // going.
        type = ((Class) type).getGenericSuperclass();
      } else {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assert parameterizedType != null;
        Class<?> rawType = (Class) parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
        for (int i = 0; i < actualTypeArguments.length; i++) {
          resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
        }
        if (!rawType.equals(baseClass)) {
          type = rawType.getGenericSuperclass();
        }
      }
    }

    // finally, for each actual type argument provided to baseClass, determine
    // (if possible)
    // the raw class for that type argument.
    Type[] actualTypeArguments;
    if (type instanceof Class) {
      actualTypeArguments = ((Class) type).getTypeParameters();
    } else {
      assert type != null;
      actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
    }
    List<Class<?>> typeArgumentsAsClasses = new ArrayList<Class<?>>();
    // resolve types by chasing down type variables.
    for (Type baseType : actualTypeArguments) {
      while (resolvedTypes.containsKey(baseType)) {
        baseType = resolvedTypes.get(baseType);
      }
      typeArgumentsAsClasses.add(getClass(baseType));
    }
    return typeArgumentsAsClasses;
  }
}
