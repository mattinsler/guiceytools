/**
 *      Copyright (C) 2010 Lowereast Software
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.mattinsler.guiceytools.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.*;

public class Annotations {
	private static class AnnotationProxy<T extends Annotation> implements InvocationHandler {
		private final Class<T> _annotationClass;
		private final List<String> _elements = new ArrayList<String>();
		private final Map<String, Object> _values;
		private final Map<String, Method> _methods = new HashMap<String, Method>();
		
		public AnnotationProxy(Class<T> annotationClass, Map<String, Object> values) {
			_values = (values == null ? new HashMap<String, Object>() : new HashMap<String, Object>(values));
			_annotationClass = annotationClass;
			for (Method method : _annotationClass.getDeclaredMethods()) {
				if (Modifier.isPublic(method.getModifiers()) && Modifier.isAbstract(method.getModifiers()) && method.getParameterTypes().length == 0) {
					String elementName = method.getName();
					_elements.add(elementName);
					_methods.put(elementName, method);
					Object defaultValue = method.getDefaultValue();
					if (defaultValue != null && !_values.containsKey(elementName))
						_values.put(elementName, defaultValue);
				}
			}
		}
		
		private static boolean isEqualToAny(Object lhs, Object... rhs) {
			for (Object item : rhs) {
				if (lhs.equals(item))
					return true;
			}
			return false;
		}
		
		private int valueHashCode(Class<?> elementType, Object value) {
			if (elementType.isArray()) {
				return Arrays.hashCode((Object[])value);
			} else if (isEqualToAny(elementType,
					byte.class, Byte.class,
					char.class, Character.class,
					double.class, Double.class,
					float.class, Float.class,
					int.class, Integer.class,
					long.class, Long.class,
					short.class, Short.class,
					boolean.class, Boolean.class,
					String.class)
					|| elementType.isEnum()
					|| elementType.isAnnotation()) {
				return value.hashCode();
			}
			throw new RuntimeException("This shouldn't happen");
		}
		
		private int calculateHashCode() {
			// This is specified in java.lang.Annotation.
			int hashCode = 0;
			for (String element : _elements)
				hashCode += (127 * element.hashCode()) ^ valueHashCode(_methods.get(element).getReturnType(), _values.get(element));
			return hashCode;
		}
		
		private boolean checkEquals(Object other) throws Throwable {
			if (!_annotationClass.isAssignableFrom(other.getClass()))
				return false;
			for (String element : _elements) {
				Method method = _methods.get(element);
				method.setAccessible(true);
				Object value = _values.get(element);
				Object otherValue = method.invoke(other);
				if (!value.equals(otherValue))
					return false;
			}
			return true;
		}
		
		private String buildString() {
			StringBuilder builder = new StringBuilder("@").append(_annotationClass.getName());
			if (_elements.size() > 0) {
				builder.append("(");
				for (int x = 0; x < _elements.size(); ++x) {
					if (x > 0)
						builder.append(",");
					builder.append(_elements.get(x)).append("=").append(_values.get(_elements.get(x)));
				}
				builder.append(")");
			}
			return builder.toString();
		}
		
		// http://java.sun.com/j2se/1.5.0/docs/api/java/lang/annotation/Annotation.html
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();

			if ("annotationType".equals(methodName)) {
				return _annotationClass;
			} else if ("equals".equals(methodName)) {
				return checkEquals(args[0]);
			} else if ("hashCode".equals(methodName)) {
				return calculateHashCode();
			} else if ("toString".equals(methodName)) {
				return buildString();
			} else if (_elements.contains(methodName)) {
				return _values.get(methodName);
			}
			throw new RuntimeException();
		}
	}
	
	public static <T extends Annotation> T proxy(Class<T> annotationClass) {
		return proxy(annotationClass, null);
	}

	public static <T extends Annotation> T proxy(Class<T> annotationClass, Map<String, Object> values) {
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, values));
	}

    public static <T extends Annotation> T proxy(Class<T> annotationClass, String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, map));
	}

    public static <T extends Annotation> T proxy(Class<T> annotationClass, String key1, Object value1, String key2, Object value2) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key1, value1);
        map.put(key2, value2);
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, map));
	}

    public static <T extends Annotation> T proxy(Class<T> annotationClass, String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, map));
	}

    public static <T extends Annotation> T proxy(Class<T> annotationClass, String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, map));
	}

    public static <T extends Annotation> T proxy(Class<T> annotationClass, String key1, Object value1, String key2, Object value2, String key3, Object value3, String key4, Object value4, String key5, Object value5) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        map.put(key4, value4);
        map.put(key5, value5);
		return (T)Proxy.newProxyInstance(annotationClass.getClassLoader(), new Class[] { annotationClass, Serializable.class }, new AnnotationProxy(annotationClass, map));
	}
}
