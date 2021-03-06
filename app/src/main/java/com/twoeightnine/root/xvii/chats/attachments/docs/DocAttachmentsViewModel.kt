package com.twoeightnine.root.xvii.chats.attachments.docs

import com.twoeightnine.root.xvii.chats.attachments.base.BaseAttachmentsViewModel
import com.twoeightnine.root.xvii.model.attachments.Attachment
import com.twoeightnine.root.xvii.model.attachments.Doc
import com.twoeightnine.root.xvii.network.ApiService

class DocAttachmentsViewModel(api: ApiService) : BaseAttachmentsViewModel<Doc>(api) {

    override val mediaType = "doc"

    override fun convert(attachment: Attachment?) = attachment?.doc
}