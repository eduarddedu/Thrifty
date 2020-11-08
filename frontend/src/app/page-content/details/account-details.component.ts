import { Component, OnInit } from '@angular/core';

import { Account } from '../../model';
import { MessageService, Kind, Message } from '../../services/messages.service';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
    templateUrl: './details.component.html'
})
export class AccountDetailsComponent implements OnInit {

    account: Account;

    viewType = 'account';

    activeSince: Date;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dataReady = false;

    constructor(private ms: MessageService, private analytics: AnalyticsService) {
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe(this.init.bind(this), err => {
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
            this.showNotification = true;
        });
    }

    private init(account: Account) {
        this.account = account;
        if (this.account.expenses.length > 0) {
            this.activeSince = Utils.localDateToJsDate(account.dateRange.startDate);
        }
        this.dataReady = true;
    }
}
