package fr.gouv.gmampa.rapportnav.infrastructure.api.security

import fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionAccessAspect
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

/**
 * Wires [MissionAccessAspect] and turns on AspectJ auto-proxying inside `@WebMvcTest` slices, which do
 * not register `@Component` aspects nor AOP auto-config by default.
 *
 * Add it to a controller slice's `@ContextConfiguration` classes so that `@CheckMissionAccess` is
 * genuinely enforced in the test (the `AssertMissionAccess` bean the aspect depends on is supplied by
 * the test's own `@MockitoBean`).
 */
@Configuration
@EnableAspectJAutoProxy
@Import(MissionAccessAspect::class)
class MissionAccessTestConfig
