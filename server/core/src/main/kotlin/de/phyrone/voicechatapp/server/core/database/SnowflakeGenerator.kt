package de.phyrone.voipsys.server.id

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.updateAndGet

class SnowflakeGenerator(
    private val machineId: Int = 0, // TODO get from config or autoconfigure
) {
  private val lastSnowflake = MutableStateFlow(0L to 0)

  // gets the iteration for the current time and waits if the iteration is full for 1ms
  private suspend fun getUniquePair(): Pair<Long, Short> {
    while (true) {
      val (time, sequence) =
          lastSnowflake.updateAndGet { (lastTime, lastIteration) ->
            val time = System.currentTimeMillis()
            require(lastTime <= time) { "lastTime must be before or eq time" }
            if (lastTime != time) {
              time to 0
            } else if (lastIteration >= SEQUENCE_MAX) {
              time to SEQUENCE_MAX // lock sequence for next time
            } else {
              time to (lastIteration + 1)
            }
          }
      if (sequence != SEQUENCE_MAX) {
        return time to sequence.toShort()
      }
      delay(1)
    }
  }

  suspend fun generateSnowflake(): Long {
    val (timeMillis, iteration) = getUniquePair()
    return assembleSnowflake(timeMillis, iteration, machineId)
  }

  companion object {

    // Thu Apr 18 2019 02:45:55 UTC
    private const val EPOCH_START = 1555555555555L

    private const val SEQUENCE_BITS = 11
    private const val MACHINE_ID_BITS = 11

    private const val SEQUENCE_MAX = 2047 // 11 bits or 2^11-1
    private const val MACHINE_ID_MAX = 2047 // 11 bits or 2^11-1

    /* 42 bits timestamp (millis) | 11 bits machineID |  11 bits iteration  */
    private fun assembleSnowflake(time: Long, sequence: Short, machineID: Int): Long {
      require(sequence in 0..SEQUENCE_MAX) {
        "iteration must be in range 0..$SEQUENCE_MAX but is $sequence"
      }
      require(machineID in 0..MACHINE_ID_MAX) {
        "machineID must be in range 0..$MACHINE_ID_MAX but is $sequence"
      }
      require(time in EPOCH_START..System.currentTimeMillis()) {
        "time must be in range $EPOCH_START..${System.currentTimeMillis()} but is $time"
      }

      val timePart = (time - EPOCH_START) shl (MACHINE_ID_BITS + SEQUENCE_BITS)
      val machinePart = machineID.toLong() shl SEQUENCE_BITS
      val iterationPart = sequence.toLong()

      return timePart or iterationPart or machinePart
    }
  }
}
