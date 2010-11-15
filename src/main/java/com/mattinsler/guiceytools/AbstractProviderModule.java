package com.mattinsler.guiceytools;

import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/9/10
 * Time: 8:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProviderModule<T> extends AbstractConfigureOnceModule<Key<T>> implements Provider<T> {
    protected AbstractProviderModule(Key<T> key) {
        super(key);
    }
}
