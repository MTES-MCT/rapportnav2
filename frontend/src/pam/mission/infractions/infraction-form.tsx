import { FC } from "react";
import { Stack, Toggle } from 'rsuite'
import { Accent, Button, Size, Textarea, THEME } from '@mtes-mct/monitor-ui'
import Text from '../../../ui/text'
import { InfractionTypeEnum } from '../../../types/env-mission-types'
import NatinfsMultiSelect from "./natinfs-multi-select.tsx";
import { ControlType } from "../../../types/control-types.ts";

export interface InfractionFormData {
  controlType?: ControlType
  infractionType?: InfractionTypeEnum
  natinfs?: string[]
  observations?: string
}

export interface InfractionFormProps {
  infraction?: InfractionFormData
  onChange: (field: string, value: any) => void
  onCancel: () => void
}

const InfractionForm: FC<InfractionFormProps> = ({infraction, onChange, onCancel}) => {
  return (
    <>
      <input type="hidden" value={infraction?.id} name="id"/>
      <Stack direction="column" spacing={'2rem'} style={{width: '100%'}}>
        <Stack.Item style={{width: '100%'}}>
          <Stack direction="row" alignItems="baseline" spacing={'0.5rem'}>
            <Stack.Item>
              <Toggle
                checked={infraction?.infractionType === InfractionTypeEnum.WITH_REPORT}
                defaultValue={InfractionTypeEnum.WITHOUT_REPORT}
                role="toggle-infraction-type"
                size="sm"
                onChange={(checked: boolean) =>
                  onChange('infractionType', checked ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT)
                }
              />
            </Stack.Item>
            <Stack.Item>
              <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                PV émis
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <NatinfsMultiSelect onChange={onChange}
                              selectedNatinfs={infraction?.natinfs}/>
        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <Textarea
            label="Observations"
            value={infraction?.observations}
            name="observations"
            role="observations"
            onChange={(nextValue?: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{width: '100%'}}>
          <Stack justifyContent="flex-end" spacing={'1rem'} style={{width: '100%'}}>
            <Stack.Item>
              <Button accent={Accent.TERTIARY} type="submit" size={Size.NORMAL} onClick={onCancel}>
                Annuler
              </Button>
            </Stack.Item>
            <Stack.Item>
              <Button accent={Accent.PRIMARY} type="submit" size={Size.NORMAL} role="validate-infraction"
                      disabled={infraction?.controlType === undefined || !infraction?.natinfs?.length}
              >
                Valider l'infraction
              </Button>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default InfractionForm
