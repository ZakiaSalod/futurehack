import { BaseEntity, User } from './../../shared';

export class JobOpportunity implements BaseEntity {
    constructor(
        public id?: number,
        public title?: string,
        public position?: string,
        public salary?: number,
        public distance?: number,
        public flexibility?: number,
        public longevity?: number,
        public businessUnit?: BaseEntity,
        public user?: User,
    ) {
    }
}
