import { Directive, Output, EventEmitter, HostListener } from '@angular/core';

@Directive({ selector: '[middleclick]' })
export class MiddleclickDirective {
  @Output('middleclick') middleclick = new EventEmitter();

  @HostListener('mouseup', ['$event'])
  middleclickEvent(event) {
    if (event.button === 1) {
      this.middleclick.emit(event);
    }
  }
}