import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { INgxMyDpOptions } from 'ngx-mydatepicker';

import { LabelData, RadioOption } from '../../../model';
import * as MyValidators from '../../../validators/validators';
import { Utils } from '../../../util/utils';


export class ExpenseForm {
    form: FormGroup;
    showForm = false;
    showSpinner = true;

    datepickerOptions: INgxMyDpOptions = {
        dateFormat: 'dd-mm-yyyy',
        showTodayBtn: false
    };

    radioOptionsLabel: RadioOption[] = [];

    selectedLabels: LabelData[] = [];

    constructor(protected fb: FormBuilder) {
        this.initForm();
    }

    protected initForm() {
        this.form = this.fb.group({
            createdOn: [{ jsdate: new Date() }, Validators.required],
            description: [null, [Validators.required, Validators.maxLength(100)]],
            amount: [null, [Validators.required, MyValidators.isDecimalNumber()]],
            category: [null, Validators.required]
        });
    }

    protected get createdOn(): AbstractControl {
        return this.form.get('createdOn');
    }

    protected get description(): AbstractControl {
        return this.form.get('description');
    }

    protected get amount(): AbstractControl {
        return this.form.get('amount');
    }

    protected readFormData() {
        return {
            createdOn: Utils.jsDateToLocalDate(this.createdOn.value.jsdate).toString(),
            description: (<string>this.description.value).trim(),
            amount: -1 * Math.abs(this.amount.value)
        };
    }

    protected onClickLabelOption(selector: any) {
        this.selectedLabels = this.filterChecked(selector.options);
    }

    protected filterChecked(options: RadioOption[]) {
        return options.filter(o => o.checked).map(o => {
            const option: any = Object.assign({}, o);
            delete option.checked;
            return option;
        });

    }

}
