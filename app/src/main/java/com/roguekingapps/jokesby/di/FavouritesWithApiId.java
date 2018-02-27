package com.roguekingapps.jokesby.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Defines a {@link Qualifier} which distinguishes URI path for favourites with an API ID.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface FavouritesWithApiId {
}
