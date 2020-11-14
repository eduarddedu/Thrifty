import { Component, OnInit } from '@angular/core';

import { AppMessage, Alert } from '../model/app-message';
import { NotificationService } from '../services/notification.service';

@Component({
    selector: 'app-notification',
    template: `
        <div *ngIf="message" >
            <p [class]="bootstrapClasses">{{ message.text }}</p>
        </div>`
})
export class NotificationComponent implements OnInit {

    message: AppMessage;

    constructor(private ns: NotificationService) {}

    ngOnInit() {
        this.ns.subscribe(((message: AppMessage) => {
            this.message = message;
        }));
    }

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
