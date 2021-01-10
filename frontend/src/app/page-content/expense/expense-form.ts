import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { INgxMyDpOptions } from 'ngx-mydatepicker';

import { Label, RadioOption } from '../../model';
import * as MyValidators from '../../validators/validators';
import { Utils } from '../../util/utils';


export class ExpenseForm {

    expenseForm: FormGroup;

    showForm = false;

    datepickerOptions: INgxMyDpOptions = {
        dateFormat: 'dd-mm-yyyy',
        showTodayBtn: false
    };

    radioOptionsLabel: RadioOption[] = [];

    selectedLabels: Label[] = [];

    constructor(protected fb: FormBuilder) {
        this.expenseForm = this.fb.group({
            createdOn: [{ jsdate: new Date() }, Validators.required],
            description: [null, [Validators.required, Validators.maxLength(100)]],
            amount: [null, [Validators.required, MyValidators.forbiddenNumber(0), MyValidators.isNegativeNumber()]],
            category: [null, Validators.required]
        });
    }

    protected get date(): AbstractControl {
        return this.expenseForm.get('createdOn');
    }

    protected get description(): AbstractControl  {
        return this.expenseForm.get('description');
    }

    protected get amount(): AbstractControl {
        return this.expenseForm.get('amount');
    }

    protected readFormData() {
        return {
            createdOn: Utils.jsDateToLocalDate(this.date.value.jsdate),
            description: this.description.value,
            amount: this.amount.value
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
