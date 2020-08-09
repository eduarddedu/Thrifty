import { Component, OnInit } from '@angular/core';

import { RestService } from '../../services/rest.service';
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

    categoryId: number;

    activeSince: Date;

    selectedExpenseId: number;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dataReady = false;

    constructor(
        private rest: RestService,
        private ms: MessageService,
        private analytics: AnalyticsService) {
        this.categoryId = 0;
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe(this.init.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private init(account: Account) {
        this.account = account;
        if (this.account.expenses.length > 0) {
            this.activeSince = Utils.localDateToJsDate(account.dateRange.startDate);
        }
        this.dataReady = true;
    }

    onClickDeleteExpense(selectedId: number) {
        this.selectedExpenseId = selectedId;
        this.modalMessage = this.ms.get(Kind.EXPENSE_DELETE_WARN);
        this.showModal = true;
    }

    onConfirmDelete() {
        this.dataReady = false;
        this.showModal = false;
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.rest.deleteExpense(this.selectedExpenseId).subscribe(() => {
            Utils.scrollPage();
            this.notificationMessage = this.ms.get(Kind.EXPENSE_DELETE_OK);
        });

    }
}
