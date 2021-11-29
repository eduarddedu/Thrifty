import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Chart } from 'angular-highcharts';

import { Account, RefPeriod, Expense } from '../../../model';
import { Utils } from '../../../util/utils';
import { Charts } from '../../../charts/charts';
import { NotificationService } from '../../../services/notification.service';
import { DataService } from '../../../services/data.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { Report as Report } from '../report';



@Component({
    templateUrl: './account-details.component.html'
})
export class AccountDetailsComponent extends Report implements OnInit {
    entity: Account;
    currency: String;
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
        private ds: DataService) {
        super();
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(() => this.ds.load()))
            .subscribe(this.init.bind(this), err => {
                this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
            });
    }

    init(account: Account) {
        this.entity = account;
        this.currency = account.currency;
        if (this.entity.expenses.length > 0) {
            this.setPeriodOptions();
            this.setCharts();
            this.setSize();
            this.setSpent();
            this.setSince();
            this.setExpenses();
        }
        this.dataReady = true;
    }

    onSelectPeriod(index: number) {
        this.refPeriod = this.options[index].value;
        this.setSize();
        this.setCharts();
        this.setSpent();
        this.setSince();
        this.setExpenses();
    }

    setCharts() {
        let navigate = function (entity: string, id: number) {
            this.router.navigate(['view', entity, id]);
        };
        navigate = navigate.bind({ router: this.router });
        if (this.refPeriod === 'All time') {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.entity, null, navigate);
            this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.entity);
        } else {
            this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.entity, this.refPeriod, navigate);
            this.columnChart = Charts.getAccountSpendingPerMonthColumnChart(this.entity, this.refPeriod);
        }
    }

    setSize() {
        if (this.refPeriod === 'All time') {
            this.size = this.entity.expenses.length;
            this.sizePercentage = '100%';
        } else {
            this.size = this.entity.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
            this.sizePercentage = Utils.percent(this.entity.expenses.length, this.size);
        }
    }

    setSpent() {
        let spent: number;
        if (this.refPeriod === 'All time') {
            spent = this.entity.balance || 0;
            this.spentPercentage = '100%';
        } else {
            spent = this.entity.mapYearBalance.get(this.refPeriod) || 0;
            this.spentPercentage = Utils.percent(this.entity.balance, spent);
        }
        this.spent = (Math.abs(spent) / 100).toFixed(2);
    }

    setSince() {
        if (this.entity && this.entity.expenses.length > 0) {
            if (this.refPeriod === 'All time') {
                this.since = Utils.localDateToJsDate(this.entity.dateRange.startDate);
            } else {
                const oldestExpenseInYear: Expense =
                    this.entity.expenses.slice().reverse().find(e => e.createdOn.year === this.refPeriod);
                this.since = Utils.localDateToJsDate(oldestExpenseInYear.createdOn);
            }
        }
    }

    get hasExpenses() {
        return this.entity && this.entity.expenses.length > 0;
    }
}
