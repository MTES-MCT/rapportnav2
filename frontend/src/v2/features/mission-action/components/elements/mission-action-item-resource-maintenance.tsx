import { FormikMultiRadio, FormikSelect, Option } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { store } from '../../../../store'
import useResourceByControlUnitQuery from '../../../common/services/use-resources-control-unit'
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
  const { resources } = useResourceByControlUnitQuery(user?.controlUnitId)

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
            <FormikMultiRadio
              isRequired
              name="resourceType"
              label="Type du moyen"
              options={resourceOptions}
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '50%' }}>
            <FormikSelect
              isRequired
              isLight
              searchable
              name={`resourceId`}
              label="Nom du moyen"
              style={{ width: '100%' }}
              isErrorMessageHidden={true}
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
