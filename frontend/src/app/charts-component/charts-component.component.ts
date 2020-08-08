import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Chart } from 'angular-highcharts';


import { AnalyticsService } from '../services/analytics.service';
import { Account, Expense, Category, Label, LocalDate, DateRange } from '../model';
import { Charts } from './charts.api';
import { Utils } from '../util/utils';

@Component({
  selector: 'app-charts-component',
  templateUrl: './charts-component.component.html',
  styleUrls: ['./charts-component.component.css']
})
export class ChartsComponent implements OnInit {
  private account: Account;
  private columnChart: Chart;
  private pieChart: Chart;
  @Input() viewType: 'account' | 'category';
  @Input() category: Category;
  @Input() chartType: 'pieChart' | 'columnChart';

  private selectOptions: Array<'All time' | number>;

  constructor(private analytics: AnalyticsService, private router: Router) { }

  ngOnInit() {
    this.analytics.loadAccount().subscribe(account => {
      this.account = account;
      this.setSelectorOptions();
      if (this.category) {
        this.pieChart = Charts.getCategorySpendingPerLabelPieChart(this.category);
        this.columnChart = Charts.getCategorySpendingPerYearColumnChart(this.category);
      } else {
        this.pieChart = Charts.getAccountSpendingPerCategoryPieChart(this.account, this.router);
        this.columnChart = Charts.getAccountSpendingPerYearColumnChart(this.account);
      }
    });
  }

  private setSelectorOptions() {
    const options: Array<any> = [];
    options.push('All time');
    Utils.getYearsSeries(this.account.expenses).reverse().forEach(year => options.push(year));
    this.selectOptions = options;
  }


  onSelectOption(index: number) {
    const selectedValue = this.selectOptions[index];
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
