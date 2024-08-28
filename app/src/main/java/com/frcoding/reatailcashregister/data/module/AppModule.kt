package com.frcoding.reatailcashregister.data.module

import android.content.Context
import com.frcoding.reatailcashregister.data.AppDatabase
import com.frcoding.reatailcashregister.data.dao.InvoiceDao
import com.frcoding.reatailcashregister.data.dao.ItemDao
import com.frcoding.reatailcashregister.data.dao.UserDao
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    fun provideInvoiceDao(database: AppDatabase): InvoiceDao {
        return database.invoiceDao()
    }

//    @Provides
//    fun provideInvoiceItemDao(database: AppDatabase): InvoiceItemDao {
//        return database.invoiceItemDao()
//    }
}