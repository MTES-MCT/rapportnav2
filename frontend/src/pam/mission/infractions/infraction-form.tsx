import React from 'react'
import styled from 'styled-components'
import { FlexboxGrid, Stack, Toggle } from 'rsuite'
import { Accent, Icon, Button, Size, THEME, Label, MultiSelect, Textarea } from '@mtes-mct/monitor-ui'
import Title from '../../../ui/title'

interface InfractionFormProps {
  data?: any
  availableNatinfs?: string[]
  onSubmit?: (data: any) => void
  onCancel?: () => void
}

const InfractionForm: React.FC<InfractionFormProps> = ({ data, availableNatinfs, onSubmit, onCancel }) => {
  const saveAndQuitMission = () => {
    // TODO add save
  }
  const finishMission = () => {}

  const onChange = (field: string, value: any) => {}

  return (
    <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
          <Stack.Item>
            {/* TODO add Toggle component to monitor-ui */}
            <Toggle checked={false} size="sm" onChange={(checked: boolean) => onChange('unitHasConfirmed', checked)} />
          </Stack.Item>
          <Stack.Item>
            <Title as="h3" weight="bold" color={THEME.color.gunMetal}>
              PV émis
            </Title>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiSelect
          error=""
          label="NATINF"
          name="natinfs"
          onChange={function noRefCheck() {}}
          options={[
            {
              label: 'First Option',
              value: 'FIRST_OPTION'
            },
            {
              label: 'Second Option',
              value: 'SECOND_OPTION'
            },
            {
              label: 'Third Option',
              value: 'THIRD_OPTION'
            },
            {
              label:
                'Lorem ipsum dolor sit amet, consectetur adipiscing elit.Vivamus sit amet purus justo.Sed dapibus, turpis non laoreet consectetur, sapien elit varius lacus, dignissim tincidunt dui metus eget metus.Etiam commodo, augue at condimentum semper, dui sapien auctor urna, sit amet laoreet felis justo gravida purus.Maecenas sagittis mollis erat eu pulvinar.Donec pellentesque commodo mauris, ac lobortis justo vestibulum et.Donec mattis maximus elit, id euismod leo faucibus hendrerit.Sed aliquet, purus sed pulvinar cursus, velit dolor pretium est, eu mattis sem libero a nibh.Curabitur diam urna, lacinia eu nulla at, mattis faucibus ligula.Proin placerat accumsan placerat.Etiam eget erat nisi.',
              value: 'LOREM_IPSUM'
            }
          ]}
          placeholder=""
          searchable
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations générales sur le contrôle"
          value={data?.observations}
          name="observations"
          onChange={(nextValue?: string) => onChange('observations', nextValue)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
          <Stack.Item>
            <Button accent={Accent.TERTIARY} type="submit" size={Size.NORMAL}>
              Annuler
            </Button>
          </Stack.Item>
          <Stack.Item>
            <Button accent={Accent.PRIMARY} type="submit" size={Size.NORMAL}>
              Valider l'infraction
            </Button>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default InfractionForm
