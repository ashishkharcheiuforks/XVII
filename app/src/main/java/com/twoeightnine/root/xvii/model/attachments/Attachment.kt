package com.twoeightnine.root.xvii.model.attachments

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.twoeightnine.root.xvii.model.WallPost
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Attachment(

        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("photo")
        @Expose
        var photo: Photo? = null,

        @SerializedName("sticker")
        @Expose
        val sticker: Sticker? = null,

        @SerializedName("audio")
        @Expose
        var audio: Audio? = null,

        @SerializedName("link")
        @Expose
        var link: Link? = null,

        @SerializedName("video")
        @Expose
        var video: Video? = null,

        @SerializedName("doc")
        @Expose
        var doc: Doc? = null,

        @SerializedName("wall")
        @Expose
        var wall: WallPost? = null,

        @SerializedName("gift")
        @Expose
        var gift: Gift? = null,

        @SerializedName("audio_message")
        @Expose
        var audioMessage: AudioMessage? = null,

        @SerializedName("poll")
        @Expose
        val poll: Poll? = null
) : Parcelable {

    constructor(photo: Photo) : this(
            type = TYPE_PHOTO,
            photo = photo
    )

    constructor(audio: Audio) : this(
            type = TYPE_AUDIO,
            audio = audio
    )

    constructor(video: Video) : this(
            type = TYPE_VIDEO,
            video = video
    )

    constructor(doc: Doc) : this(
            doc = doc,
            type = TYPE_DOC
    )

    override fun toString() = (when (type) {
        TYPE_PHOTO -> photo
        TYPE_AUDIO -> audio
        TYPE_VIDEO -> video
        TYPE_DOC -> doc
        TYPE_POLL -> poll
        else -> null
    } as? IdTypeable)?.getId() ?: "null"

    companion object {

        const val TYPE_PHOTO = "photo"
        const val TYPE_STICKER = "sticker"
        const val TYPE_AUDIO = "audio"
        const val TYPE_LINK = "link"
        const val TYPE_VIDEO = "video"
        const val TYPE_DOC = "doc"
        const val TYPE_WALL = "wall"
        const val TYPE_GIFT = "gift"
        const val TYPE_AUDIO_MESSAGE = "audio_message"
        const val TYPE_POLL = "poll"
    }
}

fun ArrayList<Attachment>.isSticker() = isNotEmpty() && this[0].sticker != null

fun ArrayList<Attachment>.isGift() = isNotEmpty() && this[0].gift != null

fun ArrayList<Attachment>.getPhotos() = ArrayList(this.mapNotNull { it.photo })

fun ArrayList<Attachment>.photosCount() = getPhotos().size

fun ArrayList<Attachment>.getAudios() = filter { it.type == Attachment.TYPE_AUDIO }.map { it.audio }

fun ArrayList<Attachment>.getAudioMessage() = find { it.type == Attachment.TYPE_AUDIO_MESSAGE }?.audioMessage