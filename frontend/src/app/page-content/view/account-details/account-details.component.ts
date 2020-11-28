import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Chart } from 'angular-highcharts';

import { Account } from '../../../model';
import { Utils } from '../../../util/utils';
import { Charts } from '../../../charts/charts';
import { NotificationService } from '../../../services/notification.service';
import { AnalyticsService } from '../../../services/analytics.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { PeriodSelector } from '../period-selector';

type RefPeriod = number | 'All time';

@Component({
    templateUrl: './account-details.component.html'
})
export class AccountDetailsComponent extends PeriodSelector implements OnInit {

    account: Account;
    activeSince: Date;
    pieChart: Chart;
    columnChart: Chart;
    dataReady = false;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private ns: NotificationService,
        private analytics: AnalyticsService) {
        super();
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(() => this.analytics.loadAccount()))
            .subscribe(this.init.bind(this), err => {
                this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
            });
    }

    init(account: Account) {
        this.account = account;
        if (this.account.expenses.length > 0) {
            this.activeSince = Utils.localDateToJsDate(this.account.dateRange.startDate);
            this.setSelectOptions(this.account.yearsSeries);
            this.setCharts(this.account.dateRange.endDate.year);
        }
        this.dataReady = true;
    }

    setCharts(refPeriod: RefPeriod) {
        let navigate = function (entity: string, id: number) {
            this.router.navigate(['view', entity, id]);
        };
        navigate = navigate.bind({ router: this.router });
        if (typeof refPeriod === 'number') {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, refPeriod, navigate);
            this.columnChart = Charts.getAccountSpendingPerMonthColumnChart(this.account, refPeriod);
        } else {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, null, navigate);
            this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.account);
        }
    }

    onSelectPeriod(index: number) {
        const refPeriod = this.selectorOptions[index].value;
        this.setCharts(refPeriod);
    }

    get totalSpentStr() {
        return (Math.abs(this.account.balance) / 100).toFixed(2) + ' lei';
    }
}
