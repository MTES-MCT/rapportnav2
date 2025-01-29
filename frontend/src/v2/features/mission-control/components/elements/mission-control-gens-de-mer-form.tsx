import { ControlGensDeMer, ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Panel, Stack } from 'rsuite'
import MissionInfractionList from '../../../mission-infraction/components/elements/mission-infraction-list-form'
import { ControlGensDeMerInput, useControlGensDeMer } from '../../hooks/use-control-gens-de-mer'
import MissionActionControlFormikMultiRadio from '../ui/mission-control-fomik-multi-radio'
import { MissionControlTitle } from '../ui/mission-control-title'
import MissionControlUnitConfirm from '../ui/mission-control-unit-confirm'

type MissionControlGensDeMerFormProps = {
  name: string
  isToComplete?: boolean
  fieldFormik: FieldProps<ControlGensDeMer>
}

const MissionControlGensDeMerForm: FC<MissionControlGensDeMerFormProps> = ({ name, isToComplete, fieldFormik }) => {
  const { radios, controlTypeLabel, initValue, handleSubmit } = useControlGensDeMer(name, fieldFormik)

  return (
    <Panel
      bordered
      collapsible
      header={<MissionControlTitle text={controlTypeLabel} isToComplete={isToComplete} />}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize>
          {formik => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ControlGensDeMerInput)} />

              <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlUnitConfirm
                    value={formik.values.hasBeenDone}
                    onSubmit={value => formik.setFieldValue('hasBeenDone', value)}
                  />
                </Stack.Item>
                <Stack.Item
                  style={{
                    width: '100%',
                    ...(!formik.values.hasBeenDone ? { pointerEvents: 'none', opacity: 0.4 } : {})
                  }}
                >
                  <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '100%' }}>
                      <MissionActionControlFormikMultiRadio radios={radios ?? []} control={ControlType.GENS_DE_MER} />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%', paddingTop: 4 }}>
                      <FormikTextarea
                        style={{ width: '100%' }}
                        name={`observations`}
                        label="Observations (hors infraction) sur les pièces administratives"
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <FieldArray name="infractions">
                        {(fieldArray: FieldArrayRenderProps) => (
                          <MissionInfractionList
                            name="infractions"
                            fieldArray={fieldArray}
                            controlType={ControlType.GENS_DE_MER}
                          />
                        )}
                      </FieldArray>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </Panel>
  )
}

export default MissionControlGensDeMerForm
