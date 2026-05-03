import { RruleDay, RruleFrequency } from "../interfaces/rrule.interface";

/**
 * Builds an Rrule from given parameters
 * @param freq the RruleFrequency, one of ['DAILY', 'WEEKLY', 'MONTHLY']
 * @param day the RruleDay, one of ['MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU']
 * @param monthDay the day of the month, between 1 - 31
 * @returns the formatted Rrule
 */
export function buildRule(freq: RruleFrequency, day?: RruleDay, monthDay?: number): string {
    if (freq === 'WEEKLY' && day) {
        return `FREQ=WEEKLY;BYDAY=${day}`;
    }
    if (freq === 'MONTHLY' && monthDay) {
        return `FREQ=MONTHLY;BYMONTHDAY=${monthDay}`;
    }
    return `FREQ=${freq}`;
}

export function parseRule(rrule: string): {
    frequency: RruleFrequency;
    dayOfWeek?: RruleDay;
    monthDay?: number;
} {
    const freq = /FREQ=(\w+)/.exec(rrule)?.[1] as RruleFrequency ?? RruleFrequency.WEEKLY;
    const byday = /BYDAY=(\w+)/.exec(rrule)?.[1] as RruleDay | undefined;
    const bymonthday = /BYMONTHDAY=(\d+)/.exec(rrule)?.[1];
    return {
        frequency: freq,
        dayOfWeek: byday,
        monthDay: bymonthday ? Number(bymonthday) : undefined,
    };
}