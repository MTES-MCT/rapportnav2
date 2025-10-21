import { UTCDate } from '@date-fns/utc'
import { Accent, Button, Icon } from '@mtes-mct/monitor-ui'
import { ULAM_SIDEBAR_ITEMS } from '@router/routes.tsx'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import { useStore } from '@tanstack/react-store'
import React, { useEffect } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import ItemListDateRangeNavigator from '../features/common/components/elements/item-list-daterange-navigator.tsx'
import MissionListPageContentWrapper from '../features/common/components/layout/mission-list-page-content-wrapper.tsx'
import MissionListPageHeaderWrapper from '../features/common/components/layout/mission-list-page-header-wrapper.tsx'
import MissionListPageWrapper from '../features/common/components/layout/mission-list-page-wrapper.tsx'
import MissionListPageSidebarWrapper from '../features/common/components/ui/mission-list-page-sidebar.tsx'
import MissionListPageTitle from '../features/common/components/ui/mission-list-page-title.tsx'
import { useDate } from '../features/common/hooks/use-date.tsx'
import { Inquiry, InquiryStatusType } from '../features/common/types/inquiry.ts'
import { OwnerType } from '../features/common/types/owner-type.ts'
import InquiryList from '../features/inquiry/components/elements/inquiry-list.tsx'
import useCreateInquiryMutation from '../features/inquiry/services/use-create-inquiry.tsx'
import useInquiriesQuery from '../features/inquiry/services/use-inquiries.tsx'
import { store } from '../store/index.ts'

const InquiryListPage: React.FC = () => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const { getTodayYearRange } = useDate()
  const mutation = useCreateInquiryMutation()
  const user = useStore(store, state => state.user)
  const [searchParams, setSearchParams] = useSearchParams()
  const { isLoading, data: inquiries } = useInquiriesQuery(searchParams)

  useEffect(() => {
    if (searchParams.get('endDateTimeUtc') && searchParams.get('startDateTimeUtc')) return
    setSearchParams(getTodayYearRange())
    return () => {}
  }, [searchParams, setSearchParams, getTodayYearRange])

  const handleUpdateDateTime = (currentDate: Date) => {
    setSearchParams(getTodayYearRange(currentDate))
  }

  const handleCreateInquiry = async () => {
    const response = await mutation.mutateAsync({
      serviceId: user?.serviceId,
      status: InquiryStatusType.IN_PROGRESS,
      startDateTimeUtc: new UTCDate().toISOString()
    } as Inquiry)
    if (response.id) navigate(`${getUrl(OwnerType.INQUIRY)}/${response.id}`)
  }

  return (
    <MissionListPageWrapper
      header={<MissionListPageHeaderWrapper title={<MissionListPageTitle user={user} />} />}
      sidebar={<MissionListPageSidebarWrapper defaultItemKey="inquiries" items={ULAM_SIDEBAR_ITEMS} />}
      footer={undefined}
    >
      <MissionListPageContentWrapper
        loading={isLoading}
        hasMissions={!!inquiries?.length}
        title={'Mes contrôles croisés (vérifications post contrôle terrain et enquêtes)'}
        filters={
          <>
            <Stack direction="column" spacing={'2rem'} style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" justifyContent={'flex-end'} alignItems={'flex-end'} style={{ width: '100%' }}>
                  <Stack.Item>
                    <Button Icon={Icon.Plus} accent={Accent.PRIMARY} onClick={() => handleCreateInquiry()}>
                      Créer un contrôle croisé
                    </Button>
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <ItemListDateRangeNavigator
                  timeframe={'year'}
                  onUpdateCurrentDate={handleUpdateDateTime}
                  startDateTimeUtc={searchParams.get('startDateTimeUtc')}
                />
              </Stack.Item>
            </Stack>
          </>
        }
        list={<InquiryList inquiries={inquiries} user={user} />}
      />
    </MissionListPageWrapper>
  )
}

export default InquiryListPage
