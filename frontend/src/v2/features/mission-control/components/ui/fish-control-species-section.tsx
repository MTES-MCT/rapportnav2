import Text from '@common/components/ui/text'
import { DiscardedSpeciesControl, Marge, SpeciesControl } from '@common/types/fish-mission-types.ts'
import { Accent, Icon, IconButton, Label, SimpleTable, Size, THEME } from '@mtes-mct/monitor-ui'
import { isEmpty } from 'lodash'
import React, { useState } from 'react'
import { Stack, Tooltip, Whisper } from 'rsuite'
import styled from 'styled-components'
import { MissionFishActionData } from '../../../common/types/mission-action'
import { SatiModuleType } from '../../../common/types/sati'
import { ConformitySection, ConformityTable } from './conformity-table.tsx'
import FishControlMarge from './fish-control-marge.tsx'

interface FishControlSpeciesSectionProps {
  action: MissionFishActionData
}

const Th = styled(SimpleTable.Th)({ boxSizing: 'border-box', textAlign: 'left' })
const Td = styled(SimpleTable.Td)({ boxSizing: 'border-box' })

type ConformityField =
  | 'speciesSizeControlled'
  | 'speciesWeightControlled'
  | 'holdControlledAfterUnloading'
  | 'weighingOperationsMonitoredByInspectors'
  | 'underSizedSeparateRecording'
  | 'separateStowageOfPreservedSpecies'
  | 'underSizedSeparateStowage'

const CONFORMITY_ROWS: ConformitySection<ConformityField>[] = [
  {
    title: 'Pour les espèces débarquées',
    rows: [
      { field: 'speciesSizeControlled', label: 'Taille des espèces vérifiée' },
      { field: 'speciesWeightControlled', label: 'Poids des espèces vérifié' },
      {
        field: 'holdControlledAfterUnloading',
        label: 'Cale contrôlée après déchargement',
        hide: action => (action as MissionFishActionData)?.sati?.module !== SatiModuleType.M3
      },
      {
        field: 'weighingOperationsMonitoredByInspectors',
        label: 'Suivi des opérations de pesée par les inspecteurs',
        hide: action => (action as MissionFishActionData)?.sati?.module !== SatiModuleType.M3
      },

      {
        field: 'separateStowageOfPreservedSpecies',
        label: 'Arrimage séparé des espèces soumises à plan',
        hide: action => (action as MissionFishActionData)?.sati?.module !== SatiModuleType.M1
      },
      {
        field: 'underSizedSeparateStowage',
        label: 'Arrimage séparé des poissons n’ayant pas la taille requise',
        hide: action => (action as MissionFishActionData)?.sati?.module !== SatiModuleType.M1
      }
    ]
  },
  {
    title: 'Pour les espèces non débarquées',
    rows: [
      {
        field: 'underSizedSeparateRecording',
        label: "Enregistrement séparé des poissons n'ayant pas la taille requise"
      }
    ]
  }
]

const FishControlSpeciesSection: React.FC<FishControlSpeciesSectionProps> = ({ action }) => {
  const [showModal, setShowModal] = useState(false)
  const isM3 = action.sati?.module === SatiModuleType.M3
  const [currentMarge, setCurrentMarge] = useState<Marge>()
  const [currentSpecies, setCurrentSpecies] = useState<SpeciesControl>()

  const handleUpdateMarge = (response: boolean, data?: Marge) => {
    if (response && data) {
      console.log(data) //TODO: Remove and set formik value
    }
    setShowModal(false)
    setCurrentMarge(undefined)
    setCurrentSpecies(undefined)
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
        <ConformityTable values={action} rows={isM3 ? CONFORMITY_ROWS : CONFORMITY_ROWS.map(c => ({ rows: c.rows }))} />
      </Stack.Item>

      {!isEmpty(action?.speciesOnboard) && (
        <Stack.Item style={{ width: '100%', backgroundColor: 'white', padding: '16px' }}>
          <SimpleTable.Table style={{ width: '100%', tableLayout: 'fixed' }}>
            <SimpleTable.Head>
              <tr>
                <Th>Espèce(s)</Th>
                <Th $width={70}>Déclaré</Th>
                <Th $width={60}>{isM3 ? 'Pesé' : 'Estimé'}</Th>
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
                      <Stack direction="row" alignItems="center" justifyContent="space-between">
                        <Stack.Item>-</Stack.Item>
                        <Stack.Item>
                          <IconButton
                            color="white"
                            size={Size.SMALL}
                            accent={Accent.SECONDARY}
                            Icon={Icon.EditUnbordered}
                            onClick={() => {
                              setCurrentMarge(species.marge)
                              setCurrentSpecies(species)
                              setShowModal(true)
                            }}
                            style={{
                              color: 'white',
                              borderColor: THEME.color.blueGray,
                              backgroundColor: THEME.color.blueGray,
                              borderRadius: '50%'
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
                </SimpleTable.BodyTr>
              ))}
            </tbody>
          </SimpleTable.Table>
        </Stack.Item>
      )}
      {showModal && currentSpecies && (
        <FishControlMarge
          species={currentSpecies}
          currentMarge={currentMarge}
          onSubmit={handleUpdateMarge}
          onChangeMarge={setCurrentMarge}
          onClose={() => setShowModal(false)}
        />
      )}
      {!isM3 && !isEmpty(action?.discardedSpecies) && (
        <Stack.Item>
          <Stack direction="column" alignItems="flex-start" spacing={'0.1rem'} style={{ width: '100%' }}>
            <Stack.Item>
              <Label>Rejets</Label>
            </Stack.Item>

            <Stack.Item
              style={{
                width: '100%',
                padding: '16px',
                backgroundColor: THEME.color.white,
                border: `1px solid ${THEME.color.lightGray}`
              }}
            >
              <SimpleTable.Table style={{ width: '100%', tableLayout: 'fixed' }}>
                <SimpleTable.Head>
                  <tr>
                    <Th style={{ textAlign: 'left' }}>Espèce(s) rejétées</Th>
                    <Th $width={70}>Qté</Th>
                    <Th $width={130}>Nature rejet</Th>
                    <Th $width={100}>Zone</Th>
                  </tr>
                </SimpleTable.Head>
                <tbody>
                  {action?.discardedSpecies?.map((species: DiscardedSpeciesControl, index: number) => (
                    <SimpleTable.BodyTr key={`${species.speciesCode}${index}`}>
                      <Td>{`${species.speciesCode}`}</Td>
                      <Td>{`${species.rejectedWeight} kg`}</Td>
                      <Td>{`${species.discardReason}`}</Td>
                      <Td>
                        <Text as="h3" weight="normal" truncate>
                          {species.faoZones?.join(', ')}
                        </Text>
                      </Td>
                    </SimpleTable.BodyTr>
                  ))}
                </tbody>
              </SimpleTable.Table>
            </Stack.Item>
          </Stack>
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
