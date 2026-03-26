import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input'
import { MissionAction } from '../../../common/types/mission-action'
import MissionActionItemGenericControl from './mission-action-item-generic-control'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'

const MissionActionItemOtherControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { allControlTypes } = useTarget()
  return (
    <MissionActionItemGenericControl
      action={action}
      onChange={onChange}
      withGeoCoords={true}
      data-testid={'action-control-other'}
      controlTypes={allControlTypes}
      component={() => (
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput name="controlType" label="Nature du contrôle / Type de contrôle" />
        </Stack.Item>
      )}
    />
  )
}
export default MissionActionItemOtherControl
