import { Directive, ElementRef, inject, OnInit } from '@angular/core';

@Directive({
    selector: '[autoFocus]',
    standalone: true
})
export class AutoFocusDirective implements OnInit {

    private readonly el = inject(ElementRef<HTMLElement>);

    ngOnInit(): void {
        this.el.nativeElement.focus();
    }
}
