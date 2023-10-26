import { Form, Panel, Stack, Toggle } from 'rsuite'
import { ControlAdministrative, ControlGensDeMer, ControlNavigation, ControlSecurity } from '../../mission-types'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  Textarea,
  MultiRadio,
  OptionValue,
  Checkbox,
  Label,
  NumberInput,
  TextInput
} from '@mtes-mct/monitor-ui'
import { useMutation } from '@apollo/client'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../queries'
import omit from 'lodash/omit'
import { controlResultOptions } from './control-result'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import Title from '../../../ui/title'

interface EnvControlForm {
  data?: ControlAdministrative | ControlSecurity | ControlNavigation | ControlGensDeMer
  maxAmountOfControls?: number
}

const EnvControlForm: React.FC<EnvControlForm> = ({ data, maxAmountOfControls }) => {
  const { missionId, actionId } = useParams()

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    {
      refetchQueries: [GET_MISSION_BY_ID]
    }
  )

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      deletedAt: undefined,
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: data?.amountOfControls || 1
    }

    if (!!field && !!value) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }

    await mutate({ variables: { control: updatedData } })
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="center">
          <Stack.Item alignSelf="baseline">
            <Checkbox
              error=""
              label=""
              name="control"
              // checked={action.data.}
            />
          </Stack.Item>
          <Stack.Item>
            <Title as="h3">Contrôle administratif navire</Title>
          </Stack.Item>
          {false && <Stack.Item>.</Stack.Item>}
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" style={{ width: '100%' }} spacing={'0.5rem'}>
          <Stack.Item style={{ width: '33%' }}>
            <NumberInput
              label="Nb contrôles"
              name="amountOfControls"
              value={data?.amountOfControls}
              max={maxAmountOfControls}
              isLight={true}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '67%' }}>
            <TextInput
              label="Observations (hors infraction)"
              name="observations"
              value={data?.observations}
              isLight={true}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default EnvControlForm
