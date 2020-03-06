package com.example.bubblechat

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
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
import com.getstream.sdk.chat.rest.response.ChannelState
import com.getstream.sdk.chat.view.MessageListViewStyle
import java.text.DateFormat


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
        if (messageListItem!!.isMine) {
            tv_text!!.setTextColor(Color.WHITE)
        } else {
            tv_text!!.setTextColor(Color.BLACK)
        }

        val params = tv_text!!.layoutParams as ConstraintLayout.LayoutParams
        if (messageListItem!!.isMine) {
            tv_text!!.setBackgroundResource(R.drawable.bubble_right)
            tv_text!!.setPadding(dpToPixel(10f), dpToPixel(5f), dpToPixel(20f), dpToPixel(5f))
            params.horizontalBias = 1f
        } else {
            tv_text!!.setBackgroundResource(R.drawable.bubble_left)
            tv_text!!.setPadding(dpToPixel(20f), dpToPixel(5f), dpToPixel(10f), dpToPixel(5f))
            params.horizontalBias = 0f
        }
    }

    private fun configSpacing() {
        if (!messageListItem!!.positions.contains(MessageViewHolderFactory.Position.TOP)) {
            space_header!!.visibility = View.VISIBLE
            space_header!!.layoutParams.height = 5
        }
    }

    private fun isBottom(): Boolean {
        return messageListItem!!.positions.contains(MessageViewHolderFactory.Position.BOTTOM)
    }

    override fun setStyle(style: MessageListViewStyle) {
        // nothing
    }

    private fun dpToPixel(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            itemView.resources.displayMetrics
        ).toInt()
    }
}