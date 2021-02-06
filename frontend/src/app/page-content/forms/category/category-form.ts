 import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { Validators } from '@angular/forms';

import { Category, CategoryData, RadioOption } from '../../../model';
import * as MyValidators from '../../../validators/validators';

export class CategoryForm {

    form: FormGroup;

    showForm = false;

    forbiddenNames: string[];

    radioOptions: RadioOption[] = [];

    selectedLabels: Category[];

    constructor(protected fb: FormBuilder) {
    }

    get name(): AbstractControl {
        return this.form.get('name');
    }

    get description(): AbstractControl {
        return this.form.get('description');
    }

    protected createForm() {
        this.form = this.fb.group({
            name: [null, [Validators.required, Validators.maxLength(25), MyValidators.forbiddenNames(this.forbiddenNames)]],
            description: [null, [Validators.required, Validators.maxLength(100)]]
        });
    }

    protected readFormData(): CategoryData {
        return {
            name: (<string>this.name.value).trim(),
            description: (<string>this.description.value).trim()
        };
    }

    protected onClickOption(selector) {
        this.selectedLabels = selector.options.map(o => Object.assign({}, o)).filter(o => o.checked && delete o.checked);
    }

}
