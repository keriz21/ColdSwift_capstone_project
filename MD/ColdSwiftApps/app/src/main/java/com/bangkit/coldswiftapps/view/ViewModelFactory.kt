package com.bangkit.coldswiftapps.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.coldswiftapps.data.EventRepository
import com.bangkit.coldswiftapps.di.Injection
import com.bangkit.coldswiftapps.view.detail.DetailViewModel
import com.bangkit.coldswiftapps.view.home.HomeViewModel
import com.bangkit.coldswiftapps.view.login.LoginViewModel
import com.bangkit.coldswiftapps.view.main.MainViewModel
import com.bangkit.coldswiftapps.view.myprofile.MyProfileViewModel
import com.bangkit.coldswiftapps.view.myticket.MyTicketViewModel
import com.bangkit.coldswiftapps.view.register.RegisterViewModel

class ViewModelFactory(private val repository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MyTicketViewModel::class.java) -> {
                MyTicketViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(MyProfileViewModel::class.java) -> {
                MyProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
        fun resetInstance() {
            INSTANCE = null
            Injection.resetInstance()
        }
    }
}