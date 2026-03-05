import Text from '@common/components/ui/text'
import { ToggleLabel } from '@common/components/ui/toogle-label'
import { FormikCheckbox, FormikEffect, FormikMultiRadio, FormikToggle, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikNumberInput } from '../../../common/components/ui/formik-number-input'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input'
import { MissionAction } from '../../../common/types/mission-action'
import { RescueType } from '../../../common/types/rescue-type'
import { useMissionActionRescue } from '../../hooks/use-mission-action-rescue'
import { ActionRescueInput } from '../../types/action-type'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

const RESCUE_TYPE_OPTIONS = [
  {
    label: 'Assistance de navire en difficulté',
    value: RescueType.VESSEL
  },
  {
    label: 'Sauvegarde de la vie humaine',
    value: RescueType.PERSON
  }
]

const MissionActionItemRescue: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionRescue(action, onChange)

  return (
    <form style={{ width: '100%' }} data-testid={'action-rescue-form'}>
      {initValue && (
        <Formik
          validateOnChange={true}
          validateOnMount={true}
          onSubmit={handleSubmit}
          initialValues={initValue}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {() => (
            <>
              <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionRescueInput)} />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <MissionBoundFormikDateRangePicker
                        isLight={true}
                        missionId={action.ownerId ?? action.missionId}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="geoCoords">
                    {(field: FieldProps<number[]>) => (
                      <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextInput
                    name="locationDescription"
                    isRequired={false}
                    label="Précision concernant la localisation"
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikMultiRadio label="" name="rescueType" options={RESCUE_TYPE_OPTIONS} />
                </Stack.Item>
              </Stack>

              {initValue.rescueType === RescueType.VESSEL && (
                <Stack
                  direction="column"
                  spacing="2rem"
                  alignItems="flex-start"
                  style={{ width: '100%', marginTop: '35px' }}
                >
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                      <Stack.Item>
                        <FormikToggle size="sm" label="" isLight={true} name="operationFollowsDEFREP" />
                      </Stack.Item>
                      <Stack.Item alignSelf="flex-end">
                        <ToggleLabel>Opération suivie (DEFREP)</ToggleLabel>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>

                  <Stack.Item style={{ marginBottom: 15 }}>
                    <FormikCheckbox
                      isLight
                      readOnly={false}
                      name="isVesselNoticed"
                      style={{ marginBottom: 8 }}
                      label="Le navire a été mis en demeure avant intervention"
                    />
                    <FormikCheckbox isLight readOnly={false} name="isVesselTowed" label="Le navire a été remorqué" />
                  </Stack.Item>
                </Stack>
              )}

              {initValue.rescueType === RescueType.PERSON && (
                <Stack>
                  <Stack.Item>
                    <Stack style={{ width: '100%', marginBottom: 25, marginTop: 25 }}>
                      <Stack.Item>
                        <FormikNumberInput
                          style={{ marginRight: 10 }}
                          name="numberPersonsRescued"
                          label="Nb de personnes secourues"
                        />
                      </Stack.Item>
                      <Stack.Item>
                        <FormikNumberInput name="numberOfDeaths" label="Nb de personnes disparues / décédées" />
                      </Stack.Item>
                    </Stack>
                    <Stack.Item>
                      <FormikCheckbox
                        isLight
                        readOnly={false}
                        style={{ marginBottom: 25 }}
                        name="isInSRRorFollowedByCROSSMRCC"
                        label="Opération en zone SRR ou suivie par un CROSS / MRCC"
                      />
                    </Stack.Item>
                  </Stack.Item>
                </Stack>
              )}

              <Stack>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextAreaInput label="Observations" name="observations" data-testid="observations" />
                </Stack.Item>
              </Stack>

              {initValue.rescueType === RescueType.PERSON && (
                <Stack direction={'column'} spacing={'1rem'} alignItems={'flex-start'}>
                  <Stack.Item style={{ width: '100%' }}>
                    <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                      <Stack.Item>
                        <FormikToggle label="" size="sm" name="isMigrationRescue" />
                      </Stack.Item>
                      <Stack.Item alignSelf="flex-end">
                        <Text as={'h3'} weight={'medium'}>
                          Sauvetage dans le cadre d'un phénomène migratoire
                        </Text>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>

                  <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
                    <FormikNumberInput
                      disabled={!initValue?.isMigrationRescue}
                      isRequired={!!initValue?.isMigrationRescue}
                      name="nbOfVesselsTrackedWithoutIntervention"
                      label="Nb d'embarcations suivies sans nécessité d'intervention"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '50%', maxWidth: '50%' }}>
                    <FormikNumberInput
                      disabled={!initValue?.isMigrationRescue}
                      isRequired={!!initValue?.isMigrationRescue}
                      name="nbAssistedVesselsReturningToShore"
                      label="Nb d'embarcations assistées pour un retour à terre"
                    />
                  </Stack.Item>
                </Stack>
              )}
              <Stack.Item style={{ width: '100%' }}>
                <MissionActionDivingOperation />
              </Stack.Item>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemRescue
