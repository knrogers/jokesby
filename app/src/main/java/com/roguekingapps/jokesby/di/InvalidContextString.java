package com.roguekingapps.jokesby.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Defines a {@link Qualifier} which distinguishes the string to use when logging that a context
 * is null.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface InvalidContextString {
}
