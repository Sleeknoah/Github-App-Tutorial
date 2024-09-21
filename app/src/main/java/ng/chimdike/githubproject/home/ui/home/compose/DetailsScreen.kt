package ng.chimdike.githubproject.home.ui.home.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.chimdike.home.domain.entity.UserInfoEntity
import com.chimdike.home.ui.components.UserRepositoryCards
import com.chimdike.ui_compose.compose.GreTypography
import com.chimdike.ui_compose.compose.ImageLoader
import ng.chimdike.ui_compose.NetworkImageLoader
import com.chimdike.ui_compose.compose.color.Background
import ng.chimdike.githubproject.home.ui.home.viewmodel.HomeViewModel
import ng.chimdike.githubproject.home.ui.home.viewmodel.ViewState
import ng.chimdike.ui_compose.R

@Composable
fun DetailsScreen(
    viewModel: HomeViewModel,
    navController: NavHostController,
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    if(uiState.isFirstIntent && uiState.selectedUser != null){
        viewModel.searchUserInfo(uiState.selectedUser.login)
    }

    Scaffold(containerColor = Background) {
        Column(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp,
            )
        ) {
            Row(modifier = Modifier.padding(
                top = 24.dp
            )) {
                Box(
                    Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                        }) {
                    ImageLoader(resource = R.drawable.arrow_left)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "User",
                    style = GreTypography.searchTextFieldText.copy(
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W600,
                    )
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (uiState.userLoading) {
                true -> UserLoadingState()
                false -> {
                    if(uiState.userInfo != null) {
                        UserLoadedState(uiState = uiState)
                    }
                    if(uiState.userInfoError.isNotEmpty()){
                        ErrorState(uiState.userInfoError)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(
    userInfoError: String,
    emptyState: Boolean = false,
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ImageLoader(resource = R.drawable.empty_state)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = if(emptyState) "This user  doesn’t have repositories yet, come back later :-)" else userInfoError,
            style = GreTypography.bodyMedium.copy(
                color = Color.Black,
                fontWeight = FontWeight.W500
            ),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun UserLoadedState(uiState: ViewState){
    val userInfo = uiState.userInfo
    userInfo?.let { user ->
        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ){
                NetworkImageLoader(url = user.avatarUrl , size = 60.dp)
                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = user.name,
                        style = GreTypography.bodyMedium.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.W600,
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = user.login,
                        style = GreTypography.bodyMedium.copy(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if(user.bio.isNotEmpty())
                Text(
                    text = user.bio,
                    style = GreTypography.bodyMedium.copy(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                    )
                )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                val location = user.location.ifEmpty { "No Location" }
                ImageText(icon = R.drawable.location, text = location)
                Spacer(modifier = Modifier.width(12.dp))
                ImageText(icon = R.drawable.clarity_link_line, text = user.htmlUrl, boldText = true)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                ImageText(
                    icon = R.drawable.people,
                    text = "${user.followers} followers . ${user.following} following"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            RepositoryHeader(user)
            Spacer(modifier = Modifier.height(16.dp))

            if(uiState.repoLoading){
                UserLoadingState()
                return
            }
            if(uiState.userInfoError.isNotEmpty()){
                ErrorState(userInfoError = uiState.userInfoError)
                return
            }
            if(uiState.repos != null && uiState.repos.isEmpty()){
                ErrorState(
                    userInfoError = uiState.userInfoError,
                    emptyState = true
                )
                return
            }
            if(!uiState.repos.isNullOrEmpty()){
                RepositoryBody(uiState)
                return
            }

        }
    }
}

@Composable
fun RepositoryBody(uiState: ViewState) {
    uiState.repos?.let {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp)) {
            items(items = it){ repo ->
                UserRepositoryCards(item = repo)
            }
        }
    }
}

@Composable
fun RepositoryHeader(user: UserInfoEntity) {
    Column {

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Repository",
                style = GreTypography.bodyMedium.copy(
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF1F1F1),
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    draggedElevation = 0.dp,
                    disabledElevation = 0.dp,
                )
            ) {
                Text(
                    text = user.publicRepos.toString(),
                    modifier = Modifier.padding(8.dp),
                    style = GreTypography.bodyMedium.copy(
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            HorizontalDivider(
                modifier = Modifier.weight(1.5f),
                thickness = 2.dp,
                color = Color.Black
            )
            HorizontalDivider(
                modifier = Modifier.weight(3.5f),
                thickness = 2.dp,
                color = Color(0xFFF1F1F1)
            )
        }
    }
}

@Composable
fun UserLoadingState(){
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color.Black,
            strokeWidth =  5.dp
        )
    }
}

@Composable
fun ImageText(
    icon: Int,
    text: String,
    boldText: Boolean = false,
){
    Row(verticalAlignment = Alignment.CenterVertically){
        ImageLoader(resource = icon)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = GreTypography.searchTextFieldText.copy(
                fontSize = 12.sp,
                color = if(boldText) Color(0xFF1A1A1A) else Color(0x8C1A1A1A),
                fontWeight = if(boldText) FontWeight.W600 else FontWeight.W500,
            )
        )
    }
}