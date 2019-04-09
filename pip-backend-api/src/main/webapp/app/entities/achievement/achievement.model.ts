import { BaseEntity, User } from './../../shared';

export class Achievement implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public count?: number,
        public total?: number,
        public user?: User,
    ) {
    }
}
