package com.fernandopretell.pruebanisum.di

import android.app.Application
import com.fernandopretell.pruebanisum.ApplicationClass
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton



@Singleton
@Component(modules = [ AndroidSupportInjectionModule::class,ActivityBuilder::class/*,AppModule::class*/])
interface AppComponent : AndroidInjector<ApplicationClass> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application) : Builder

        fun build() : AppComponent
    }
}