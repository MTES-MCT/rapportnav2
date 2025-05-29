import {
  Accent,
  Button,
  Checkbox,
  FormikDatePicker,
  FormikEffect,
  FormikMultiRadio,
  FormikNumberInput,
  FormikSelect,
  FormikTextarea,
  Icon,
  Label,
  MultiRadio,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, Formik } from 'formik'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikSearchVessel } from '../../../common/components/ui/formik-search-vessel'
import { FormikSelectCrossControl } from '../../../common/components/ui/formik-select-crossed-control'
import { usecontrolCheck } from '../../../common/hooks/use-control-check'
import { useCrossControl } from '../../../common/hooks/use-crossed-control'
import useAgentsQuery from '../../../common/services/use-agents'
import useCrossControlListQuery from '../../../common/services/use-crossed-control'
import useVesselListQuery from '../../../common/services/use-vessel-service'
import { Agent } from '../../../common/types/crew-type'
import { CrossControlConclusionType, CrossControlStatusType } from '../../../common/types/crossed-control-type'
import { MissionAction } from '../../../common/types/mission-action'
import MissionTargetCrossControl from '../../../mission-target/components/elements/mission-target-crossed-control'
import { useMissionActionCrossControl } from '../../hooks/use-mission-action-crossed-control'
import MissionActionCrossControlSummary from '../ui/mission-action-crossed-control-summary'

const MissionActionItemCrossControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { controlCheckRadioBooleanOptions } = usecontrolCheck()
  const {
    availableControlTypes,
    crossControlOriginOptions,
    crossControlStatusOptions,
    crossControlTargetOptions,
    crossControlConclusionOptions
  } = useCrossControl()

  const { data: agents } = useAgentsQuery()
  const { data: vessels } = useVesselListQuery()
  const { data: crossControls } = useCrossControlListQuery()
  const [crossControlStatus, setCrossControlStatus] = useState<CrossControlStatusType>()
  const { initValue, handleSubmit, validationSchema } = useMissionActionCrossControl(action, onChange)

  useEffect(() => {
    if (!initValue || !initValue.crossControlStatus) return
    setCrossControlStatus(initValue.crossControlStatus)
  }, [initValue])

  return (
    <form style={{ width: '100%' }}>
      {initValue && crossControlStatus && (
        <Formik
          enableReinitialize
          validateOnChange={false}
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          {({ values, isValid, validateForm, setErrors }) => (
            <Stack direction="column" alignItems="flex-start" style={{ width: '100%' }}>
              <FormikEffect
                onChange={nextValue =>
                  validateForm().then(async errors => {
                    await handleSubmit({ ...nextValue, crossControlStatus })
                    setErrors(errors)
                  })
                }
              />

              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    <MultiRadio
                      label={''}
                      name="status"
                      value={crossControlStatus}
                      options={crossControlStatusOptions}
                      disabled={!!values.crossControl?.id}
                      data-testid={`crossed-control-status`}
                      onChange={value => setCrossControlStatus(value)}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    {[CrossControlStatusType.CLOSED, CrossControlStatusType.FOLLOW_UP].includes(crossControlStatus) ? (
                      <MissionActionCrossControlSummary
                        data={crossControls?.find(c => c.id === values.crossControl?.id)}
                      >
                        <FormikSelectCrossControl
                          name="crossControl.id"
                          crossControls={crossControls ?? []}
                          label="Rechercher la cible d’un contrôle en cours*"
                        />
                      </MissionActionCrossControlSummary>
                    ) : (
                      <Stack direction="column" spacing="16px" alignItems="flex-start" style={{ width: '100%' }}>
                        <Stack.Item style={{ width: '40%' }}>
                          <FormikDatePicker
                            name="startDate"
                            isLight={true}
                            withTime={true}
                            isRequired={true}
                            isCompact={true}
                            label="Date et heure de début"
                          />
                        </Stack.Item>
                        <Stack.Item style={{ width: '50%' }}>
                          <Stack direction="column" spacing={'16px'} style={{ width: '100%' }}>
                            <Stack.Item style={{ width: '100%' }}>
                              <FormikSelect
                                name="crossControl.origin"
                                isLight={true}
                                isRequired={true}
                                label="Origine du contrôle"
                                isErrorMessageHidden={true}
                                options={crossControlOriginOptions}
                              />
                            </Stack.Item>
                            <Stack.Item style={{ width: '100%' }}>
                              <FormikSelect
                                name="crossControl.agentId"
                                isLight={true}
                                isRequired={true}
                                data-testid="agent"
                                label="Agent en charge du contrôle"
                                options={
                                  agents?.map((agent: Agent) => ({
                                    value: agent.id,
                                    label: `${agent.firstName} ${agent.lastName}`
                                  })) ?? []
                                }
                              />
                            </Stack.Item>
                            <Stack.Item style={{ width: '100%' }}>
                              <FormikSelect
                                name={'crossControl.type'}
                                isLight={true}
                                isRequired={true}
                                label="Type de cible"
                                isErrorMessageHidden={true}
                                options={crossControlTargetOptions}
                              />
                            </Stack.Item>
                          </Stack>
                        </Stack.Item>
                        {vessels && (
                          <Stack.Item style={{ width: '100%' }}>
                            <FormikSearchVessel
                              vessels={vessels}
                              name="crossControl.vesselId"
                              isLight={true}
                              label="Nom du navire contrôlée"
                            />
                          </Stack.Item>
                        )}
                      </Stack>
                    )}
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Divider style={{ backgroundColor: THEME.color.charcoal }} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '50%' }}>
                    <FormikNumberInput
                      isLight={true}
                      isRequired={true}
                      name="crossControl.nbrOfHours"
                      data-testid="crossed-control-nbr-hours"
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
                      role={'crossed-control-add-tide-button'}
                    >
                      Ajouter une marée contrôlée
                    </Button>
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
                      <Stack.Item>
                        <Label>Conclusions du contrôle</Label>
                      </Stack.Item>
                      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
                        <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
                          <Stack.Item>
                            <FormikMultiRadio
                              isInline={true}
                              name={'crossControl.conclusion'}
                              label={'Actions mise en oeuvre'}
                              data-testid={`crossed-control-conlusion`}
                              options={crossControlConclusionOptions}
                            />
                          </Stack.Item>
                          <Stack.Item style={{ width: '100%' }}>
                            <FieldArray name="targets">
                              {(fieldArray: FieldArrayRenderProps) => (
                                <MissionTargetCrossControl
                                  fieldArray={fieldArray}
                                  availableControlTypes={availableControlTypes}
                                  isDisabled={
                                    values.crossControl?.conclusion !== CrossControlConclusionType.WITH_REPORT
                                  }
                                />
                              )}
                            </FieldArray>
                          </Stack.Item>
                          <Stack.Item style={{ width: '100%' }}>
                            <FormikTextarea
                              isLight={false}
                              label="Observations"
                              name="observations"
                              data-testid="observations"
                            />
                          </Stack.Item>
                        </Stack>
                      </Stack.Item>
                      <Stack.Item style={{ width: '100%' }}>
                        <Checkbox disabled={true} name="withVALID" label={`Controle réalisé avec l'outil VALID`} />
                      </Stack.Item>
                    </Stack>
                  </Stack.Item>

                  <Stack.Item>
                    <FormikMultiRadio
                      isInline={true}
                      isRequired={true}
                      name={'crossControl.isSignedByInspector'}
                      label={`Rapport signé par l'inspecteur`}
                      data-testid={`actions-mise-en-croisé`}
                      options={controlCheckRadioBooleanOptions}
                    />
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" alignItems="flex-end" style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '50%', justifyContent: 'start' }}>
                        <FormikDatePicker
                          name="endDate"
                          isLight={true}
                          withTime={true}
                          isRequired={true}
                          isCompact={true}
                          label="Date et heure de fin"
                        />
                      </Stack.Item>
                      <Stack.Item style={{ width: '50%' }}>
                        <Stack direction={'row'} style={{ justifyContent: 'end' }}>
                          <Button
                            Icon={Icon.Check}
                            accent={Accent.PRIMARY}
                            disabled={!isValid || values.crossControl?.isReferentialClosed}
                            onClick={() =>
                              validateForm().then(errors =>
                                handleSubmit({ ...values, crossControlStatus: CrossControlStatusType.CLOSED }, errors)
                              )
                            }
                          >
                            Cloturer le contrôle
                          </Button>
                        </Stack>
                      </Stack.Item>
                    </Stack>
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

export default MissionActionItemCrossControl
