package com.frcoding.reatailcashregister.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.frcoding.reatailcashregister.data.dao.InvoiceDao
import com.frcoding.reatailcashregister.data.dao.ItemDao
import com.frcoding.reatailcashregister.data.dao.UserDao
import com.frcoding.reatailcashregister.models.Invoice
import com.frcoding.reatailcashregister.models.Item
import com.frcoding.reatailcashregister.models.User

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Kreiranje nove tabele Invoice
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `Invoice` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `userId` INTEGER NOT NULL,
                `totalPrice` REAL NOT NULL,
                `paymentMethod` TEXT NOT NULL
            )
        """)
    }
}

@Database(entities = [User::class, Item::class, Invoice::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun itemDao(): ItemDao
    abstract fun invoiceDao(): InvoiceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "cash_register_db"
                        )
                        .addMigrations(MIGRATION_1_2)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}