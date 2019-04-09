import { BaseEntity } from './../../shared';

export class Industry implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
    ) {
    }
}
