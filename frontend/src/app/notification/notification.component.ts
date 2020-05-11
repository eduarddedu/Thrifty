import { Component, Input } from '@angular/core';

import { Message, Alert } from '../services/messages.service';

@Component({
    selector: 'app-notification',
    template: `
        <div *ngIf="message" [class]="bootstrapClasses">
            {{ message.text }}
        </div>`
})
export class NotificationComponent {

    @Input() message: Message;

    get bootstrapClasses() {
        let classes = 'alert ';
        switch (this.message.alert) {
            case Alert.INFO: classes += 'alert-info'; break;
            case Alert.ERROR: classes += 'alert-danger'; break;
            case Alert.WARNING: classes += 'alert-warning'; break;
            case Alert.SUCCESS: classes += 'alert-success';
        }
        return classes;
    }
}
