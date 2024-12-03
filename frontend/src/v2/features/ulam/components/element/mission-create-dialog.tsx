import React, { FC } from 'react'
import { Dialog, Button, THEME, Accent } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import MissionGeneralInformationUlamFormNew from './mission-general-information-ulam-form-new.tsx'

const MissionCreateDialog: FC = () => {
  return (
    <Dialog>
      <Dialog.Title>Création d'un rapport de mission</Dialog.Title>
      <Dialog.Body style={{ backgroundColor: THEME.color.gainsboro }}>
        <Stack direction="column" spacing="1.5rem">
          <MissionGeneralInformationUlamFormNew
            startDateTimeUtc="2024-01-01T00:00:00Z"
            endDateTimeUtc="2024-01-01T00:00:00Z"
            missionType={[MissionTypeEnum.AIR]}
          />
        </Stack>
      </Dialog.Body>
      <Dialog.Action style={{backgroundColor: THEME.color.gainsboro}}>
        <Button accent={Accent.SECONDARY}>Annuler</Button>
      </Dialog.Action>


    </Dialog>
  )
}

export default MissionCreateDialog
