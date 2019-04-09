import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PipSharedModule } from '../../shared';
import {
    BusinessUnitService,
    BusinessUnitPopupService,
    BusinessUnitComponent,
    BusinessUnitDetailComponent,
    BusinessUnitDialogComponent,
    BusinessUnitPopupComponent,
    BusinessUnitDeletePopupComponent,
    BusinessUnitDeleteDialogComponent,
    businessUnitRoute,
    businessUnitPopupRoute,
} from './';

const ENTITY_STATES = [
    ...businessUnitRoute,
    ...businessUnitPopupRoute,
];

@NgModule({
    imports: [
        PipSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BusinessUnitComponent,
        BusinessUnitDetailComponent,
        BusinessUnitDialogComponent,
        BusinessUnitDeleteDialogComponent,
        BusinessUnitPopupComponent,
        BusinessUnitDeletePopupComponent,
    ],
    entryComponents: [
        BusinessUnitComponent,
        BusinessUnitDialogComponent,
        BusinessUnitPopupComponent,
        BusinessUnitDeleteDialogComponent,
        BusinessUnitDeletePopupComponent,
    ],
    providers: [
        BusinessUnitService,
        BusinessUnitPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PipBusinessUnitModule {}
