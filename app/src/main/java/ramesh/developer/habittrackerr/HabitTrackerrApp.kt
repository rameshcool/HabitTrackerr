package ramesh.developer.habittrackerr

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ramesh.developer.habittrackerr.di.dataModule
import ramesh.developer.habittrackerr.di.presentationModule

class HabitTrackerrApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HabitTrackerrApp)
            modules(dataModule, presentationModule)
        }
    }
}
