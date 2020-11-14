import { Component, OnInit } from '@angular/core';

import { NotificationService } from '../services/notification.service';
import { AppMessage } from '../model/app-message';
import { Utils } from '../util/utils';

@Component({
    templateUrl: './page.component.html',
    styleUrls: ['./page.component.css']
})

export class PageComponent implements OnInit {
    private message: AppMessage;

    constructor(private ns: NotificationService) {}

    ngOnInit() {
        this.ns.subscribe((message: AppMessage) => this.message = message);
    }

    onActivateRoute() {
        Utils.scrollPage();
        this.ns.push(null);
    }
}
