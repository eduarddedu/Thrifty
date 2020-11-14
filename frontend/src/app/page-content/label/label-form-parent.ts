import { Validators, FormGroup, AbstractControl, FormBuilder } from '@angular/forms';

import { Label } from '../../model';
import * as MyValidators  from '../../validators/validators';

export class LabelFormParent {

    labelForm: FormGroup;

    forbiddenNames: string[];

    showForm = false;

    constructor(protected fb: FormBuilder) {}

    get name(): AbstractControl {
        return this.labelForm.get('name');
    }

    protected createForm() {
        const validators = [Validators.required, Validators.maxLength(25), MyValidators.forbiddenNames(this.forbiddenNames)];
        this.labelForm = this.fb.group({name: [null, validators]});
    }

    protected readFormData(): Label {
        return {
            name: this.name.value
        };
    }
}
