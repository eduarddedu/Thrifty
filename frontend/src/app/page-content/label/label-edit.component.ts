import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';

import { Label } from '../../model';
import { LabelFormParent } from './label-form-parent';
import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';

@Component({
    templateUrl: './label-form.component.html'
})
export class LabelEditComponent extends LabelFormParent implements OnInit {

    pageTitle = 'Edit label';

    id: number;

    sourceLabel: Label;

    constructor(protected fb: FormBuilder, private route: ActivatedRoute, private rs: RestService, private ms: MessageService) {
        super(fb);
    }

    ngOnInit() {
        this.route.queryParams.pipe(switchMap(params => {
            this.id = +params.id;
            return this.rs.getAccount();
        })).subscribe((account: Account) => {
            this.sourceLabel = account.labels.find(label => this.id === label.id);
            this.forbiddenNames = account.labels.filter(label => this.id  !== label.id).map(l => l.name);
            this.createForm();
            this.labelForm.patchValue({name: this.sourceLabel.name});
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        const newLabel = Object.assign({id: this.id}, this.readFormData());
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        this.rs.updateLabel(newLabel).subscribe(() => this.notificationMessage = this.ms.get(Kind.LABEL_EDIT_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }

}
