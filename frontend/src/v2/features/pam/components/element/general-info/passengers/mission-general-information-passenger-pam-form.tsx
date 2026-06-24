import { Dialog } from '@common/components/ui/custom-dialog.tsx'
import {
  Accent,
  Button,
  DialogProps,
  FormikCheckbox,
  FormikSelect,
  FormikTextInput,
  Icon,
  IconButton,
  IconButtonProps,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { FC } from 'react'
import { Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import { MissionPassenger, PASSENGER_OPTIONS } from '../../../../../common/types/passenger-type.ts'
import MissionBoundFormikDateRangePicker from '../../../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'
import { MissionPassengerInput, usePassengerForm } from '../../../../hooks/use-passenger-form.tsx'

const CrewFormDialogBody = styled((props: DialogProps) => <Dialog.Body {...props} />)(({ theme }) => ({
  padding: 24,
  width: '100%',
  backgroundColor: theme.color.gainsboro
}))

const CrewFormDialogAction = styled((props: DialogProps) => <Dialog.Action {...props} />)(({ theme }) => ({
  paddingTop: 12,
  paddingBottom: 12,
  justifyContent: 'center',
  backgroundColor: theme.color.gainsboro
}))

const CrewFormStack = styled((props: StackProps) => (
  <Stack direction="column" alignItems="flex-start" spacing="1rem" {...props} />
))({
  width: '100%'
})

const CloseIconButton = styled((props: Omit<IconButtonProps, 'Icon'>) => (
  <IconButton
    {...props}
    Icon={Icon.Close}
    size={Size.NORMAL}
    role={'quit-modal-crew'}
    accent={Accent.TERTIARY}
    color={THEME.color.gainsboro}
    data-testid="close-crew-form-icon"
  />
))(({ theme }) => ({
  color: theme.color.gainsboro
}))

interface MissionPassengerModalProps {
  missionId?: string
  passenger?: MissionPassenger
  handleClose: (open: boolean) => void
  handleSubmitForm: (passenger: MissionPassenger) => Promise<void>
}

const MissionGeneralInformationPassengerPamForm: FC<MissionPassengerModalProps> = ({
  missionId,
  passenger,
  handleClose,
  handleSubmitForm
}) => {
  const { getInitValue, fromInputToPassenger, validationSchema } = usePassengerForm(missionId)

  const handleSubmit = async (value: MissionPassengerInput) => {
    await handleSubmitForm(fromInputToPassenger(value))
  }
  debugger
  return (
    <Dialog data-testid={'passenger-form'}>
      <Dialog.Title>
        <Stack direction={'row'} justifyContent={'space-between'} alignItems={'flex-start'} style={{ width: '100%' }}>
          <Stack.Item>{`${passenger ? 'Mise à jour' : 'Ajout'} d'un passager`}</Stack.Item>
          <Stack.Item>
            <CloseIconButton onClick={() => handleClose(false)} />
          </Stack.Item>
        </Stack>
      </Dialog.Title>
      <Formik
        onSubmit={handleSubmit}
        validateOnChange={false}
        initialValues={getInitValue(passenger)}
        validationSchema={validationSchema}
      >
        <Form>
          <CrewFormDialogBody>
            <CrewFormStack>
              <Stack.Item style={{ width: '100%' }}>
                <CrewFormStack direction="row">
                  <Stack.Item style={{ flex: 1, width: '50%' }}>
                    <FormikTextInput
                      name="fullName"
                      label="Prénom, Nom"
                      aria-label="Identité"
                      placeholder={'Prénom Nom'}
                      isLight={true}
                      isRequired={true}
                      data-testid={'fullName'}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ flex: 1, width: '50%' }}>
                    <FormikSelect
                      name="organization"
                      isLight={true}
                      label="Organisation"
                      aria-label="Organisation"
                      isRequired={true}
                      options={PASSENGER_OPTIONS}
                      data-testid={'organization'}
                    />
                  </Stack.Item>
                </CrewFormStack>
                <CrewFormStack direction="row">
                  <Stack.Item style={{ flex: 1, width: '50%' }}>
                    <div> </div>
                  </Stack.Item>
                  <Stack.Item style={{ flex: 1, width: '50%' }}>
                    <FormikCheckbox name="isIntern" isLight={true} label="Stagiaire" aria-label="Stagiaire" />
                  </Stack.Item>
                </CrewFormStack>
              </Stack.Item>
              <Stack.Item style={{ flex: 1, width: '100%' }}>
                <MissionBoundFormikDateRangePicker
                  name="dates"
                  label="Dates de début et de fin"
                  missionId={missionId}
                  withTime={false}
                  isLight={true}
                />
              </Stack.Item>
            </CrewFormStack>
          </CrewFormDialogBody>
          <CrewFormDialogAction>
            <Button accent={Accent.SECONDARY} onClick={() => handleClose(false)}>
              Annuler
            </Button>
            <Button type="submit" data-testid="submit-passenger-form-button" accent={Accent.PRIMARY}>
              {`${passenger ? 'Mettre à jour' : 'Ajouter'} passager`}
            </Button>
          </CrewFormDialogAction>
        </Form>
      </Formik>
    </Dialog>
  )
}

export default MissionGeneralInformationPassengerPamForm
