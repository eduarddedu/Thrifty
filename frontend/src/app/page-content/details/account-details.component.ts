import { Component, OnInit } from '@angular/core';

import { DetailsComponentParent } from './details-component-parent';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { MessageService, Kind } from '../../services/messages.service';
import { ChartsService } from '../../services/charts.service';
import { Utils } from '../../util/utils';

@Component({
    templateUrl: './details.component.html'
})
export class AccountDetailsComponent extends DetailsComponentParent implements OnInit {

    account: Account;

    viewType = 'account';

    showEditDeleteCategoryButtons = false;

    constructor(
        private rs: RestService,
        private ms: MessageService,
        private charts: ChartsService) {
        super();
        this.categoryId = 0;
    }

    ngOnInit() {
        this.rs.getAccount().subscribe(this.setPageContent.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private setPageContent(account: Account) {
        this.account = account;
        this.mapYearBalance = this.toMap(account.mapYearBalance);
        if (this.account.expenses.length > 0 && account.categories.length > 0) {
            this.activeSince = Utils.localDateToJsDate(account.dateRange.startDate);
            this.hasCharts = true;
            this.pieChart = this.charts.getSharePerCategory(
                account.other ? account.categories.concat(account.other) : account.categories);
            this.setSelectorOptions();
            this.setColumnChart('All time');
        } else {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.EMPTY_ACCOUNT_ERROR);
        }
        this.dataReady = true;
    }

    private setColumnChart(option: number | 'All time') {
        this.columnChart = option === 'All time' ?
        this.charts.yearlyTotalSpending(this.mapYearBalance)
        :
        this.charts.monthlySharePerCategory(this.account, +option);
    }

    onSelectOption(selectedIndex: number | 'All time') {
        this.setColumnChart(this.selectOptions[selectedIndex]);
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
        this.rs.deleteExpense(this.selectedExpenseId).subscribe(() => {
            Utils.scrollPage();
            this.notificationMessage = this.ms.get(Kind.EXPENSE_DELETE_OK);
        });

    }
}
