package com.example.polylineandpolygon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.polylineandpolygon.ui.theme.PolylineAndPolygonTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PolylineAndPolygonTheme {
                // A surface container using the background color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapScreen()
                }
            }
        }
    }
}

@Composable
fun MapScreen() {
    // polyline points : restaurants in Boston
    val trailPoints = remember {
        listOf(
            LatLng(42.36436846992889, -71.06296773631638), // Boston Doner
            LatLng(42.36460056319848, -71.05342386798105), // Giacomo's Boston North End
            LatLng(42.364163255161664, -71.05423472165894), // Mike's Pastry
            LatLng(42.34618225381554, -71.1078239905158), // Japonaise Bakery & Cafe
            LatLng(42.34299377349414, -71.11675597586334)  // Xiang Yu China Bistro
        )
    }

    // polygon points - BU campus
    val polygonPoints = remember {
        listOf(
            LatLng(42.35329639422544, -71.11933428440582), // Nickerson Field
            LatLng(42.3509653247651, -71.11754256878616), // School of Hospitality Administration
            LatLng(42.348442, -71.101860), // LSE Building
            LatLng(42.349662603562614, -71.09448553850807), // Wituwamat Memorial Hall

        )
    }

    // Camera centered on the first trail point
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(trailPoints.first(), 12f)
    }


    //variables to initialize color/widths of polyline  and polygon
    var polylineColor by remember { mutableStateOf(Color.Blue) }
    var polylineWidth by remember { mutableStateOf(8f) }
    var polygonFillColor by remember { mutableStateOf(Color(0xFFFF6666)) }
    var polygonStrokeColor by remember { mutableStateOf(Color.Red) }
    var polygonStrokeWidth by remember { mutableStateOf(4f) }

    //to control when to show settings
    var showTrailControls by remember { mutableStateOf(false) }
    var showPolygonControls by remember { mutableStateOf(false) }

    //Display map in full screen
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ) {
            Polyline(
                points = trailPoints,
                color = polylineColor,
                width = polylineWidth,
                clickable = true,
                onClick = {
                    showTrailControls = true
                    showPolygonControls =false
                }
            )
            Polygon(
                points = polygonPoints,
                fillColor = polygonFillColor,
                strokeColor = polygonStrokeColor,
                strokeWidth = polygonStrokeWidth,
                clickable = true,
                onClick = {
                    showTrailControls = false
                    showPolygonControls = true
                }
            )
        }

        /////////////////////////////////////////////////////////
        // info and settings section
        if (showTrailControls || showPolygonControls)
        {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.9f)) //white semi transparent background
                    .padding(16.dp)
            ) {

                // displaying info about trail or area
                Text(
                    text = if (showTrailControls == true) "Trail: Boston Food Tour"
                    else "Area: BU Campus",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                //color settings
                Text("Change Color:", color = Color.Black)
                Row {

                    //std colors
                    val colorOptions = listOf(Color.Blue, Color.Red, Color.Green)

                    //creating a box for each color to display them (clickable box)
                    colorOptions.forEach { color ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(40.dp)
                                .background(color)
                                .clickable {
                                    if (showTrailControls == true) polylineColor = color
                                    else polygonFillColor = color
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //width settings
                Text("Change Width:", color = Color.Black)
                Slider(
                    value = if (showTrailControls == true) polylineWidth else polygonStrokeWidth,
                    onValueChange = {
                        if (showTrailControls == true) polylineWidth = it
                        else polygonStrokeWidth = it
                    },
                    valueRange = 2f..20f
                )

                Spacer(modifier = Modifier.height(8.dp))

                //close info and settings section
                Button(
                    onClick = {
                    showTrailControls = false
                    showPolygonControls = false

                }) {
                    Text("Close")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PolylineAndPolygonTheme {
//        Greeting("Android")
    }
}