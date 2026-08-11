import Text from '@common/components/ui/text'
import { Marge, SpeciesControl } from '@common/types/fish-mission-types.ts'
import { Accent, Icon, IconButton, Label, SimpleTable, Size, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import React, { useState } from 'react'
import { Stack, Tooltip, Whisper } from 'rsuite'
import styled from 'styled-components'
import { MissionFishActionData } from '../../../common/types/mission-action'
import { SatiModuleType } from '../../../common/types/sati'
import { ConformityRow, ConformityTable } from './conformity-table.tsx'
import FishControlMarge from './fish-control-marge.tsx'

interface FishControlSpeciesSectionProps {
  action: MissionFishActionData
}

const Th = styled(SimpleTable.Th)({ boxSizing: 'border-box' })
const Td = styled(SimpleTable.Td)({ boxSizing: 'border-box' })

type ConformityField =
  | 'speciesSizeControlled'
  | 'speciesWeightControlled'
  | 'holdControlledAfterUnloading'
  | 'weighingOperationsMonitoredByInspectors'
  | 'underSizedSeparateRecording'

const CONFORMITY_ROWS: ConformityRow<ConformityField>[] = [
  { field: 'speciesSizeControlled', label: 'Taille des espèces vérifiée', sectionLabel: 'Pour les espèces débarquées' },
  { field: 'speciesWeightControlled', label: 'Poids des espèces vérifié' },
  { field: 'holdControlledAfterUnloading', label: 'Cale contrôlée après déchargement' },
  { field: 'weighingOperationsMonitoredByInspectors', label: 'Suivi des opérations de pesée par les inspecteurs' },
  {
    field: 'underSizedSeparateRecording',
    label: "Enregistrement séparé des poissons n'ayant pas la taille requise",
    sectionLabel: 'Pour les espèces non débarquées'
  }
]

const FishControlSpeciesSection: React.FC<FishControlSpeciesSectionProps> = ({ action }) => {
  const [showModal, setShowModal] = useState(false)
  const isM3 = action.sati?.module === SatiModuleType.M3
  const [currentMarge, setCurrentMarge] = useState<Marge>()

  const handleUpdateMarge = (response: boolean, data?: Marge) => {
    if (!response || !data) return
    console.log(data)
    setCurrentMarge(undefined)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'} style={{ width: '100%' }}>
      <Stack.Item>
        <Label>Inspection des espèces</Label>
      </Stack.Item>

      <Stack.Item
        style={{
          width: '100%',
          padding: '16px',
          backgroundColor: THEME.color.white,
          border: `1px solid ${THEME.color.lightGray}`
        }}
      >
        <ConformityTable rows={CONFORMITY_ROWS} values={action} />
      </Stack.Item>

      {!isEmpty(action?.speciesOnboard) && (
        <Stack.Item style={{ width: '100%', backgroundColor: 'white', padding: '16px' }}>
          <SimpleTable.Table style={{ width: '100%', tableLayout: 'fixed' }}>
            <SimpleTable.Head>
              <tr>
                <Th>Espèce(s)</Th>
                <Th $width={70}>Déclaré</Th>
                <Th $width={60}>Pesé</Th>
                <Th $width={70}>Ss-taille</Th>
                <Th $width={110}>Présentation</Th>
                <Th $width={100}>Zone</Th>
                {isM3 && <Th $width={70}>Marge</Th>}
                {isM3 && <Th $width={40} />}
              </tr>
            </SimpleTable.Head>
            <tbody>
              {action?.speciesOnboard?.map((species: SpeciesControl, index: number) => (
                <SimpleTable.BodyTr key={`${species.speciesCode}${index}`}>
                  <Td>
                    <Whisper
                      placement="top"
                      trigger="hover"
                      speaker={<Tooltip>{`${species.speciesCode} – ${species.speciesName}`}</Tooltip>}
                    >
                      <Text as="h3" truncate weight="bold" fontStyle={species.isNotLanded ? 'italic' : 'normal'}>
                        {`${species.speciesCode} – ${species.speciesName}`}
                      </Text>
                    </Whisper>
                  </Td>
                  <Td>{species.declaredWeight !== undefined ? `${species.declaredWeight} kg` : '--'}</Td>
                  <Td>{species.controlledWeight !== undefined ? `${species.controlledWeight} kg` : '--'}</Td>
                  <Td>{species.underSizedWeight !== undefined ? `${species.underSizedWeight} kg` : '- kg'}</Td>
                  <Td>
                    <Text as="h3" weight="normal" truncate>
                      {species.presentationCodes?.join(', ')}
                    </Text>
                  </Td>
                  <Td>
                    <Text as="h3" weight="normal" truncate>
                      {species.faoZones?.join(', ')}
                    </Text>
                  </Td>
                  {isM3 && (
                    <Td>
                      {/* Marge: not backed by any data yet, displayed as a placeholder until the field is defined. */}
                      <Stack direction="row" alignItems="center" justifyContent="space-between">
                        <Stack.Item>--</Stack.Item>
                        <Stack.Item>
                          <IconButton
                            disabled={true}
                            size={Size.SMALL}
                            accent={Accent.TERTIARY}
                            Icon={Icon.EditUnbordered}
                            color={THEME.color.slateGray}
                            onClick={() => {
                              setCurrentMarge(species.marge)
                              setShowModal(true)
                            }}
                          />
                        </Stack.Item>
                      </Stack>
                    </Td>
                  )}
                  {isM3 && (
                    <Td $isCenter>
                      {species.isNotLanded ? (
                        <Whisper placement="top" trigger="hover" speaker={<Tooltip>Espèce non débarquée</Tooltip>}>
                          <span>
                            <Icon.VesselPro color={THEME.color.slateGray} size={18} />
                          </span>
                        </Whisper>
                      ) : (
                        <Icon.VesselPro color={THEME.color.lightGray} size={18} />
                      )}
                    </Td>
                  )}
                  {showModal && (
                    <FishControlMarge
                      species={species}
                      onSubmit={handleUpdateMarge}
                      currentMarge={currentMarge}
                      onChangeMarge={setCurrentMarge}
                      onClose={() => setShowModal(false)}
                    />
                  )}
                </SimpleTable.BodyTr>
              ))}
            </tbody>
          </SimpleTable.Table>
        </Stack.Item>
      )}

      {!isEmpty(action?.speciesObservations) && (
        <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
          <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
            <Stack.Item>
              <Label>Observations (hors infractions) sur les espèces</Label>
            </Stack.Item>
            <Stack.Item>
              <Text as="h3" weight="medium">
                {action?.speciesObservations}
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default FishControlSpeciesSection
