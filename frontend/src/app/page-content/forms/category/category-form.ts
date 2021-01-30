 import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { Validators } from '@angular/forms';

import { Category, RadioOption } from '../../../model';
import * as MyValidators from '../../../validators/validators';

export class CategoryForm {

    categoryForm: FormGroup;

    showForm = false;

    forbiddenNames: string[];

    radioOptions: RadioOption[] = [];

    selectedLabels: Category[];

    constructor(protected fb: FormBuilder) {
    }

    get name(): AbstractControl {
        return this.categoryForm.get('name');
    }

    get description(): AbstractControl {
        return this.categoryForm.get('description');
    }

    protected createForm() {
        this.categoryForm = this.fb.group({
            name: [null, [Validators.required, Validators.maxLength(25), MyValidators.forbiddenNames(this.forbiddenNames)]],
            description: [null, [Validators.required, Validators.maxLength(100)]]
        });
    }

    protected readFormData(): Category {
        return {
            name: this.name.value,
            description: this.description.value
        };
    }

    protected onClickOption(selector) {
        this.selectedLabels = selector.options.map(o => Object.assign({}, o)).filter(o => o.checked && delete o.checked);
    }

}
