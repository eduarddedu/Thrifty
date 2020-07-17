import { FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { INgxMyDpOptions } from 'ngx-mydatepicker';

import { Message } from '../../services/messages.service';
import { Category, Label, RadioOption } from '../../model';
import * as MyValidators from '../../validators/validators';
import { Utils } from '../../util/utils';


export class ExpenseFormParent {

    expenseForm: FormGroup;

    showForm = false;

    datepickerOptions: INgxMyDpOptions = {
        dateFormat: 'dd-mm-yyyy',
        showTodayBtn: false
    };

    showNotification = false;

    notificationMessage: Message;

    radioOptionsLabel: RadioOption[] = [];

    radioOptionsCategory: RadioOption[] = [];

    selectedLabels: Label[] = [];

    selectedCategory: Category;

    constructor(protected fb: FormBuilder) {
        this.expenseForm = this.fb.group({
            date: [null, Validators.required],
            description: [null, [Validators.required, Validators.maxLength(100)]],
            amount: [null, [Validators.required, MyValidators.forbiddenNumber(0), MyValidators.isNegativeNumber()]]
        });
    }

    protected get date(): AbstractControl {
        return this.expenseForm.get('date');
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

    protected onClickLabelOption(selector) {
        this.selectedLabels = <Label[]> this.filterChecked(selector.options);
    }

    protected onClickCategoryOption(selector) {
        this.selectedCategory = this.filterChecked(selector.options);
    }

    protected filterChecked(options: RadioOption[]) {
        const clone = objects => objects.map(o => Object.assign({}, o));
        return clone(options).filter(o => o.checked && delete o.checked);
    }

}
