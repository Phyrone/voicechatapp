package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.database.types.UIDTable
import org.jetbrains.exposed.sql.ReferenceOption

@AutoloadTable
class TextMessageReactionsTable : UIDTable("message_text_reaction") {
  val message =
      reference("message", TextMessagesTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
  val profile =
      reference("profile", ProfilesTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
  val reaction =
      reference("reaction", ReactionsTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)

  init {
    // each user can only react once per message
    uniqueIndex("INDEX_TEXT_MESSAGE_REACTION", message, profile)
  }
}
