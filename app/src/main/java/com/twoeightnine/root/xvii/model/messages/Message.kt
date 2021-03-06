package com.twoeightnine.root.xvii.model.messages

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.twoeightnine.root.xvii.R
import com.twoeightnine.root.xvii.background.longpoll.models.events.BaseMessageEvent
import com.twoeightnine.root.xvii.model.attachments.Attachment
import com.twoeightnine.root.xvii.model.attachments.isGift
import com.twoeightnine.root.xvii.model.attachments.isSticker
import com.twoeightnine.root.xvii.model.attachments.photosCount
import com.twoeightnine.root.xvii.utils.matchesChatId
import com.twoeightnine.root.xvii.utils.time
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("peer_id")
        @Expose
        val peerId: Int = 0,

        @SerializedName("date")
        @Expose
        val date: Int = 0,

        @SerializedName("from_id")
        @Expose
        val fromId: Int = 0,

        @SerializedName("text")
        @Expose
        var text: String = "",

        @SerializedName("out")
        @Expose
        val out: Int = 0,

        @SerializedName("action")
        @Expose
        val action: MessageAction? = null,
//
//        @SerializedName("action_text")
//        @Expose
//        val actionText: String? = null,
//
//        @SerializedName("action_mid")
//        @Expose
//        val actionMid: String? = null,

        @SerializedName("fwd_messages")
        @Expose
        val fwdMessages: ArrayList<Message>? = arrayListOf(),

        @SerializedName("attachments")
        @Expose
        val attachments: ArrayList<Attachment>? = arrayListOf(),

        @SerializedName("reply_message")
        @Expose
        var replyMessage: Message? = null,

        @SerializedName("update_time")
        @Expose
        var updateTime: Int = 0,

        @SerializedName("important")
        val important: Boolean = false,

        // ------------------- manually added values
        @SerializedName("read")
        @Expose
        var read: Boolean = false,

        @SerializedName("name")
        @Expose
        var name: String? = null,

        @SerializedName("photo")
        @Expose
        var photo: String? = null
) : Parcelable {

    constructor(event: BaseMessageEvent, prepareText: (String) -> String = { it }) : this(
            id = event.id,
            peerId = event.peerId,
            date = event.timeStamp,
            fromId = event.info.from,
            text = prepareText(event.text),
            out = if (event.isOut()) 1 else 0
    )

    fun isEdited() = updateTime != 0

    fun isOut() = out == 1

    fun isSystem() = action != null

    fun isSticker() = attachments != null && attachments.isSticker() && replyMessage == null

    fun isGift() = attachments != null && attachments.isGift() && replyMessage == null

    fun isReplyingSticker() = attachments != null && attachments.isSticker() && replyMessage != null

    fun hasPhotos() = attachments != null && attachments.photosCount() > 0

    fun isChat() = peerId.matchesChatId()

    fun isFresh() = time() - date < DAY

    fun isEditable() = isOut() && isFresh() && !isSticker()

    fun isDeletableForAll() = isOut() && isFresh()

    fun getResolvedMessage(context: Context?) = when {
        context == null || text.isNotBlank() -> text
        attachments != null && attachments.isSticker() -> context.getString(R.string.sticker)
        attachments != null && attachments.isNotEmpty() -> {
            val count = attachments.size
            context.resources.getQuantityString(R.plurals.attachments, count, count)
        }
        fwdMessages != null && fwdMessages.isNotEmpty() -> {
            val count = fwdMessages.size
            context.resources.getQuantityString(R.plurals.messages, count, count)
        }
        else -> text
    }

    companion object {
        const val DAY = 3600 * 24
    }
}