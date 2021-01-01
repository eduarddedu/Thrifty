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
import { Timespan } from '../timespan';

@Component({
  templateUrl: './label-details.component.html'
})
export class LabelDetailsComponent extends Timespan implements OnInit {
  account: Account;
  labelId: number;
  label: Label;
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
    this.label = account.labels.find(label => label.id === this.labelId);
    this.setOptions(this.label.yearsSeries);
    this.refPeriod = this.options.find(o => o.selected).value;
    this.setCharts();
    this.setSize();
    this.setSpent();
    this.dataReady = true;
  }

  onSelectPeriod(index: number) {
    this.refPeriod = this.options[index].value;
    this.setCharts();
    this.setSize();
    this.setSpent();
  }

  setSpent() {
    let spent: number;
    if (this.refPeriod === 'All time') {
      spent = this.label.balance;
      this.spentPercentage = Utils.percent(this.account.balance, spent);
    } else {
      spent = this.label.mapYearBalance.get(this.refPeriod) || 0;
      const base = this.account.mapYearBalance.get(this.refPeriod) || 0;
      this.spentPercentage = Utils.percent(base, spent);
    }
    this.spent = '-' + (Math.abs(spent) / 100).toFixed(2) + ' lei ';
  }

  setSize() {
    if (this.refPeriod === 'All time') {
      this.size = this.label.expenses.length;
      this.sizePercentage = Utils.percent(this.account.expenses.length, this.size);
    } else {
      this.size = this.label.expenses.filter(e => e.createdOn.year === this.refPeriod).length;
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
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.label, null, navigate);
      this.columnChart = Charts.getLabelSpendingPerYearColumnChart(this.label);
    } else {
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.label, this.refPeriod, navigate);
      this.columnChart = Charts.getLabelSpendingPerMonthColumnChart(this.label, this.refPeriod);
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

}
