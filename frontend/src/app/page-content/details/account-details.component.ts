import { Component, OnInit } from '@angular/core';

import { DetailsComponentParent } from './details-component-parent';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { MessageService, Kind } from '../../services/messages.service';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
    templateUrl: './details.component.html'
})
export class AccountDetailsComponent extends DetailsComponentParent implements OnInit {

    account: Account;

    viewType = 'account';

    showEditDeleteCategoryButtons = false;

    constructor(
        private rest: RestService,
        private ms: MessageService,
        private analytics: AnalyticsService) {
        super();
        this.categoryId = 0;
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe(this.setPageContent.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private setPageContent(account: Account) {
        this.account = account;
        if (this.account.expenses.length > 0 && account.categories.length > 0) {
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
