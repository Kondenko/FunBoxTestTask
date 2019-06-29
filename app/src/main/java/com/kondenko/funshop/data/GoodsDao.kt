package com.kondenko.funshop.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kondenko.funshop.entities.Good
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface GoodsDao : GoodsProvider {

    @Query("SELECT * FROM good")
    override fun getGoods(): Observable<Good>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(good: Good): Completable

}