import { FC } from 'react'
import { ActionFishControlInput } from '../../types/action-type.ts'

const FishControlInfos: FC<{
  values: ActionFishControlInput
}> = ({ values }) => {
  console.log(values)
  return <div style={{ width: '100%' }}>Infos</div>
}

export default FishControlInfos
