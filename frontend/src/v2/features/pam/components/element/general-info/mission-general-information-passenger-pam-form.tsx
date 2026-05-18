import {
  Accent,
  Button,
  Dialog,
  DialogProps,
  FormikCheckbox,
  FormikDatePicker,
  FormikSelect,
  FormikTextInput,
  Icon,
  IconButton,
  IconButtonProps,
  Label,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { FC, useMemo } from 'react'
import { FlexboxGrid, Stack, StackProps } from 'rsuite'
import styled from 'styled-components'
import * as Yup from 'yup'
import { MissionPassenger, PASSENGER_OPTIONS } from '../../../../common/types/passenger-type.ts'
import { MissionDates, useMissionDates } from '../../../../common/hooks/use-mission-dates.tsx'
import { useMissionFinished } from '../../../../common/hooks/use-mission-finished.tsx'
import { getSingleDateSchema } from '../../../../common/schemas/dates-schema.ts'

const CrewFormDialogBody = styled((props: DialogProps) => <Dialog.Body {...props} />)(({ theme }) => ({
  padding: 24,
  width: '100%',
  backgroundColor: theme.color.gainsboro
}))

const CrewFormDialogAction = styled((props: DialogProps) => <Dialog.Action {...props} />)(({ theme }) => ({
  paddingTop: 0,
  paddingBottom: 32,
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

const getPassengerSchema = (missionDates?: MissionDates, isMissionFinished?: boolean) =>
  Yup.object().shape({
    fullName: Yup.string().required('Nom requis.'),
    organization: Yup.string().required('Organisation requise.'),
    isIntern: Yup.boolean(),
    startDate: getSingleDateSchema({
      isMissionFinished,
      missionStartDate: missionDates?.startDateTimeUtc,
      missionEndDate: missionDates?.endDateTimeUtc
    }),
    endDate: getSingleDateSchema({
      isMissionFinished,
      missionStartDate: missionDates?.startDateTimeUtc,
      missionEndDate: missionDates?.endDateTimeUtc
    })
  })

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
  const missionDates = useMissionDates(missionId)
  const isMissionFinished = useMissionFinished(missionId)
  const handleSubmit = async (value: MissionPassenger) => {
    await handleSubmitForm(value)
  }

  const passengerSchema = useMemo(
    () => getPassengerSchema(missionDates, isMissionFinished),
    [missionDates, isMissionFinished]
  )

  return (
    <Dialog data-testid={'crew-form'}>
      <Dialog.Title>
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 24, paddingRight: 24 }}>
          <FlexboxGrid.Item>{`${passenger ? 'Mise à jour' : 'Ajout'} d'un passager`}</FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <CloseIconButton onClick={() => handleClose(false)} />
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Dialog.Title>
      <Formik
        onSubmit={handleSubmit}
        validateOnChange={false}
        initialValues={passenger ?? ({} as MissionPassenger)}
        validationSchema={passengerSchema}
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
                <Label style={{ textAlign: 'left' }}>Dates de début et de fin *</Label>
                <CrewFormStack direction="row">
                  <Stack.Item style={{}}>
                    <FormikDatePicker name="startDate" isRequired={true} isLight={true} />
                  </Stack.Item>
                  <Stack.Item style={{}}>
                    <div> au </div>
                  </Stack.Item>
                  <Stack.Item style={{}}>
                    <FormikDatePicker name="endDate" isRequired={true} isLight={true} />
                  </Stack.Item>
                </CrewFormStack>
              </Stack.Item>
            </CrewFormStack>
          </CrewFormDialogBody>
          <CrewFormDialogAction>
            <Button type="submit" data-testid="submit-passenger-form-button" accent={Accent.PRIMARY}>
              {`${passenger ? 'Mettre à jour' : 'Ajouter'} passager`}
            </Button>
            <Button accent={Accent.SECONDARY} onClick={() => handleClose(false)}>
              Annuler
            </Button>
          </CrewFormDialogAction>
        </Form>
      </Formik>
    </Dialog>
  )
}

export default MissionGeneralInformationPassengerPamForm
