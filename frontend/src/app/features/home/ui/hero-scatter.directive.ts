import { Directive, ElementRef, HostListener, inject, OnDestroy } from '@angular/core';

@Directive({
  selector: '[rbHeroScatter]',
  standalone: true
})
export class HeroScatterDirective implements OnDestroy {
  private readonly element = inject<ElementRef<HTMLElement>>(ElementRef);
  private pointerFrame: number | null = null;
  private lastPointer = { x: 0, y: 0 };

  @HostListener('mousemove', ['$event'])
  scatter(event: MouseEvent): void {
    if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;

    const pointer = { x: event.clientX, y: event.clientY };
    const velocity = {
      x: Math.max(-28, Math.min(28, pointer.x - this.lastPointer.x)),
      y: Math.max(-28, Math.min(28, pointer.y - this.lastPointer.y))
    };
    this.lastPointer = pointer;

    if (this.pointerFrame !== null) cancelAnimationFrame(this.pointerFrame);
    this.pointerFrame = requestAnimationFrame(() => {
      this.letters().forEach((letter, index) => {
        const rect = letter.getBoundingClientRect();
        const deltaX = rect.left + rect.width / 2 - pointer.x;
        const deltaY = rect.top + rect.height / 2 - pointer.y;
        const force = Math.max(0, 1 - Math.hypot(deltaX, deltaY) / 180);
        const direction = index % 2 === 0 ? 1 : -1;

        letter.style.setProperty('--scatter-x', `${(deltaX * .14 + velocity.x * 1.2 * direction) * force}px`);
        letter.style.setProperty('--scatter-y', `${(deltaY * .12 + velocity.y * .9) * force}px`);
        letter.style.setProperty('--scatter-rotate', `${(velocity.x * .65 + direction * 10) * force}deg`);
        letter.style.setProperty('--scatter-scale', `${1 + force * .08}`);
      });
      this.pointerFrame = null;
    });
  }

  @HostListener('mouseleave')
  restore(): void {
    this.letters().forEach((letter) => {
      letter.style.removeProperty('--scatter-x');
      letter.style.removeProperty('--scatter-y');
      letter.style.removeProperty('--scatter-rotate');
      letter.style.removeProperty('--scatter-scale');
    });
    this.lastPointer = { x: 0, y: 0 };
  }

  ngOnDestroy(): void {
    if (this.pointerFrame !== null) cancelAnimationFrame(this.pointerFrame);
  }

  private letters(): NodeListOf<HTMLElement> {
    return this.element.nativeElement.querySelectorAll<HTMLElement>('.hero-letter');
  }
}
