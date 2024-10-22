import { Accent, Tag } from '@mtes-mct/monitor-ui'

type MissionTimelineItemCardTagProps = {
  tags?: string[]
}

const MissionTimelineItemCardTag: React.FC<MissionTimelineItemCardTagProps> = ({ tags }) => {
  return (
    <div>
      {tags?.map(tag => (
        <Tag key={tag} accent={Accent.PRIMARY} style={{ marginRight: '0.5rem' }}>
          {tag}
        </Tag>
      ))}
    </div>
  )
}

export default MissionTimelineItemCardTag
