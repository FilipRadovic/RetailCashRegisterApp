package com.frcoding.reatailcashregister.data.module

import android.content.Context
import com.frcoding.reatailcashregister.data.dao.InvoiceApi
import com.frcoding.reatailcashregister.data.dao.ItemApi
import com.frcoding.reatailcashregister.data.dao.UserApi
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.repository.InvoiceRepository
import com.frcoding.reatailcashregister.repository.ItemRepository
import com.frcoding.reatailcashregister.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8082/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideItemApi(retrofit: Retrofit): ItemApi {
        return retrofit.create(ItemApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInvoiceApi(retrofit: Retrofit): InvoiceApi {
        return retrofit.create(InvoiceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideItemRepository(itemApi: ItemApi): ItemRepository {
        return ItemRepository(itemApi)
    }

    @Provides
    @Singleton
    fun provideInvoiceRepository(invoiceApi: InvoiceApi): InvoiceRepository {
        return InvoiceRepository(invoiceApi)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userApi: UserApi, sessionManager: SessionManager): UserRepository {
        return UserRepository(userApi, sessionManager);
    }
}