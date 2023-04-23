import de.phyrone.voicechatapp.server.core.database.UID
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class TestUIDEncoding {

    @Test
    fun testUIDToStringEncoding() {
        val uid = UID(183257676496339456U, "example.com")
        val uidString = uid.toUIDString()
        assertEquals("183257676496339456:example.com", uidString)
    }

    @Test
    fun testUIDToBinaryEncoding() {
        val uid = UID(183257676496339456U, "example.com")
        val uidBytes = uid.toBinary()
        assertEquals("028b0fe2c5ed82006578616d706c652e636f6d", uidBytes.joinToString("") { "%02x".format(it) })
    }

}