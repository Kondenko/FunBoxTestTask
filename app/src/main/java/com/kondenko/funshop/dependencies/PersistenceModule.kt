package com.kondenko.funshop.dependencies

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kondenko.funshop.data.AppDatabase
import com.kondenko.funshop.data.GoodsRepository
import io.reactivex.disposables.Disposable
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.module

class PersistenceModule : ModuleCreator, ScopeCallback {

    private var databaseInitialization: Disposable? = null

    override fun create() = module {
        single {
            registerCallback(this@PersistenceModule)
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "goodsDatabase")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        databaseInitialization = get<GoodsRepository>().prepopulateDatabase()
                    }
                })
                .build()
        }
        single { get<AppDatabase>().goodsDao() }
    }

    override fun onScopeClose(scope: Scope) {
        databaseInitialization?.dispose()
    }

}