import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'angular-highcharts';

import { Expense, Account, Label } from '../../../model';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { Utils } from '../../../util/utils';
import { AnalyticsService } from '../../../services/analytics.service';
import { DeleteEntityModalService } from '../../../services/modal.service';
import { Charts } from '../../../charts/charts';
import { PeriodSelector } from '../period-selector';

type RefPeriod = number | 'All time';

@Component({
  templateUrl: './label-details.component.html'
})
export class LabelDetailsComponent extends PeriodSelector implements OnInit {

  account: Account;
  labelId: number;
  label: Label;
  dataReady = false;
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
      this.labelId = +params.get('id');
      return this.analytics.loadAccount();
    })).subscribe(this.init.bind(this), err => {
      this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
    });
  }

  private init(account: Account) {
    this.account = account;
    this.label = account.labels.find(label => label.id === this.labelId);
    this.setSelectOptionsAndCharts();
    this.dataReady = true;
  }

  setSelectOptionsAndCharts() {
    const years = this.label.yearsSeries;
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
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.label, refPeriod, navigate);
      this.columnChart = Charts.getLabelSpendingPerMonthColumnChart(this.label, refPeriod);
    } else {
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.label, null, navigate);
      this.columnChart = Charts.getLabelSpendingPerYearColumnChart(this.label);
    }
  }

  extractDate(expense: Expense): Date {
    return Utils.localDateToJsDate(expense.createdOn);
  }

  onClickEditLabel() {
    this.router.navigate(['edit/label', this.labelId]);
  }

  onClickDeleteLabel() {
    this.ms.pushMessage(AppMessage.of(Kind.LABEL_DELETE_WARN));
    this.ms.onConfirmDelete(this.onConfirmDeleteLabel.bind(this));
  }

  onConfirmDeleteLabel() {
    this.router.navigate(['delete/label', this.labelId]);
  }

  onSelectPeriod(index: number) {
    const refPeriod = this.selectorOptions[index].value;
    this.setCharts(refPeriod);
  }

  get totalSpentStr() {
    return (Math.abs(this.label.balance) / 100).toFixed(2) + ' lei';
  }

  get percentage() {
    const base = this.account.expenses.length;
    if (base === 0) {
        return 0;
    }
    const fr = this.label.expenses.length;
    return (fr * 100 / base).toFixed(2);
}

}
