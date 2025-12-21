export function getYearFromDate(releaseDate: string): string {
  if (!releaseDate) return 'Unknown';
  return new Date(releaseDate).getFullYear().toString();
}

export function capitalizeFirstLetter(string: string) {
  if (!string) return ""; // Handle empty strings
  return string.charAt(0).toUpperCase() + string.slice(1);
}
