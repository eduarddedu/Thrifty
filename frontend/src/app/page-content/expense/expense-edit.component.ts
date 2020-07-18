import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { combineLatest } from 'rxjs';

import { RestService } from '../../services/rest.service';
import { MessageService, Kind } from '../../services/messages.service';
import { Expense, Account, RadioOption } from '../../model';
import { ExpenseFormParent } from './expense-form-parent';
import { Utils } from '../../util/utils';

@Component({
    templateUrl: './expense-form.component.html',
})

export class ExpenseEditComponent extends ExpenseFormParent implements OnInit {

    expenseModel: Expense;

    pageTitle = 'Edit expense';

    constructor(
        protected fb: FormBuilder,
        private ms: MessageService,
        private route: ActivatedRoute,
        private rs: RestService) {
        super(fb);
    }

    ngOnInit() {
        combineLatest(this.rs.getAccount(), this.route.queryParams).subscribe(v => {
            const account = v[0];
            this.expenseModel = account.expenses.find(ex => ex.id === +v[1].expenseId);
            this.setFormWithModelValues();
            this.setRadioOptionsLabel(account);
            this.selectedLabels = [].concat(this.expenseModel.labels);
            this.setRadioOptionsCategory(account);
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        const expense: Expense = Object.assign(this.readFormData(), {
            id: this.expenseModel.id,
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rs.updateExpense(expense).subscribe(
            () => this.notificationMessage = this.ms.get(Kind.EXPENSE_EDIT_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }

    private setFormWithModelValues() {
        this.expenseForm.patchValue({
            date: { jsdate: Utils.localDateToJsDate(this.expenseModel.createdOn) },
            description: this.expenseModel.description,
            amount: this.expenseModel.amount
        });
    }

    private setRadioOptionsLabel(account: Account) {
        const map: Map<number, RadioOption> = new Map();
        account.labels.forEach(label => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: false
        }));
        this.expenseModel.labels.forEach(label => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: true
        }));
        this.radioOptionsLabel = Array.from(map.values());
    }

    private setRadioOptionsCategory(account: Account) {
        account.categories.forEach(category => {
            const option = {
                id: category.id,
                name: category.name,
                description: category.description,
                checked: category.id === this.expenseModel.category.id ? true : false
            };
            this.radioOptionsCategory.push(<RadioOption>option);
            if (option.checked) {
                this.selectedCategory = {id: category.id, name: category.name, description: category.description};
            }
        });
    }
}

