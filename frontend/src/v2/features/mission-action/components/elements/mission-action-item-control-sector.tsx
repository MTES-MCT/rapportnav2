import { FC } from 'react'
import { string } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemSectorControlForm from '../ui/mission-action-control-sector-form.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'

const MissionActionItemSectorControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const schema = {
    resourceId: string().required(),
    resourceType: string().required()
  }
  return (
    <MissionActionItemGenericControl
      action={action}
      schema={schema}
      onChange={onChange}
      withGeoCoords={false}
      data-testid={'action-control-sector'}
      component={MissionActionItemSectorControlForm}
    />
  )
}
export default MissionActionItemSectorControl
