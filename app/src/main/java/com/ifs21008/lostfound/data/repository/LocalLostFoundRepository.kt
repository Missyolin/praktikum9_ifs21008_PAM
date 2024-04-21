package com.ifs21008.lostfound.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.ifs21008.lostfound.data.local.entity.DelcomLostFoundEntity
import com.ifs21008.lostfound.data.local.room.DelcomLostFoundDatabase
import com.ifs21008.lostfound.data.local.room.IDelcomTodoDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LocalLostFoundRepository(context: Context) {
    private val mDelcomTodoDao: IDelcomTodoDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = DelcomLostFoundDatabase.getInstance(context)
        mDelcomTodoDao = db.delcomLostFoundDao()
    }
    fun getAllLostFound(): LiveData<List<DelcomLostFoundEntity>?> = mDelcomTodoDao.getAllTodos()

    fun get(todoId: Int): LiveData<DelcomLostFoundEntity?> = mDelcomTodoDao.get(todoId)

    fun insert(todo: DelcomLostFoundEntity) {
        executorService.execute { mDelcomTodoDao.insert(todo) }
    }

    fun delete(todo: DelcomLostFoundEntity) {
        executorService.execute { mDelcomTodoDao.delete(todo) }
    }

    companion object {
        @Volatile
        private var INSTANCE: LocalLostFoundRepository? = null
        fun getInstance(
            context: Context
        ): LocalLostFoundRepository {
            synchronized(LocalLostFoundRepository::class.java) {
                INSTANCE = LocalLostFoundRepository(
                    context
                )
            }
            return INSTANCE as LocalLostFoundRepository
        }
    }

}