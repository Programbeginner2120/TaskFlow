import { Component, input, model } from '@angular/core';
import { LucideAngularModule, Search, X } from 'lucide-angular';

@Component({
    selector: 'app-search-bar',
    templateUrl: './search-bar.component.html',
    styleUrls: ['./search-bar.component.scss'],
    imports: [LucideAngularModule],
})
export class SearchBarComponent {
    readonly searchIcon = Search;
    readonly clearIcon = X;

    searchQuery = model<string>('');
    placeholder = input<string>('Search tasks...');

    clear(): void {
        this.searchQuery.set('');
    }
}
