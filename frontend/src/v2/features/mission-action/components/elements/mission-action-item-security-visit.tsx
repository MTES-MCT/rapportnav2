import { FormikMultiRadio, FormikNumberInput, Option } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { number, string } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import { VisitSecurityType } from '../../../common/types/visit-security-type'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'

const securityOptions: Option[] = [
  {
    value: VisitSecurityType.SCHOOL_BOAT,
    label: `Visite triennale de sécurité sur les bateaux écoles`
  },
  {
    value: VisitSecurityType.UNDER_12M_BOAT,
    label: `Visite de sécurité sur les navires de moins de 12 mètres`
  }
]

const MissionActionItemSecurityVisit: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const schema = {
    nbrSecurityVisit: number().required(),
    securityVisitType: string().required()
  }
  return (
    <MissionActionItemGenericDateObservation
      action={action}
      schema={schema}
      onChange={onChange}
      showDivingCheckBox={true}
      data-testid={'security-visit-form'}
    >
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1rem" alignItems="flex-start">
          <Stack.Item style={{ width: '100%' }}>
            <FormikMultiRadio label="" name="securityVisitType" options={securityOptions} isErrorMessageHidden={true} />
          </Stack.Item>
          <Stack.Item style={{ width: '50%' }}>
            <FormikNumberInput
              isLight={true}
              isRequired={true}
              name="nbrSecurityVisit"
              label="Nombre de visites réalisées"
              isErrorMessageHidden={true}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemSecurityVisit
