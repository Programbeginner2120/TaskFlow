import {
  ChangeDetectionStrategy,
  Component,
  computed,
  ElementRef,
  HostListener,
  inject,
  input,
  model,
  signal,
  Signal,
} from '@angular/core';
import { LucideAngularModule, Filter } from 'lucide-angular';

export abstract class MultiSelectHost {
  abstract selectedValues: Signal<(string | number)[]>;
  abstract toggleValue(value: string | number): void;
}

@Component({
  selector: 'app-multi-select',
  templateUrl: './multi-select.component.html',
  styleUrls: ['./multi-select.component.scss'],
  imports: [LucideAngularModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [{ provide: MultiSelectHost, useExisting: MultiSelectComponent }],
})
export class MultiSelectComponent implements MultiSelectHost {
  private readonly elementRef = inject(ElementRef);

  readonly filterIcon = Filter;

  label = input<string>('');
  title = input<string>('');
  direction = input<'left' | 'center' | 'right'>('right');
  maxLength = input<string>('400px');
  selectedValues = model<(string | number)[]>([]);

  isOpen = signal(false);

  readonly selectedCount = computed(() => this.selectedValues().length);

  toggleValue(value: string | number): void {
    const current = this.selectedValues();
    if (current.includes(value)) {
      this.selectedValues.set(current.filter(v => v !== value));
    } else {
      this.selectedValues.set([...current, value]);
    }
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.isOpen.update(open => !open);
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    if (!this.elementRef.nativeElement.contains(event.target)) {
      this.isOpen.set(false);
    }
  }
}
