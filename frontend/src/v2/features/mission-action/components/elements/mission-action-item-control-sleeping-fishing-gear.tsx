import { FormikCheckbox, FormikNumberInput, FormikSelect, Message } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FISHING_GEAR_TYPES, FishingGearType } from '../../../common/types/leisure-fishing-gear-type'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericControl from './mission-action-item-generic-control'

const MissionActionItemSleepingFishingGearControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={true}
      data-testid={'action-control-other'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
          <Stack.Item>
            <Message>
              Renseigner uniquement les engins contrôlés hors cadre d'un contrôle sur navire de pêche pro. Dans le cas
              d'un engin rattaché à un navire de pêche pro veuillez contacter le CNSP
            </Message>
          </Stack.Item>
          <Stack.Item style={{ width: '70%' }}>
            <FormikSelect
              name="fishingGearType"
              label="Type d'engin de pêche dormant"
              isLight={true}
              isRequired={true}
              options={Object.keys(FishingGearType)?.map(key => ({
                value: key,
                label: FISHING_GEAR_TYPES[key as keyof typeof FishingGearType]
              }))}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '50%' }}>
            <FormikNumberInput
              isLight={true}
              isRequired={true}
              name="nbrOfControl"
              label="Nombre total de contrôles"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FormikCheckbox name="isSeizureSleepingFishingGear" label="Appréhension des engins de pêche dormant" />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </MissionActionItemGenericControl>
  )
}
export default MissionActionItemSleepingFishingGearControl
