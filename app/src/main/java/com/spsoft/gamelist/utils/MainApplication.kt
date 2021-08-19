package com.spsoft.gamelist.utils

import android.app.Application
import com.spsoft.gamelist.data.remote.ApiService
import com.spsoft.gamelist.ui.main.MainRepository
import com.spsoft.gamelist.ui.main.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MainApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))
        bind() from singleton { ApiService() }
        bind<MainRepository>() with singleton { MainRepository(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
    }

}
