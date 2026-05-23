import { LucideIconData } from "lucide-angular";

export interface StatisticsCard {
    label: string;
    value: number;
    iconBackgroundColor: string;
    icon: LucideIconData;
}