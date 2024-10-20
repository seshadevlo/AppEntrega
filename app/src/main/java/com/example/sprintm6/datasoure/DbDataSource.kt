package com.example.sprintm6.datasoure

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sprintm6.model.User
import com.example.sprintm6.model.UserDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class DbDataSource: RoomDatabase() {
    abstract fun userDao(): UserDao
}