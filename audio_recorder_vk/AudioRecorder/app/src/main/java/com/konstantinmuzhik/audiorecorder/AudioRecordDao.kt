package com.konstantinmuzhik.audiorecorder

import androidx.room.*

@Dao
interface AudioRecordDao {
    @Query("SELECT * FROM audioRecords")
    fun getAll(): List<AudioRecord>

    @Insert
    fun insert(vararg audioRecord: AudioRecord)

//    @Query("DELETE FROM audioRecords WHERE id=:id")
//    fun delete(id: Int)

//    @Delete
//    fun delete(audioRecords: Array<AudioRecord>)

//    @Update
//    fun update(audioRecord: AudioRecord)
}