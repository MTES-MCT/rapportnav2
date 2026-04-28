import { Label, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import MissionControlFishAdministrativeSection from '../../../mission-control/components/ui/mission-control-fish-administrative-section.tsx'
import FishControlEnginesSection from '../../../mission-control/components/ui/mission-control-fish-engines-section.tsx'
import FishControlFleetSegmentSection from '../../../mission-control/components/ui/mission-control-fish-fleet-segment-section.tsx'
import FishControlOtherObservationsSection from '../../../mission-control/components/ui/mission-control-fish-other-observation-section.tsx'
import FishControlSeizureSection from '../../../mission-control/components/ui/mission-control-fish-seizure-section.tsx'
import FishControlSpeciesSection from '../../../mission-control/components/ui/mission-control-fish-species-section.tsx'
import MissionInfractionFishSummary from '../../../mission-infraction/components/elements/mission-infraction-fish-summary.tsx'
import { ActionFishControlInput } from '../../types/action-type.ts'

const FishControlPolpeche: FC<{
  values: ActionFishControlInput
}> = ({ values }) => {
  return (
    <div style={{ width: '100%' }}>
      <Stack
        direction="column"
        spacing="2rem"
        alignItems="flex-start"
        style={{ width: '100%' }}
        data-testid={'action-control-nav'}
      >
        <Stack.Item grow={1}></Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MissionControlFishAdministrativeSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlEnginesSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlSpeciesSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlSeizureSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlOtherObservationsSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal }} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlFleetSegmentSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal }} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
            <Stack.Item>
              <Label>Infractions</Label>
            </Stack.Item>
            <Stack.Item style={{ width: '100%' }}>
              <MissionInfractionFishSummary
                showIndex={true}
                title="Infraction"
                isActionDisabled={true}
                infractions={values?.fishInfractions ?? []}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" alignItems="flex-start">
            <Stack.Item>
              <Label>Saisi par</Label>
            </Stack.Item>
            <Stack.Item>{values?.userTrigram ?? '--'}</Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </div>
  )
}

export default FishControlPolpeche
