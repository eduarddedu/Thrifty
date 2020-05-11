import { AbstractControl, ValidatorFn } from '@angular/forms';

export function forbiddenNumber(val: number): ValidatorFn {
    return (c: AbstractControl): {[key: string]: any} | null => {
        return c.value && (+c.value === val) ? { 'forbiddenValue': `Expense value can't be ${val}!` } : null;
    };
}

export function forbiddenNames(names: Array<string>): ValidatorFn {
    return (c: AbstractControl): {[key: string]: any} | null => {
        return names.includes(c.value) ? { forbiddenNames : 'Name already taken' } : null;
    };

}

export function isNegativeNumber(): ValidatorFn {
    return (c: AbstractControl): {[key: string]: any} | null => {
        const numberExp = new RegExp('^-\\d*\\.?\\d*$');
        if (numberExp.test(c.value)) {
            return null;
        } else {
            return c.value ? { 'nan': 'Please enter a negative number' } : null;
        }
    };
}
