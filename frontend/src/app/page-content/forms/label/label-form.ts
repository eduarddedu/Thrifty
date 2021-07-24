import { Validators, FormGroup, AbstractControl, FormBuilder } from '@angular/forms';

import { LabelData } from '../../../model';
import * as MyValidators from '../../../validators/validators';

export class LabelForm {

    form: FormGroup;

    forbiddenNames: string[];

    showForm = false;

    constructor(protected fb: FormBuilder) { }

    get name(): AbstractControl {
        return this.form.get('name');
    }

    get description(): AbstractControl {
        return this.form.get('description');
    }

    protected createForm() {
        const validators = [Validators.required, Validators.maxLength(25), MyValidators.forbiddenNames(this.forbiddenNames)];
        this.form = this.fb.group(
            {
                name: [null, validators],
                description: [null, [Validators.required, Validators.maxLength(100)]]
            });
    }

    protected readFormData() {
        return {
            name: (<string>this.name.value).trim(),
            description: (<string>this.description.value).trim()
        };
    }
}
