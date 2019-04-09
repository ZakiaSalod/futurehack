import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipSharedModule } from '../../shared';
import { PipAdminModule } from '../../admin/admin.module';
import {
    JobOpportunityService,
    JobOpportunityPopupService,
    JobOpportunityComponent,
    JobOpportunityDetailComponent,
    JobOpportunityDialogComponent,
    JobOpportunityPopupComponent,
    JobOpportunityDeletePopupComponent,
    JobOpportunityDeleteDialogComponent,
    jobOpportunityRoute,
    jobOpportunityPopupRoute,
} from './';

const ENTITY_STATES = [
    ...jobOpportunityRoute,
    ...jobOpportunityPopupRoute,
];

@NgModule({
    imports: [
        PipSharedModule,
        PipAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JobOpportunityComponent,
        JobOpportunityDetailComponent,
        JobOpportunityDialogComponent,
        JobOpportunityDeleteDialogComponent,
        JobOpportunityPopupComponent,
        JobOpportunityDeletePopupComponent,
    ],
    entryComponents: [
        JobOpportunityComponent,
        JobOpportunityDialogComponent,
        JobOpportunityPopupComponent,
        JobOpportunityDeleteDialogComponent,
        JobOpportunityDeletePopupComponent,
    ],
    providers: [
        JobOpportunityService,
        JobOpportunityPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PipJobOpportunityModule {}
