package ng.chimdike.githubproject.home.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chimdike.core.navigation.NavigationScreens
import com.chimdike.home.domain.entity.UserEntityResult
import com.chimdike.home.domain.entity.UserInfoEntity
import com.chimdike.home.domain.entity.UserInfoResult
import com.chimdike.home.domain.entity.UserItemResult
import com.chimdike.home.domain.entity.UserRepositoryEntityResult
import com.chimdike.home.domain.entity.UserRepositoryItemResult
import com.chimdike.home.domain.usecase.SearchUserInfoUsecase
import com.chimdike.home.domain.usecase.SearchUserRepositoriesUsecase
import com.chimdike.home.domain.usecase.SearchUserUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchUserUsecase: SearchUserUsecase,
    private val searchUserInfoUsecase: SearchUserInfoUsecase,
    private val searchUserRepositoriesUsecase: SearchUserRepositoriesUsecase,
): ViewModel(){
    private val _viewState = MutableStateFlow(ViewState.initial)
    private val _latestState = _viewState.value
    val uiState: StateFlow<ViewState> = _viewState

    fun updateAppTextField(field: String){
        _viewState.update {
            it.copy(
                user = field,
            )
        }
    }

    fun searchUsers(user: String){
        viewModelScope.launch(
            context = Dispatchers.IO
        ) {
            _viewState.emit(_latestState.copy(
                emptyState = true,
                isLoading = true,
            ))
            when  (val result = searchUserUsecase.execute(user)){
               is UserEntityResult.UserSuccess ->{
                   _viewState.update {
                       it.copy(
                           isLoading = false,
                           entityResult = result,
                           emptyState = false,
                       )
                   }
               }
               is UserEntityResult.UserFailure ->{
                   _viewState.update {
                       it.copy(
                           isLoading = false,
                           errorMessage = result.message,
                           emptyState = false,
                       )
                   }
               }
           }
        }
    }

    fun searchUserInfo(user: String){
        viewModelScope.launch(IO) {
            _viewState.update {
                it.copy(
                    userLoading = true,
                    userInfoError = "",
                    userInfo = null,
                    isFirstIntent = false,
                )
            }
            when  (val result = searchUserInfoUsecase.execute(user)){
                is UserInfoResult.Success ->{
                    searchUserRepo(user)
                    _viewState.update {
                        it.copy(
                            userLoading = false,
                            userInfo = result.result,
                            repoLoading = true,
                        )
                    }
                }
                is UserInfoResult.Failure ->{
                    _viewState.update {
                        it.copy(
                            userLoading = false,
                            userInfoError = result.message,
                        )
                    }
                }
            }
        }
    }
     private fun searchUserRepo(user: String){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                when  (val result = searchUserRepositoriesUsecase.execute(user)){
                    is UserRepositoryEntityResult.Success ->{
                        _viewState.update {
                            it.copy(
                                repoLoading = false,
                                repos = result.result,
                            )
                        }
                    }
                    is UserRepositoryEntityResult.Failure ->{
                        _viewState.update {
                            it.copy(
                                repoLoading = false,
                                userInfoError = result.message,
                            )
                        }
                    }
                }
            }
        }
    }

    fun selectUser(item: UserItemResult){
        _viewState.update {
            it.copy(
                navigate = NavigationScreens.DetailsScreen,
                selectedUser = item,
            )
        }
//        searchUserInfo(item.login)
    }

    fun navigationUser(page: NavigationScreens){
        _viewState.update {
            it.copy(
                navigate = page
            )
        }
    }

    fun navigationDone(){
        _viewState.update {
            it.copy(
                navigate = null,
                isFirstIntent = true
            )
        }
    }

}

sealed class Navigator{
    data class Navigate(val page: NavigationScreens): Navigator()
    data object NavigateDone: Navigator()
}

@Immutable
data class ViewState(
    val isFirstIntent: Boolean,
    val navigate: NavigationScreens?,
    val user: String,
    val entityResult: UserEntityResult.UserSuccess?,
    val errorMessage: String,
    val isLoading: Boolean,
    val userLoading: Boolean,
    val repoLoading: Boolean,
    val emptyState: Boolean?,
    val selectedUser: UserItemResult?,
    val userInfo: UserInfoEntity?,
    val userInfoError: String,
    val repos: List<UserRepositoryItemResult>?,
){
    companion object{
        val initial = ViewState(
            isFirstIntent = true,
            navigate = null,
            user = "",
            entityResult = null,
            errorMessage = "",
            isLoading = false,
            emptyState = true,
            selectedUser = null,
            userLoading = false,
            repoLoading = false,
            userInfo = null,
            userInfoError = "",
            repos = null,
        )
    }
}