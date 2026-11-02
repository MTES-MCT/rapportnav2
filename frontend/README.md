# RapportNav Frontend

> For full installation and local-dev instructions (backend + frontend), see
> [Installation & développement local](../docs/engineering/getting-started/index.md).
> This file only covers frontend-specific commands.

## Prerequisites & Stack

You will need Node.js v26 (with npm v12), installed with
[nvm](https://github.com/nvm-sh/nvm) (recommended).

The main dependencies are:

- [vite](https://docs.vite.org/) to build the project
- [react](https://react.dev/)
- [react-router-6](https://reactrouter.com/en/main)
- [react-query](https://tanstack.com/query/latest) following async server state pattern
- [monitor-ui](https://github.com/MTES-MCT/monitor-ui) as design system

## Commands

You can see them in the package.json. Here are the main ones:

- `npm run dev` to run the project locally
- `npm run build` to build the project, used in CI
- `npm run test` to run tests once
- `npm run test:watch` to run tests continuously
