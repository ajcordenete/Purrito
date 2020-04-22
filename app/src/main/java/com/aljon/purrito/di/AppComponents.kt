package com.aljon.purrito.di

import android.content.Context
import com.aljon.purrito.PurritoApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    FragmentBuilder::class,
    ViewModelBuilderModule::class,
    AppModule::class,
    RepositoryModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(app: PurritoApplication)
}