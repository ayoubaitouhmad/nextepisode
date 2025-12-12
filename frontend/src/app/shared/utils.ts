export function getYearFromDate(releaseDate: string): string {
  if (!releaseDate) return 'Unknown';
  return new Date(releaseDate).getFullYear().toString();
}
