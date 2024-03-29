import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { switchMap } from 'rxjs/operators';

import { RestService } from '../../../services/rest.service';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { Expense, ExpenseData, Account, Category, RadioOption } from '../../../model';
import { ExpenseForm } from './expense-form';
import { Utils } from '../../../util/utils';
import { DataService } from '../../../services/data.service';

@Component({
    templateUrl: './expense-form.component.html',
    styleUrls: ['./expense-form.component.css']
})
export class EditExpenseComponent extends ExpenseForm implements OnInit {
    pageTitle = 'Update Expense';
    submitFormButtonText = 'Update';
    expenseId: number;
    expense: Expense;
    account: Account;

    constructor(
        protected fb: FormBuilder,
        private ns: NotificationService,
        private route: ActivatedRoute,
        private rest: RestService,
        private ds: DataService) {
        super(fb);
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(params => {
            this.expenseId = +params.get('id');
            return this.ds.load();
        })).subscribe((account: Account) => {
            this.account = account;
            this.expense = account.expenses.find(ex => ex.id === this.expenseId);
            this.fillForm();
            this.setLabelOptions();
            this.selectedLabels = this.expense.labels.map(l => ({ id: l.id, name: l.name, accountId: this.account.id }));
            this.showSpinner = false;
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const expense: ExpenseData = Object.assign(this.readFormData(), {
            id: this.expense.id,
            accountId: this.account.id,
            labels: this.selectedLabels,
            category: {
                id: this.selectedCategory.id,
                name: this.selectedCategory.name,
                description: this.selectedCategory.description,
                accountId: this.account.id
            }
        });
        this.rest.updateExpense(expense).subscribe(
            () => {
                this.ns.push(AppMessage.of(Kind.EXPENSE_EDIT_OK));
                this.ds.reload();
            },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

    private fillForm() {
        this.form.patchValue({
            createdOn: { jsdate: Utils.localDateToJsDate(this.expense.createdOn) },
            description: this.expense.description,
            amount: (this.expense.cents / 100).toFixed(2),
            category: this.expense.category.name
        });
    }

    private get selectedCategory(): Category {
        return this.account.categories.find(c => c.name === this.form.get('category').value);
    }

    private onSelectCategory(event: any) {
        this.form.patchValue({ category: event.target.value });
    }

    private setLabelOptions() {
        const map: Map<number, RadioOption> = new Map();
        this.account.labels.forEach(l => map.set(l.id, { id: l.id, name: l.name, accountId: l.accountId, checked: false }));
        this.expense.labels.forEach(l => map.get(l.id).checked = true);
        this.radioOptionsLabel = Array.from(map.values());
    }
}

