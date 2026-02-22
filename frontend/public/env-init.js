// Runtime environment variables injection
// This script is used by the `import-meta-env` library to inject
// runtime environment variables when running in production
// see: https://import-meta-env.org/guide/getting-started/introduction.html

// Ensure globalThis exists (for older browsers)
if (typeof globalThis === 'undefined') {
  var globalThis = window;
}
globalThis.import_meta_env = JSON.parse('"import_meta_env_placeholder"')
