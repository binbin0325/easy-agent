package com.easy.lsy.agent.core.plugin.bytebuddy;

import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author binbin.zhang 2019.1.17
 */
public abstract class AbstractJunction<V> implements ElementMatcher.Junction<V> {
    @Override
    public <U extends V> Junction<U> and(ElementMatcher<? super U> other) {
        return new Conjunction<U>(this, other);
    }

    @Override
    public <U extends V> Junction<U> or(ElementMatcher<? super U> other) {
        return new Disjunction<U>(this, other);
    }
}