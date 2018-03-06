package com.roguekingapps.jokesby.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Defines a {@link Qualifier} which distinguishes the database version.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseVersion {
}
