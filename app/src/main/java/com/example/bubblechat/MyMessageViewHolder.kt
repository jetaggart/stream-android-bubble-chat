package com.example.bubblechat

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.getstream.sdk.chat.adapter.BaseMessageListItemViewHolder
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory.MESSAGEITEM_DATE_SEPARATOR
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory.MESSAGEITEM_MESSAGE
import com.getstream.sdk.chat.model.ModelType
import com.getstream.sdk.chat.rest.response.ChannelState
import com.getstream.sdk.chat.view.MessageListViewStyle
import top.defaults.drawabletoolbox.DrawableBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter


class MyMessageListItemViewHolder(resId: Int, viewGroup: ViewGroup?) :
    BaseMessageListItemViewHolder(resId, viewGroup) {
    private var space_header: Space? = null
    private var tv_text: TextView? = null
    private var tv_username: TextView? = null
    private var position: Int? = null
    private var context: Context? = null
    private var channelState: ChannelState? = null
    private var messageListItem: MessageListItem? = null
    private var set: ConstraintSet? = null

    init {
        space_header = itemView.findViewById(R.id.space_header)
        tv_text = itemView.findViewById(R.id.tv_text)
        tv_username = itemView.findViewById(R.id.tv_username)
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

    private fun init() {
        when (messageListItem!!.type) {
            MESSAGEITEM_DATE_SEPARATOR -> {
                configDate()
            }
            MESSAGEITEM_MESSAGE -> {
                configUsername()
                configText()
                configSpacing()
            }
            else -> {
                space_header!!.visibility = View.GONE
                tv_text!!.visibility = View.GONE
                tv_username!!.visibility = View.GONE
            }
        }
    }

    private fun configDate() {
        tv_text!!.visibility = View.VISIBLE
        tv_username!!.visibility = View.GONE
        tv_text!!.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(messageListItem!!.date)
    }

    private fun configUsername() {
        if (channelState!!.members.size == 2) {
            tv_username!!.visibility = View.GONE
            return
        }

        if (!messageListItem!!.positions.contains(MessageViewHolderFactory.Position.TOP)) {
            tv_username!!.visibility = View.GONE
            return
        }

        tv_username!!.text = messageListItem!!.message.user.name
        val params =
            tv_username!!.layoutParams as ConstraintLayout.LayoutParams
        if (messageListItem!!.isMine) {
            params.horizontalBias = 1f
            params.rightMargin = 50
        } else {
            params.horizontalBias = 0f
            params.leftMargin = 50
        }
    }

    private fun configText() {
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
        val params =
            tv_text!!.layoutParams as ConstraintLayout.LayoutParams
        if (messageListItem!!.isMine) {
            params.horizontalBias = 1f
            params.rightMargin = 25
            params.leftMargin = 250
        } else {
            params.horizontalBias = 0f
            params.rightMargin = 250
            params.leftMargin = 25
        }
    }

    private fun configSpacing() {
        if (!messageListItem!!.positions.contains(MessageViewHolderFactory.Position.TOP)) {
            space_header!!.visibility = View.VISIBLE
            space_header!!.layoutParams.height = 5
        }
    }

    override fun setStyle(style: MessageListViewStyle) {
        // nothing
    }
}