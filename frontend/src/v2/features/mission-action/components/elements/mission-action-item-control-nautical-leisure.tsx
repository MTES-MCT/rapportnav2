import { FormikNumberInput, FormikSelect } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { LEISURE_TYPES, LeisureType } from '../../../common/types/leisure-fishing-gear-type'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericControl from './mission-action-item-generic-control'

const MissionActionItemNauticalLeisureControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={true}
      data-testid={'action-control-other'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
          <Stack.Item style={{ width: '70%' }}>
            <FormikSelect
              name="leisureType"
              label="Type de loisir"
              isLight={true}
              isRequired={true}
              options={Object.keys(LeisureType)?.map(key => ({
                value: key,
                label: LEISURE_TYPES[key as keyof typeof LeisureType]
              }))}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing={'.5rem'}>
              <Stack.Item style={{ width: '100%' }}>
                <FormikNumberInput
                  isLight={true}
                  isRequired={true}
                  name="nbrOfControl"
                  label="Nombre total de contrôles"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikNumberInput
                  isLight={true}
                  isRequired={true}
                  name="nbrOfControl300m"
                  label="Nb dans la bande des 300m"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>
              <Stack.Item style={{ width: '70%' }}>
                <FormikNumberInput
                  isLight={true}
                  isRequired={true}
                  name="nbrOfControlAmp"
                  label="Nb en AMP"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <StyledFormikToggle
              name="isControlDuringSecurityDay"
              label="Contrôle réalisés dans le cadre d'une journée sécurité mer"
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </MissionActionItemGenericControl>
  )
}
export default MissionActionItemNauticalLeisureControl
