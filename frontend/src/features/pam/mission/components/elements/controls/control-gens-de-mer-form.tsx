import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikMultiRadio, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import _ from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import styled from 'styled-components'
import { ControlGensDeMer, ControlResult, ControlType } from '../../../../../common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'
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

  const getControlInput = (data?: ControlGensDeMer) =>
    data
      ? _.omitBy(
          _.pick(
            data,
            'observations',
            'unitHasConfirmed',
            'staffOutnumbered',
            'upToDateMedicalCheck',
            'knowledgeOfFrenchLawAndLanguage'
          ),
          _.isNull
        )
      : ({} as ControlGensDeMerFormInput)

  const getControl = (value?: ControlGensDeMerFormInput) => {
    if (!value) return
    //TODO:  unitConfrimed by default? if (!_.isBoolean(value.unitHasConfirmed)) value.unitHasConfirmed = true
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
    if (value === control) return
    controlChanged(actionId, getControl(value))
  }

  const handleToogleControl = async (isChecked: boolean) => {
    toggleControl(isChecked, actionId, getControl(control))
  }

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          controlType={ControlType.GENS_DE_MER}
          onChange={(isChecked: boolean) => handleToogleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {control !== undefined && (
          <Formik
            initialValues={control}
            onSubmit={handleControlChange}
            validateOnChange={true}
            enableReinitialize={true}
          >
            <>
              <FormikEffect onChange={handleControlChange} />
              <Form>
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
              </Form>
            </>
          </Formik>
        )}
      </Stack>
    </Panel>
  )
}

export default ControlGensDeMerForm
