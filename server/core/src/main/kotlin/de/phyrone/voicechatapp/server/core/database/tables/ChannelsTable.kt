package de.phyrone.voicechatapp.server.core.database.tables

import de.phyrone.voicechatapp.server.core.ChannelType
import de.phyrone.voicechatapp.server.core.database.types.UIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or

@AutoloadTable
object ChannelsTable : UIDTable("channel") {
  val space =
      reference(
              "space",
              SpacesTable,
              ReferenceOption.CASCADE,
              ReferenceOption.CASCADE,
          )
          .nullable() // null = private or global channel (usually private)

  val name = varchar("name", 64).uniqueIndex()
  val type = enumerationByName<ChannelType>("type", ChannelType.values().maxOf { it.name.length })

  val parent =
      reference("parent", this, ReferenceOption.SET_NULL, ReferenceOption.CASCADE)
          .nullable() // null = no parent

  val settings = text("settings").default("{}") // just a json string of various settings

  init {

    // check that space is only null if its supported by the type
    check("CHECK_CHANNEL_TYPE_MATCH_SPACE") {
      space.isNotNull() or (type inList ChannelType.ALLOWED_OUTSIDE_SPACE)
    }
    // check that parent is only set when in a space and that it matches the same space

    check("CHECK_CHANNEL_PARENT_MATCHES") {
      parent.isNull() or (parent.isNotNull() and space.isNotNull())
    }

    /* does not work since table is not created for check
    check("CHECK_CHANNEL_PARENT_MATCHES") {
        val parentsTable = alias("PARENTS_SUBQUERY_TABLE")
        val selectTablesInSameSpace = parentsTable
            .slice(parentsTable[id])
            .select { parentsTable[space] eq space }

        parent.isNull() or (
                parent.isNotNull() and
                        space.isNotNull() and
                        (parent inSubQuery selectTablesInSameSpace)
                )
    }*/
  }
}
