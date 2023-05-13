package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.database.types.UIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.timestamp

@AutoloadTable
object TextMessagesTable : UIDTable("message_text") {
  val channel =
      reference(
              "channel",
              ChannelsTable,
              ReferenceOption.CASCADE,
              ReferenceOption.CASCADE,
          )
          .index("INDEX_TEXT_MESSAGE_CHANNEL")
  val timestamp = timestamp("timestamp")
  val sender =
      reference("profile", ProfilesTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
          .nullable()

  val replace =
      reference("replace", this, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
          .nullable()
          .index("INDEX_TEXT_MESSAGE_REPLACE")

  val response =
      reference("response", this, ReferenceOption.SET_NULL, ReferenceOption.CASCADE).nullable()

  val message = text("message").nullable() // null = deleted message

  init {

    index("INDEX_RICH_TEXT_MESSAGE_STREAM_TIMESTAMP", false, channel, timestamp)
  }
}
