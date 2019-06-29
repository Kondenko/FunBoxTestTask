package com.kondenko.funshop.dependencies

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kondenko.funshop.data.AppDatabase
import com.kondenko.funshop.data.CsvGoodsProvider
import com.kondenko.funshop.data.GoodsDao
import com.kondenko.funshop.data.GoodsProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.module
import timber.log.Timber

class PersistenceModule : ModuleCreator, ScopeCallback {

    private var databaseInitialization: Disposable? = null

    override fun create() = module {
        single { CsvGoodsProvider(androidContext()) }
        single {
            registerCallback(this@PersistenceModule)
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "goodsDatabase")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        prepopulateDatabase(get<CsvGoodsProvider>(), get<AppDatabase>().goodsDao())
                    }
                })
                .build()
        }
        single { get<AppDatabase>().goodsDao() }
    }

    private fun prepopulateDatabase(initialGoodsProvider: GoodsProvider, goodsDao: GoodsDao) {
        databaseInitialization = initialGoodsProvider
            .getGoods()
            .doOnNext { Timber.d("Storing good to database: $it") }
            .flatMap { it.toObservable() }
            .flatMapCompletable { goodsDao.insert(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = { Timber.d("Database populated") },
                onError = { Timber.e(it, "Error populated database") }
            )
    }

    override fun onScopeClose(scope: Scope) {
        databaseInitialization?.dispose()
    }

}