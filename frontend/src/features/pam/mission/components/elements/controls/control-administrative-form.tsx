import { FormikEffect, FormikMultiRadio, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import _ from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useRef, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import styled from 'styled-components'
import { ControlAdministrative, ControlResult, ControlType } from '../../../../../common/types/control-types'
import useAddOrUpdateControl from '../../../../../pam/mission/hooks/use-add-update-control'
import useDeleteControl from '../../../../../pam/mission/hooks/use-delete-control'
import ControlTitleCheckbox from '../../ui/control-title-checkbox'
import ControlInfraction from '../infractions/infraction-for-control'
import { controlResultOptions } from './control-result'

const DEBOUNCE_TIME_TRIGGER = 5000

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
  shouldCompleteControl,
  unitShouldConfirm
}) => {
  const { missionId, actionId } = useParams()
  const controlOptions = controlResultOptions()
  const [isRequired, setIsRequired] = useState<boolean>()
  const timerRef = useRef<ReturnType<typeof setTimeout>>()
  const [checkedControl, setCheckedControl] = useState<boolean>()
  const [control, setControl] = useState<ControlAdministrativeFormInput>()

  const [deleteControl] = useDeleteControl({ controlType: ControlType.ADMINISTRATIVE })
  const [mutateControl] = useAddOrUpdateControl({ controlType: ControlType.ADMINISTRATIVE })

  const getControlInput = (data?: ControlAdministrative) =>
    data
      ? _.omitBy(
          _.pick(
            data,
            'observations',
            'unitHasConfirmed',
            'compliantOperatingPermit',
            'upToDateNavigationPermit',
            'compliantSecurityDocuments'
          ),
          _.isNull
        )
      : ({} as ControlAdministrativeFormInput)

  useEffect(() => {
    setControl(getControlInput(data))
    setIsRequired(!!shouldCompleteControl && !!!data)
    setCheckedControl(!!data || shouldCompleteControl)
  }, [data, shouldCompleteControl])

  const handleChange = async (value: ControlAdministrativeFormInput): Promise<void> => {
    clearTimeout(timerRef.current)
    if (value === control) return
    timerRef.current = setTimeout(() => handleUpdate(value), DEBOUNCE_TIME_TRIGGER)
  }

  const handleUpdate = async (value?: ControlAdministrativeFormInput) => {
    if (!value) return
    if (!_.isBoolean(value.unitHasConfirmed)) value.unitHasConfirmed = true
    let variables = {
      control: {
        ...omit(data, 'infractions'),
        missionId,
        unitShouldConfirm,
        amountOfControls: 1,
        actionControlId: actionId,
        ...value
      }
    }
    await mutateControl({ variables })
  }

  const toggleControl = async (isChecked: boolean) => {
    setCheckedControl(isChecked)
    const variables = { actionId }
    isChecked ? await handleUpdate(control) : await deleteControl({ variables })
  }

  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={checkedControl}
          shouldCompleteControl={isRequired}
          controlType={ControlType.ADMINISTRATIVE}
          onChange={(isChecked: boolean) => toggleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {control !== undefined && (
          <Formik initialValues={control} onSubmit={handleChange} validateOnChange={true} enableReinitialize={true}>
            <>
              <FormikEffect onChange={handleChange} />
              <Form>
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
                    name="observations"
                    label="Observations (hors infraction) sur les pièces administratives"
                  />
                </Stack.Item>
              </Form>
            </>
          </Formik>
        )}

        <Stack.Item style={{ width: '100%' }}>
          <ControlInfraction
            controlId={data?.id}
            infractions={data?.infractions}
            controlType={ControlType.ADMINISTRATIVE}
          />
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlAdministrativeForm
