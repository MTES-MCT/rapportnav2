import { FormikCheckbox, FormikEffect, FormikTextarea, Icon, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import { useMissionActionAntiPollution } from '../../hooks/use-mission-action-anti-pollution'
import { ActionAntiPollutionInput } from '../../types/action-type'
import MissionActionAntiPollutionWarning from '../ui/mission-action-anti-pollution-warning'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

const MissionActionItemAntiPollution: FC<{
  action: MissionActionOutput
  onChange: (newAction: MissionActionOutput) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit } = useMissionActionAntiPollution(action, onChange)
  return (
    <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionAntiPollutionInput)} />
            <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item alignSelf="baseline">
                    <Icon.Info color={THEME.color.charcoal} size={20} />
                  </Stack.Item>
                  <Stack.Item>
                    <MissionActionAntiPollutionWarning />
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item grow={1}>
                    <MissionActionFormikDateRangePicker name="dates" />
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <MissionActionFormikCoordinateInputDMD name={'geoCoords'} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction={'column'} spacing={'0.5rem'}>
                  <Stack.Item style={{ width: '100%', display: 'flex', flexDirection: 'row', alignItems: 'end' }}>
                    <StyledFormikToggle
                      name="isSimpleBrewingOperationDone"
                      label="Opération de brassage simple effectuée"
                      data-testid="action-antipol-simple-brewing-operation"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <StyledFormikToggle
                      name="isAntiPolDeviceDeployed"
                      data-testid="action-antipol-device-deployed"
                      label="Mise en place d'un dispositif ANTIPOL (dispersant, barrage, etc...)"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox readOnly={false} isLight name="detectedPollution" label="Pollution détectée" />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox
                      readOnly={false}
                      isLight
                      name="pollutionObservedByAuthorizedAgent"
                      label="Pollution constatée par un agent habilité"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox readOnly={false} isLight name="diversionCarriedOut" label="Déroutement effectué" />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemAntiPollution
