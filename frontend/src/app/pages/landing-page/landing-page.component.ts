import { Component, signal } from "@angular/core";
import { SidebarComponent } from "../../shared/components/sidebar/sidebar.component";
import { NavItem } from "../../shared/interfaces/sidebar.interface";
import { Calendar, Clock, Sun, LucideAngularModule, Plus, Ellipsis } from "lucide-angular";

export interface ListItem {
    title: string;
    color: string;
    numItems: number;
}

@Component({
    selector: 'app-landing-page',
    templateUrl: './landing-page.component.html',
    styleUrls: ['./landing-page.component.scss'],
    imports: [SidebarComponent, LucideAngularModule]
})
export class LandingPageComponent {
    
    navItems = signal<NavItem[]>([
        {
            navItemLabel: 'My Day',
            navItemIcon: Sun,
            navItemRouteFn: () => {}
        },
        {
            navItemLabel: 'Upcoming',
            navItemIcon: Clock,
            navItemRouteFn: () => {}
        },
        {
            navItemLabel: 'Calendar',
            navItemIcon: Calendar,
            navItemRouteFn: () => {}
        }
    ]);

    listItems = signal<ListItem[]>([
        {
            title: 'Work',
            color: 'blue',
            numItems: 4
        },
        {
            title: 'Personal',
            color: 'green',
            numItems: 3
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
        {
            title: 'Shopping',
            color: 'orange',
            numItems: 2
        },
    ]);

    readonly plus = Plus;
    readonly ellipsis = Ellipsis;

}