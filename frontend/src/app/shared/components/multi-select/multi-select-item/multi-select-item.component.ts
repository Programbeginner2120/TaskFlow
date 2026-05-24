import {
  ChangeDetectionStrategy,
  Component,
  computed,
  HostListener,
  inject,
  input,
} from '@angular/core';
import { LucideAngularModule, Check } from 'lucide-angular';
import { MultiSelectHost } from '../multi-select.component';

@Component({
  selector: 'app-multi-select-item',
  templateUrl: './multi-select-item.component.html',
  styleUrls: ['./multi-select-item.component.scss'],
  imports: [LucideAngularModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MultiSelectItemComponent {
  private readonly parent = inject(MultiSelectHost);

  readonly checkIcon = Check;

  value = input.required<string | number>();

  readonly isSelected = computed(() =>
    this.parent.selectedValues().includes(this.value())
  );

  @HostListener('click')
  onItemClick(): void {
    this.parent.toggleValue(this.value());
  }
}
