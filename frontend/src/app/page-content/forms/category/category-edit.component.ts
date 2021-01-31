import { switchMap } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { AnalyticsService } from '../../../services/analytics.service';
import { Account, Category, Label, RadioOption } from '../../../model';
import { CategoryForm } from './category-form';


@Component({
    templateUrl: './category-form.component.html',
})
export class CategoryEditComponent extends CategoryForm implements OnInit {

    categoryId: number;

    pageTitle = 'Update Category';

    constructor(protected fb: FormBuilder,
        private route: ActivatedRoute,
        private rest: RestService,
        private ns: NotificationService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(params => {
            this.categoryId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe((account: Account) => {
            const category = account.categories.find(c => c.id === this.categoryId);
            const categoryNames = account.categories.map(c => c.name);
            this.forbiddenNames = categoryNames.filter(name => name !== category.name);
            this.setRadioSelectorOptions(account, category);
            this.selectedLabels = [].concat(category.labels);
            this.createForm();
            this.form.patchValue({ name: category.name, description: category.description });
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    private setRadioSelectorOptions(account: Account, category: Category) {
        this.resolveCheckedStatus(account, category);
    }

    private resolveCheckedStatus(account: Account, category: Category) {
        const map: Map<number, RadioOption> = new Map();
        account.labels.forEach(label => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: false
        }));
        category.labels.forEach((label: Label) => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: true
        }));
        this.radioOptions = Array.from(map.values());

    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const category = Object.assign({ id: this.categoryId }, this.readFormData());
        this.rest.updateCategory(category).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.CATEGORY_EDIT_OK));
            this.analytics.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }
}
