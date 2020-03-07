package com.example.bubblechat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.bubblechat.databinding.ActivityChannelBinding
import com.getstream.sdk.chat.StreamChat
import com.getstream.sdk.chat.model.Channel
import com.getstream.sdk.chat.view.MessageInputView
import com.getstream.sdk.chat.viewmodel.ChannelViewModel
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory

/**
 * Show the messages for a channel
 */
class ChannelActivity : AppCompatActivity(),
    MessageInputView.OpenCameraViewListener {

    private var viewModel: ChannelViewModel? = null
    private var binding: ActivityChannelBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // receive the intent and create a channel object
        val intent = intent
        val channelType = intent.getStringExtra(EXTRA_CHANNEL_TYPE)
        val channelID = intent.getStringExtra(EXTRA_CHANNEL_ID)
        val client = StreamChat.getInstance(application)

        // we're using data binding in this example
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel)
        // most the business logic of the chat is handled in the ChannelViewModel view model
        binding!!.lifecycleOwner = this

        val channel = client.channel(channelType, channelID)
        viewModel = ViewModelProviders.of(
            this,
            ChannelViewModelFactory(this.application, channel)
        ).get(ChannelViewModel::class.java)

        // set listeners
        binding!!.messageInput.setOpenCameraViewListener(this)

        // connect the view model
        binding!!.viewModel = viewModel
        binding!!.messageList.setViewHolderFactory(BubbleMessageViewHolderFactory())
        binding!!.messageList.setViewModel(viewModel!!, this)
        binding!!.messageInput.setViewModel(viewModel, this)
        binding!!.channelHeader.setViewModel(viewModel, this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding!!.messageInput.captureMedia(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        binding!!.messageInput.permissionResult(requestCode, permissions, grantResults)
    }

    override fun openCameraView(intent: Intent, REQUEST_CODE: Int) {
        startActivityForResult(intent, REQUEST_CODE)
    }

    companion object {

        private val EXTRA_CHANNEL_TYPE = "com.example.chattutorial.CHANNEL_TYPE"
        private val EXTRA_CHANNEL_ID = "com.example.chattutorial.CHANNEL_ID"

        fun newIntent(context: Context, channel: Channel): Intent {
            val intent = Intent(context, ChannelActivity::class.java)
            intent.putExtra(EXTRA_CHANNEL_TYPE, channel.type)
            intent.putExtra(EXTRA_CHANNEL_ID, channel.id)
            return intent
        }
    }

}