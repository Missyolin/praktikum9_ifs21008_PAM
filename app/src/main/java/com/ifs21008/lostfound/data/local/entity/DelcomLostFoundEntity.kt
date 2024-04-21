package com.ifs21008.lostfound.data.local.entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "delcom_lostfound")
data class DelcomLostFoundEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "is_completed")
    var isCompleted: Int,

    @ColumnInfo(name = "cover")
    var cover: String?,

    @ColumnInfo(name="status")
    var status: String,

    @ColumnInfo(name="is_me")
    var isMe: Int,

    @ColumnInfo(name = "created_at")
    var createdAt: String,

    @ColumnInfo(name = "updated_at")
    var updatedAt: String,
    ) : Parcelable