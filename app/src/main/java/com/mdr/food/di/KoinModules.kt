package com.mdr.food.di

import androidx.room.Room
import com.mdr.food.data.Database
import com.mdr.food.data.Database.Companion.DB_NAME
import com.mdr.food.data.FoodApi
import com.mdr.food.data.food.local.FoodLocalDataSource
import com.mdr.food.data.food.remote.FoodRemoteDataSource
import com.mdr.food.data.food.FoodRepoImpl
import com.mdr.food.data.NetworkStatusImpl
import com.mdr.food.domain.FoodRepo
import com.mdr.food.domain.NetworkStatus
import com.mdr.food.presentation.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val KOIN_MODULES by lazy {
    listOf(foodModule, viewModelsModule)
}

private val viewModelsModule = module {
    viewModel { MainViewModel(get()) }
}

private val foodModule = module {
    single { FoodRemoteDataSource(get()) }
    single { FoodApi.build(get(named("baseUrl"))) }
    single(named("baseUrl")) { "https://api.themoviedb.org/" }
    single<NetworkStatus> { NetworkStatusImpl(get()) }

    single {
        Room.databaseBuilder(get(), Database::class.java, DB_NAME)
            .build()
    }
    single { get<Database>().foodDao() }
    single { FoodLocalDataSource(get()) }

    single<FoodRepo> { FoodRepoImpl(get(), get(), get()) }
}