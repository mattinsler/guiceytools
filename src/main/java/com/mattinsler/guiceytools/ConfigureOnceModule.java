package com.mattinsler.guiceytools;

import com.google.inject.Module;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/9/10
 * Time: 7:54 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConfigureOnceModule<T> implements Module {
	protected T key;

	public ConfigureOnceModule(T key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ConfigureOnceModule<?> && ((ConfigureOnceModule<?>)obj).key.equals(key);
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + "(key=" + key.toString() + ")";
	}
}
