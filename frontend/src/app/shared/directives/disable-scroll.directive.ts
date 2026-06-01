import { Directive, ElementRef, OnInit, OnDestroy } from '@angular/core';

@Directive({
  selector: 'input[type="number"][disableScroll]',
  standalone: true
})
export class DisableScrollInputDirective implements OnInit, OnDestroy {
  private wheelListener = (event: WheelEvent) => {
    // This stops the input field from changing value
    event.preventDefault(); 
  };

  constructor(private el: ElementRef<HTMLInputElement>) {}

  ngOnInit() {
    // Register natively with passive: false to preserve general page scroll
    this.el.nativeElement.addEventListener('wheel', this.wheelListener, { passive: false });
  }

  ngOnDestroy() {
    // Clean up to prevent memory leaks
    this.el.nativeElement.removeEventListener('wheel', this.wheelListener);
  }
}
