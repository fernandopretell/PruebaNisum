package com.fernandopretell.pruebanisum.di
import com.fernandopretell.pruebanisum.ui.detalle.DetalleActivity
import com.fernandopretell.pruebanisum.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class  ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector/*(modules = [MainActivityModule::class])*/
    abstract fun bindMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector/*(modules = [MainActivityModule::class])*/
    abstract fun bindDetalleActivity(): DetalleActivity
}