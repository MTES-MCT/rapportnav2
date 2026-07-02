import { FormikMultiRadio, FormikMultiSelect, Option } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { array, string } from 'yup'
import { store } from '../../../../store'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'
import useResources from '../../../common/hooks/use-resources.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

export enum ResourceType {
  NAUTICAL = 'NAUTICAL',
  TERRESTRIAL = 'TERRESTRIAL'
}

const resourceOptions: Option[] = [
  {
    label: 'Moyen nautique',
    value: ResourceType.NAUTICAL
  },
  {
    label: 'Moyen terrestre',
    value: ResourceType.TERRESTRIAL
  }
]

const MissionActionItemResourceMaintenance: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const user = useSelector(store, state => state.user)
  const { getResourcesOptionsByControlUnitId } = useResources()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const schema = {
    resourceIds: isMissionFinished ? array().of(string()).min(1).required() : array().of(string()).nullable(),
    resourceType: isMissionFinished ? string().required() : string().nullable()
  }

  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'action-resource-maintenance-form'}
    >
      <Stack
        direction="column"
        justifyContent="flex-start"
        alignItems="flex-start"
        style={{ width: '100%' }}
        spacing={'1rem'}
      >
        <Stack.Item style={{ width: '50%' }}>
          <FormikMultiRadio
            isRequired
            name="resourceType"
            label="Type du moyen"
            options={resourceOptions}
            isErrorMessageHidden={true}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FormikMultiSelect
            isRequired
            isLight
            searchable
            name={`resourceIds`}
            label="Nom du moyen"
            style={{ width: '100%' }}
            isErrorMessageHidden={true}
            options={getResourcesOptionsByControlUnitId(user?.controlUnitId)}
          />
        </Stack.Item>
      </Stack>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemResourceMaintenance
