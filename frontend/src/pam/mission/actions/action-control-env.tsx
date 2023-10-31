import React, { useState } from 'react'
import {
  Accent,
  Button,
  Checkbox,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  Icon,
  Label,
  NumberInput,
  Size,
  THEME,
  TextInput
} from '@mtes-mct/monitor-ui'
import Divider from 'rsuite/Divider'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { FishAction } from '../../fish-mission-types'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import EnvControlForm from '../controls/env-control-form'
import { Action, ControlType } from '../../mission-types'
import { EnvActionControl } from '../../env-mission-types'
import EnvInfractionNewTargetForm from '../infractions/env-infraction-new-target-form'
import EnvInfractionSummary from '../infractions/env-infraction-summary'

interface ActionControlPropsEnv {
  action: Action
}

const ActionControlEnv: React.FC<ActionControlPropsEnv> = ({ action }) => {
  const [showInfractionForNewTarget, setShowInfractionForNewTarget] = useState<boolean>(false)
  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing={'0.5rem'} style={{ width: '100%' }}>
          <Stack.Item alignSelf="baseline">
            <Icon.Control color={THEME.color.charcoal} size={20} />
          </Stack.Item>
          <Stack.Item grow={2}>
            <Title as="h2">
              Contrôles {action.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(action.startDateTimeUtc)})`}
            </Title>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Thématique de contrôle</Label>
        <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
          {(action.data as any as EnvActionControl)?.themes[0].theme ?? 'inconnue'}
        </Title>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Sous-thématiques de contrôle</Label>
        <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
          {(action.data as any as EnvActionControl)?.themes[0].subThemes?.length
            ? (action.data as any as EnvActionControl)?.themes[0].subThemes?.join(', ')
            : 'inconnues'}
        </Title>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <DateRangePicker
          defaultValue={[action.startDateTimeUtc || new Date(), action.endDateTimeUtc || new Date()]}
          label="Date et heure de début et de fin"
          withTime={true}
          isCompact={true}
          isLight={true}
          disabled={true}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <CoordinatesInput
          defaultValue={[
            (action.data as unknown as FishAction).latitude as any,
            (action.data as unknown as FishAction).longitude as any
          ]}
          coordinatesFormat={CoordinatesFormat.DECIMAL_DEGREES}
          label="Lieu du contrôle"
          isLight={true}
          disabled={true}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Divider style={{ backgroundColor: THEME.color.charcoal }} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
              <Stack.Item style={{ width: '33%' }}>
                <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
                  <Stack.Item>
                    <Label>Nbre total de contrôles</Label>
                    <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {action.data?.actionNumberOfControls ?? 'inconnu'}
                    </Title>
                  </Stack.Item>
                  <Stack.Item>
                    <Label>Type de cible</Label>
                    <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {action.data?.actionTargetType ?? 'inconnu'}
                    </Title>
                  </Stack.Item>
                  <Stack.Item>
                    <Label>Type de véhicule</Label>
                    <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {action.data?.vehicleType ?? 'inconnu'}
                    </Title>
                  </Stack.Item>
                  <Stack.Item>
                    <Label>Observations</Label>
                    <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                      {action.data?.observations ?? 'aucunes'}
                    </Title>
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item style={{ width: '67%' }}>
                <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
                  {((action.data as any as EnvActionControl)?.controlsToComplete?.length || 0) > 0 && (
                    <Stack.Item alignSelf="flex-end">
                      <ControlsToCompleteTag
                        amountOfControlsToComplete={
                          (action.data as any as EnvActionControl)?.controlsToComplete?.length
                        }
                        isLight={true}
                      />
                    </Stack.Item>
                  )}

                  <Stack.Item>
                    <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
                      dont...
                    </Title>
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <EnvControlForm
                      controlType={ControlType.ADMINISTRATIVE}
                      data={(action.data as any as EnvActionControl)?.controlAdministrative}
                      maxAmountOfControls={(action.data as any as EnvActionControl)?.actionNumberOfControls}
                      shouldCompleteControl={
                        !!(action.data as any as EnvActionControl)?.controlsToComplete?.includes(
                          ControlType.ADMINISTRATIVE
                        )
                      }
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      controlType={ControlType.NAVIGATION}
                      data={(action.data as any as EnvActionControl)?.controlNavigation}
                      maxAmountOfControls={(action.data as any as EnvActionControl)?.actionNumberOfControls}
                      shouldCompleteControl={
                        !!(action.data as any as EnvActionControl)?.controlsToComplete?.includes(ControlType.NAVIGATION)
                      }
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      controlType={ControlType.GENS_DE_MER}
                      data={(action.data as any as EnvActionControl)?.controlGensDeMer}
                      maxAmountOfControls={(action.data as any as EnvActionControl)?.actionNumberOfControls}
                      shouldCompleteControl={
                        !!(action.data as any as EnvActionControl)?.controlsToComplete?.includes(
                          ControlType.GENS_DE_MER
                        )
                      }
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      controlType={ControlType.SECURITY}
                      data={(action.data as any as EnvActionControl)?.controlSecurity}
                      maxAmountOfControls={(action.data as any as EnvActionControl)?.actionNumberOfControls}
                      shouldCompleteControl={
                        !!(action.data as any as EnvActionControl)?.controlsToComplete?.includes(ControlType.SECURITY)
                      }
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            {showInfractionForNewTarget ? (
              <div style={{ width: '100%', backgroundColor: THEME.color.white, padding: '1rem' }}>
                <EnvInfractionNewTargetForm />
              </div>
            ) : (
              <Stack justifyContent="flex-end">
                <Stack.Item>
                  {' '}
                  <Button
                    onClick={() => setShowInfractionForNewTarget(true)}
                    accent={Accent.SECONDARY}
                    size={Size.NORMAL}
                    Icon={Icon.Plus}
                  >
                    Ajouter une nouvelle cible avec infraction
                  </Button>
                </Stack.Item>
              </Stack>
            )}
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            {true &&
              [{}].map((whatever: any) => (
                <div style={{ width: '100%', backgroundColor: THEME.color.white, padding: '1rem' }}>
                  <EnvInfractionSummary />
                </div>
              ))}
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default ActionControlEnv
