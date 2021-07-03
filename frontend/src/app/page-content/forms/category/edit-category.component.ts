import { zip } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { AccountService } from '../../../services/account.service';
import { Account, Category } from '../../../model';
import { CategoryForm } from './category-form';


@Component({
    templateUrl: './category-form.component.html',
})
export class EditCategoryComponent extends CategoryForm implements OnInit {
    pageTitle = 'Update Category';
    category: Category;

    constructor(protected fb: FormBuilder,
        private route: ActivatedRoute,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: AccountService) {
        super(fb);
    }

    ngOnInit() {
        zip(this.route.paramMap, this.accountService.loadAccount()).subscribe(value => {
            const params: ParamMap = value[0];
            const account: Account = value[1];
            this.category = account.categories.find(c => c.id === +params.get('id'));
            this.forbiddenNames = account.categories.filter(c => c.id !== this.category.id).map(c => c.name);
            this.createForm();
            this.form.patchValue({ name: this.category.name, description: this.category.description });
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const category = Object.assign({ id: this.category.id, accountId:  this.category.accountId}, this.readFormData());
        this.rest.updateCategory(category).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.CATEGORY_EDIT_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }
}
