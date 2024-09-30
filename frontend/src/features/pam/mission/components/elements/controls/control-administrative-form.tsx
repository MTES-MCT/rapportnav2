import { useControl } from '@features/pam/mission/hooks/control/use-control'
import { FormikEffect, FormikMultiRadio, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isEmpty, isNil, isNull, omitBy, pick, isEqual } from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import styled from 'styled-components'
import { ControlAdministrative, ControlResult, ControlType } from '@common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox'
import ControlInfraction from '../infractions/infraction-for-control'
import { controlResultOptions } from './control-result'

const StackItemStyled = styled(Stack.Item)({
  width: '100%',
  paddingTop: 2,
  paddingBottom: 2
})

export type ControlAdministrativeFormInput = {
  observations?: string
  unitHasConfirmed?: boolean
  compliantOperatingPermit?: ControlResult
  upToDateNavigationPermit?: ControlResult
  compliantSecurityDocuments?: ControlResult
}

interface ControlAdministrativeFormProps {
  unitShouldConfirm?: boolean
  data?: ControlAdministrative
  shouldCompleteControl?: boolean
}

const ControlAdministrativeForm: FC<ControlAdministrativeFormProps> = ({
  data,
  unitShouldConfirm,
  shouldCompleteControl
}) => {
  const { missionId, actionId } = useParams()
  const controlOptions = controlResultOptions()
  const [control, setControl] = useState<ControlAdministrativeFormInput>()

  const { isRequired, controlChanged, toggleControl, controlIsChecked } = useControl(
    data,
    ControlType.ADMINISTRATIVE,
    shouldCompleteControl
  )

  const getControlInput = (data?: ControlAdministrative) => {
    let control = data
      ? omitBy(
          pick(
            data,
            'observations',
            'unitHasConfirmed',
            'compliantOperatingPermit',
            'upToDateNavigationPermit',
            'compliantSecurityDocuments'
          ),
          isNull
        )
      : ({} as ControlAdministrativeFormInput)

    // hack to circumvent toggle and FormikEffect behaviour
    if ((isEmpty(control) || isNil(control.unitHasConfirmed)) && unitShouldConfirm) {
      control.unitHasConfirmed = false
    }
    return control
  }

  const getControl = (value?: ControlAdministrativeFormInput) => {
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

  const handleControlChange = async (value: ControlAdministrativeFormInput): Promise<void> => {
    if (isEqual(value, control) || isNil(value) || isEmpty(value)) return
    debugger
    await controlChanged(actionId, getControl(value))
  }

  const handleToggleControl = async (isChecked: boolean) => toggleControl(isChecked, actionId, getControl(control))

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          controlType={ControlType.ADMINISTRATIVE}
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
                    <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
                      <Stack.Item>
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
                    options={controlOptions}
                    name="compliantOperatingPermit"
                    label="Permis de mise en exploitation (autorisation à pêcher) conforme"
                    data-testid="control-administrative-form-compliant"
                  />
                </StackItemStyled>
                <StackItemStyled>
                  <FormikMultiRadio
                    isInline
                    options={controlOptions}
                    name="upToDateNavigationPermit"
                    label="Permis de navigation à jour"
                  />
                </StackItemStyled>
                <StackItemStyled>
                  <FormikMultiRadio
                    isInline
                    options={controlOptions}
                    name="compliantSecurityDocuments"
                    label="Titres de sécurité conformes"
                  />
                </StackItemStyled>
                <Stack.Item style={{ width: '100%', paddingTop: 4 }}>
                  <FormikTextarea
                    style={{ width: '100%' }}
                    name="observations"
                    label="Observations (hors infraction) sur les pièces administratives"
                  />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <ControlInfraction
                    controlId={data?.id}
                    infractions={data?.infractions}
                    controlType={ControlType.ADMINISTRATIVE}
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

export default ControlAdministrativeForm
