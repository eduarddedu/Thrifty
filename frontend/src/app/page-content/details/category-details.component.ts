import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { DetailsComponentParent } from './details-component-parent';
import { Category, Account, Expense } from '../../model';
import { RestService } from '../../services/rest.service';
import { ChartsService } from '../../services/charts.service';
import { MessageService, Kind, Message } from '../../services/messages.service';
import { Utils } from '../../util/utils';


@Component({
    templateUrl: './details.component.html',
    styles: [`button {margin-bottom: 7px}`]
})
export class CategoryDetailsComponent extends DetailsComponentParent implements OnInit {

    category: Category;

    viewType = 'category';

    deleteActionConcernsCategory = true;

    showEditDeleteCategoryButtons = false;

    constructor(
        private rs: RestService,
        private ms: MessageService,
        private charts: ChartsService,
        private route: ActivatedRoute,
        private router: Router) {
        super();
    }

    ngOnInit(): void {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.rs.getAccount();
        })).subscribe(this.resetPageContent.bind(this), err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private resetPageContent(account: Account) {
        if (account.other && this.categoryId === account.other.id) {
            this.category = account.other;
            this.hasCharts = false;
        } else {
            this.showEditDeleteCategoryButtons = true;
            this.category = account.categories.find(c => c.id === this.categoryId);
            this.mapYearBalance = this.toMap(this.category.mapYearBalance);
            this.hasCharts = this.category.labels.length > 0;
            if (this.category.expenses.length > 0) {
                this.activeSince = this.getEarliestExpenseDate(this.category.expenses);
            }
            if (!this.category) {
                this.router.navigate(['/not-found']);
            } else if (this.hasCharts) {
                this.pieChart = this.charts.spendingByLabelPieChart(this.category);
                this.setSelectorOptions();
                this.setColumnChart('All time');
            }
        }
        this.resetNotification();
        Utils.scrollPage();
        this.dataReady = true;
    }

    private resetNotification() {
        this.showNotification = false;
    }

    private setColumnChart(option: number | 'All time') {
        if (option === 'All time') {
            this.columnChart = this.charts.yearlyTotalSpending(this.mapYearBalance);
        } else {
            this.columnChart = this.charts.monthlySpendingByLabelColumnChart(this.category, +option);
        }
    }

    private getEarliestExpenseDate(expenses: Expense[]) {
        const earliestLocalDate = expenses.reduce((prevExp, currExp) => prevExp <= currExp ? prevExp : currExp).createdOn;
        return Utils.localDateToJsDate(earliestLocalDate);
    }

    onClickDeleteCategory() {
        this.modalMessage = this.ms.get(Kind.CATEGORY_DELETE_WARN);
        this.deleteActionConcernsCategory = true;
        this.showModal = true;
    }

    onClickEditCategory() {
        this.router.navigate(['edit/category'], {queryParams: {id: this.categoryId}});
    }

    onSelectOption(selectedIndex) {
        this.setColumnChart(this.selectOptions[selectedIndex]);
    }

    onClickDeleteExpense(selectedId) {
        this.selectedExpenseId = selectedId;
        this.deleteActionConcernsCategory = false;
        this.modalMessage = this.ms.get(Kind.EXPENSE_DELETE_WARN);
        this.showModal = true;
    }

    onConfirmDelete() {
        this.dataReady = false;
        this.showModal = false;
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        let action: Observable<any>, successMessage: Message;
        if (this.deleteActionConcernsCategory) {
            action = this.rs.deleteCategory(this.category.id);
            successMessage = this.ms.get(Kind.CATEGORY_DELETE_OK);
        } else {
            action = this.rs.deleteExpense(this.selectedExpenseId);
            successMessage = this.ms.get(Kind.EXPENSE_DELETE_OK);
        }
        action.subscribe(() => {
            this.notificationMessage = successMessage;
            Utils.scrollPage();
        }, err => {
            this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR);
            Utils.scrollPage();
        });
    }
}
