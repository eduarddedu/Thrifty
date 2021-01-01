import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'angular-highcharts';

import { Category, Account, RefPeriod } from '../../../model';
import { Kind, AppMessage } from '../../../model/app-message';
import { NotificationService } from '../../../services/notification.service';
import { AnalyticsService } from '../../../services/analytics.service';
import { DeleteEntityModalService } from '../../../services/modal.service';
import { Utils } from '../../../util/utils';
import { Charts } from '../../../charts/charts';
import { Timespan } from '../timespan';

@Component({
    templateUrl: './category-details.component.html'
})
export class CategoryDetailsComponent extends Timespan implements OnInit {

    account: Account;
    category: Category;
    categoryId: number;
    dataReady = false;
    pieChart: Chart;
    columnChart: Chart;
    refPeriod: RefPeriod;
    size: number;
    spent: string;
    sizePercentage: string;
    spentPercentage: string;
    since: Date;

    constructor(
        private analytics: AnalyticsService,
        private ns: NotificationService,
        private ms: DeleteEntityModalService,
        private route: ActivatedRoute,
        private router: Router) {
        super();
    }

    ngOnInit(): void {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe(this.init.bind(this), err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    init(account: Account) {
        this.account = account;
        this.category = account.categories.find(c => c.id === this.categoryId);
        this.setOptions(this.category.yearsSeries);
        this.refPeriod = this.options.find(o => o.selected).value;
        this.setCharts();
        this.setSpent();
        this.setSize();
        this.dataReady = true;
    }

    onSelectPeriod(index: number) {
        this.refPeriod = this.options[index].value;
        this.setSpent();
        this.setSize();
        this.setCharts();
    }

    setSize() {
        if (this.refPeriod === 'All time') {
            this.size = this.category.expenses.length;
            this.sizePercentage = Utils.percent(this.account.expenses.length, this.size);
        } else {
            this.size = this.category.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
            const base = this.account.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
            this.sizePercentage = Utils.percent(base, this.size);
        }
    }

    setSpent() {
        let spent: number;
        if (this.refPeriod === 'All time') {
            spent = this.category.balance;
            this.spentPercentage = Utils.percent(this.account.balance, spent);
        } else {
            spent = this.category.mapYearBalance.get(this.refPeriod);
            const base = this.account.mapYearBalance.get(this.refPeriod);
            this.spentPercentage = Utils.percent(base, spent);
        }
        this.spent = '-' + (Math.abs(spent) / 100).toFixed(2) + ' lei ';
    }

    setCharts() {
        let navigate = function (entity: string, id: number) {
            this.router.navigate(['view', entity, id]);
        };
        navigate = navigate.bind({ router: this.router });
        if (this.refPeriod === 'All time') {
            this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category, null, navigate);
            this.columnChart = Charts.getCategorySpendingPerYearColumnChart(this.category);
        } else {
            this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category, this.refPeriod, navigate);
            this.columnChart = Charts.getCategorySpendingPerMonthColumnChart(this.category, this.refPeriod);
        }
    }

    onClickEditCategory() {
        this.router.navigate(['edit/category', this.categoryId]);
    }

    onClickDeleteCategory() {
        this.ms.pushMessage(AppMessage.of(Kind.CATEGORY_DELETE_WARN));
        this.ms.onConfirmDelete(this.onConfirmDeleteCategory.bind(this));
    }

    onConfirmDeleteCategory() {
        this.router.navigate(['delete/category', this.categoryId]);
    }

}
