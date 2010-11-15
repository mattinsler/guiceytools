package com.mattinsler.guiceytools;

import com.google.inject.AbstractModule;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/9/10
 * Time: 8:00 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractConfigureOnceModule<T> extends AbstractModule {
    protected T key;

	public AbstractConfigureOnceModule(T key) {
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
