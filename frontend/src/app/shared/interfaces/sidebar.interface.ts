import { LucideIconData } from "lucide-angular";

export interface NavItem {
    navItemIcon?: LucideIconData;
    navItemLabel: string;
    navItemRouteFn: () => void;
}