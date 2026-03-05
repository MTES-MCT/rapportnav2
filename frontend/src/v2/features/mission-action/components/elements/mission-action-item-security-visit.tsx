import { FormikMultiRadio, Option } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { number, string } from 'yup'
import { FormikNumberInput } from '../../../common/components/ui/formik-number-input'
import { MissionAction } from '../../../common/types/mission-action'
import { VisitSecurityType } from '../../../common/types/visit-security-type'
import MissionActionItemGenericDateObservation from './mission-action-item-generic-date-observation'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'

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
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const schema = {
    nbrSecurityVisit: isMissionFinished ? number().required() : number().nullable(),
    securityVisitType: isMissionFinished ? string().required() : string().nullable()
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
            <FormikNumberInput name="nbrSecurityVisit" label="Nombre de visites réalisées" />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </MissionActionItemGenericDateObservation>
  )
}
export default MissionActionItemSecurityVisit
