import Text from '@common/components/ui/text'
import { VesselTypeEnum } from '@common/types/mission-types'
import { Accent, Button, Dialog } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import { Stack } from 'rsuite'
import MissionControlSelection from '../../../common/components/ui/mission-control-selection'
import { useMissionTimeline } from '../../../common/hooks/use-mission-timeline'
import useCreateMissionActionMutation from '../../../common/services/use-create-mission-action'
import { ActionType } from '../../../common/types/action-type'
import { TimelineDropdownItem } from '../../hooks/use-timeline'
import MissionTimelineDropdownWrapper from '../layout/mission-timeline-dropdown-wrapper'

type MissionTimelineAddActionProps = {
  missionId: number
  onSumbit?: (id?: string) => void
  dropdownItems: TimelineDropdownItem[]
}

function MissionTimelineAddAction({ missionId, onSumbit, dropdownItems }: MissionTimelineAddActionProps): JSX.Element {
  const { getActionInput } = useMissionTimeline(missionId)
  const mutation = useCreateMissionActionMutation(missionId)
  const [showModal, setShowModal] = useState<boolean>(false)

  const handleAddAction = async (actionType: ActionType, data?: unknown) => {
    const action = getActionInput(actionType, data)
    const response = await mutation.mutateAsync(action)
    if (onSumbit) onSumbit(response?.id)
  }

  const handleSelect = async (actionType: ActionType) => {
    if (actionType === ActionType.CONTROL) {
      setShowModal(true)
    } else {
      await handleAddAction(actionType)
    }
  }

  const handleAddControl = async (controlMethod: string, vesselType: VesselTypeEnum) => {
    await handleAddAction(ActionType.CONTROL, { controlMethod, vesselType })
    setShowModal(false)
  }

  return (
    <Stack direction={'row'} spacing={'0.5rem'}>
      <Stack.Item>
        <Text as="h2" weight="bold">
          Actions réalisées en mission
        </Text>
      </Stack.Item>
      <Stack.Item>
        <MissionTimelineDropdownWrapper dropdownItems={dropdownItems} onSelect={handleSelect} />
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
