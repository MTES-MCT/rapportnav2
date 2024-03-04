export const csrfToken = (): string | null => {
  const match = document.cookie.match(/XSRF-TOKEN=([^;]*)/);
  return match ? match[1] : null;
}
