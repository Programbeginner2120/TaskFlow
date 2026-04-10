import { Component, input, NgModule, signal, WritableSignal } from "@angular/core";
import { NavItem } from "../../interfaces/sidebar.interface";
import { PanelRight, LucideAngularModule, List, ArrowRightFromLine, ArrowLeftFromLine } from "lucide-angular";
import { CommonModule } from "@angular/common";

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss'],
    imports: [LucideAngularModule, CommonModule]
})
export class SidebarComponent {

    sidebarTitle = input<string>('');
    navItems = input<NavItem[]>([]);
    collapsible = input<boolean>(false);
    initialllyExpanded = input<boolean>(true);

    isExpanded: WritableSignal<boolean> = signal<boolean>(this.initialllyExpanded());

    readonly list = List;
    readonly panelRight = PanelRight;
    readonly arrowRightFromLine = ArrowRightFromLine;
    readonly arrowLeftFromLine = ArrowLeftFromLine;

}