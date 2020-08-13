import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { Router } from '@angular/router';
import { Chart } from 'angular-highcharts';


import { AnalyticsService } from '../services/analytics.service';
import { Account, Category, Label, Expense } from '../model';
import { Charts } from './charts.api';
import { Utils } from '../util/utils';

@Component({
  selector: 'app-charts-component',
  templateUrl: './charts-component.component.html',
  styleUrls: ['./charts-component.component.css']
})
export class ChartsComponent implements OnInit, OnChanges {
  private account: Account;
  private columnChart: Chart;
  private pieChart: Chart;
  @Input() viewType: 'account' | 'category' | 'label';
  @Input() category: Category;
  @Input() label: Label;
  @Input() chartType: 'pieChart' | 'columnChart';

  private periodOptions: Array<{ value: any, selected: boolean }>;

  constructor(private analytics: AnalyticsService, private router: Router) { }

  ngOnInit() {
    this.analytics.loadAccount().subscribe(account => {
      this.account = account;
      this.init();
    });
  }

  private init() {
    if (!this.account) {
      return;
    }
    if (this.category) {
      this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category, this.router);
      this.columnChart = Charts.getCategorySpendingPerYearColumnChart(this.category);
    } else if (this.label) {
      this.pieChart = Charts.getLabelSpendingPerCategoryPieChart(this.label, this.router);
      this.columnChart = Charts.getLabelSpendingPerYearColumnChart(this.label);
    } else {
      this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, this.router);
      this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.account);
    }
    this.setPeriodOptions();
  }

  ngOnChanges() {
    this.init();
  }

  private setPeriodOptions() {
    const expenses: Expense[] = this.label ? this.label.expenses : this.category ? this.category.expenses : this.account.expenses;
    this.periodOptions = [{ value: 'All time', selected: false }];
    const years: number[] = Utils.getYearsSeries(expenses).reverse();
    if (years.length > 0) {
      const maxYear = years[0];
      for (const year of years) {
        this.periodOptions.push({
          value: year,
          selected: year === maxYear ? true : false
        });
      }
      this.onSelectPeriod(1);
    }
  }

  onSelectPeriod(index: number) {
    const selector = this.periodOptions[index].value;
    if (this.category) {
      this.columnChart = selector === 'All time' ?
        Charts.getCategorySpendingPerYearColumnChart(this.category) :
        Charts.getCategorySpendingPerMonthColumnChart(selector, this.category);
    } else if (this.label) {
      this.columnChart = selector === 'All time' ?
        Charts.getLabelSpendingPerYearColumnChart(this.label) :
        Charts.getLabelSpendingPerMonthColumnChart(selector, this.label);
    } else {
      this.columnChart = selector === 'All time' ?
        Charts.getAccountSpendingPerYearColumnChart(this.account) :
        Charts.getAccountSpendingPerMonthColumnChart(selector, this.account);
    }
  }

}
