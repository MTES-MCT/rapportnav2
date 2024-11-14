import React, { FC } from 'react'
import { Accent, Button, Dialog, MultiCheckbox, Select, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'
import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import { toast } from 'react-toastify'
import onChange = toast.onChange

const REPORT_TYPE_OPTIONS = [
  {
    label: 'Rapport avec sortie terrain',
    value: 1
  },
  {
    label: 'Rapport sans sortie terrain (admin. uniquement)',
    value: 2
  },
  {
    label: 'Rapport de temps agent en renfort exterieur',
    value: 3
  }
]

const MISSION_TYPE_OPTIONS = [
  {
    label: 'Terre',
    value: false
  },
  {
    label: 'Mer',
    value: false
  },
  {
    label: 'Air',
    value: false
  }
]

const MissionCreateDialog: FC = () => {
  return (
    <Dialog>
      <Dialog.Title>Création d'un rapport de mission</Dialog.Title>
      <Dialog.Body style={{backgroundColor: THEME.color.gainsboro}}>
        <Stack style={{width: '100%'}} direction={"column"} spacing={"1.5rem"}>
          <Stack.Item style={{width: '100%'}}>
            <Select
              options={REPORT_TYPE_OPTIONS}
              label={"Type de rapport"}
              isRequired={true}
            />
          </Stack.Item>

          <Stack.Item style={{width: '100%'}}>
            <MultiCheckbox
              label={"Type de mission"}
              name={"mission_type_input"}
              options={MISSION_TYPE_OPTIONS}
              isInline={true}
              style={{textAlign: "left"}}
              isRequired={true}
            />
          </Stack.Item>

          <Stack.Item>
            <DateRangePicker
              name="dates"
              isRequired={true}
              label="Date et heure de début et de fin"
              withTime={true}
              isCompact={true}
              isLight={true}
              role={'ok'}
              onChange={async (nextValue?: DateRange) => {
                await onChange(nextValue)('dates')
              }}
            />
          </Stack.Item>
        </Stack>
      </Dialog.Body>
      <Dialog.Action style={{backgroundColor: THEME.color.gainsboro}}>
        <Button accent={Accent.PRIMARY}>Créer le rapport</Button>
        <Button accent={Accent.SECONDARY}>Annuler</Button>
      </Dialog.Action>


    </Dialog>
  )
}

export default MissionCreateDialog
