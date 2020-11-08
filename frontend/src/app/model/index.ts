export interface Account {
    expenses?: Expense[];
    categories?: Category[];
    labels?: Label[];
    balance?: number;
    dateRange?: DateRange;
}

export interface Category {
    id?: number;
    name: string;
    description: string;
    balance?: number;
    expenses?: Expense[];
    labels?: Label[];
    dateRange?: DateRange;
}

export interface Label {
    id?: number;
    name: string;
    expenses?: Expense[];
    categories?: Category[];
    balance?: number;
}

export interface Expense {
    id?: number;
    createdOn: LocalDate;
    description: string;
    amount: number;
    labels?: Label[];
    category: Category;
}

export interface LocalDate {
    year: number;
    month: number;
    day: number;
}

export interface DateRange {
    startDate: LocalDate;
    endDate: LocalDate;
}

export interface CheckLabel  {
    id: number;
    name: string;
    checked: boolean;
}

export interface RadioOption {
    id: number;
    name: string;
    checked: boolean;
}
