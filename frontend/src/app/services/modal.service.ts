import { Injectable } from '@angular/core';

import { AppMessage } from '../model/app-message';

@Injectable()
export class DeleteEntityModalService {
    private onDeleteMessageCallback: (message: AppMessage) => void;
    private onConfirmDeleteCallback: () => void;

    onDeleteMessage(callback: (message: AppMessage) => void) {
        this.onDeleteMessageCallback = callback;
    }

    pushMessage(message: AppMessage) {
        this.onDeleteMessageCallback(message);
    }

    onConfirmDelete(callback: () => void) {
        this.onConfirmDeleteCallback = callback;
    }

    pushDelete() {
        this.onConfirmDeleteCallback();
    }

}
