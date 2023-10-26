import React from 'react'
import {
  Checkbox,
  CoordinatesFormat,
  CoordinatesInput,
  DateRangePicker,
  Icon,
  Label,
  NumberInput,
  THEME,
  TextInput
} from '@mtes-mct/monitor-ui'
import { ActionControlEnv as ActionControlTypeEnv, EnvActionControl } from '../../env-mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { FishAction } from '../../fish-mission-types'
import ControlsToCompleteTag from '../controls/controls-to-complete-tag'
import EnvControlForm from '../controls/env-control-form'

interface ActionControlPropsEnv {
  action: any
}

const ActionControlEnv: React.FC<ActionControlPropsEnv> = ({ action }) => {
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
          {action.data?.themes[0].theme ?? 'inconnue'}
        </Title>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Label>Sous-thématiques de contrôle</Label>
        <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
          {action.data?.themes[0].subThemes.length ? action.data?.themes[0].subThemes?.join(', ') : 'inconnues'}
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
        <div>-----------------------------------------</div>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" style={{ width: '100%' }}>
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
                <Stack direction="column" alignItems="flex-start" spacing={'1rem'} style={{ width: '100%' }}>
                  {(action as EnvActionControl).amountOfControlsToComplete !== undefined &&
                    (action as EnvActionControl).amountOfControlsToComplete! > 0 && (
                      <Stack.Item alignSelf="flex-end">
                        <ControlsToCompleteTag
                          amountOfControlsToComplete={action.data?.amountOfControlsToComplete}
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
                      data={action.data?.controlAdministrative}
                      maxAmountOfControls={action.data?.actionNumberOfControls}
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      data={action.data?.controlNavigation}
                      maxAmountOfControls={action.data?.actionNumberOfControls}
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      data={action.data?.controlGensDeMer}
                      maxAmountOfControls={action.data?.actionNumberOfControls}
                    />
                  </Stack.Item>
                  <Stack.Item>
                    <EnvControlForm
                      data={action.data?.controlSecurity}
                      maxAmountOfControls={action.data?.actionNumberOfControls}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item>INFRATIONS</Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default ActionControlEnv
