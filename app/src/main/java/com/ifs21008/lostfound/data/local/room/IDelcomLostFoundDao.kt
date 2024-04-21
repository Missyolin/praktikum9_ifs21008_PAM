package com.ifs21008.lostfound.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ifs21008.lostfound.data.local.entity.DelcomLostFoundEntity

@Dao
interface IDelcomTodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(delcomTodo: DelcomLostFoundEntity)

    @Delete
    fun delete(delcomTodo: DelcomLostFoundEntity)

    @Query("SELECT * FROM delcom_lostfound WHERE id = :id LIMIT 1")
    fun get(id: Int): LiveData<DelcomLostFoundEntity?>

    @Query("SELECT * FROM delcom_lostfound ORDER BY created_at DESC")
    fun getAllTodos(): LiveData<List<DelcomLostFoundEntity>?>
}