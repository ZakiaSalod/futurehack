import { BaseEntity, User } from './../../shared';

export class Rating implements BaseEntity {
    constructor(
        public id?: number,
        public stars?: number,
        public user?: User,
    ) {
    }
}
