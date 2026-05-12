import { ChangeDetectionStrategy, Component, inject, model, signal, WritableSignal } from '@angular/core';
import { LucideAngularModule, Plus, Moon, Sun, User } from 'lucide-angular';
import { SearchBarComponent } from '../../../shared/components/search-bar/search-bar.component';
import { ThemeService } from '../../../services/theme.service';
import { AuthService } from '../../../services/auth.service';
import { PlatformService } from '../../../services/platform.service';
import { HamburgerMenuComponent } from '../../../shared/components/hamburger/hamburger.component';
import { QuickAddTaskModalComponent } from '../../quick-add-task-modal/quick-add-task-modal.component';

@Component({
    selector: 'app-landing-page-header',
    templateUrl: './landing-page-header.component.html',
    styleUrls: ['./landing-page-header.component.scss'],
    imports: [LucideAngularModule, SearchBarComponent, HamburgerMenuComponent, QuickAddTaskModalComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LandingPageHeaderComponent {
    readonly plusIcon = Plus;
    readonly moonIcon = Moon;
    readonly sunIcon = Sun;
    readonly userIcon = User;

    private readonly themeService = inject(ThemeService);
    private readonly authService = inject(AuthService);
    readonly platformService = inject(PlatformService);

    readonly theme = this.themeService.theme;
    readonly currentUser = this.authService.currentUser;

    searchQuery = model<string>('');

    readonly isQuickAddingTask: WritableSignal<boolean> = signal<boolean>(false);

    toggleTheme(): void {
        this.themeService.toggleTheme();
    }

    logout(): void {
        this.authService.logout();
    }
}
