package com.kondenko.funshop.data

import androidx.room.*
import com.kondenko.funshop.entities.Good
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface GoodsDao : GoodsProvider {

    @Query("SELECT * FROM good")
    override fun getGoods(): Observable<List<Good>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(good: Good): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(good: Good): Completable

}