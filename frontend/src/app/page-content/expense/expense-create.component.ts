import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { combineLatest } from 'rxjs';

import { RestService } from '../../services/rest.service';
import { MessageService, Kind } from '../../services/messages.service';
import { Expense, Account, Category, RadioOption } from '../../model';
import { ExpenseFormParent } from './expense-form-parent';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './expense-form.component.html',
})
export class ExpenseCreateComponent extends ExpenseFormParent implements OnInit {
    pageTitle = 'Create Expense';
    submitFormButtonText = 'Save';
    category: Category;

    constructor(
        protected fb: FormBuilder,
        private messages: MessageService,
        private route: ActivatedRoute,
        private rest: RestService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        combineLatest(this.analytics.loadAccount(), this.route.queryParams).subscribe(v => {
            const account = v[0];
            this.category = account.categories.find(c => c.id === +v[1].categoryId);
            this.setRadioOptionsLabel(account);
            this.setRadioOptionsCategory(account);
            this.expenseForm.patchValue({ date: { jsdate: new Date() } });
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        const expense: Expense = Object.assign(this.readFormData(), {
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rest.createExpense(expense).subscribe(
            () => {
                this.notificationMessage = this.messages.get(Kind.EXPENSE_CREATE_OK);
                this.analytics.reload();
            },
            err => this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR));
    }

    private setRadioOptionsLabel(account: Account) {
        account.labels.forEach(label => this.radioOptionsLabel.push({
            id: label.id,
            name: label.name,
            checked: false
        }));
    }

    private setRadioOptionsCategory(account: Account) {
        account.categories.forEach(category => {
            const option = {
                id: category.id,
                name: category.name,
                description: category.description,
                checked: false
            };
            this.radioOptionsCategory.push(<RadioOption>option);
        });
    }
}
