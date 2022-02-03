package com.example.drcdemo.roomdb

import androidx.room.*
import com.example.drcdemo.models.Tasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Tasks) : Long

    @Update
    suspend fun update(item: Tasks)

    @Delete
    suspend fun delete(item: Tasks)

    @Query("SELECT * from tasks")
    suspend fun getTasks(): List<Tasks>

    @Query("SELECT count (*) from tasks")
    suspend fun getTotalCount(): Long

    @Query("SELECT count (*) from tasks where status=1")
    suspend fun getFinishCount(): Long
}