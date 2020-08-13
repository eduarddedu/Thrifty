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

  private periodOptions: Array<'All time' | number>;

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
    this.periodOptions = ['All time'];
    Utils.getYearsSeries(expenses).reverse().forEach(year => this.periodOptions.push(year));
  }


  onSelectPeriod(index: number) {
    const selectedValue = this.periodOptions[index];
    if (selectedValue === 'All time') {
      this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.account);
    } else {
      if (this.category) {
        this.columnChart = Charts.getCategorySpendingPerMonthColumnChart(selectedValue, this.category);
      } else {
        this.columnChart = Charts.getAccountSpendingPerMonthColumnChart(selectedValue, this.account);
      }
    }
  }

}
