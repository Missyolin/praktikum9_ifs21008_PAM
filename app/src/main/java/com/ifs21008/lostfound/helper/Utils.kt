package com.ifs21008.lostfound.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.ifs18005.delcomtodo.data.remote.response.TodosItemResponse
import com.ifs21008.lostfound.data.local.entity.DelcomLostFoundEntity
import com.ifs21008.lostfound.data.remote.MyResult

class Utils {
    companion object{
        fun <T> LiveData<T>.observeOnce(observer: (T) -> Unit) {
            val observerWrapper = object : Observer<T> {
                override fun onChanged(value: T) {
                    observer(value)
                    if (value is MyResult.Success<*> || value is MyResult.Error) {
                        removeObserver(this)
                    }
                }
            }
            observeForever(observerWrapper)
        }

        fun entitiesToResponses(entities: List<DelcomLostFoundEntity>):
                List<TodosItemResponse> {
            val responses = ArrayList<TodosItemResponse>()
            entities.map {
                val response = TodosItemResponse(
                    cover = it.cover,
                    updatedAt = it.updatedAt,
                    description = it.description,
                    createdAt = it.createdAt,
                    id = it.id,
                    title = it.title,
                    status = it.status,
                    isCompleted = it.isCompleted,
                    isMe = it.isMe,
                )

                responses.add(response)
            }
            return responses
        }
    }
}