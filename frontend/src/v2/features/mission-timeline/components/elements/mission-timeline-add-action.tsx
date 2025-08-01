import Text from '@common/components/ui/text'
import { VesselTypeEnum } from '@common/types/mission-types'
import { Accent, Button, Dialog } from '@mtes-mct/monitor-ui'
import { JSX, useState } from 'react'
import { Stack } from 'rsuite'
import MissionControlSelection from '../../../common/components/ui/mission-control-selection'
import { useTimelineAction } from '../../../common/hooks/use-timeline-action'
import useCreateActionMutation from '../../../common/services/use-create-action'
import { ActionType } from '../../../common/types/action-type'
import { ModuleType } from '../../../common/types/module-type'
import { TimelineDropdownItem } from '../../hooks/use-timeline'
import MissionTimelineDropdownWrapper from '../layout/mission-timeline-dropdown-wrapper'
import { MissionNavAction } from '../../../common/types/mission-action.ts'
import { useNavigate } from 'react-router-dom'
import { navigateToActionId } from '@router/routes.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { v4 as uuidv4 } from 'uuid'
import { OwnerType } from '../../../common/types/owner-type.ts'

type MissionTimelineAddActionProps = {
  missionId: string
  moduleType: ModuleType
  onSubmit?: (id?: string) => void
  dropdownItems: TimelineDropdownItem[]
}

function MissionTimelineAddAction({
  missionId,
  onSubmit,
  moduleType,
  dropdownItems
}: MissionTimelineAddActionProps): JSX.Element {
  const navigate = useNavigate()
  const { isOnline } = useOnlineManager()
  const mutation = useCreateActionMutation()
  const { getActionInput } = useTimelineAction(missionId)
  const [showModal, setShowModal] = useState<boolean>(false)

  const handleAddAction = async (actionType: ActionType, data?: unknown) => {
    const action = {
      id: uuidv4(), // Generate a UUID locally
      ...getActionInput(actionType, data)
    }

    mutation.mutate(
      { ownerId: missionId, ownerType: OwnerType.MISSION, action },
      {
        onSuccess: (data: MissionNavAction) => {
          const id = data?.data?.id
          if (id) {
            navigateToActionId(id, navigate)
          }
          if (onSubmit && isOnline) {
            onSubmit(id)
          }
        }
      }
    )

    navigateToActionId(action.id, navigate)
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
            <MissionControlSelection onSelect={handleAddControl} moduleType={moduleType} />
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
