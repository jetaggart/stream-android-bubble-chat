package com.example.bubblechat

import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.*

import com.getstream.sdk.chat.model.Attachment
import com.getstream.sdk.chat.rest.Message

class MyMessageViewHolderFactory : MessageViewHolderFactory() {

    override fun createMessageViewHolder(
        adapter: MessageListItemAdapter?,
        parent: ViewGroup?,
        viewType: Int
    ): BaseMessageListItemViewHolder {
        val holder = MyMessageListItemViewHolder(R.layout.bubble_message, parent)

        return holder
    }

}

