package actor.proto.tests

import actor.proto.*
import org.junit.Assert
import org.junit.Test
import java.time.Duration

class SupervisionTests_ExponentialBackoff {
    @Test fun `a failure outside window should zero the count` () {
        val lastFailureIsOlderThanWindow = java.lang.System.currentTimeMillis() - java.time.Duration.ofSeconds(11).toMillis()
        val rs : RestartStatistics = RestartStatistics(10, lastFailureIsOlderThanWindow)
        val strategy : ExponentialBackoffStrategy = ExponentialBackoffStrategy(Duration.ofSeconds(10), Duration.ofSeconds(1))

        strategy.handleFailure(DummySupervisor(), dummyPID(), rs, Exception())

        Assert.assertEquals(0, rs.failureCount)
    }


    @Test fun `a failure inside window should increment count` () {
        val lastFailureIsNewerThanWindow = java.lang.System.currentTimeMillis() - java.time.Duration.ofSeconds(9).toMillis()
        val rs : RestartStatistics = RestartStatistics(10, lastFailureIsNewerThanWindow)
        val strategy : ExponentialBackoffStrategy = ExponentialBackoffStrategy(Duration.ofSeconds(10), Duration.ofSeconds(1))

        strategy.handleFailure(DummySupervisor(), dummyPID(), rs, Exception())

        Assert.assertEquals(11, rs.failureCount)
    }
}

class DummySupervisor : Supervisor {
    override val children: Collection<PID>
        get() = emptyList()

    override fun escalateFailure(reason: Exception, who: PID) { }

    override fun restartChildren(reason: Exception, vararg pids: PID) { }

    override fun stopChildren(vararg pids: PID) { }

    override fun resumeChildren(vararg pids: PID) { }
}

fun dummyPID(): PID {
    return PID("", "")
}
