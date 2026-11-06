package org.example.project.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/simple_wallet.db"
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
}
