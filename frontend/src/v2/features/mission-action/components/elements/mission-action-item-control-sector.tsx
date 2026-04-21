import { FC } from 'react'
import { object, string } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import MissionActionItemSectorControlForm from '../ui/mission-action-control-sector-form.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'

const MissionActionItemSectorControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { allControlTypes } = useTarget()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const schema = {
    sectorEstablishmentType: isMissionFinished ? string().required() : string().nullable(),
    sectorType: isMissionFinished ? string().required() : string().nullable(),
    // TODO: that's the idea but some more work is required to highlight the fields in rec
    establishment: isMissionFinished
      ? object()
          .shape({
            name: string().required()
          })
          .required()
      : object().nullable()
  }

  return (
    <MissionActionItemGenericControl
      action={action}
      schema={schema}
      onChange={onChange}
      withGeoCoords={false}
      controlTypes={allControlTypes}
      data-testid={'action-control-sector'}
      component={MissionActionItemSectorControlForm}
    />
  )
}
export default MissionActionItemSectorControl
