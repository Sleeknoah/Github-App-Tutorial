package ng.chimdike.ui_compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NetworkImageLoader(
    url: String,
    modifier:Modifier = Modifier,
    contentDescription: String = "",
    size: Dp,
){
    Box(modifier = modifier.width(size).height(size)) {
        GlideImage(
            model = url,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize().clip(CircleShape),
        )
    }
}