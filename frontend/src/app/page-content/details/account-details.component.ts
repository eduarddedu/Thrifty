import { Component, OnInit } from '@angular/core';

import { Account } from '../../model';
import { NotificationService } from '../../services/notification.service';
import { Kind, AppMessage } from '../../model/app-message';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
    templateUrl: './z-details.component.html'
})
export class AccountDetailsComponent implements OnInit {

    account: Account;

    viewType = 'account';

    activeSince: Date;

    dataReady = false;

    constructor(private ns: NotificationService, private analytics: AnalyticsService) {
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe(this.init.bind(this), err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
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
