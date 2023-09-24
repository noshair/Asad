package com.example.asad

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.asad.ui.WebView
import com.example.compose.AppTheme
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

const val ONESIGNAL_APP_ID = "d7e99f96-fdf5-45f6-943e-297ab4d9281a"


class MainActivity : ComponentActivity() {

    @SuppressLint("FlowOperatorInvokedInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                // Verbose Logging set to help debug issues, remove before releasing your app.
                // OneSignal.Debug.logLevel = LogLevel.VERBOSE


                // OneSignal Initialization
                OneSignal.initWithContext(this, ONESIGNAL_APP_ID)

                // requestPerxmission will show the native Android notification permission prompt.
                // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.
                CoroutineScope(Dispatchers.IO).launch {
                    OneSignal.Notifications.requestPermission(true)
                }
                val id = OneSignal.User.pushSubscription.id
                val context = LocalContext.current

                val preferencesManager = remember { PreferencesManager(context) }
                val data = remember { mutableStateOf(preferencesManager.getData("myKey", "")) }
                if (data.value == "") {
                    C.a = "auth"
                } else {
                    C.a = "pro"
                }

                Box {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = C.a
                    ) {
                        composable(
                            route = "auth"
                        ) {

                            Greeting(onClick = {
                                val d = "no"
                                preferencesManager.saveData("myKey", d)
                                data.value = d
                                C.url = "https://coindive.app/pricing"
                                navController.navigate(route = "profile")
                                //navController.popBackStack()
                            }, onExploreClick = {
                                val d = "no"
                                preferencesManager.saveData("myKey", d)
                                // data.value = d
                                C.url = "https://coindive.app/dashboard"
                                navController.navigate(route = "profile")
                                //navController.popBackStack()
                            })
                        }
                        composable(
                            route = "profile"
                        ) {
                            WebView(
                                url = C.url
                            )
                        }
                        composable(
                            route = "pro"
                        ) {
                            WebView(
                                url = "https://coindive.app/watchlist?player_id=${id}"
                            )
                        }
                    }
                }


            }
        }
    }
}

object C {
    var url = ""
    var a = ""
}

@Composable
fun Greeting(modifier: Modifier = Modifier, onClick: () -> Unit, onExploreClick: () -> Unit) {
    val scrollState = rememberScrollState()
    val fontFamily = FontFamily(
        Font(
            resId = R.font.montserratbold
        )
    )
    val f = FontFamily(
        Font(
            resId = R.font.montserratlight
        )
    )
    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize(1f)

            .verticalScroll(scrollState),

        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(0.5f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painterResource(R.drawable.down),
                contentDescription = "",
                contentScale = ContentScale.Fit,
            )
        }
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth(1f)
                .padding(start = 60.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Track More\nThan Just\nPrice!",
                modifier = Modifier,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "Stay in the loop with community \nupdates and major announcements.\nRegister now to never miss any \nmomenum again.",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = f
            )
        }
        Column(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(1f)
                .padding(horizontal = 80.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {/*
            Button(
                onClick = { onClick() }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = Color.Blue,
                        spotColor = Color.Blue
                    )
            ) {
                Text(text = "Get started now", fontFamily = fontFamily)
            }*/
            FilledIconButton(
                onClick = { onExploreClick() }, modifier = Modifier
                    .fillMaxWidth(1f)
                    .height(50.dp)
                    .shadow(
                        2.dp,
                        shape = RoundedCornerShape(2.dp),
                        ambientColor = Color.LightGray,
                        spotColor = Color.LightGray
                    )
                   // .background(Color.White)
            ) {
                Text(
                    text = "Explore",
                    fontFamily = fontFamily,
                   // color = Color.Blue,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                       /* .background(
                            Color.White
                        )*/
                )
            }
        }
    }


}
