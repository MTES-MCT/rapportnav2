import { FormikMultiRadio, FormikSelect, Option } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { store } from '../../../../store'
import useControlUnitResourcesQuery from '../../../common/services/use-control-unit-resources'
import { ControlUnitResource } from '../../../common/types/control-unit-types'
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
  const user = useStore(store, state => state.user)
  const { data: resources } = useControlUnitResourcesQuery(user?.controlUnitId)

  const schema = {
    resourceId: string().required(),
    resourceType: string().required()
  }

  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'action-resource-maintenance-form'}
      children={
        <Stack
          direction="column"
          justifyContent="flex-start"
          alignItems="flex-start"
          style={{ width: '100%' }}
          spacing={'1rem'}
        >
          <Stack.Item style={{ width: '50%' }}>
            <FormikMultiRadio isRequired name="resourceType" options={resourceOptions} label="Type du moyen" />
          </Stack.Item>
          <Stack.Item style={{ width: '50%' }}>
            <FormikSelect
              isRequired
              isLight
              searchable
              style={{ width: '100%' }}
              name={`resourceId`}
              label="Nom du moyen"
              options={
                resources?.map((resource: ControlUnitResource) => ({
                  value: resource.id!!,
                  label: `${resource.name}`
                })) ?? []
              }
            />
          </Stack.Item>
        </Stack>
      }
    />
  )
}
export default MissionActionItemResourceMaintenance
