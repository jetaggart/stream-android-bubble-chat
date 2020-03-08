package com.example.bubblechat

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.getstream.sdk.chat.adapter.BaseMessageListItemViewHolder
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory.MESSAGEITEM_DATE_SEPARATOR
import com.getstream.sdk.chat.adapter.MessageViewHolderFactory.MESSAGEITEM_MESSAGE
import com.getstream.sdk.chat.rest.response.ChannelState
import com.getstream.sdk.chat.view.MessageListViewStyle
import java.text.DateFormat

class BubbleMessageViewHolder(resId: Int, viewGroup: ViewGroup?) :
    BaseMessageListItemViewHolder(resId, viewGroup) {
    private val header: Space = itemView.findViewById(R.id.space_header)
    private val text: TextView = itemView.findViewById(R.id.tv_text)
    private val username: TextView = itemView.findViewById(R.id.tv_username)

    private lateinit var channelState: ChannelState
    private lateinit var messageListItem: MessageListItem

    override fun bind(
        _context: Context,
        channelState: ChannelState,
        messageListItem: MessageListItem,
        _position: Int
    ) {
        this.channelState = channelState
        this.messageListItem = messageListItem

        when (this.messageListItem.type) {
            MESSAGEITEM_MESSAGE -> {
                configMessage()
            }
            MESSAGEITEM_DATE_SEPARATOR -> {
                configDate()
            }
            else -> {
                header.visibility = View.GONE
                text.visibility = View.GONE
                username.visibility = View.GONE
            }
        }
    }

    private fun configMessage() {
        configText()
        configUsername()
        configSpacing()
    }

    private fun configDate() {
        username.visibility = View.GONE
        text.text = DateFormat
            .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(messageListItem.date)
    }

    private fun configUsername() {
        if (channelState.members.size == 2 || messageListItem.isMine || !isTop()) {
            username.visibility = View.GONE
            return
        }

        username.text = messageListItem.message.user.name

        val params = username.layoutParams as ConstraintLayout.LayoutParams
        if (messageListItem.isMine) {
            params.horizontalBias = 1f
            params.rightMargin = 40
        } else {
            params.horizontalBias = 0f
            params.leftMargin = 40
        }
    }

    private fun configText() {
        text.text = messageListItem.message.text

        val params = text.layoutParams as ConstraintLayout.LayoutParams
        if (messageListItem.isMine) {
            params.horizontalBias = 1f
            text.setTextColor(Color.WHITE)
            text.setPadding(dpToPixel(10f), dpToPixel(5f), dpToPixel(15f), dpToPixel(5f))
            if (isBottom()) {
                text.setBackgroundResource(R.drawable.bubble_right_tail)
            } else {
                text.setBackgroundResource(R.drawable.bubble_right)
            }
        } else {
            params.horizontalBias = 0f
            text.setTextColor(Color.BLACK)
            text.setPadding(dpToPixel(15f), dpToPixel(5f), dpToPixel(10f), dpToPixel(5f))
            if (isBottom()) {
                text.setBackgroundResource(R.drawable.bubble_left_tail)
            } else {
                text.setBackgroundResource(R.drawable.bubble_left)
            }
        }

    }

    private fun configSpacing() {
        if (!isTop()) {
            header.layoutParams.height = 5
        }
    }

    private fun isBottom(): Boolean {
        return messageListItem.positions.contains(MessageViewHolderFactory.Position.BOTTOM)
    }

    private fun isTop(): Boolean {
        return messageListItem.positions.contains(MessageViewHolderFactory.Position.TOP)
    }

    private fun dpToPixel(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            itemView.resources.displayMetrics
        ).toInt()
    }

    override fun setStyle(style: MessageListViewStyle) {
        // nothing
    }

}