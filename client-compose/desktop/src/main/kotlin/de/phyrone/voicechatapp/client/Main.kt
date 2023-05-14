@file:JvmName("Main")

package de.phyrone.voicechatapp.client

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main(args: Array<String>) {
    application {
        Window(onCloseRequest = ::exitApplication) {
            windowTestContent()
        }
    }
}

@Preview
@Composable
fun windowTestContent() {
    MaterialTheme {
        Button(onClick = {}) {
            Text("Hello World")
        }
    }
}