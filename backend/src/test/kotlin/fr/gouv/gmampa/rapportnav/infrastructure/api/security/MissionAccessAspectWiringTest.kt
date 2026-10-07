package fr.gouv.gmampa.rapportnav.infrastructure.api.security

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test

/**
 * Guards that the AspectJ runtime that makes [fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionAccessAspect]
 * actually weave is present on the runtime classpath — the one link no other test covers.
 *
 * Spring Boot's `AopAutoConfiguration` enables AspectJ auto-proxying (which weaves `@Aspect`
 * `@Component`s, i.e. our `@CheckMissionAccess` enforcement) only when `org.aspectj.weaver.Advice`
 * and `org.aspectj.lang.annotation.Aspect` are on the classpath (its `@ConditionalOnClass`). If they
 * ever drop off — e.g. the transitive `aspectjweaver` from another starter is pruned — the aspect
 * becomes an inert bean and **every `@CheckMissionAccess` silently fails open** while all slice tests
 * (which force-enable AOP via `@EnableAspectJAutoProxy`) stay green. The weaver arrives transitively
 * (via `spring-aspects`); Boot 4 has no `spring-boot-starter-aop` to pin it, so this test is the guard
 * that turns red the moment that transitive pull ever disappears.
 *
 * It complements [MissionAccessAspectTest] (proves the aspect's logic once woven) and the controller
 * slice tests (prove the end-to-end 403 when AOP is enabled).
 */
class MissionAccessAspectWiringTest {

    @Test
    fun `aspectj runtime is on the classpath so @CheckMissionAccess is woven in production`() {
        val classLoader = this::class.java.classLoader
        assertDoesNotThrow({
            Class.forName("org.aspectj.weaver.Advice", false, classLoader)
        }, "org.aspectj.weaver.Advice missing: @CheckMissionAccess would fail open in prod")
        assertDoesNotThrow({
            Class.forName("org.aspectj.lang.annotation.Aspect", false, classLoader)
        }, "org.aspectj.lang.annotation.Aspect missing: @CheckMissionAccess would fail open in prod")
    }
}
