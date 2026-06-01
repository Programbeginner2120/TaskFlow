import { Directive, ElementRef, OnInit, OnDestroy } from '@angular/core';

@Directive({
  selector: 'input[disableScroll]',
  standalone: true
})
export class DisableScrollInputDirective implements OnInit, OnDestroy {
    private wheelListener = () => {
        this.el.nativeElement.blur();
    };

    constructor(private el: ElementRef<HTMLInputElement>) {}

    ngOnInit() {
    this.el.nativeElement.addEventListener('wheel', this.wheelListener, { passive: true });
    }

    ngOnDestroy() {
        // Clean up to prevent memory leaks
        this.el.nativeElement.removeEventListener('wheel', this.wheelListener);
    }
}
