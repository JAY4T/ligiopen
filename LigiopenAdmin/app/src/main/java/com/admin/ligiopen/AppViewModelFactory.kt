package com.admin.ligiopen

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.admin.ligiopen.ui.screens.auth.LoginViewModel
import com.admin.ligiopen.ui.screens.home.HomeViewModel
import com.admin.ligiopen.ui.screens.match.clubs.ClubAdditionViewModel
import com.admin.ligiopen.ui.screens.match.clubs.ClubDetailsViewModel
import com.admin.ligiopen.ui.screens.match.clubs.ClubUpdateViewModel
import com.admin.ligiopen.ui.screens.match.clubs.ClubsViewModel
import com.admin.ligiopen.ui.screens.match.fixtures.FixtureCreationViewModel
import com.admin.ligiopen.ui.screens.match.fixtures.FixturesViewModel
import com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.HighlightsScreenViewModel
import com.admin.ligiopen.ui.screens.match.fixtures.fixtureDetails.commentary.EventUploadViewModel
import com.admin.ligiopen.ui.screens.match.location.LocationAdditionVewModel
import com.admin.ligiopen.ui.screens.match.location.LocationsViewModel
import com.admin.ligiopen.ui.screens.news.NewsDetailsViewModel
import com.admin.ligiopen.ui.screens.news.NewsViewModel
import com.admin.ligiopen.ui.screens.news.newsManagement.NewsAdditionViewModel
import com.admin.ligiopen.ui.screens.news.newsManagement.NewsItemAdditionViewModel
import com.admin.ligiopen.ui.screens.player.PlayerDetailsViewModel
import com.admin.ligiopen.ui.screens.start.SplashViewModel
import com.admin.ligiopen.ui.screens.users.ClubSearchViewModel
import com.admin.ligiopen.ui.screens.users.UserSearchViewModel
import com.admin.ligiopen.ui.screens.users.UsersManagementViewModel

object AppViewModelFactory {
    val Factory = viewModelFactory {

        initializer {
            SplashViewModel(
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            LoginViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            LocationsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
            )
        }

        initializer {
            LocationAdditionVewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            ClubsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            ClubAdditionViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            FixturesViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            EventUploadViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            HighlightsScreenViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            NewsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            NewsDetailsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            NewsAdditionViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

        initializer {
            NewsItemAdditionViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            ClubDetailsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            ClubUpdateViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            PlayerDetailsViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            UsersManagementViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
            )
        }

        initializer {
            UserSearchViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            FixtureCreationViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
            )
        }

        initializer {
            ClubSearchViewModel(
                apiRepository = ligiopenApplication().container.apiRepository,
                dbRepository = ligiopenApplication().container.dbRepository,
                savedStateHandle = this.createSavedStateHandle()
            )
        }

        initializer {
            HomeViewModel(
                dbRepository = ligiopenApplication().container.dbRepository
            )
        }

    }
}

fun CreationExtras.ligiopenApplication(): Ligiopen =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Ligiopen)