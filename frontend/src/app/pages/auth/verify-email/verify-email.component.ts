import { Component, inject, signal } from "@angular/core";
import { ActivatedRoute, RouterLink } from "@angular/router";
import { AuthService } from "../../../services/auth.service";
import { HeaderComponent } from "../../../components/header/header.component";

@Component({
    selector: 'app-verify-email',
    templateUrl: './verify-email.component.html',
    styleUrls: ['./verify-email.component.scss', '../../../shared/common-styles/auth-common-styles.scss'],
    imports: [RouterLink, HeaderComponent]
})
export class VerifyEmailComponent {
    private readonly authService = inject(AuthService);
    private readonly route = inject(ActivatedRoute);

    protected readonly status = signal<'verifying' | 'success' | 'error'>('verifying');

    constructor() {
        const token = this.route.snapshot.queryParamMap.get('token') ?? '';

        if (!token) {
            this.status.set('error');
            return;
        }

        this.authService.verifyEmail(token).subscribe({
            next: () => this.status.set('success'),
            error: () => this.status.set('error')
        });
    }

}