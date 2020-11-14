import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

import { AppMessage } from '../model/app-message';


@Injectable()
export class NotificationService {

    private subject: Subject<AppMessage> = new Subject();

    public push(message: AppMessage) {
        this.subject.next(message);
    }

    public subscribe(observer: (message: AppMessage) => void) {
        return this.subject.subscribe(observer);
    }
}
