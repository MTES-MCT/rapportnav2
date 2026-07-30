package fr.gouv.gmampa.rapportnav.infrastructure.api.concurrency

import fr.gouv.dgampa.rapportnav.infrastructure.api.concurrency.PerActionLock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread

class PerActionLockTest {

    @Test
    fun `withLock serializes concurrent access for the same actionId`() {
        val lock = PerActionLock()
        val threads = 20
        val perThread = 1_000
        // non-atomic on purpose: without mutual exclusion, concurrent increments would lose updates
        var counter = 0

        val workers = (1..threads).map {
            thread {
                repeat(perThread) {
                    lock.withLock("action-1") { counter++ }
                }
            }
        }
        workers.forEach { it.join() }

        assertThat(counter).isEqualTo(threads * perThread)
    }

    @Test
    fun `withLock is reentrant for the same actionId`() {
        val lock = PerActionLock()
        // would deadlock (hang) if the lock were not reentrant
        val result = lock.withLock("action-1") { lock.withLock("action-1") { 42 } }
        assertThat(result).isEqualTo(42)
    }

    @Test
    fun `withLock returns the block result`() {
        val lock = PerActionLock()
        assertThat(lock.withLock("action-1") { "ok" }).isEqualTo("ok")
    }
}
