package ng.chimdike.githubproject.home.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chimdike.home.domain.entity.UserRepositoryItemResult
import com.chimdike.ui_compose.compose.GreTypography
import com.chimdike.ui_compose.compose.ImageLoader
import com.chimdike.ui_compose.compose.color.GithubLightGrey
import com.chimdike.ui_compose.compose.color.UserLoginTextColor
import ng.chimdike.ui_compose.R

@Composable
fun UserRepositoryCards(
    modifier: Modifier = Modifier,
    item : UserRepositoryItemResult
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        border = BorderStroke(1.dp, GithubLightGrey),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 0.dp,
            pressedElevation= 0.dp,
            focusedElevation= 0.dp,
            hoveredElevation= 0.dp,
            draggedElevation= 0.dp,
            disabledElevation= 0.dp,
        )
    ){

        Column(modifier = Modifier.fillMaxWidth()) {
            TitleComposable(item)
            Spacer(modifier = Modifier.height(4.dp))
            UserDescriptionComposable(bio = item.description)
            Spacer(modifier = Modifier.height(12.dp))
            if(item.topics.isNotEmpty())
                TopicsGridComposable(topics = item.topics)
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
}

@Composable
private fun TitleComposable(item: UserRepositoryItemResult) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "${item.fullName.split("/")[0]}/",
                style = GreTypography.bodyMedium.copy(
                    fontSize = 12.sp,
                    color = UserLoginTextColor,
                    fontWeight = FontWeight.W400,
                )
            )
            Text(
                text = item.fullName.split("/")[1],
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = GreTypography.bodyMedium.copy(
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W600,
                )
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageLoader(resource = R.drawable.star)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${item.stargazersCount}",
                style = GreTypography.searchTextFieldText.copy(
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                )
            )
        }
        if (item.language.isNotEmpty())
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Green)
                ) {
                    Spacer(modifier = Modifier.size(8.dp))
                }
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = item.language,
                    style = GreTypography.searchTextFieldText.copy(
                        fontSize = 10.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.W500,
                    )
                )
            }
    }
}

@Composable
fun UserDescriptionComposable(bio: String){
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = bio,
        style = GreTypography.searchTextFieldText.copy(
            fontSize = 12.sp,
            color = Color.Black,
            fontWeight = FontWeight.W400,
        )
    )
}
