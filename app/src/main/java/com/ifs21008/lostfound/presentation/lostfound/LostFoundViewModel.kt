package com.ifs21008.lostfound.presentation.lostfound

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ifs18005.delcomtodo.data.remote.response.DataAddTodoResponse
import com.ifs18005.delcomtodo.data.remote.response.DelcomResponse
import com.ifs18005.delcomtodo.data.remote.response.DelcomTodoResponse
import com.ifs21008.lostfound.data.local.entity.DelcomLostFoundEntity
import com.ifs21008.lostfound.data.remote.MyResult
import com.ifs21008.lostfound.data.repository.LocalLostFoundRepository
import com.ifs21008.lostfound.data.repository.LostFoundRepository
import com.ifs21008.lostfound.presentation.ViewModelFactory

class LostFoundViewModel (
    private val lostFoundRepository : LostFoundRepository,
    private val localLostFoundRepository: LocalLostFoundRepository
) : ViewModel() {

    fun getLostFound(lostfoundId: Int) : LiveData<MyResult<DelcomTodoResponse>> {
        return lostFoundRepository.getDetail(lostfoundId).asLiveData()
    }

    fun postLostFound(
        title: String,
        description : String,
        status: String,
    ) : LiveData<MyResult<DataAddTodoResponse>>{
        return lostFoundRepository.postLostFound(
            title,
            description,
            status
        ).asLiveData()
    }

    fun putLostFound(
        lostfoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ) : LiveData<MyResult<DelcomResponse>> {
        return lostFoundRepository.putLostFound(
            lostfoundId,
            title,
            description,
            status,
            isCompleted
        ).asLiveData()
    }

    fun delete(lostfoundId: Int) : LiveData<MyResult<DelcomResponse>>{
        return lostFoundRepository.delete(lostfoundId).asLiveData()
    }

    fun getLocalLostFound() : LiveData<List<DelcomLostFoundEntity>?>{
        return localLostFoundRepository.getAllLostFound()
    }

    fun getLocalLostFound(lostfoundId: Int) : LiveData<DelcomLostFoundEntity?> {
        return localLostFoundRepository.get(lostfoundId)
    }

    fun insertLocalLostFound(lostfound: DelcomLostFoundEntity) {
        localLostFoundRepository.insert(lostfound)
    }

    fun deleteLocalLostFound(lostfound: DelcomLostFoundEntity){
        localLostFoundRepository.delete(lostfound)
    }

    companion object {
        @Volatile
        private var INSTANCE: LostFoundViewModel? = null
        fun getInstance (
            lostFoundRepository: LostFoundRepository,
            localLostFoundRepository: LocalLostFoundRepository
        ) : LostFoundViewModel {
            synchronized(ViewModelFactory::class.java) {
                INSTANCE = LostFoundViewModel(
                    lostFoundRepository,
                    localLostFoundRepository
                )
            }
            return INSTANCE as LostFoundViewModel
        }
    }
}