/** Serialize a Date to "YYYY-MM-DD" in local time, avoiding UTC midnight drift. */
export function toLocalDateString(date: Date): string {
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
}

/** Returns "Today" if the date is today, otherwise a short formatted date like "Apr 15". */
export function formatDisplayDate(date: Date): string {
    const today = new Date();
    if (
        date.getFullYear() === today.getFullYear() &&
        date.getMonth() === today.getMonth() &&
        date.getDate() === today.getDate()
    ) {
        return 'Today';
    }
    return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}
