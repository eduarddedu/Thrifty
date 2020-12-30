import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Chart } from 'angular-highcharts';

import { Account, RefPeriod } from '../../../model';
import { Utils } from '../../../util/utils';
import { Charts } from '../../../charts/charts';
import { NotificationService } from '../../../services/notification.service';
import { AnalyticsService } from '../../../services/analytics.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { Timespan } from '../timespan';



@Component({
    templateUrl: './account-details.component.html'
})
export class AccountDetailsComponent extends Timespan implements OnInit {
    account: Account;
    pieChart: Chart;
    columnChart: Chart;
    dataReady = false;
    refPeriod: RefPeriod;
    size: number;
    spent: string;
    sizePercentage: string;
    spentPercentage: string;
    since: Date;

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
            this.setOptions(this.account.yearsSeries);
            this.refPeriod = this.options.find(o => o.selected).value;
            this.setCharts();
            this.setSize();
            this.setSpent();
            this.setSince();
        }
        this.dataReady = true;
    }

    onSelectPeriod(index: number) {
        this.refPeriod = this.options[index].value;
        this.setSize();
        this.setCharts();
        this.setSpent();
        this.setSince();
    }

    setCharts() {
        let navigate = function (entity: string, id: number) {
            this.router.navigate(['view', entity, id]);
        };
        navigate = navigate.bind({ router: this.router });
        if (this.refPeriod === 'All time') {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, null, navigate);
            this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.account);
        } else {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, this.refPeriod, navigate);
            this.columnChart = Charts.getAccountSpendingPerMonthColumnChart(this.account, this.refPeriod);
        }
    }

    setSize() {
        if (this.refPeriod === 'All time') {
            this.size = this.account.expenses.length;
            this.sizePercentage = '100%';
        } else {
            this.size = this.account.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
            this.sizePercentage = Utils.percent(this.account.expenses.length, this.size);
        }
    }

    setSpent() {
        let spent: number;
        if (this.refPeriod === 'All time') {
            spent = this.account.balance || 0;
            this.spentPercentage = '100%';
        } else {
            spent = this.account.mapYearBalance.get(this.refPeriod) || 0;
            this.spentPercentage = Utils.percent(this.account.balance, spent);
        }
        this.spent = '-' + (Math.abs(spent) / 100).toFixed(2) + ' lei ';
    }

    setSince() {
        if (this.account && this.account.expenses.length > 0) {
            if (this.refPeriod === 'All time') {
                this.since = Utils.localDateToJsDate(this.account.dateRange.startDate);
            } else {
                this.since = Utils.localDateToJsDate({ year: this.refPeriod, month: 1, day: 1 });
            }
        }
    }
}
