package fr.gouv.dgampa.rapportnav.infrastructure.api.concurrency

import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock

/**
 * Serializes concurrent mutations of the same mission action within this JVM (the app runs as a single
 * instance). Two updates to the same actionId can otherwise interleave and race on cascaded child rows
 * (control_2, establishment, ...), which surfaces as ObjectOptimisticLockingFailureException / stale deletes.
 *
 * Uses a fixed pool of striped [ReentrantLock]s: memory is bounded (no per-id map that grows forever), and
 * different actions map to (mostly) different stripes so unrelated updates still run in parallel. Reentrant so
 * a nested call under the same action does not self-deadlock.
 */
@Component
class PerActionLock {

    private val stripes = Array(STRIPE_COUNT) { ReentrantLock() }

    private fun lockFor(actionId: String): ReentrantLock =
        stripes[(actionId.hashCode() and Int.MAX_VALUE) % STRIPE_COUNT]

    fun <T> withLock(actionId: String, block: () -> T): T {
        val lock = lockFor(actionId)
        lock.lock()
        try {
            return block()
        } finally {
            lock.unlock()
        }
    }

    companion object {
        private const val STRIPE_COUNT = 128
    }
}
