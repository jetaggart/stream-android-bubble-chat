package com.example.bubblechat

import android.R.attr.*
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import com.getstream.sdk.chat.adapter.BaseMessageListItemViewHolder
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.rest.response.ChannelState
import com.getstream.sdk.chat.view.MessageListViewStyle
import top.defaults.drawabletoolbox.DrawableBuilder


class MyMessageListItemViewHolder(resId: Int, viewGroup: ViewGroup?) :
    BaseMessageListItemViewHolder(resId, viewGroup) {
    private var tv_text: TextView? = null
    private var position: Int? = null
    private var context: Context? = null
    private var channelState: ChannelState? = null
    private var messageListItem: MessageListItem? = null
    private var set: ConstraintSet? = null

    init {
        tv_text = itemView.findViewById(R.id.tv_text)
    }

    override fun bind(
        context: Context,
        channelState: ChannelState,
        messageListItem: MessageListItem,
        position: Int
    ) { // set binding
        this.context = context
        this.channelState = channelState
        this.messageListItem = messageListItem
        this.position = position
        set = ConstraintSet()
        init()
    }

    private fun init() { // Configure UIs
        tv_text!!.visibility = View.VISIBLE
        tv_text!!.text = messageListItem!!.message.text
        val color = if (messageListItem!!.isMine) {
            tv_text!!.setTextColor(Color.WHITE)
            Color.rgb(0, 122, 255)
        } else {
            tv_text!!.setTextColor(Color.BLACK)
            Color.rgb(229, 229, 234)
        }

        val background = DrawableBuilder()
            .rectangle()
            .strokeColor(color)
            .strokeWidth(10)
            .solidColor(color)
            .cornerRadii(
                20,
                20,
                20,
                20
            )
            .build()
        tv_text!!.background = background
    }

    override fun setStyle(style: MessageListViewStyle) {
        // nothing
    }
}