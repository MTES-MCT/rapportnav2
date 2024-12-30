import Text from '@common/components/ui/text'
import { ActionTypeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { Accent, Button, Dialog } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { Stack } from 'rsuite'
import MissionActionDropdownWrapper from '../../../common/components/ui/mission-action-dropdown-wrapper'
import MissionControlSelection from '../../../common/components/ui/mission-control-selection'
import { useMissionTimeline } from '../../../common/hooks/use-mission-timeline'
import useCreateMissionActionMutation from '../../../common/services/use-create-mission-action'
import { ModuleType } from '../../../common/types/module-type'

type MissionTimelineAddActionProps = {
  missionId: number
  moduleType: ModuleType
  onSumbit?: (id?: string) => void
}

function MissionTimelineAddAction({ missionId, onSumbit, moduleType }: MissionTimelineAddActionProps): JSX.Element {
  const { getActionInput } = useMissionTimeline(missionId)
  const mutation = useCreateMissionActionMutation(missionId)
  const [showModal, setShowModal] = useState<boolean>(false)

  const handleAddAction = async (actionType: ActionTypeEnum, data?: unknown) => {
    const action = getActionInput(actionType, data)
    const response = await mutation.mutateAsync(action)
    if (onSumbit) onSumbit(response?.id)
  }

  const handleSelect = async (actionType: ActionTypeEnum) => {
    if (actionType === ActionTypeEnum.CONTROL) {
      setShowModal(true)
    } else {
      await handleAddAction(actionType)
    }
  }

  const handleAddControl = async (controlMethod: string, vesselType: VesselTypeEnum) => {
    handleAddAction(ActionTypeEnum.CONTROL, { controlMethod, vesselType })
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
