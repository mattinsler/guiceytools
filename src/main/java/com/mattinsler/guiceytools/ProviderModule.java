package com.mattinsler.guiceytools;

import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/9/10
 * Time: 7:52 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProviderModule<T> extends ConfigureOnceModule<Key<T>> implements Provider<T> {
    protected ProviderModule(Key<T> key) {
        super(key);
    }
}
