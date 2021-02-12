import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Chart } from 'angular-highcharts';

import { Account, Label, RefPeriod } from '../../../model';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { Utils } from '../../../util/utils';
import { AnalyticsService } from '../../../services/analytics.service';
import { DeleteEntityModalService } from '../../../services/modal.service';
import { Charts } from '../../../charts/charts';
import { Report } from '../report';

@Component({
  templateUrl: './label-details.component.html'
})
export class LabelDetailsComponent extends Report implements OnInit {
  account: Account;
  labelId: number;
  entity: Label;
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
    this.entity = account.labels.find(label => label.id === this.labelId);
    this.setPeriodOptions();
    this.setCharts();
    this.setSize();
    this.setSpent();
    this.setExpenses();
    this.dataReady = true;
  }

  onSelectPeriod(index: number) {
    this.refPeriod = this.options[index].value;
    this.setCharts();
    this.setSize();
    this.setSpent();
    this.setExpenses();
  }

  setSpent() {
    let spent: number;
    if (this.refPeriod === 'All time') {
      spent = this.entity.balance;
      this.spentPercentage = Utils.percent(this.account.balance, spent);
    } else {
      spent = this.entity.mapYearBalance.get(this.refPeriod) || 0;
      const base = this.account.mapYearBalance.get(this.refPeriod) || 0;
      this.spentPercentage = Utils.percent(base, spent);
    }
    this.spent = (Math.abs(spent) / 100).toFixed(2);
  }

  setSize() {
    if (this.refPeriod === 'All time') {
      this.size = this.entity.expenses.length;
      this.sizePercentage = Utils.percent(this.account.expenses.length, this.size);
    } else {
      this.size = this.entity.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
      const base = this.account.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
      this.sizePercentage = Utils.percent(base, this.size);
    }
  }

  setCharts() {
    let navigate = function (entity: string, id: number) {
      this.router.navigate(['view', entity, id]);
    };
    navigate = navigate.bind({ router: this.router });
    if (this.refPeriod === 'All time') {
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.entity, null, navigate);
      this.columnChart = Charts.getLabelSpendingPerYearColumnChart(this.entity);
    } else {
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.entity, this.refPeriod, navigate);
      this.columnChart = Charts.getLabelSpendingPerMonthColumnChart(this.entity, this.refPeriod);
    }
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

  get hasExpenses() {
    return this.entity && this.entity.expenses.length > 0;
  }

}
