package  com.app.givebackrx.di.component

import android.app.Application
import com.app.givebackrx.di.builder.ActivityBuilder
import com.app.givebackrx.di.module.AppModule
import com.app.givebackrx.GBRxApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidInjectionModule::class), (AppModule::class), (ActivityBuilder::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: GBRxApp)

}