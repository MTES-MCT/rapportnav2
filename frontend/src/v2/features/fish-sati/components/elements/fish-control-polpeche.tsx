import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import FishAdministrativeSection from '../../../mission-control/components/ui/fish-control-administrative-section.tsx'
import FishControlEnginesSection from '../../../mission-control/components/ui/fish-control-engines-section.tsx'
import FishControlFleetSegmentSection from '../../../mission-control/components/ui/fish-control-fleet-segment-section.tsx'
import FishControlInnSection from '../../../mission-control/components/ui/fish-control-inn-section.tsx'
import FishControlOtherObservationsSection from '../../../mission-control/components/ui/fish-control-other-observation-section.tsx'
import FishControlSpeciesSection from '../../../mission-control/components/ui/fish-control-species-section.tsx'

interface FishControlPolpecheProps {
  values: ActionFishControlInput
}

const FishControlPolpeche: FC<FishControlPolpecheProps> = ({ values }) => {
  return (
    <div style={{ width: '100%' }}>
      <Stack
        direction="column"
        spacing="1rem"
        alignItems="flex-start"
        style={{ width: '100%' }}
        data-testid={'action-control-nav'}
      >
        <Stack.Item grow={1}></Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishAdministrativeSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlEnginesSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlSpeciesSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlOtherObservationsSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Divider style={{ backgroundColor: THEME.color.charcoal, margin: 0 }} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlFleetSegmentSection action={values} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FishControlInnSection action={values} />
        </Stack.Item>
      </Stack>
    </div>
  )
}

export default FishControlPolpeche
