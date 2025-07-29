import {
  Accent,
  Button,
  FormikEffect,
  FormikNumberInput,
  FormikTextarea,
  Icon,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, Formik } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import { useInquiry } from '../../../inquiry/hooks/use-inquiry'
import MissionTargetInquiry from '../../../mission-target/components/elements/mission-target-inquiry'
import { useMissionActionInquiry } from '../../hooks/use-mission-action-inquiry'

const MissionActionItemInquiry: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { availableControlTypes } = useInquiry()
  const { initValue, handleSubmit, validationSchema } = useMissionActionInquiry(action, onChange)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          enableReinitialize
          validateOnChange={false}
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          {({ validateForm, setErrors }) => (
            <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
              <FormikEffect
                onChange={nextValue =>
                  validateForm().then(async errors => {
                    await handleSubmit(nextValue)
                    setErrors(errors)
                  })
                }
              />
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="1rem" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '50%' }}>
                    <FormikNumberInput
                      isLight={true}
                      isRequired={true}
                      name="nbrOfHours"
                      data-testid="inquiry-nbr-hours"
                      label="Temps passé sur le controle (heure)"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Button
                      Icon={Icon.Plus}
                      size={Size.NORMAL}
                      disabled={true}
                      accent={Accent.SECONDARY}
                      style={{ width: 'inherit' }}
                      role={'inquiry-add-tide-button'}
                    >
                      Ajouter une marée contrôlée
                    </Button>
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FieldArray name="targets">
                      {(fieldArray: FieldArrayRenderProps) => (
                        <MissionTargetInquiry fieldArray={fieldArray} availableControlTypes={availableControlTypes} />
                      )}
                    </FieldArray>
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikTextarea
                      isLight={true}
                      label="Observations"
                      name="observations"
                      data-testid="observations"
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemInquiry
