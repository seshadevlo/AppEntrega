package com.example.sprintm6.di

import com.example.sprintm6.repository.UserRepository
import com.example.sprintm6.repository.UserRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    @Singleton
    @Binds
    abstract fun userRepository(repositoryImp: UserRepositoryImp): UserRepository
}
