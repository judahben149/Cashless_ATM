package com.shashank.cashlessatm.di

import android.content.Context
import android.content.SharedPreferences
import com.shashank.cashlessatm.domain.SessionManager
import com.shashank.cashlessatm.utils.Constants
import com.shashank.cashlessatm.utils.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Named("shared_prefs")
    @Provides
    @Singleton
    fun providesSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SETTINGS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesPreferencesHelper(
        @Named("shared_prefs") sharedPreferences: SharedPreferences
    ): PreferencesHelper {
        return PreferencesHelper(sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesSessionManager(prefsHelper: PreferencesHelper): SessionManager {
        return SessionManager(prefsHelper)
    }
}