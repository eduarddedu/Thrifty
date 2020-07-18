import { combineLatest } from '../../../../node_modules/rxjs';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account, Category, Label, RadioOption} from '../../model';
import { CategoryFormParent } from './category-form-parent';


@Component({
    templateUrl: './category-form.component.html',
})
export class CategoryEditComponent extends CategoryFormParent implements OnInit {

    id: number;

    pageTitle = 'Edit category';

    constructor(protected fb: FormBuilder, private route: ActivatedRoute, private rs: RestService, private ms: MessageService) {
        super(fb);
    }

    ngOnInit() {
        combineLatest(this.route.queryParams, this.rs.getAccount()).subscribe(v => {
            this.id = +v[0].id;
            const account = v[1];
            const category = account.categories.find(c => c.id === this.id);
            const categoryNames = account.categories.map(c => c.name);
            this.forbiddenNames = categoryNames.filter (name => name !== category.name);
            this.setRadioSelectorOptions(account, category);
            this.selectedLabels = [].concat(category.labels);
            this.createForm();
            this.categoryForm.patchValue({name: category.name, description: category.description});
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
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
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        const category = Object.assign({id: this.id}, this.readFormData());
        this.rs.updateCategory(category).subscribe(() =>
            this.notificationMessage = this.ms.get(Kind.CATEGORY_EDIT_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }
}
