export interface LineSeriesData {
    name: string;
    data: number[];
}

export interface BarSeriesData {
    name: string;
    data: number[];
}

export interface GaugeThreshold {
    value: number; // 0-1 inclusive
    color: string;
}

export interface DonutSlice {
    name: string;
    value: number;
}