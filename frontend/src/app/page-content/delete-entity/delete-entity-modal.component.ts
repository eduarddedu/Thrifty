import { Component, ViewChild, OnInit } from '@angular/core';
import { AppMessage } from '../../model/app-message';
import { DeleteEntityModalService } from '../../services/modal.service';

@Component({
    selector: 'app-delete-entity-modal',
    templateUrl: './delete-entity-modal.component.html'
})
export class DeleteEntityModalComponent implements OnInit {

    @ViewChild('modal')
    modal;

    text: string;

    constructor(private ms: DeleteEntityModalService) {}

    ngOnInit() {
        this.ms.onDeleteMessage((message: AppMessage) => {
            if (message === null) {
                this.modal.close();
            } else {
                this.modal.open();
                this.text = message.text;
            }
        });
    }

    onClickOk() {
        this.modal.close();
        this.ms.pushDelete();
    }
}
