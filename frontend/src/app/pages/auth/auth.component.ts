import { Component, computed, inject, signal, WritableSignal } from "@angular/core";
import { LayoutGrid, LucideAngularModule, Moon, Sun } from "lucide-angular";
import { ThemeService } from "../../services/theme.service";
import { PlatformService } from "../../services/platform.service";
import { Theme } from "../../interfaces/theme.interface";
import { AuthMode } from "../../interfaces/auth.interface";
import { LoginComponent } from "../../components/login/login.component";
import { RegisterComponent } from "../../components/register/register.component";
import { ForgotPasswordComponent } from "../../components/forgot-password/forgot-password.component";

@Component({
    selector: 'app-auth-layout',
    templateUrl: './auth.component.html',
    styleUrls: ['./auth.component.scss'],
    imports: [LucideAngularModule, LoginComponent, RegisterComponent, ForgotPasswordComponent]
})
export class AuthComponent {

    readonly layoutGrid = LayoutGrid;
    readonly moon = Moon;
    readonly sun = Sun;

    readonly themeService = inject(ThemeService);
    readonly platformService = inject(PlatformService);

    readonly theme = computed<Theme>(() => this.themeService.theme());

    currentMode: WritableSignal<AuthMode> = signal('LOGIN');

}
