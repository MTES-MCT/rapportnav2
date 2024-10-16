import { ActionTypeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { useMissionTimeline } from '@features/v2/common/hooks/use-mission-timeline'
import { useAddOrUpdateActionMutation } from '@features/v2/common/services/use-add-action'
import { ModuleType } from '@features/v2/common/types/module-type'
import { Accent, Button, Dialog } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { Stack } from 'rsuite'
import Text from '../../../../common/components/ui/text'
import useAddOrUpdateControlMutation from '../../services/use-add-update-action-control'
import MissionActionDropdownWrapper from '../ui/mission-action-dropdown-wrapper'
import MissionControlSelection from '../ui/mission-control-selection'

type MissionTimelineAddActionProps = {
  missionId?: string
  moduleType: ModuleType
  onSumbit?: (id?: string) => void
}

function MissionTimelineAddAction({ missionId, onSumbit, moduleType }: MissionTimelineAddActionProps): JSX.Element {
  const [addControl] = useAddOrUpdateControlMutation()
  const [addOrUpdateAction] = useAddOrUpdateActionMutation()
  const [showModal, setShowModal] = useState<boolean>(false)
  const { getBaseInput, getActionDataInput } = useMissionTimeline(missionId)

  const handleAddAction = async (actionType: ActionTypeEnum) => {
    const data = getActionDataInput(actionType)
    const response = await addOrUpdateAction({ variables: { action: { missionId, type: actionType, data } } })
    if (onSumbit) onSumbit(response.data?.id)
  }

  const handleSelect = async (actionType: ActionTypeEnum) => {
    if (actionType === ActionTypeEnum.CONTROL) {
      setShowModal(true)
    } else {
      await handleAddAction(actionType)
    }
  }

  const handleAddControl = async (controlMethod: string, vesselType: VesselTypeEnum) => {
    const controlAction = {
      vesselType,
      controlMethod,
      ...getBaseInput(),
      endDateTimeUtc: new Date()
    }
    const response = await addControl({ variables: { controlAction } })
    if (onSumbit) onSumbit(response.data?.id)
  }

  return (
    <Stack direction={'row'} spacing={'0.5rem'}>
      <Stack.Item>
        <Text as="h2" weight="bold">
          Actions réalisées en mission
        </Text>
      </Stack.Item>
      <Stack.Item>
        <MissionActionDropdownWrapper moduleType={moduleType} onSelect={handleSelect} />
      </Stack.Item>
      {showModal && (
        <Dialog>
          <Dialog.Title>Ajouter des contrôles</Dialog.Title>
          <Dialog.Body>
            <MissionControlSelection onSelect={handleAddControl} />
          </Dialog.Body>
          <Dialog.Action style={{ justifyContent: 'flex-end', paddingRight: '1.5rem' }}>
            <Button accent={Accent.SECONDARY} onClick={() => setShowModal(false)}>
              Fermer
            </Button>
          </Dialog.Action>
        </Dialog>
      )}
    </Stack>
  )
}

export default MissionTimelineAddAction
