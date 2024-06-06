package com.example.focuschildapp.ScreensPkg

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.focuschildapp.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import coil.compose.rememberAsyncImagePainter
@Composable
fun UsageAccess(navController: NavController) {

    val requestCode = 100
    val context = LocalContext.current
    val systemUiController = rememberSystemUiController()
    val colorStops = arrayOf(
        0.2f to Color(0xFF0E4B4D),
        0.5f to Color(0xFF10585A),
        1f to Color(0xFF126366)
    )

    val colorStopsButton = arrayOf(
        0.2f to Color(0xFF09F5A3),
        0.5f to Color(0xFF098358),
        1f to Color(0xFF012E1E)
    )
    val scrollState = rememberScrollState()


    val density = LocalDensity.current.density
    val screenWidthDp = LocalDensity.current.run { (10 / density).toInt() }

    val fontSize = when {
        screenWidthDp >= 600 -> 27.sp
        screenWidthDp >= 400 -> 15.sp
        else -> 15.sp
    }

    SideEffect {
        systemUiController.setSystemBarsColor(Color(0xFF0E4B4D))
    }


    val listOfInstructions = listOf(
        "Click the button below to open the Settings app.",
        "Find Focus. in the list of apps and click on it.",
        "Click on Permit usage access to enable the permission."
    )

    Column(
        modifier = Modifier
            .background(Brush.linearGradient(colorStops = colorStops))
            .fillMaxSize()
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text ="To provide a comprehensive parental control experience, our app requires access to your child's app usage. This permission enables us to accurately monitor and block inappropriate apps. Rest assured, your child's data privacy is our priority, and we only utilize this information to enhance their safety. Your commitment to creating a safe digital space for your child inspires us!",
            color = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxWidth(if (LocalConfiguration.current.screenWidthDp > 600) .9f else 1f),
            textAlign = TextAlign.Justify,
            fontSize = 19.sp,
            fontFamily = FontFamily(Font(R.font.opensans_res))
        )
        Spacer(modifier = Modifier.height(60.dp))
        Image(
            painter = painterResource(id = R.drawable.info),
            contentDescription = "info image",
            Modifier.fillMaxSize(.1f)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "How to activate the permission?",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.opensans_res))
            )
        }

        Column {
            listOfInstructions.forEach {
                Row (Modifier.padding(start = if (LocalConfiguration.current.screenWidthDp > 600) 50.dp else 0.dp )){
                    Text(
                        text = "\u2022",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(20.dp),
                        color = Color.White,
                        fontSize = 19.sp
                    )
                    Text(
                        text = it,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(1f, fill = true),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.opensans_res)),
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(20.dp))

        GifImage(modifier = Modifier.fillMaxWidth(), R.drawable.usage)

        Spacer(modifier = Modifier.padding(10.dp))

        Button(
            onClick = {
                val settingsIntent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                (context as Activity).startActivityForResult(settingsIntent, requestCode)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
                .fillMaxWidth(.8f)
                .clip(shape = RoundedCornerShape(10.dp))
                .border(
                    BorderStroke(
                        1.dp,
                        brush = Brush.linearGradient(colorStops = colorStopsButton)
                    ), RoundedCornerShape(100.dp)
                )
        ) {
            Text(
                text = "Go to settings",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.opensans_res))
            )
        }

    }

}


@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    image: Int
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = image).apply(block = {
                size(Size.ORIGINAL)
            }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
    )
}