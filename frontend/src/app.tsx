import { createSyncStoragePersister } from "@tanstack/query-sync-storage-persister";
import { PersistQueryClientProvider } from "@tanstack/react-query-persist-client";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { RouterProvider } from "./router/router-provider";
import { router } from "./router/router";
import { QueryClient, NetworkMode } from "@tanstack/react-query";
import UIThemeWrapper from "./ui/ui-theme-wrapper";

const queryClient: QueryClient = new QueryClient({
  defaultOptions: {
    queries: {
      gcTime: 1000 * 60 * 60 * 24, // 24 hours
      networkMode: "offlineFirst" as NetworkMode,
    },
    mutations: {
      networkMode: "offlineFirst" as NetworkMode,
    },
  },
  // configure global cache callbacks to show toast notifications
  // mutationCache: new MutationCache({
  //   onSuccess: (data: any) => {
  //    console.log(data.message)
  //   },
  //   onError: (error: any) => {
  //     console.log(error.message)
  //   },
  // }),
});

const persister = createSyncStoragePersister({
  storage: window.localStorage,
});

const App: React.FC = () => {
  return (
    <PersistQueryClientProvider
      client={queryClient}
      persistOptions={{ persister }}
      onSuccess={() => {
        console.log("PersistStorage: load success");
        // resume mutations after initial restore from localStorage was successful
        // queryClient.resumePausedMutations().then(() => {
        //   queryClient.invalidateQueries()
        // })
      }}
    >
      <UIThemeWrapper>
        <RouterProvider router={router} />
      </UIThemeWrapper>

      <ReactQueryDevtools initialIsOpen />
    </PersistQueryClientProvider>
  );
};

export default App;
