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
import { PeriodSelector } from '../period-selector';

@Component({
    templateUrl: './category-details.component.html'
})
export class CategoryDetailsComponent extends PeriodSelector implements OnInit {

    account: Account;
    category: Category;
    categoryId: number;
    dataReady = false;
    activeSince: Date;
    pieChart: Chart;
    columnChart: Chart;
    selectorOptions: { value: RefPeriod, selected: boolean }[] = [];

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
        this.setActiveSince();
        this.setSelectOptionsAndCharts();
        this.dataReady = true;
    }

    setActiveSince() {
        const i = this.category.expenses.length - 1;
        if (i >= 0) {
            this.activeSince = Utils.localDateToJsDate(this.category.expenses[i].createdOn);
        }
    }

    setSelectOptionsAndCharts() {
        const years = this.category.yearsSeries;
        this.setSelectOptions(years);
        const period: RefPeriod = years.length > 0 ? years[years.length - 1] : 'All time';
        this.setCharts(period);
    }

    setCharts(refPeriod: RefPeriod) {
        let navigate = function (entity: string, id: number) {
            this.router.navigate(['view', entity, id]);
        };
        navigate = navigate.bind({ router: this.router });
        if (typeof refPeriod === 'number') {
            this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category, refPeriod, navigate);
            this.columnChart = Charts.getCategorySpendingPerMonthColumnChart(this.category, refPeriod);
        } else {
            this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category, null, navigate);
            this.columnChart = Charts.getCategorySpendingPerYearColumnChart(this.category);
        }
    }

    onSelectPeriod(index: number) {
        const refPeriod = this.selectorOptions[index].value;
        this.setCharts(refPeriod);
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

    get totalSpentStr() {
        return (Math.abs(this.category.balance) / 100).toFixed(2) + ' lei';
    }

    get percentage() {
        const base = this.account.expenses.length;
        if (base === 0) {
            return 0;
        }
        const fr = this.category.expenses.length;
        return (fr * 100 / base).toFixed(2);
    }

}
