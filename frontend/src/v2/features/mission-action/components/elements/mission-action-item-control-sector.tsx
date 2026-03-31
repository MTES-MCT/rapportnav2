import { FC } from 'react'
import { mixed, object, string } from 'yup'
import { SectorFishingType, SectorType } from '../../../common/types/sector-types'
import { MissionAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import MissionActionItemSectorControlForm from '../ui/mission-action-control-sector-form.tsx'
import MissionActionItemGenericControl from './mission-action-item-generic-control.tsx'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'

const isControlLocationMode = (sectorType?: SectorType, sectorEstablishmentType?: string) =>
  sectorType === SectorType.FISHING &&
  (sectorEstablishmentType === SectorFishingType.LANDING_SITE ||
    sectorEstablishmentType === SectorFishingType.FISH_AUCTION)

const MissionActionItemSectorControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { allControlTypes } = useTarget()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const schema = {
    sectorType: isMissionFinished ? string().required() : string().nullable(),
    sectorEstablishmentType: isMissionFinished ? string().required() : string().nullable(),
    establishment: isMissionFinished
      ? object()
          .shape({
            name: string().required()
          })
          .when(['sectorType', 'sectorEstablishmentType'], {
            is: (sectorType: SectorType, sectorEstablishmentType: string) =>
              !isControlLocationMode(sectorType, sectorEstablishmentType),
            then: schema => schema.required(),
            otherwise: schema => schema.nullable()
          })
      : object().nullable(),
    locationDescription: isMissionFinished
      ? string().when(['sectorType', 'sectorEstablishmentType'], {
          is: isControlLocationMode,
          then: schema => schema.required(),
          otherwise: schema => schema.nullable()
        })
      : string().nullable()
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
