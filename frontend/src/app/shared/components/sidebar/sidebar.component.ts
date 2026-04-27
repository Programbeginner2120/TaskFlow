import { Component, HostListener, input, linkedSignal } from "@angular/core";
import { NavItem } from "../../interfaces/sidebar.interface";
import { LucideAngularModule, List, ArrowRightFromLine, ArrowLeftFromLine } from "lucide-angular";

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html',
    styleUrls: ['./sidebar.component.scss'],
    imports: [LucideAngularModule]
})
export class SidebarComponent {

    sidebarTitle = input<string>('');
    navItems = input<NavItem[]>([]);
    collapsible = input<boolean>(false);
    initiallyExpanded = input<boolean>(false);

    isExpanded = linkedSignal(() => this.initiallyExpanded());

    readonly list = List;
    readonly arrowRightFromLine = ArrowRightFromLine;
    readonly arrowLeftFromLine = ArrowLeftFromLine;

    onNavItemClick(navItem: NavItem): void {
        navItem.navItemRouteFn();
        if (this.collapsible()) {
            this.isExpanded.set(false);
        }
    }

}