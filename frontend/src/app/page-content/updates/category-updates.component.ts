import { Component, ViewChild, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { UpdatesFormParent } from './updates-form-parent';



@Component({
    templateUrl: './updates-form.component.html'
})
export class CategoryUpdatesComponent extends UpdatesFormParent implements OnInit {

    formTitle = 'Update categories';

    constructor(private rs: RestService, private router: Router, private ms: MessageService) {
        super();
    }

    ngOnInit() {
        this.rs.getAccount().subscribe((account: Account) => {
            this.setRadioSelectorOptions(account);
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    private setRadioSelectorOptions(account) {
        account.categories.forEach(category => this.radioOptions.push({
            id: category.id,
            name: category.name,
            checked: false
        }));
    }

    handleSelectedAction() {
        switch (this.formAction) {
            case 'New' : this.router.navigate(['new/category']); break;
            case 'Edit' : this.router.navigate(['edit/category'], {queryParams: {id: this.checkedOption.id}}); break;
            case 'Delete' :
            this.showModal = true;
            this.modalMessage = this.ms.get(Kind.LABEL_DELETE_WARN);
        }
    }

    onConfirmDelete() {
        this.showForm = this.showModal = false;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.showNotification = true;
        this.rs.deleteCategory(this.checkedOption.id).subscribe(() => {
            this.notificationMessage = this.ms.get(Kind.CATEGORY_DELETE_OK);
        }, err => {
            this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR);
            this.showNotification = true;
        });
    }
}
