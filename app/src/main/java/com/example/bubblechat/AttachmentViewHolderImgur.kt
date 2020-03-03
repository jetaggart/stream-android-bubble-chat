package com.example.bubblechat

import android.content.Context
import android.view.ViewGroup
import com.bumptech.glide.Glide

import com.getstream.sdk.chat.adapter.BaseAttachmentViewHolder;
import com.getstream.sdk.chat.adapter.MessageListItem
import com.getstream.sdk.chat.model.Attachment
import com.getstream.sdk.chat.utils.roundedImageView.PorterShapeImageView
import com.getstream.sdk.chat.view.MessageListView
import com.getstream.sdk.chat.view.MessageListViewStyle

class AttachmentViewHolderImgur(resId: Int, parent: ViewGroup) :
    BaseAttachmentViewHolder(resId, parent) {
    private val iv_media_thumb: PorterShapeImageView

    init {

        iv_media_thumb = itemView.findViewById(R.id.iv_media_thumb)
    }

    override fun bind(
        context: Context,
        messageListItem: MessageListItem,
        attachment: Attachment,
        style: MessageListViewStyle,
        clickListener: MessageListView.AttachmentClickListener,
        longClickListener: MessageListView.MessageLongClickListener
    ) {
        super.bind(context, messageListItem, attachment, style, clickListener, longClickListener)

        val background = bubbleHelper.getDrawableForAttachment(
            messageListItem.message,
            messageListItem.isMine,
            messageListItem.positions,
            attachment
        )
        iv_media_thumb.setShape(context, background)
        iv_media_thumb.setOnClickListener(this)
        iv_media_thumb.setOnLongClickListener(this)

        Glide.with(context)
            .load(attachment.thumbURL)
            .into(iv_media_thumb)
    }
}
