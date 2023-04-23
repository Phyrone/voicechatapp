package de.phyrone.voicechatapp.server.core.database.types

import de.phyrone.voicechatapp.server.core.database.UID
import java.sql.ResultSet
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect

class UIDColumnType : ColumnType() {
  override fun sqlType(): String =
      currentDialect.dataTypeProvider.binaryType(UID.UID_BINARY_MAX_LENGTH)

  override fun readObject(rs: ResultSet, index: Int): Any? = rs.getBytes(index)

  override fun notNullValueToDB(value: Any): Any {
    return when (value) {
      is UID -> value.toBinary()
      is ByteArray -> value
      is String -> UID.fromUIDString(value).toBinary()
      else -> error("unsupported value: $value")
    }
  }

  override fun nonNullValueToString(value: Any): String {
    return when (value) {
      is UID -> value.toUIDString()
      is ByteArray -> UID.fromBinary(value).toString()
      else -> error("unsupported value: $value")
    }
  }

  override fun valueFromDB(value: Any): Any {
    return when (value) {
      is String -> UID.fromUIDString(value)
      is ByteArray -> UID.fromBinary(value)
      is UID -> value
      else -> error("unsupported value: $value")
    }
  }
}
