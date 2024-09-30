import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikMultiRadio, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isEmpty, isEqual, isNil, isNull, omit, omitBy, pick } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import styled from 'styled-components'
import { ControlGensDeMer, ControlResult, ControlType } from '@common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'
import ControlInfraction from '../infractions/infraction-for-control.tsx'
import { ControlResultExtraOptions, controlResultOptions } from './control-result.ts'

const StackItemStyled = styled(Stack.Item)({
  width: '100%',
  paddingTop: 2,
  paddingBottom: 2
})

export type ControlGensDeMerFormInput = {
  observations?: string
  unitHasConfirmed?: boolean
  staffOutnumbered?: ControlResult
  upToDateMedicalCheck?: ControlResult
  knowledgeOfFrenchLawAndLanguage?: ControlResult
}

interface ControlGensDeMerFormProps {
  data?: ControlGensDeMer
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlGensDeMerForm: FC<ControlGensDeMerFormProps> = ({ data, shouldCompleteControl, unitShouldConfirm }) => {
  const { missionId, actionId } = useParams()
  const controlOptions = controlResultOptions()
  const [control, setControl] = useState<ControlGensDeMerFormInput>()

  const { isRequired, controlChanged, toggleControl, controlIsChecked } = useControl(
    data,
    ControlType.GENS_DE_MER,
    shouldCompleteControl
  )

  const getControlInput = (data?: ControlGensDeMer) => {
    let control = data
      ? omitBy(
          pick(
            data,
            'observations',
            'unitHasConfirmed',
            'staffOutnumbered',
            'upToDateMedicalCheck',
            'knowledgeOfFrenchLawAndLanguage'
          ),
          isNull
        )
      : ({} as ControlGensDeMerFormInput)

    // hack to circumvent toggle and FormikEffect behaviour
    if ((isEmpty(control) || isNil(control.unitHasConfirmed)) && unitShouldConfirm) {
      control.unitHasConfirmed = false
    }
    return control
  }

  const getControl = (value?: ControlGensDeMerFormInput) => {
    if (!value) return
    return {
      ...omit(data, 'infractions'),
      missionId,
      unitShouldConfirm,
      amountOfControls: 1,
      actionControlId: actionId,
      ...value
    }
  }

  useEffect(() => {
    setControl(getControlInput(data))
  }, [data])

  const handleControlChange = async (value: ControlGensDeMerFormInput): Promise<void> => {
    if (isEqual(value, control) || isNil(value) || isEmpty(value)) return
    await controlChanged(actionId, getControl(value))
  }

  const handleToggleControl = async (isChecked: boolean) => toggleControl(isChecked, actionId, getControl(control))

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          controlType={ControlType.GENS_DE_MER}
          onChange={(isChecked: boolean) => handleToggleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {control !== undefined && (
        <Formik initialValues={control} validateOnChange={true} enableReinitialize={true}>
          <>
            <FormikEffect onChange={handleControlChange} />
            <Form>
              <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                {unitShouldConfirm && (
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                      <Stack.Item>
                        {/* TODO add Toggle component to monitor-ui */}
                        <FormikToggle label="" size="sm" name="unitHasConfirmed" />
                      </Stack.Item>
                      <Stack.Item alignSelf="flex-end">
                        <Label style={{ marginBottom: 0 }}>
                          <b>Contrôle confirmé par l’unité</b>
                        </Label>
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>
                )}
                <StackItemStyled>
                  <FormikMultiRadio
                    isInline
                    name="staffOutnumbered"
                    options={controlOptions}
                    label="Décision d’effectif conforme au nombre de personnes à bord"
                  />
                </StackItemStyled>
                <StackItemStyled>
                  <FormikMultiRadio
                    isInline
                    options={controlOptions}
                    name="upToDateMedicalCheck"
                    label="Aptitudes médicales ; Visites médicales à jour"
                  />
                </StackItemStyled>
                <StackItemStyled>
                  <FormikMultiRadio
                    isInline
                    name="knowledgeOfFrenchLawAndLanguage"
                    options={controlResultOptions([ControlResultExtraOptions.NOT_CONCERNED])}
                    label="Connaissance suffisante de la langue et de la loi français (navires fr/esp)"
                  />
                </StackItemStyled>
                <Stack.Item style={{ width: '100%', paddingTop: 4 }}>
                  <FormikTextarea
                    name="observations"
                    label="Observations (hors infraction) sur les pièces administratives"
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <ControlInfraction
                    controlId={data?.id}
                    infractions={data?.infractions}
                    controlType={ControlType.GENS_DE_MER}
                  />
                </Stack.Item>
              </Stack>
            </Form>
          </>
        </Formik>
      )}
    </Panel>
  )
}

export default ControlGensDeMerForm
