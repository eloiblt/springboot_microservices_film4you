import { Component, OnInit, Input, Output, EventEmitter, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ModalComponent {

  @Input()
  open: boolean;

  @Input()
  modalTitle: string;

  @Input()
  dissmissable = true;

  @Output()
  openChange = new EventEmitter<boolean>();

  @Output()
  modalClose = new EventEmitter<void>();

  get _open(): boolean {
    return this.open;
  }
  set _open(value: boolean) {
    this.openChange.emit(value);
  }

  closeModal() {
    if (this.dissmissable) {
      this.modalClose.emit();
      this._open = false;
    }
  }

}
