package ramesh.developer.habittrackerr.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ramesh.developer.habittrackerr.ui.habitform.HabitFormViewModel
import ramesh.developer.habittrackerr.ui.stats.StatsViewModel
import ramesh.developer.habittrackerr.ui.today.TodayViewModel

val presentationModule = module {
    viewModel { TodayViewModel(get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { HabitFormViewModel(get(), get()) }
}
