package com.roguekingapps.jokesby.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Defines a {@link Qualifier} which distinguishes URI path for favourites.
 */
@Qualifier
@Retention(RetentionPolicy.CLASS)
public @interface Favourites {
}
