import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LabelFormParent } from './label-form-parent';
import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';


@Component({
    templateUrl: './label-form.component.html',
    styles: []
})
export class LabelCreateComponent extends LabelFormParent implements OnInit {

    pageTitle = 'New label';

    constructor(protected fb: FormBuilder, private rs: RestService, private ms: MessageService) {
        super(fb);
    }

    ngOnInit() {
        this.rs.getAccount().subscribe((account: Account) => {
            this.forbiddenNames = account.labels.map(l => l.name);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });

    }

    onSubmit() {
        this.showForm = false;
        const newLabel = this.readFormData();
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.rs.createLabel(newLabel).subscribe(() => this.notificationMessage = this.ms.get(Kind.LABEL_CREATE_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }
}
