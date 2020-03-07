package com.example.bubblechat

import android.view.ViewGroup
import com.getstream.sdk.chat.adapter.*

class BubbleMessageViewHolderFactory : MessageViewHolderFactory() {

    override fun createMessageViewHolder(
        adapter: MessageListItemAdapter?,
        parent: ViewGroup?,
        viewType: Int
    ): BaseMessageListItemViewHolder {
        return BubbleMessageListItemViewHolder(R.layout.bubble_message, parent)
    }

}

