package ng.chimdike.githubproject.home.ui.home.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.chimdike.home.domain.entity.UserEntityResult
import com.chimdike.home.ui.components.UserCards
import com.chimdike.ui_compose.compose.GreTypography
import ng.chimdike.githubproject.home.ui.home.viewmodel.HomeViewModel
import com.chimdike.ui_compose.compose.ImageLoader
import com.chimdike.ui_compose.compose.color.Background
import com.chimdike.ui_compose.compose.color.SearchTextUnfocusedContainerColor
import com.chimdike.ui_compose.compose.widgets.AppTextField
import ng.chimdike.githubproject.home.ui.home.viewmodel.Navigator
import ng.chimdike.githubproject.home.ui.home.viewmodel.ViewState
import ng.chimdike.ui_compose.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value


    Scaffold(containerColor = Background){
        uiState.navigate?.let {
            navController.navigate(it.route, navOptions = navOptions {
                launchSingleTop = true
            })
            viewModel.navigationDone()
        }

        HomePage(
            homeViewModel = viewModel,
            uiState = uiState,
            entityResult = uiState.entityResult
        )
    }
}

@Composable
private fun HomePage(
    homeViewModel: HomeViewModel,
    uiState: ViewState,
    entityResult: UserEntityResult.UserSuccess?
) {
    Column(
        modifier = Modifier.padding(
            top = 24.dp,
            start = 24.dp,
            end = 24.dp,
        )
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.users),
            style = GreTypography.headlineSmall.copy(
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box(Modifier.height(TextFieldDefaults.MinHeight)) {
            UpperBand(homeViewModel, uiState)
        }
        Box(Modifier.weight(9f)) {
            when (uiState.emptyState) {
                true -> {
                    InitialState(uiState = uiState)
                }

                false -> {
                    if (entityResult != null && entityResult.user.items.isNotEmpty()) {
                        UserSuccessLayout(uiState = uiState, viewModel = homeViewModel)
                        return
                    }
                    if (entityResult != null && entityResult.user.items.isEmpty()) {
                        InitialState(
                            uiState = uiState,
                            isEmpty = true,
                        )
                        return
                    }
                    if(uiState.errorMessage.isNotEmpty()){
                        InitialState(
                            uiState = uiState,
                            errorState = true,
                        )
                        return
                    }
                }

                null -> InitialState(uiState = uiState)
            }

        }
    }
}

@Composable
private fun UpperBand(homeViewModel: HomeViewModel, uiState: ViewState) {

    AppTextField(
        value = uiState.user,
        placeholder = stringResource(id = R.string.search_for_users),
        borderColor = if(uiState.user.isEmpty()) SearchTextUnfocusedContainerColor else Color.Black,
        leadingRes = R.drawable.search_normal,
        onValueChange = {
            homeViewModel.updateAppTextField(it)
        }
    ) {
        if(uiState.user.isNotEmpty()){
            homeViewModel.searchUsers(uiState.user)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InitialState(
    uiState: ViewState,
    modifier: Modifier = Modifier,
    isEmpty: Boolean = false,
    errorState: Boolean = false
){
    var descriptionText = ""
    if(uiState.errorMessage.isNotEmpty()){
        descriptionText = uiState.errorMessage
    }
    if(uiState.entityResult == null && uiState.errorMessage.isEmpty()){
        descriptionText = "Search Github for users..."
    }
    if(isEmpty){
        descriptionText = "We’ve searched the ends of the earth and we’ve not found this user, please try again"
    }
    if(uiState.isLoading){
        descriptionText = "Searching for github users..."
    }
    if(errorState){
        descriptionText = uiState.errorMessage
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageLoader(resource = R.drawable.empty_state)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = descriptionText,
            style = GreTypography.bodyMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.W500
            ),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if(uiState.isLoading)
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color.Black,
                strokeWidth =  5.dp
            )
    }
}

@Composable
fun UserSuccessLayout(
    uiState: ViewState,
    viewModel: HomeViewModel,
){
    uiState.entityResult?.let {
        val userList = it.user.items
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp)) {
            itemsIndexed(userList) { index, user ->
                UserCards(name = user.login, url = user.avatarUrl) {
                    viewModel.selectUser(user)
                }
            }
        }
    }
}