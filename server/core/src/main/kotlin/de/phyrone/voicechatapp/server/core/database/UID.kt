package de.phyrone.voicechatapp.server.core.database

import de.phyrone.voicechatapp.server.core.DOMAIN_MAX_LENGTH
import java.io.Serializable
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class UID(
    val snowflake:
        Long, // negative values are reserved for system f.e. system messages or something
    val host: String,
) : Comparable<UID>, Serializable {

  override fun compareTo(other: UID): Int {
    return when {
      snowflake != other.snowflake -> snowflake.compareTo(other.snowflake)
      else -> host.compareTo(other.host)
    }
  }

  fun toUIDString(): String = "$snowflake$UID_STRING_SEPERATOR$host"

  // efficient binary representation for database (snowflake + port + domain) -> 8 + 2 + up to 253
  /**
   * Encode this UID to a binary representation the binary representation is build as follows: 8
   * bytes for the [snowflake] (unsigned long) followed by the [host] encoded as UTF-8 the byteorder
   * is big endian
   *
   * @return binary representation of this UID
   */
  fun toBinary(): ByteArray =
      host.toByteArray(Charsets.UTF_8).let { hostnameBytes ->
        ByteBuffer.allocate(Long.SIZE_BYTES + hostnameBytes.size)
            .also { buffer ->
              buffer.order(ByteOrder.BIG_ENDIAN)
              buffer.putLong(snowflake.toLong())
              buffer.put(hostnameBytes)
            }
            .array()
      }

  companion object {

    private const val SNOWFLAKE_STRING_MAX_LENGTH = 20
    const val UID_STRING_MAX_LENGTH = SNOWFLAKE_STRING_MAX_LENGTH + DOMAIN_MAX_LENGTH + 1

    const val UID_BINARY_MAX_LENGTH = Long.SIZE_BYTES + DOMAIN_MAX_LENGTH

    const val UID_STRING_SEPERATOR = ':'

    fun fromUIDString(uidString: String): UID {
      require(uidString.length <= UID_STRING_MAX_LENGTH) { "UID String is too long" }
      require(uidString.contains('@')) { "UID String is invalid" }
      val (snowflakeString, hostname) = uidString.split(UID_STRING_SEPERATOR, limit = 2)
      val snowflake = snowflakeString.toLongOrNull() ?: error("UID String is invalid")
      return UID(snowflake, hostname)
    }

    fun fromBinary(binary: ByteArray): UID {
      require(binary.size > Long.SIZE_BYTES) { "Binary representation of UID is too short" }
      require(binary.size <= UID_BINARY_MAX_LENGTH) { "Binary representation of UID is too long" }
      val buffer = ByteBuffer.wrap(binary)
      buffer.order(ByteOrder.BIG_ENDIAN)
      val snowflake = buffer.long
      val hostnameBytes = ByteArray(buffer.remaining())
      buffer.get(hostnameBytes)
      val hostname = hostnameBytes.toString(Charsets.UTF_8)
      return UID(snowflake, hostname)
    }
  }
}
