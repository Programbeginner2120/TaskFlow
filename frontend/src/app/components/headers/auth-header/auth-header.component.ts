import { ChangeDetectionStrategy, Component, computed, inject, input } from "@angular/core";
import { ThemeService } from "../../../services/theme.service";
import { LayoutGrid, LucideAngularModule, Moon, Sun } from "lucide-angular";

@Component({
    selector: 'app-auth-header',
    templateUrl: './auth-header.component.html',
    styleUrls: ['./auth-header.component.scss'],
    imports: [LucideAngularModule],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AuthHeaderComponent {

    readonly themeService = inject(ThemeService);

    readonly headerTitle = input.required<string>();

    readonly theme = computed(() => this.themeService.theme());

    readonly layoutGrid = LayoutGrid;
    readonly sun = Sun;
    readonly moon = Moon;

}