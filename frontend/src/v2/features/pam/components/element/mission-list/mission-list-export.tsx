import React, { FC, useState } from 'react'
import { Accent, Button, Dialog, Icon, Label, MultiRadio, Select, Size, THEME } from '@mtes-mct/monitor-ui'
import { CompletenessForStatsStatusEnum, Mission } from '@common/types/mission-types.ts'
import { Divider, Stack } from 'rsuite'
import { formatMissionName } from '@features/pam/mission/utils/utils.ts'
import { getOpenByText } from '../../../../common/hooks/use-mission-tag.tsx'
import some from 'lodash/some'
import Text from '@common/components/ui/text.tsx'
import { ExportReportType } from '../../../../common/types/mission-export-types.ts'
import ExportFileButton from '../../../../common/components/elements/export-file-button.tsx'
import { every } from 'lodash'

type MissionListExportDialogProps = {
  availableMissions: Mission[]
  toggleDialog: () => void
  triggerExport: (missions: Mission[], variant: ExportReportType, zip: boolean) => void
  variant: ExportReportType
  exportInProgress: boolean
}

const MissionListExportDialog: FC<MissionListExportDialogProps> = ({
  availableMissions,
  toggleDialog,
  triggerExport,
  exportInProgress,
  variant
}) => {
  const options = [
    {
      label: 'J’exporte un seul fichier (intégrant des missions inter-services)',
      value: false
    },
    {
      label: 'Exporter un fichier pour chaque mission sélectionnée',
      value: true
    }
  ]

  const [exportAsZip, setExportAsZip] = useState<boolean>(options[0].value)
  const [mainMissionId, setMainMissionId] = useState<number | undefined>(undefined)

  const exportButtonDisabled =
    (!exportAsZip && !mainMissionId) ||
    exportInProgress ||
    every(availableMissions, (m: Mission) => m.completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE)

  const missionOptions = availableMissions.map((mission: Mission) => ({
    label: `${formatMissionName(mission.startDateTimeUtc)} - ${getOpenByText(mission.missionSource)} - ${mission.actions?.length ?? 0} action(s)`,
    value: mission.id,
    isDisabled: mission.completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE
  }))

  const title =
    variant === ExportReportType.AEM
      ? 'Exporter les tableaux AEM des missions sélectionnées'
      : 'Exporter les rapports de patrouille des missions sélectionnées'

  const onSelectExportType = (nextValue: string | undefined) => {
    setExportAsZip(nextValue)
    nextValue && setMainMissionId(undefined) // reset the select field
  }

  const onSelectMainMission = (nextValue: number | undefined) => {
    setMainMissionId(nextValue)
  }

  const onExport = () => {
    // set main mission first
    const missions = [
      ...availableMissions.filter((m: Mission) => m.id === mainMissionId),
      ...availableMissions.filter((m: Mission) => m.id !== mainMissionId)
    ]
    triggerExport(missions, variant, exportAsZip)
  }

  return (
    <Dialog>
      <Dialog.Title style={{ border: '2px solid white' }}>
        <Text as={'h2'} color={THEME.color.white} style={{ textAlign: 'center' }}>
          {title}
        </Text>
      </Dialog.Title>
      <Dialog.Body style={{ border: '2px solid white' }}>
        <Stack
          direction={'column'}
          spacing={'0.4rem'}
          divider={<Divider style={{ width: '100%', backgroundColor: THEME.color.lightGray }} />}
          style={{ width: '100%' }}
        >
          <Stack.Item style={{ width: '100%', borderRadius: 0 }}>
            <MultiRadio label="" onChange={onSelectExportType} options={options} value={exportAsZip} />
            {exportAsZip &&
              some(
                availableMissions,
                (m: Mission) => m.completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE
              ) && (
                <div style={{ marginTop: '1rem' }}>
                  <Text as={'h3'} color={THEME.color.maximumRed} fontStyle={'italic'} weight={'medium'}>
                    Attention, les missions suivantes ne seront pas exportées car le statut de la donnée est "à
                    compléter" :
                  </Text>
                  {availableMissions
                    .filter((m: Mission) => m.completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE)
                    .map((m: Mission) => (
                      <Text as={'h3'} color={THEME.color.maximumRed} fontStyle={'italic'} weight={'medium'} key={m.id}>
                        - {formatMissionName(m.startDateTimeUtc)} - {getOpenByText(m.missionSource)} -{' '}
                        {m.actions?.length ?? 0} action(s)
                      </Text>
                    ))}
                </div>
              )}
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Select
              label={
                <Label>
                  Mission principale de la patrouille{' '}
                  <Icon.Info
                    color={THEME.color.slateGray}
                    title={`La mission principale est celle qui correspond à la période complète de la patrouille (date de début et de fin).\n\nAttention, la mission principale doit être complète pour pouvoir être exportée.`}
                  />
                </Label>
              }
              options={missionOptions}
              disabled={exportAsZip}
              onChange={onSelectMainMission}
              value={mainMissionId}
            />
          </Stack.Item>
        </Stack>
      </Dialog.Body>
      <Dialog.Action>
        <Stack
          direction={'row'}
          style={{ width: '100%' }}
          justifyContent={'flex-end'}
          alignItems={'center'}
          spacing={'1rem'}
        >
          <Stack.Item>
            <Button
              accent={Accent.SECONDARY}
              size={Size.NORMAL}
              onClick={toggleDialog}
              Icon={() => <svg height={20} width={0} />}
            >
              Annuler
            </Button>
          </Stack.Item>
          <Stack.Item>
            <ExportFileButton
              onClick={() => onExport()}
              disabled={exportButtonDisabled}
              isLoading={exportInProgress}
              label={'Exporter'}
              accent={Accent.PRIMARY}
            />
          </Stack.Item>
        </Stack>
      </Dialog.Action>
    </Dialog>
  )
}

export default MissionListExportDialog