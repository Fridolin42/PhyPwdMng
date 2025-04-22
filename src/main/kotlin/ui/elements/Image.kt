package ui.elements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import data.IconGetter
import java.awt.image.BufferedImage

@Composable
@Preview
fun Image(url: String, modifier: Modifier = Modifier) {
    val bitmap by produceState(initialValue = getDefaultImage(), key1 = url) {
        IconGetter.getImageBitmap(url)?.let {
            value = it
        }
    }
    Image(bitmap = bitmap, contentDescription = null, modifier = modifier)
}

fun getDefaultImage(): ImageBitmap {
    return BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB).toComposeImageBitmap()
}