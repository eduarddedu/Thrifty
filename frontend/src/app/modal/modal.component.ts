import { Component, Input, Output, ViewChild, OnChanges, EventEmitter } from '@angular/core';

import { Message } from '../services/messages.service';

@Component({
    selector: 'app-modal',
    templateUrl: './modal.component.html'
})
export class ModalComponent implements OnChanges {

    @ViewChild('modal')
    modal;

    @Input() message: Message;

    @Input() display: boolean;

    @Output() confirm = new EventEmitter<string>();

    ngOnChanges() {
        if (this.display) {
            this.modal.open();
        }
    }

    onClickOk() {
        this.modal.close();
        this.confirm.emit();
    }

}
