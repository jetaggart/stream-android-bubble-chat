package com.example.bubblechat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.bubblechat.databinding.ActivityMainBinding
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.enums.Filters.*
import com.getstream.sdk.chat.rest.User
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel
import java.util.*

/**
 * This activity shows a list of channels
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // setup the client using the example API key
        // normally you would call init in your Application class and not the activity
        StreamChat.init("3fjyncqw9raj", this.applicationContext)
        val client = StreamChat.getInstance(this.application)
        val extraData = HashMap<String, Any>()
        extraData["name"] = "Paranoid Android"
        extraData["image"] = "https://bit.ly/2TIt8NR"
        val currentUser = User("raspy-scene-1", extraData)
        // User token is typically provided by your server when the user authenticates
        client.setUser(
            currentUser,
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoicmFzcHktc2NlbmUtMSJ9.GYK_RrawiaiRke_hrLIoFa9FWJSPNs9a5wl8_nPo6YQ"
        )

        // we're using data binding in this example
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Specify the current activity as the lifecycle owner.
        binding.lifecycleOwner = this

        // most the business logic for chat is handled in the ChannelListViewModel view model
        val viewModel = ViewModelProviders.of(this).get(ChannelListViewModel::class.java)
        binding.viewModel = viewModel
        binding.channelList.setViewModel(viewModel, this)

        // query all channels of type messaging
        val filter = and(eq("type", "messaging"), `in`("members", "raspy-scene-1"))
        viewModel.setChannelFilter(filter)

        // click handlers for clicking a user avatar or channel
        binding.channelList.setOnChannelClickListener { channel ->
            val intent = ChannelActivity.newIntent(this, channel)
            startActivity(intent)
        }
        binding.channelList.setOnUserClickListener { user ->
            // open your user profile
        }

    }
}