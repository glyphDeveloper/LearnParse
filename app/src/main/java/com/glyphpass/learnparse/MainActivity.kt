package com.glyphpass.learnparse

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.glyphpass.learnparse.ui.theme.LearnParseTheme
import com.parse.ParseObject
import com.parse.ParseQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        val firstObject = ParseObject("ThirdClass")
        firstObject.put("message","Hey ! Fourth  message from android. Parse is now connected")
        firstObject.saveInBackground {
            if (it != null){
                it.localizedMessage?.let { message -> Log.e("MainActivity", message) }
            }else{
                Log.d("MainActivity","Object saved.")
            }
        }
        */
        /*
        // Store a new record "eventually"
        val gameScore = ParseObject("GameScore")
        gameScore.put("score", 1240)
        gameScore.put("playerName", "John Mohler")
        gameScore.put("cheatMode", false)
        // try another persistent approach
        gameScore.saveEventually()
        */


        // Issue some queries
        // See also https://docs.parseplatform.org/android/guide/#queries
        val query = ParseQuery.getQuery<ParseObject>("GameScore")
        query.whereEqualTo("playerName", "Jeff Plott")
        query.findInBackground { scoreList, e ->
            if (e == null) {
                if (scoreList != null) {
                    for (score in scoreList) {
                        Log.d("score", "Retrieved record")
                        Log.d("score", "First Score: " + score.getNumber("score"))
                    }
                }
                Log.d("score", "Retrieved " + scoreList.size + " scores")
            } else {
                Log.d("score", "Error: " + e.message)
            }
        }

        val uploadFileAppend = UUID.randomUUID().toString()
        // val uploadFileAppend = ""
        val downloadFileName = UUID.randomUUID().toString() + ".jpg"
        // val downloadFileName = "bill.jpg"

        // Get the Device ID
        val theContext = this.applicationContext
        val deviceIdentifier : GetAndroidDeviceID = GetAndroidDeviceID()
        val deviceID = deviceIdentifier.getDeviceID(theContext)
        Log.d("TAG", "The Device ID is: $deviceID")

        GlobalScope.launch(Dispatchers.IO) {
            uploadImageFromInternalStorageImpl(theContext, deviceID, "bill.jpg", uploadFileAppend)
        }



        setContent {
            LearnParseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Parse!")
                }
            }
        }
    }   // end onCreate()


    private suspend fun uploadImageFromInternalStorageImpl(
        context: Context,
        device: String,
        filename: String,
        uploadFileAppend: String
    ) {
        return withContext(Dispatchers.IO) {

            val files = context.filesDir.listFiles()
            val storageImage = files?.filter {
                it.canRead() && it.isFile && it.name.startsWith(filename) && it.name.endsWith(
                    ".jpg"
                )
            }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                ROStorageImage(it.name, bmp)
            }

        }
    }   // end uploadImageFromInternalStorageImpl()
}   // end MainActivity class

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearnParseTheme {
        Greeting("Android")
    }
}